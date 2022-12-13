import os

import cv2
import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
import tensorflow_hub as hub

# map edges to a RGB color, copied from TensorFlow examples
KEYPOINT_EDGE_INDS_TO_COLOR = {
    (0, 1): (147, 20, 255),
    (0, 2): (255, 255, 0),
    (1, 3): (147, 20, 255),
    (2, 4): (255, 255, 0),
    (0, 5): (147, 20, 255),
    (0, 6): (255, 255, 0),
    (5, 7): (147, 20, 255),
    (7, 9): (147, 20, 255),
    (6, 8): (255, 255, 0),
    (8, 10): (255, 255, 0),
    (5, 6): (0, 255, 255),
    (5, 11): (147, 20, 255),
    (6, 12): (255, 255, 0),
    (11, 12): (0, 255, 255),
    (11, 13): (147, 20, 255),
    (13, 15): (147, 20, 255),
    (12, 14): (255, 255, 0),
    (14, 16): (255, 255, 0)
}

os.environ['TFHUB_CACHE_DIR'] = "./tfhub_cache"
model = hub.load("https://tfhub.dev/google/movenet/singlepose/lightning/4")
print(model)
movenet = model.signatures['serving_default']

def detect(image):
    """Detects pose landmarks from an image."""
    outputs = movenet(image)
    keypoints = outputs['output_0']  # [1, 1, 17, 3]
    return keypoints


def visualize(image: np.ndarray, keypoints, mode="save"):
    """Visualizes the keypoints on the image."""

    image = image[0]
    for keypoint in keypoints:
        x, y, _ = keypoint
        cv2.circle(image, (int(x), int(y)), 2, (0, 255, 0), -1)

    # lines between keypoints
    for edge, color in KEYPOINT_EDGE_INDS_TO_COLOR.items():
        x0, y0, _ = keypoints[edge[0]]
        x1, y1, _ = keypoints[edge[1]]
        cv2.line(image, (int(x0), int(y0)), (int(x1), int(y1)), color, 2)
    if mode == "save":
        cv2.imwrite("output.jpg", image)
    else:
        plt.imshow(image)
        plt.show()


# dummy image
image = tf.zeros([1, 192, 192, 3], dtype=tf.int32)
keypoints = detect(image)
print(keypoints.shape)
