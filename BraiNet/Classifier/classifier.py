
import pandas as pd
import numpy as np 
from sklearn.naive_bayes import GaussianNB
from sklearn.svm import SVC
from sklearn.model_selection import cross_val_score
from sklearn.tree import DecisionTreeClassifier
clf = DecisionTreeClassifier()
def save_obj(obj, filename):
	with open(filename, 'wb') as output:
		pickle.dump(obj, output, pickle.HIGHEST_PROTOCOL)
X = pd.read_csv('finaldataset.csv')#np.genfromtxt('finaldataset.csv',delimiter = ",")

X = X.values
print X.shape	
print X[:,:64].shape
print X[:,64]
y_pred = clf.fit(X[:,:64], X[:,64]).predict(X[:,:64])
print y_pred
print set(y_pred)
print("Number of mislabeled points out of a total %d points : %d"% (X[:,:64].shape[0],(X[:,64] != y_pred).sum()))
save_obj(clf, 'decision_tree.pkl')