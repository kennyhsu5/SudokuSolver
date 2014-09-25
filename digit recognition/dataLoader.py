from scipy import misc
import numpy as np
import os

"""Loads the data from file into numpy arrays.
   TODO: combine two methods into one? """
def loadLabeledSet(binary=False):
    dir = os.getcwd() + "/training_data/labeled/copy_training_data"
    dataSet = np.empty([20000, 410])
    num = 0

    for folder in os.listdir(dir):
        subDir = dir + "/" + folder
        temp = np.zeros(10)
        ans = 0 if folder == "none" else int(folder)
        temp[ans] = 1
        for file in os.listdir(subDir):
            img = misc.imread(subDir + '/' + file).reshape(1, 400)

            #Convert to binary if using RBM
            if (binary):
                white = img > 128
                black = img <= 128
                img[white] = 1
                img[black] = 0

            dataSet[num, :-10] = img
            dataSet[num, -10:] = temp
            num += 1
    np.random.shuffle(dataSet)
    return dataSet

def loadUnlabeledSet(binary=False):
    dir = os.getcwd() + "/training_data/unlabeled/unlabeled_copy"
    dataSet = np.empty([16000, 400])
    num = 0

    for file in os.listdir(dir):
        img = misc.imread(dir + "/" + file).reshape(1, 400)

        #Convert to binary if using RBM
        if (binary):
            white = img > 128
            black = img <= 128
            img[white] = 1
            img[black] = 0

        dataSet[num, :] = img
        num += 1
    np.random.shuffle(dataSet)
    return dataSet
