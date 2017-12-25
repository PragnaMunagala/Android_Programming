from flask import Flask
from flask import render_template
from flask_cors import CORS, cross_origin
import json
import psycopg2
import pprint
import os
import sys
import string
import collections
from bson import json_util
from bson.json_util import dumps
from flask import request
import pickle
import socket
import fcntl
import struct

app = Flask(__name__)
CORS(app)

def load_obj(file):
    with open(file,'rb') as input:
        obj = pickle.load(input)
    return obj

def predictUser(input):
    clf = load_obj('decision_tree.pkl')
    arr = open(input)
    x = [[] for i in range(2)]
    i=0
    for line in arr:
        x[i]=line.strip().split(',')
        i=i+1 
    float_list = [[] for j in range(2)]
    for k in range(0,2):
        float_list[k]=[float(l) for l in x[k]]
    result =  list(clf.predict(float_list))
    print "From classifier"
    print result
    return result

@app.route("/")
def index():
    return "hello"
 
@app.route('/classify',methods=['GET', 'POST'])
def classify():
    values = request.form.to_dict()
    st=""
    for key,val in values.iteritems():
        st = st+key
    text_file = open("user.txt","w")
    text_file.write(st)
    text_file.write("\n")
    text_file.write(st)
    text_file.close()
    result = []
    result = predictUser('user.txt')
    print result
    return str(result[0])

if __name__ == "__main__":
    app.run(host="10.144.184.89",port=5000,debug=True)