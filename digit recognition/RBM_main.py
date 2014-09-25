import RBM
import numpy as np
import dataLoader
import mnistLoader
import NeuralNetwork

trainSet = dataLoader.loadUnlabeledSet(True)
labeledSet = dataLoader.loadLabeledSet(True)
labeledTrainSet = labeledSet[:16000]
labeledValidationSet = labeledSet[16000:]

hiddenUnitsSizes = [1000]
learningRates = [1.0]
batchSizes = [10]
ks = [10]
epochs = 200

for hiddenUnitsSize in hiddenUnitsSizes:
    for learningRate in learningRates:
        for batchSize in batchSizes:
            for k in ks:
                newLabeledTrainSet = np.empty([labeledTrainSet.shape[0], hiddenUnitsSize + 10])
                newLabeledValidationSet = np.empty([labeledValidationSet.shape[0], hiddenUnitsSize + 10])
                
                print("Training RBM")
                rbm = RBM.RBM(400, hiddenUnitsSize)
                rbm.train(trainSet, learningRate, epochs, batchSize, k)
                """
                newLabeledTrainSet[:, :-10] = rbm.getState(rbm.propagateUp(labeledTrainSet[:, :-10]))
                newLabeledTrainSet[:, -10:] = labeledTrainSet[:, -10:]
                newLabeledValidationSet[:, :-10] = rbm.getState(rbm.propagateUp(labeledValidationSet[:, :-10]))
                newLabeledValidationSet[:, -10:] = labeledValidationSet[:, -10:]
                print("Training Neural Network")
                nn = NeuralNetwork.NeuralNetwork(hiddenUnitsSize, 150, 10)
                nn.train(newLabeledTrainSet, 600, 0, 0.1, 100, newLabeledValidationSet[:, :-10], newLabeledValidationSet[:, -10:])"""
