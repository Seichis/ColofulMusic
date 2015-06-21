package com.jupiter.on.tetsuo.colofulmusic;

import android.util.Log;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

public class J48Wrapper extends WekaWrapper {
	
	Classifier classifier = null;
	
	@Override
	public void train(Instances instances) {
		try {
			classifier = new J48();
			classifier.buildClassifier(instances);
			instancesForTraining = instances;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "Train() error : " + e.getMessage());
			classifier = null;
		}
	}

	@Override
	public String predict(Instance instance) {
		String resultClass = null;
		try {
			Object[] s = new Object[instance.numAttributes()];

			for (int j = 0; j < s.length; j++) {
				if (!instance.isMissing(j)) {
					if (instance.attribute(j).isNominal())
						s[j] = new String(instance.stringValue(j));
					else if (instance.attribute(j).isNumeric())
						s[j] = new Double(instance.value(j));
				}
			}

			// set class value to missing
			s[instance.classIndex()] = null;
			Log.i(TAG, "Instance added : " + DecisionTreeClassifier.classify(s));
			double result = DecisionTreeClassifier.classify(s);
			resultClass = String.valueOf(result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultClass;
	}


}
