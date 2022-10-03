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

def run_movenet(image) -> dict:
    """Run MoveNet model on a single image.

    Args:
        image: A numpy array with shape [height, width, 3].

    Returns:
        A dictionary containing:
            - A numpy array with shape [1, 1, 17, 3] representing the keypoints
              and their scores.
    """
    model = hub.load('https://tfhub.dev/google/movenet/singlepose/lightning/4')
    input_image = tf.expand_dims(image, axis=0)
    outputs = model.signatures['serving_default'](input_image)
    keypoints_with_scores = outputs['output_0']
    return keypoints_with_scores

input_size = 192
image_path = 'data/images/pose.jpg'
image = tf.image.decode_jpeg(tf.io.read_file(image_path))