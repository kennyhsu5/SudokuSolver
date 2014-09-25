import NeuralNetwork
import dataLoader
import numpy as np

set1 = dataLoader.loadLabeledSet()
trainingSet = set1[:16000]
testSet = set1[16000:]

hiddenLayerSizes = [500]
alphaSizes = [0.01]
batchSizes = [50]
lamSizes = [100.0]
epochs = 500

for hiddenLayerSize in hiddenLayerSizes:
    for alpha in alphaSizes:
        for lam in lamSizes:
            for batchSize in batchSizes:
                nn = NeuralNetwork.NeuralNetwork(400, hiddenLayerSize, 10, NeuralNetwork.CrossEntropyCost)
                nn.train(trainingSet, epochs, lam, alpha, batchSize, testSet[:, :-10], testSet[:, -10:])
                #nn.train(trainingSet, epochs, lam, alpha, batchSize, None, None)
#outFile = open("yay2.net", 'w')
#nn.export(outFile)
#outFile.close()
