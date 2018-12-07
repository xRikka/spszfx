import tensorflow as tf
import numpy as np
from python.InitRGBData import *
#生成一千个点
readData()
x_data=np.random.random([1000,3])
train_X = np.asarray(list_x)
train_Y = np.asarray(list_y)
#系数矩阵的shape必须是（3，1）。如果是（3，）会导致收敛效果差，猜测可能是y-y_label处形状不匹配
y_data=np.matmul(x_data,[[2],[3],[2]])+[1.]


x=tf.placeholder(tf.float32,[None,3])
y=tf.placeholder(tf.float32,[None,1])


weight=tf.Variable(tf.random_normal([3,1]),dtype=tf.float32)

#tf.ones[1,1]，也可以写成tf.ones[1]，这样相当于标量，标量可以直接与矩阵相加
bias=tf.Variable(tf.ones([1]),dtype=tf.float32)


y_label=tf.add(tf.matmul(x,weight),bias)
loss=tf.reduce_mean(tf.square(y-y_label))
train=tf.train.GradientDescentOptimizer(0.2).minimize(loss)


with tf.Session() as sess:
    #变量初始化，目的是给Graph上的图中的变量初始化。
    sess.run(tf.global_variables_initializer())
    for i in range(1000):

        sess.run(train,feed_dict={x:x_data,y:y_data})
        if(i%100==0):
            print(sess.run(loss,feed_dict={x:x_data,y:y_data}))
            #print(sess.run(y_label,feed_dict={x:x_data,y:y_data}))
    print(sess.run(weight),sess.run(bias))
