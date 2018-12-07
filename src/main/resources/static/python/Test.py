import tensorflow as tf
from tensorflow.examples.tutorials.mnist import input_data
import numpy as np
#import matplotlib.pyplot as plt
from python.InitRGBData import *


#X = tf.placeholder("float",[None,3],name='X')
test_x = np.asarray(readTestData())
test_x_standard = standardize(test_x)
test_x_normal = normalize(test_x)
saver = tf.train.import_meta_graph(rootPath+"/out_saver_model/model.ckpt.meta")

with tf.Session() as sess:
    saver.restore(sess,rootPath+"/out_saver_model/model.ckpt")
    #print("weight:",sess.run(tf.get_default_graph().get_tensor_by_name("weight:0")),"\nbias:",sess.run(tf.get_default_graph().get_tensor_by_name("bias:0")))
    #X = sess.run(tf.get_default_graph().get_tensor_by_name("X:0"))
    W = sess.run(tf.get_default_graph().get_tensor_by_name("weight:0"))
    b = sess.run(tf.get_default_graph().get_tensor_by_name("bias:0"))
    Y = sess.run(tf.get_default_graph().get_tensor_by_name("pred:0"),feed_dict={"X:0":test_x})
    print("weight:",sess.run(tf.get_default_graph().get_tensor_by_name("weight:0")),"\nbias:",sess.run(tf.get_default_graph().get_tensor_by_name("bias:0")),
          "\npred:\n",Y)

    '''
    tf.saved_model.loader.load(sess,[tf.saved_model.tag_constants.TRAINING],rootPath+"/out_models/")
    input_x = sess.graph.get_tensor_by_name('X:0')
    input_y = sess.graph.get_tensor_by_name('Y:0')
    #output = sess.graph.get_tensor_by_name('dense_1/BiasAdd:0')
    print(sess.run({input_x:test_x}))
    '''


