from models import create_model1
from train.data.image_data import ImageDataset

images_folder = "./data/image_dataset"
detection_threshold = 0.1
dataset = ImageDataset(images_folder, detection_threshold)
# dataset.load_images_and_run_detection()
dataset.load_keypoints_and_classes_json("./data/image_dataset/detections.json")
dataset.convert_classes_to_categorical()

model = create_model1((17, 2), num_classes=dataset.unique_classes.shape[0])
model.compile(optimizer="adam", loss="sparse_categorical_crossentropy", metrics=["accuracy"])
model.fit(dataset.keypoints, dataset.labels, epochs=50, batch_size=16)

