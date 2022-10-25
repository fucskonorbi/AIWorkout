import keras
import tensorflow as tf


def create_model1(input_size, num_classes):
    inputs = tf.keras.Input(shape=input_size)
    layer = keras.layers.Dense(128, activation=tf.nn.relu6)(inputs)
    layer = keras.layers.Dropout(0.5)(layer)
    layer = keras.layers.Dense(64, activation=tf.nn.relu6)(layer)
    layer = keras.layers.Dropout(0.5)(layer)
    layer = keras.layers.Flatten()(layer)
    outputs = keras.layers.Dense(num_classes, activation="softmax")(layer)

    model = keras.Model(inputs, outputs)
    model.summary()
    return model


def create_model2(input_size, num_classes):
    inputs = tf.keras.Input(shape=input_size)
    layer = keras.layers.Dense(64, activation=tf.nn.relu6)(inputs)
    layer = keras.layers.Dropout(0.5)(layer)
    layer = keras.layers.Dense(64, activation=tf.nn.relu6)(layer)
    layer = keras.layers.Dropout(0.5)(layer)
    layer = keras.layers.Flatten()(layer)
    outputs = keras.layers.Dense(num_classes, activation="softmax")(layer)

    model = keras.Model(inputs, outputs)
    model.summary()
    return model