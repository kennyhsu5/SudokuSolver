import matplotlib.pyplot as plt
import numpy as np
import scipy
import os

"""Restricted Boltzman Machine with binary units
   With tips from Geoffrey Hinton's 'A Practical 
   Guide to Training Restricted Boltzmann Machines
   Version 1'"""
class RBM:
    """Constructor"""
    def __init__(self, numOfVisibleUnits, numOfHiddenUnits):
        self.size = [numOfVisibleUnits, numOfHiddenUnits]
        #Values initialized using Hinton's practical guide Sec. 8.1
        self.weights = np.random.normal(0.0, 0.01, [numOfVisibleUnits, numOfHiddenUnits])
        self.hiddenBias = np.zeros([1, numOfHiddenUnits])
        self.visibleBias = None

    """Trains the RBM using contrastive divergence"""
    def train(self, dataSet, learningRate, epochs, batchSize, k, persistent=None):
        #Split dataset into 25% validation set and 75% trainingSet
        #Create 1/3 subset from trainingset to evaluate free energy 
        trainSet = dataSet[:dataSet.shape[0] * 0.75, :]
        trainSize = trainSet.shape[0]
        trainSubSet = trainSet[:trainSize / 3, :]
        validationSet = dataSet[trainSize:, :]

        if (trainSize % batchSize != 0):
            print("uneven batchSize")
            return

        title = "h_{}_a_{}_b_{}k_{}".format(self.size[1], learningRate, batchSize, k)
        print(title)
        errors=[]

        #Initialize the visible units biases
        #Based on Sec. 8.1 of Hinton's paper
        p = np.sum(trainSet, 0) / float(trainSize)
        self.visibleBias = np.log10(p / (1 - p))

        lastHiddenState = None
        sb = learningRate / batchSize
        for epoch in range(0, epochs):
            np.random.shuffle(trainSet)

            error = 0
            for batchNum in range(0, trainSize / batchSize):
                start = batchNum * batchSize
                end = start + batchSize
                currBatch = trainSet[start:end, :]

                #positive phase
                posHiddenProbs = self.propagateUp(currBatch)
                posHiddenState = self.getState(posHiddenProbs)
                #Used probs instead of state...? See hinton's practical guide Sec. 3.3
                posAssoc = np.dot(np.transpose(currBatch), posHiddenProbs)

                #Negative phase/gibbs sampling
                negAssoc = None
                for kIter in range(0, k):
                    if (lastHiddenState == None or (kIter == 0 and not persistent)):
                        lastHiddenState = posHiddenState

                    negVisibleProbs = self.propagateDown(lastHiddenState)
                    negVisibleState = self.getState(negVisibleProbs)
                    negHiddenProbs = self.propagateUp(negVisibleState)
                    negHiddenState = self.getState(negHiddenProbs)
                    
                    #Store for next iteration
                    lastHiddenState = negHiddenState

                    if (kIter == k - 1):
                        #Uses probability? read Hinton's practical guide for more...
                        negAssoc = np.dot(np.transpose(negVisibleState), negHiddenProbs)

                #Update parameters
                self.weights += sb * (posAssoc - negAssoc)
                self.hiddenBias += sb * np.sum(posHiddenState - negHiddenState, axis = 0)
                self.visibleBias += sb * np.sum(currBatch - negVisibleState, axis = 0)
                error += self.squaredError(currBatch, negVisibleState)
            #Info to show state of RBM
            avgError = error / trainSize
            errors.append(avgError)
            print("Epoch {} error: {}".format(epoch + 1, avgError))
        tFE = np.sum(self.freeEnergy(trainSubSet)) / trainSubSet.shape[0]
        vFE = np.sum(self.freeEnergy(validationSet)) / validationSet.shape[0]
        print("   Free Energy-- Training: {}, Validation: {}".format(tFE, vFE))
        self.plot(errors, title)
        self.outputFeatures(title)
        print("Last Error: {}".format(avgError))
        print(" ")

                    
    """Calculate activation of hidden units given the visible units"""
    def propagateUp(self, visibleUnits):
        return self.sigmoid(np.dot(visibleUnits, self.weights) + self.hiddenBias)

    """Calculate activation of visible units given the hidden units"""
    def propagateDown(self, hiddenUnits):
        if (self.visibleBias == None):
            print("RBM not trained")
            return
        return self.sigmoid(np.dot(hiddenUnits, np.transpose(self.weights)) + self.visibleBias)

    """Given the probabilities, get the states"""
    def getState(self, probs):
        return probs > np.random.rand(probs.shape[0], probs.shape[1])

    """Calculate squared error between two vectors"""
    def squaredError(self, input, reconstruction):
        return np.sum((input - reconstruction) ** 2)

    """Free energy of boltzman machine"""
    def freeEnergy(self, visible):
        visibleTerm = -np.dot(visible, self.visibleBias)
        hiddenTerm = -np.sum(np.log(1 + np.exp(np.dot(visible, self.weights) + self.hiddenBias)), axis=1)
        return visibleTerm + hiddenTerm

    """Vectorized sigmoid activation function"""
    def sigmoid(self, z):
        return 1 / (1 + np.exp(-1 * z))

    """Outputs the weights of each unit as image"""
    def outputFeatures(self, title):
        dir = "result/rbm/{}/features".format(title)
        if not os.path.exists(dir):
            os.makedirs(dir)
        actProb = self.sigmoid(self.weights) * 255
        num = 1
        for feature in actProb.transpose():
            scipy.misc.imsave("{}/{}.jpg".format(dir, num), feature.reshape(20, 20))
            num += 1

    """Plots the reconstruction error rates"""
    def plot(self, errors, title):
        dir = "result/rbm/{}".format(title)
        if not os.path.exists(dir):
            os.makedirs(dir)
        p1, = plt.plot(errors)
        plt.legend([p1], ["Error"])
        plt.title(title)
        plt.ylabel('Errors')
        plt.xlabel('Epochs')
        plt.savefig("result/rbm/{}/{}.jpg".format(title, title))
        plt.clf()
