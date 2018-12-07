
import tensorflow as tf
import numpy
import matplotlib.pyplot as plt
from python.InitRGBData import *
rng = numpy.random
# 参数
learning_rate = 0.01
training_epochs = 20000
display_step = 2000
# 训练数据
readData()
train_X = numpy.asarray(list_x)
train_X_standard = standardize(train_X)
train_X_normal = normalize(train_X)
train_Y = numpy.asarray(list_y)
n_samples = train_X.shape[0]
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
#cost = tf.reduce_mean(tf.square(Y - pred))
# 使用梯度下降拟合数据
optimizer = tf.train.AdamOptimizer(learning_rate).minimize(cost)
#optimizer = tf.train.GradientDescentOptimizer(learning_rate).minimize(cost)
# 初始化所有变量
init = tf.global_variables_initializer()
#保存模型
saver = tf.train.Saver()
# 开始
with tf.Session() as sess:
    sess.run(init)
    # Fit all training data
    for epoch in range(training_epochs):
        sess.run(optimizer, feed_dict={X: train_X, Y: train_Y})
        #Display logs per epoch step
        if (epoch+1) % display_step == 0:
            c = sess.run(cost, feed_dict={X: train_X, Y:train_Y})
            print("Epoch:", '%04d' % (epoch+1), "cost=", "{:.9f}".format(c),
                  "W=", sess.run(W), "b=", sess.run(b))
    print ("Optimization Finished!")
    training_cost = sess.run(cost, feed_dict={X: train_X, Y: train_Y})
    print("Training cost=", training_cost, "W=", sess.run(W), "b=", sess.run(b), '\n')

    saver.save(sess,rootPath+"/out_saver_model/model.ckpt")
    output_graph_def = tf.graph_util.convert_variables_to_constants(sess, sess.graph_def, output_node_names=['X','Y','weight','pred','bias'])
    with tf.gfile.FastGFile(rootPath+"/out_saver_model/model.pb", mode='wb') as f:
        f.write(output_graph_def.SerializeToString())

    '''
    builder = tf.saved_model.builder.SavedModelBuilder(rootPath+"/out_models/")
    builder.add_meta_graph_and_variables(sess,[tf.saved_model.tag_constants.TRAINING])
    builder.save()
    
    plt.plot(train_X, train_Y, 'ro', label='Original data')
    plt.plot(train_X, sess.run(W) * train_X + sess.run(b), label='Fitted line')
    plt.legend()
    plt.show()
    '''
