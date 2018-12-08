import os
import numpy as np
import tensorflow as tf

path = os.path
rootPath = path.abspath(path.dirname(os.getcwd()))
list_x = list()
list_y = list()

def readData():
    with open(rootPath + "/data/RGBTrainingData.txt","r") as fi:
        for line in fi.readlines():
            strLine = line.strip()
            strLineSplit = strLine.split(" ")
            rgb_arr = list([float(n_str) for n_str in strLineSplit[0][1:-1].split(",")])
            list_x.append(rgb_arr)
            tag = float(strLineSplit[1][strLineSplit[1].find("=")+1:])
            list_y.append([tag])

def format(X, Y):
    strXSplit = X.split("\n")
    for rgb_str in strXSplit:
        rgb_arr = list([float(n_str) for n_str in rgb_str.split(" ")])
        list_x.append(rgb_arr)
    for n_str in Y.split("\n"):
        list_y.append([float(n_str)])

def readTestData():
    list_test_x = list()
    with open(rootPath + "/data/RGBTestData.txt","r") as fi:
        for line in fi.readlines():
            strLine = line.strip()
            rgb_arr = list([int(n_str) for n_str in strLine[1:-1].split(",")])
            list_test_x.append(rgb_arr)
    return list_test_x

# ...
def standardize(X):
    """特征标准化处理
    Args:
        X: 样本集
    Returns:
        标准后的样本集
    """
    Temp_X = X.copy()
    m, n = Temp_X.shape
    # 归一化每一个特征
    for j in range(n):
        features = Temp_X[:,j]
        meanVal = features.mean(axis=0)
        std = features.std(axis=0)
        if std != 0:
            Temp_X[:, j] = (features-meanVal)/std
        else:
            Temp_X[:, j] = 0
    return Temp_X

def normalize(X):
    """Min-Max normalization     sklearn.preprocess 的MaxMinScalar
    Args:
        X: 样本集
    Returns:
        归一化后的样本集
    """
    Temp_X = X.copy()
    m, n = Temp_X.shape
    # 归一化每一个特征
    for j in range(n):
        features = Temp_X[:,j]
        minVal = features.min(axis=0)
        maxVal = features.max(axis=0)
        diff = maxVal - minVal
        if diff != 0:
            Temp_X[:,j] = (features-minVal)/diff
        else:
            Temp_X[:,j] = 0
    return Temp_X

def create_graph():
    with tf.gfile.FastGFile(os.path.join(
            rootPath+"/out_models/", "saved_model.pb"), 'rb') as f:
        graph_def = tf.GraphDef()
        graph_def.ParseFromString(f.read())
        tf.import_graph_def(graph_def, name='')



'''
if __name__ == '__main__':
    readData()
    print(list_x)
    print(list_y)
'''
