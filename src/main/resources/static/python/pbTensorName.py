import tensorflow as tf
import os
from python.InitRGBData import *

model_dir = './out_models/'
model_name = 'saved_model.pb'

if __name__ == '__main__':
    create_graph()
    tensor_name_list = [tensor.name for tensor in tf.get_default_graph().as_graph_def().node]
    for tensor_name in tensor_name_list:
        print(tensor_name,'\n')
