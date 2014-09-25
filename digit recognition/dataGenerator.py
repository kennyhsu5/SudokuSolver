from scipy import misc
import numpy as np
import random
import time
import cv2
import os

directions = ["up", "down", "left", "right"]
options = {"up": [-1, 0],
           "down": [1, 0],
           "left": [-1, 1],
           "right": [1, 1]}

def generate():
    dir = os.getcwd() + "/copy_training_data"
    dstDir = os.getcwd() + "/copy_training_data"
    num = 0
    for folder in os.listdir(dir):
        subDir = dir + "/" + folder
        for file in os.listdir(subDir):
            #for p in range(0, 10):
            img = misc.imread(subDir + '/' + file)
            print(img.shape)
"""
            #Shift the image by a certain amount in a direction
                img = shift(img, random.choice(directions), random.randint(0, 2))
            #Decide whether to erode/dilate and how much
                kernel = np.ones((random.uniform(1, 2.5), random.uniform(1, 2.5)), np.uint8)
                rand = random.randint(0, 2)
                if (rand == 0):
                    img = cv2.dilate(img, kernel, iterations = 1)
                elif (rand == 1):
                    img = cv2.erode(img, kernel, iterations = 1)
            #Randomly select points to add noise
                for m in range(0, 10):
                    img[random.randint(0, 19)][random.randint(0, 19)] = random.randint(0, 255)
            #save image
                 num += 1
                 print(num)
                 name = "{0}/{1}/g{2}{3}.jpg".format(dstDir, folder, num, time.strftime("%m%d%H%M%S"))
                 misc.imsave(name, img)
"""




def shift(img, direction, amount):
    currOption = options[direction]
    amount *= currOption[0]
    axis = currOption[1]
    return np.roll(img, amount, axis)

generate()
