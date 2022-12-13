import json

class Config:
    def __init__(self, wandb_save, firebase_save, images_folder="./data/image_dataset"):
        self.firebase_save = firebase_save
        self.wandb_save = wandb_save
        self.images_folder = images_folder
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
            sort_keys=True, indent=4)
