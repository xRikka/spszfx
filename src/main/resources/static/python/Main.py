import sys
from python.TrainModel import *
from python.InitRGBData import *

def main(list_x,list_y):
    trainModel = TensorFlow_TrainModel(list_x,list_y)
    trainModel.run()

if __name__ == '__main__':
    format(sys.argv[1],sys.argv[2])
    main(list_x,list_y)
