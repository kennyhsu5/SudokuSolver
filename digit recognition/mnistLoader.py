import struct
import numpy as np
import cv2
import os
from array import array

"""Loads the given dataset and its labels into numpy arrays"""
def load(numbers, dataType, newSize=None):
    numOfdigits = len(numbers)
    dir = os.getcwd() + "/training_data/labeled/mnist/"

    #Figure out which dataset to load
    if dataType is "train":
        imageFile = open(dir + 'trainImages', 'rb')
        labelFile = open(dir + 'trainLabel', 'rb')
    elif dataType is "test":
        imageFile = open(dir + 'testImages', 'rb')
        labelFile = open(dir + 'testLabel', 'rb')
    else:
        print("dataset does not exist")
        return

    #Move to correct offset and retrieve size info
    magic, size = struct.unpack(">II", labelFile.read(8))
    magic, size, rowSize, columnSize = struct.unpack(">IIII", imageFile.read(16))
    imageSize = rowSize * columnSize

    #Load data into tempporary arrays
    labels = array("B", labelFile.read())
    images = array("B", imageFile.read())

    #Figure out which training examples and labels we want
    requestedIndexes = []
    for index in range(0, size):
        if labels[index] in numbers:
            requestedIndexes.append(index)
    dataSetSize = len(requestedIndexes)

    #Create empty numpy arrays to store data
    dataSet = np.empty([dataSetSize, (imageSize if (newSize == None) else newSize) + 1])

    for i in range(0, dataSetSize):
        start = requestedIndexes[i] * imageSize
        temp = np.empty([imageSize])
        temp[:] = images[start : start + imageSize]
        newImg = cv2.resize(temp.reshape(28, 28), (20, 20))

        dataSet[i][:-1] = newImg.reshape(1, 400)
        dataSet[i][-1] = labels[requestedIndexes[i]]

    return dataSetSize, imageSize if (newSize == None) else newSize, dataSet
