import tensorflow as tf
import tensorflow_hub as hub

KEYPOINT_DICT = {
    'nose': 0,
    'left_eye': 1,
    'right_eye': 2,
    'left_ear': 3,
    'right_ear': 4,
    'left_shoulder': 5,
    'right_shoulder': 6,
    'left_elbow': 7,
    'right_elbow': 8,
    'left_wrist': 9,
    'right_wrist': 10,
    'left_hip': 11,
    'right_hip': 12,
    'left_knee': 13,
    'right_knee': 14,
    'left_ankle': 15,
    'right_ankle': 16
}
input_size = 192
module = hub.load("https://tfhub.dev/google/movenet/singlepose/lightning/4")
image_path = r"C:\Users\norch\Downloads\pexels-photo-4384679.jpeg"
image = tf.image.decode_jpeg(tf.io.read_file(image_path))