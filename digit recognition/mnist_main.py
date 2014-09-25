from scipy import misc
import matplotlib.pyplot as plt
import NeuralNetwork
import mnistLoader
import numpy as np
import time
import cv2

digits = [1, 2, 3, 4, 5, 6, 7, 8, 9]
offset = 10 - len(digits)
trainingSetSize = 50000
cvSetSize = 13097


trainSize, imageSize, tempTrainingSet = mnistLoader.load(digits, "train", 400)
testSize, testImageSize, tempTestingSet = mnistLoader.load(digits, "test", 400)
np.random.shuffle(tempTrainingSet)

outputSize = 9
trainingSet = np.empty([trainingSetSize, imageSize + outputSize])
testSet = np.empty([cvSetSize, imageSize + 1])
temp = np.zeros([trainingSetSize, outputSize])

for i in range(0, trainingSetSize):
    temp[i, tempTrainingSet[i, -1] - offset] = 1

trainingSet[:, :-outputSize] = tempTrainingSet[:trainingSetSize, :-1]
trainingSet[:, -outputSize:] = temp
testSet[:(trainSize - trainingSetSize), :] = tempTrainingSet[trainingSetSize:, :]
testSet[(cvSetSize - testSize):, :] = tempTestingSet[:, :]


hiddenLayerSizes = [2]
alphaSizes = [0.1]
batchSizes = [200]
epochs = 100

for hiddenLayerSize in hiddenLayerSizes:
    for alpha in alphaSizes:
        for batchSize in batchSizes:
            nn = NeuralNetwork.NeuralNetwork(400, hiddenLayerSize, 9)

            nn.train(trainingSet, epochs, 0, alpha, batchSize, testSet[:, :-1], testSet[:, -1], offset)
"""
outFile = open("four.net", 'w')
nn.export(outFile)
outFile.close()
"""
