import csv

import numpy as np

movenet_mappings = {
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

class MMFitDataset():
    def __init__(self, filepath, dim):
        self.actions = {'squats': 0, 'lunges': 1, 'bicep_curls': 2, 'situps': 3, 'pushups': 4, 'tricep_extensions': 5,
                        'dumbbell_rows': 6, 'jumping_jacks': 7, 'dumbbell_shoulder_press': 8,
                        'lateral_shoulder_raises': 9, 'non_activity': 10}
        self.labels = self.load_labels(filepath)
        self.data = self.load_data(filepath)
        self.dim = dim
        self.mappings_2d = {
            'nose': 0,
            'left_eye': 15,
            'right_eye': 14,
            'left_ear': 17,
            'right_ear': 16,
            'left_shoulder': 5,
            'right_shoulder': 2,
            'left_elbow': 6,
            'right_elbow': 3,
            'left_wrist': 7,
            'right_wrist': 4,
            'left_hip': 11,
            'right_hip': 8,
            'left_knee': 12,
            'right_knee': 9,
            'left_ankle': 13,
            'right_ankle': 10
        }
        self.mappings_3d = {
            0: 'Hip',
            1: 'Left Hip',
            2: 'Left Knee',
            3: 'Left Foot',
            4: 'Right Hip',
            5: 'Right Knee',
            6: 'Right Foot',
            7: 'Spine',
            8: 'Thorax',
            9: 'Neck/Nose',
            10: 'Head',
            11: 'Right Shoulder',
            12: 'Right Elbow',
            13: 'Right Wrist',
            14: 'Left Shoulder',
            15: 'Left Elbow',
            16: 'Left Wrist'
        }
        
    def load_data(self, filepath, reshape_data=True) -> np.ndarray:
        """Loads the npy file and returns the data.

        Args:
            filepath (str): Filepath to the npy file.
        """
        try:
            data = np.load(filepath)
        except FileNotFoundError as error:
            data = np.empty((0, 0, 0))
            print(error)
        if reshape_data:
            data = np.reshape(data, (data.shape[1], data.shape[2], data.shape[0]))
        return data

    def load_labels(self, filepath):
        """Loads the labels from the csv file.

        Args:
            filepath (str): Filepath to the csv file.
        """
        labels = []
        with open(filepath, 'r', encoding="utf-8") as csv_file:
            reader = csv.reader(csv_file)
            for line in reader:
                labels.append([int(line[0]), int(line[1]), line[3]])
        return labels
    
    
    def filter_data(self, value):
        raise NotImplementedError
    
    def map_from_mm_to_movenet(self, pose):
        raise NotImplementedError
    
    
    def plot_joints(self, joints):
        raise NotImplementedError

        
    def plot_2d_pose(self, pose, figsize=(8, 8)): # TODO: rewrite this function
        raise NotImplementedError
        
    
    def save_data_to_csv(self, filepath):
        """Saves the data to a csv file.

        Args:
            filepath (str): Filepath to the csv file.
        """
        with open(filepath, 'w', newline='') as csv_file:
            pass