import tensorflow as tf
import numpy
import matplotlib.pyplot as plt
from python.InitRGBData import *

class TensorFlow_TrainModel:
    # 训练数据
    __train_X = list()
    __train_Y = list()
    # 参数
    learning_rate = 0.01
    training_epochs = 99999
    display_step = 2000
    least_error = 2e-6

    def __init__(self,list_x,list_y):
        self.train_X = numpy.asarray(list_x)
        self.train_Y = numpy.asarray(list_y)

    def run(self):
        rng = numpy.random
        n_samples = self.train_X.shape[0]
        #占位符，相当于形参,若没有指定行列数shape，则匹配传入参数维度
        X = tf.placeholder("float",[None,3],name='X')
        Y = tf.placeholder("float",[None,1],name='Y')
        # 模型参数
        W = tf.Variable(rng.randn(3,1), name="weight",dtype=tf.float32)
        b = tf.Variable(0.1 * rng.randn(), name="bias",dtype=tf.float32)
        # 构建线性模型
        pred = tf.add(tf.matmul(X, W), b,name="pred")
        # 求误差 pow:幂运算:x^y，reduce_sum:降维求和
        cost = tf.reduce_sum(tf.pow(pred-Y, 2))/(2*n_samples)
        # 使用梯度下降拟合数据
        optimizer = tf.train.AdamOptimizer(self.learning_rate).minimize(cost)
        # 初始化所有变量
        init = tf.global_variables_initializer()
        #保存模型
        saver = tf.train.Saver()
        # 开始
        with tf.Session() as sess:
            sess.run(init)
            # Fit all training data
            for epoch in range(self.training_epochs):
                sess.run(optimizer, feed_dict={X: self.train_X, Y: self.train_Y})
                #Display logs per epoch step
                if (epoch+1) % self.display_step == 0:
                    c = sess.run(cost, feed_dict={X: self.train_X, Y:self.train_Y})
                    print("Epoch:", '%04d' % (epoch+1), "cost=", "{:.9f}".format(c),
                          "W=", sess.run(W), "b=", sess.run(b))
                if sess.run(cost, feed_dict={X: self.train_X, Y:self.train_Y}) < self.least_error:
                    print ("Achieve The Least_Error Range Finished! Count=",epoch+1)
                    break
            print ("Optimization Finished!")
            training_cost = sess.run(cost, feed_dict={X: self.train_X, Y: self.train_Y})
            print("Training cost=", "{:.9f}".format(training_cost), "W=", sess.run(W), "b=", sess.run(b), '\n')

            saver.save(sess,rootPath+"/out_saver_model/model.ckpt")
            output_graph_def = tf.graph_util.convert_variables_to_constants(sess, sess.graph_def, output_node_names=['X','Y','weight','pred','bias'])
            with tf.gfile.FastGFile(rootPath+"/out_saver_model/model.pb", mode='wb') as f:
                f.write(output_graph_def.SerializeToString())