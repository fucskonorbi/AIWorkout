import firebase_admin
import numpy as np
import tensorflow as tf
import wandb
from firebase_admin import credentials
from firebase_admin import ml

from utils.config import Config
from utils.image_data import ImageDataset
from utils.models import create_model1

if __name__ == "__main__":
    # load config from config.py
    config = Config(wandb_save=False, firebase_save=False)

    if config.wandb_save:
        # Initialize wandb
        wandb.init(project="aiworkout", entity="norcs")

    images_folder = "./data/image_dataset"
    detection_threshold = 0.1
    dataset = ImageDataset(images_folder, detection_threshold)
    # dataset.load_images_and_run_detection()
    dataset.load_keypoints_and_classes_json("./data/image_dataset/detections.json")
    dataset.convert_classes_to_categorical()
    flattened_keypoints = np.array([keypoint.flatten() for keypoint in dataset.keypoints])
    model = create_model1(input_shape=(51,), num_classes=dataset.unique_classes.shape[0])
    model.compile(optimizer="adam", loss="sparse_categorical_crossentropy", metrics=["accuracy"])
    model.fit(flattened_keypoints, dataset.labels, epochs=50, batch_size=16)
    model.summary()

    # convert model to tflite
    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    converter.optimizations = [tf.lite.Optimize.DEFAULT]
    tflite_model = converter.convert()
    with open('../tmp/artifacts/pose_classifier.tflite', 'wb') as f:
        f.write(tflite_model)

    if config.firebase_save:
        # Initialize the app with a service account, granting admin privileges
        firebase_admin.initialize_app(
            credentials.Certificate('../tmp/aiworkout-22b29-firebase-adminsdk-qeyj9-f1e63de502.json'),
            options={
                'storageBucket': 'aiworkout-22b29.appspot.com',
            })
        # Upload the model to Firebase
        model = ml.TFLiteGCSModelSource.from_tflite_model_file('../tmp/artifacts/pose_classifier.tflite')
        model_format = ml.TFLiteFormat(model_source=model)
        model = ml.Model(
            display_name='pose_classifier_test',
            tags=["test"],
            model_format=model_format,
        )
        model = ml.create_model(model)
        ml.publish_model(model.model_id)
