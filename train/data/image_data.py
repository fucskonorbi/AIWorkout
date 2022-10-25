import csv
import json
import os

import numpy as np
import tensorflow as tf
from sklearn import preprocessing
from tqdm import tqdm

from ..movenet import detect, visualize


class ImageDataset:
    def __init__(self, images_folder, detection_threshold):
        self.images_folder = images_folder
        self.detection_threshold = detection_threshold
        self.keypoints = None
        self.labels = None
        self.unique_classes = None
        self.detections = []


    def save_detections_to_csv(self, file_path):
        """Saves the detections to a csv file."""
        with open(file_path, "w") as f:
            writer = csv.writer(f)
            writer.writerow(["image_name", "keypoints_x", "keypoints_y", "class"])
            for detection in self.detections:
                print(detection)
                writer.writerow([detection["image_name"], detection["keypoints_x"], detection["keypoints_y"], detection["class"]])

    def save_detections_to_json(self, file_path):
        """Saves the detections to a json file."""
        # convert
        with open(file_path, "w") as f:
            json.dump(self.detections, f)

    def convert_classes_to_categorical(self):
        """Converts the classes to categorical."""
        le = preprocessing.LabelEncoder()
        le.fit(self.labels)
        self.labels = le.transform(self.labels)

    def load_keypoints_and_classes_csv(self, file_path):
        """format is: frame_name, x, y, class_name where x.shape == y.shape == (17,)"""
        with open(file_path, "r") as f:
            reader = csv.reader(f)
            next(reader)
            for row in reader:
                print(row)
                image_name, keypoints_x, keypoints_y, class_name = row
                print(keypoints_x)

                # keypoints should be of shape (17, 2)
                keypoints_x = np.array(keypoints_x.split(" ")).astype(np.float32)
                keypoints_y = np.array(keypoints_y.split(" ")).astype(np.float32)
                self.keypoints.append(np.stack([keypoints_x, keypoints_y], axis=1))
                self.labels.append(class_name)
            self.unique_classes = np.unique(self.labels)

    def load_keypoints_and_classes_json(self, file_path):
        """format is: frame_name, x, y, class_name where x.shape == y.shape == (17,)"""
        keypoints = []
        labels = []
        with open(file_path, "r") as f:
            data = json.load(f)
            for detection in data:
                image_name, keypoints_x, keypoints_y, class_name = detection.values()
                keypoints_x = np.array(keypoints_x).astype(np.float32)
                keypoints_y = np.array(keypoints_y).astype(np.float32)
                keypoints.append(np.stack([keypoints_x, keypoints_y], axis=1))
                labels.append(class_name)
            self.keypoints = np.array(keypoints)
            self.labels = np.array(labels)
            self.unique_classes = np.unique(self.labels)

    def load_images_and_run_detection(self):
        """Loads images from the images folder into TensorFlow format and reshapes them to (192,192)."""
        
        # get the subfolders in the images folder
        subfolders = sorted([n for n in os.listdir(self.images_folder) if os.path.isdir(os.path.join(self.images_folder, n))])
        # each subfolder's name is a class
        self.unique_classes = subfolders
        # get the images in each subfolder
        images = []
        for subfolder in subfolders:
            images_in_subfolder = sorted([os.path.join(self.images_folder, subfolder, n) for n in os.listdir(os.path.join(self.images_folder, subfolder)) if os.path.isfile(os.path.join(self.images_folder, subfolder, n))])
            images.extend(images_in_subfolder)

        for image_path in tqdm(images):
            try:
                image = tf.io.read_file(image_path)
                image = tf.io.decode_image(image)
                image = tf.cast(tf.image.resize_with_pad(image, 192, 192), dtype=tf.int32)
                image = tf.expand_dims(image, axis=0)
            except:
                continue

            _, img_height, img_width, channels = image.shape
            if channels != 3:
                continue
            # run detection on GPU
            person = detect(image)

            min_score = tf.math.reduce_min(person[0, 0, :, 2])
            keep_image = min_score >= self.detection_threshold
            if not keep_image:
                continue

            # use numpy in all the other steps
            keypoints = np.array(
                [person[0, 0, :, 1] * img_width, person[0, 0, :, 0] * img_height], dtype=np.float32)
            keypoints = np.transpose(keypoints)
            visualize(np.array(image), keypoints, mode="save")
            keypoints = np.expand_dims(keypoints, axis=0)
            self.keypoints = np.append(self.keypoints, keypoints, axis=0) if self.keypoints is not None else keypoints

            # detections is a list of dictionaries, each dictionary has the following keys:
            # "image_name", "keypoints x coordinates", "keypoints y coordinates", "class"
            detection = {
                "image_name": image_path.split(os.sep)[-1],
                "keypoints_x": keypoints[0, :, 0].tolist(),
                "keypoints_y": keypoints[0, :, 1].tolist(),
                "class": image_path.split(os.sep)[-2]
            }
            self.detections.append(detection)

        self.save_detections_to_csv(os.path.join(self.images_folder, "detections.csv"))
        self.save_detections_to_json(os.path.join(self.images_folder, "detections.json"))
