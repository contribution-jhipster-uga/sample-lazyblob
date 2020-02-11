from imageai.Detection import ObjectDetection
import os
import sys
import numpy as np

if len(sys.argv) != 2:
    raise Exception('Invalid number of arguments. 1 argument is required.')

temp_path = str(sys.argv[0]).split("/")
execution_path = '/'.join(temp_path[:-1])
if execution_path:
    execution_path += str('/')

im_name = str(sys.argv[1])
nn_name = 'resnet50_coco_best_v2.0.1.h5'
threshold = 70


detector = ObjectDetection()
detector.setModelTypeAsRetinaNet()
detector.setModelPath( os.path.join(execution_path , nn_name))
detector.loadModel()
detections = detector.detectObjectsFromImage(input_image=im_name, output_image_path=os.path.join(execution_path , "output.jpg") , minimum_percentage_probability=threshold)

for eachObject in detections:
    #print(eachObject["name"] , " : ", eachObject["percentage_probability"], " : ", eachObject["box_points"] )
    print(eachObject["name"])
