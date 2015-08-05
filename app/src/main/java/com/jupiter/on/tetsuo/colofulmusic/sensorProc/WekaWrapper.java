package com.jupiter.on.tetsuo.colofulmusic.sensorProc;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instance;
import weka.core.Instances;

public abstract class WekaWrapper {

    protected final static String TAG = "ClassifierWrapper";
    protected Instances instancesForTraining = null;

    public static Instances loadInstancesFromArffFile(String fileName) {

        String dirPath =
                Environment.getExternalStorageDirectory()
                        + "/"
                        + Constants.WORKING_DIR_NAME;
        String filePath = dirPath + "/" + fileName;

        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader(filePath));
            Instances data = new Instances(reader);
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "loadInstancesFromArffFile() error : " + e.getMessage());
        }

        return null;
    }

    public Instances getInstances() {
        return instancesForTraining;
    }

    public abstract void train(Instances instances);

    public abstract String predict(Instance instance);

}


//import weka.core.Attribute;
//import weka.core.Capabilities;
//import weka.core.Capabilities.Capability;
//import weka.core.Instance;
//import weka.core.Instances;
//import weka.core.RevisionUtils;
//import weka.classifiers.Classifier;
//
//
//public class WekaWrapper
//        extends Classifier {
//
//    /**
//     * Returns only the toString() method.
//     *
//     * @return a string describing the classifier
//     */
//    public String globalInfo() {
//        return toString();
//    }
//
//    /**
//     * Returns the capabilities of this classifier.
//     *
//     * @return the capabilities
//     */
//    public Capabilities getCapabilities() {
//        weka.core.Capabilities result = new weka.core.Capabilities(this);
//
//        result.enable(weka.core.Capabilities.Capability.NOMINAL_ATTRIBUTES);
//        result.enable(weka.core.Capabilities.Capability.NUMERIC_ATTRIBUTES);
//        result.enable(weka.core.Capabilities.Capability.DATE_ATTRIBUTES);
//        result.enable(weka.core.Capabilities.Capability.MISSING_VALUES);
//        result.enable(weka.core.Capabilities.Capability.NOMINAL_CLASS);
//        result.enable(weka.core.Capabilities.Capability.MISSING_CLASS_VALUES);
//
//
//        result.setMinimumNumberInstances(0);
//
//        return result;
//    }
//
//    /**
//     * only checks the data against its capabilities.
//     *
//     * @param i the training data
//     */
//    public void buildClassifier(Instances i) throws Exception {
//        // can classifier handle the data?
//        getCapabilities().testWithFail(i);
//    }
//
//    /**
//     * Classifies the given instance.
//     *
//     * @param i the instance to classify
//     * @return the classification result
//     */
//    public double classifyInstance(Instance i) throws Exception {
//        Object[] s = new Object[i.numAttributes()];
//
//        for (int j = 0; j < s.length; j++) {
//            if (!i.isMissing(j)) {
//                if (i.attribute(j).isNominal())
//                    s[j] = new String(i.stringValue(j));
//                else if (i.attribute(j).isNumeric())
//                    s[j] = new Double(i.value(j));
//            }
//        }
//
//        // set class value to missing
//        s[i.classIndex()] = null;
//
//        return DecisionTreeClassifier.classify(s);
//    }
//
//    /**
//     * Returns the revision string.
//     *
//     * @return        the revision
//     */
//    public String getRevision() {
//        return RevisionUtils.extract("1.0");
//    }
//
//    /**
//     * Returns only the classnames and what classifier it is based on.
//     *
//     * @return a short description
//     */
//    public String toString() {
//        return "Auto-generated classifier wrapper, based on weka.classifiers.trees.J48 (generated with Weka 3.7.12).\n" + this.getClass().getName() + "/DecisionTreeClassifier";
//    }

