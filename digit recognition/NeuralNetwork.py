import numpy as np
import matplotlib.pyplot as plt

class SquaredCost:
    @staticmethod
    def cost(outputs, labels):
        return np.sum((outputs - labels) ** 2)/ labels.shape[0] / 2
    @staticmethod
    def delta(outputs, labels):
        return (outputs - labels) * (outputs * (1 - outputs))
    @staticmethod
    def name():
        return "SQ"

class CrossEntropyCost:
    @staticmethod
    def cost(outputs, labels):
        return np.nan_to_num(np.sum(-labels*np.log(outputs)-(1-labels)*np.log(1-outputs))) / labels.shape[0]
    @staticmethod
    def delta(outputs, labels):
        return outputs - labels
    @staticmethod
    def name():
        return "CE"

"""A neural network with one input layer,
one hidden layer, and one output layer"""
class NeuralNetwork:

    def __init__(self, numOfInputNodes, numOfHiddenNodes, numOfOutputNodes, costType=SquaredCost):
        self.size = [numOfInputNodes, numOfHiddenNodes, numOfOutputNodes]
        self.theta1 = np.random.randn(numOfHiddenNodes, numOfInputNodes) / np.sqrt(numOfInputNodes)
        self.theta2 = np.random.randn(numOfOutputNodes, numOfHiddenNodes) / np.sqrt(numOfHiddenNodes)
        self.hiddenBias = np.random.randn(1, numOfHiddenNodes)
        self.outputBias = np.random.randn(1, numOfOutputNodes)
        self.costFunc = costType
    
    """Vectorized sigmoid activation function for each neuron"""
    def sigmoid(self, z):
        return 1 / (1 + np.exp(-1 * z))

    """Evaluate performance of neural network on testing data"""
    def evaluate(self, X, y):
        return np.sum(self.predict(X) == y)

    """Predict labels for each input in array X"""
    def predict(self, X):
        z1 = np.dot(X, self.theta1.T) + self.hiddenBias
        a1 = self.sigmoid(z1)
        z2 = np.dot(a1, self.theta2.T) + self.outputBias
        outputs = self.sigmoid(z2)
        return np.argmax(outputs, axis = 1)

    """Train the neural network using stochastic gradient descent and
    backpropagation. X = training examples, y = labels
    batchSize should divide size of training examples evening"""
    def train(self, trainingSet, epoch, lam, step, batchSize, testImages, testLabels):
        inputSize = trainingSet.shape[0]

        title = "h_{}_a_{}_b_{}l_{}i_{}{}".format(self.size[1], step, batchSize, lam, inputSize, self.costFunc.name())
        print(title)
        trainCosts=[]
        cvCosts=[]

        if (inputSize % batchSize != 0):
            print("unacceptable batchSize")
            return

        #Train for given number of epoch,
        #each epoch = run throught entire training set
        for iteration in range(0, epoch):
            #Shuffle the entire training set to get
            #random mini-batches each time
            np.random.shuffle(trainingSet)
            
            for batchNum in range(0, inputSize / batchSize):
                start = batchNum * batchSize
                end = start + batchSize
                currBatch = trainingSet[start:end, :-self.size[-1]]
                currLabels = trainingSet[start:end, -self.size[-1]:]
                
                #Feedforward
                z1 = np.dot(currBatch, self.theta1.T) + self.hiddenBias
                a1 = self.sigmoid(z1)
                z2 = np.dot(a1, self.theta2.T) + self.outputBias
                outputs = self.sigmoid(z2)
                
                #Backpropagation, calculate graidents/errors for layers/weights
                outputError = self.costFunc.delta(outputs, currLabels)
                hiddenLayerError = np.dot(outputError, self.theta2) * (a1 * (1 - a1))
                theta2Grad = np.dot(outputError.T, a1)
                theta1Grad = np.dot(hiddenLayerError.T, currBatch)

                #sum errors for all exmaples in batch
                outputError = np.sum(outputError, axis = 0)
                hiddenLayerError = np.sum(hiddenLayerError, axis = 0)
                
                #Update weights/biases
                sb = step / batchSize
                regularization = (1 - step * (lam / inputSize))
                self.theta2 = regularization * self.theta2 - sb * theta2Grad
                self.theta1 = regularization * self.theta1 - sb * theta1Grad
                self.hiddenBias -= sb * hiddenLayerError
                self.outputBias -= sb * outputError

            #Evalute in sample cost and out of sample cost
            cost = self.cost(trainingSet[:, :-10], trainingSet[:, -10:])
            trainCosts.append(cost)
            print("NN Epoch {} train cost: {}".format(iteration + 1, cost))
            if (testImages is not None):
                cost = self.cost(testImages, testLabels)
                cvCosts.append(cost)
                print("    validation cost: {}".format(cost))
                print("Number correct out of 4000")
                print(self.evaluate(testImages, np.argmax(testLabels, axis = 1)))
        self.plot(cvCosts, trainCosts, title)
        print(" ")

    def export(self, file):
        size = self.size
        file.write("{} {} {}\n".format(size[0], size[1], size[2]))
        weights = [np.concatenate((self.hiddenBias.T, self.theta1), axis=1), np.concatenate((self.outputBias.T, self.theta2), axis=1)]
        for w in weights:
            np.savetxt(file, w)
            file.write("\n")

    def cost(self, X, y):
        z1 = np.dot(X, self.theta1.T) + self.hiddenBias
        a1 = self.sigmoid(z1)
        z2 = np.dot(a1, self.theta2.T) + self.outputBias
        outputs = self.sigmoid(z2)
        return self.costFunc.cost(outputs, y)

    def plot(self, cvCosts, trainCosts, title):
        p1, = plt.plot(cvCosts)
        p2, = plt.plot(trainCosts)
        plt.legend([p1, p2], ["Validation Cost", "Training Cost"])
        plt.title(title)
        plt.ylabel('Costs')
        plt.xlabel('Epochs')
        plt.savefig("result/" + title + '.jpg')
        plt.clf()
