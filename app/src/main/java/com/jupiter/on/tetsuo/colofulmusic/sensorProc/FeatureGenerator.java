package com.jupiter.on.tetsuo.colofulmusic.sensorProc;



import com.jupiter.on.tetsuo.colofulmusic.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;


public class FeatureGenerator {

    final static String TAG = "FeatureGenerator";

    public FeatureGenerator() {
        ;
    }


    public static float calculateMean(float[] data) {
        float sum = 0;
        for (float d : data) {
            sum += d;
        }
        return sum / data.length;
    }


    public static float calculateVariance(float[] data) {
        float variance = 0f;
        float mean = 0f;
        float sumMean = 0f;
        float sumVariance = 0f;

        if (data.length > 0) {
            //Calculate the average of the data
            for (int i = 0; i < data.length; i++) {
                sumMean += data[i];
            }

            mean = sumMean / data.length;
            //Calculate variance (standard deviaton ^ 2)
            for (int j = 0; j < data.length; j++) {
                sumVariance += Math.pow(Math.abs(mean - data[j]), 2);
            }
            variance = sumVariance / data.length;
        }
        return variance;
    }


    public static HashMap<String, Float> processAudio(DataInstanceList dl) {

        float[] timbre_1_Aggregated = new float[dl.size()];
        float[] timbre_2_Aggregated = new float[dl.size()];
        float[] timbre_3_Aggregated = new float[dl.size()];
        float[] timbre_4_Aggregated = new float[dl.size()];
        float[] timbre_5_Aggregated = new float[dl.size()];
        float[] timbre_6_Aggregated = new float[dl.size()];
        float[] timbre_7_Aggregated = new float[dl.size()];
        float[] timbre_8_Aggregated = new float[dl.size()];
        float[] timbre_9_Aggregated = new float[dl.size()];
        float[] timbre_10_Aggregated = new float[dl.size()];
        float[] timbre_11_Aggregated = new float[dl.size()];
        float[] timbre_12_Aggregated = new float[dl.size()];

        //double[] magAggregated = new double[dl.size()]; // Data for fft should be double type

        // Ready for calculation
        for (int i = 0; i < dl.size(); i++) {
            float x1 = (dl.get(i)).getValues()[0];
            float x2 = (dl.get(i)).getValues()[1];
            float x3 = (dl.get(i)).getValues()[2];
            float x4 = (dl.get(i)).getValues()[3];
            float x5 = (dl.get(i)).getValues()[4];
            float x6 = (dl.get(i)).getValues()[5];
            float x7 = (dl.get(i)).getValues()[6];
            float x8 = (dl.get(i)).getValues()[7];
            float x9 = (dl.get(i)).getValues()[8];
            float x10 = (dl.get(i)).getValues()[9];
            float x11 = (dl.get(i)).getValues()[10];
            float x12 = (dl.get(i)).getValues()[11];

            timbre_1_Aggregated[i] = x1;
            timbre_2_Aggregated[i] = x2;
            timbre_3_Aggregated[i] = x3;
            timbre_4_Aggregated[i] = x4;
            timbre_5_Aggregated[i] = x5;
            timbre_6_Aggregated[i] = x6;
            timbre_7_Aggregated[i] = x7;
            timbre_8_Aggregated[i] = x8;
            timbre_9_Aggregated[i] = x9;
            timbre_10_Aggregated[i] = x10;
            timbre_11_Aggregated[i] = x11;
            timbre_12_Aggregated[i] = x12;
        }

        float timbre_1_mean = calculateMean(timbre_1_Aggregated);
        float timbre_2_mean = calculateMean(timbre_2_Aggregated);
        float timbre_3_mean = calculateMean(timbre_3_Aggregated);
        float timbre_4_mean = calculateMean(timbre_4_Aggregated);
        float timbre_5_mean = calculateMean(timbre_5_Aggregated);
        float timbre_6_mean = calculateMean(timbre_6_Aggregated);
        float timbre_7_mean = calculateMean(timbre_7_Aggregated);
        float timbre_8_mean = calculateMean(timbre_8_Aggregated);
        float timbre_9_mean = calculateMean(timbre_9_Aggregated);
        float timbre_10_mean = calculateMean(timbre_10_Aggregated);
        float timbre_11_mean = calculateMean(timbre_11_Aggregated);
        float timbre_12_mean = calculateMean(timbre_12_Aggregated);

        float timbre_1_var = calculateVariance(timbre_1_Aggregated);
        float timbre_2_var = calculateVariance(timbre_2_Aggregated);
        float timbre_3_var = calculateVariance(timbre_3_Aggregated);
        float timbre_4_var = calculateVariance(timbre_4_Aggregated);
        float timbre_5_var = calculateVariance(timbre_5_Aggregated);
        float timbre_6_var = calculateVariance(timbre_6_Aggregated);
        float timbre_7_var = calculateVariance(timbre_7_Aggregated);
        float timbre_8_var = calculateVariance(timbre_8_Aggregated);
        float timbre_9_var = calculateVariance(timbre_9_Aggregated);
        float timbre_10_var = calculateVariance(timbre_10_Aggregated);
        float timbre_11_var = calculateVariance(timbre_11_Aggregated);
        float timbre_12_var = calculateVariance(timbre_12_Aggregated);

        // Output variables
        HashMap<String, Float> featureMap = new HashMap<String, Float>();
        featureMap.put(Constants.HEADER_TIMBRE_1_MEAN, timbre_1_mean);
        featureMap.put(Constants.HEADER_TIMBRE_2_MEAN, timbre_2_mean);
        featureMap.put(Constants.HEADER_TIMBRE_3_MEAN, timbre_3_mean);
        featureMap.put(Constants.HEADER_TIMBRE_4_MEAN, timbre_4_mean);
        featureMap.put(Constants.HEADER_TIMBRE_5_MEAN, timbre_5_mean);
        featureMap.put(Constants.HEADER_TIMBRE_6_MEAN, timbre_6_mean);
        featureMap.put(Constants.HEADER_TIMBRE_7_MEAN, timbre_7_mean);
        featureMap.put(Constants.HEADER_TIMBRE_8_MEAN, timbre_8_mean);
        featureMap.put(Constants.HEADER_TIMBRE_9_MEAN, timbre_9_mean);
        featureMap.put(Constants.HEADER_TIMBRE_10_MEAN, timbre_10_mean);
        featureMap.put(Constants.HEADER_TIMBRE_11_MEAN, timbre_11_mean);
        featureMap.put(Constants.HEADER_TIMBRE_12_MEAN, timbre_12_mean);

        featureMap.put(Constants.HEADER_TIMBRE_1_VARIANCE, timbre_1_var);
        featureMap.put(Constants.HEADER_TIMBRE_2_VARIANCE, timbre_2_var);
        featureMap.put(Constants.HEADER_TIMBRE_3_VARIANCE, timbre_3_var);
        featureMap.put(Constants.HEADER_TIMBRE_4_VARIANCE, timbre_4_var);
        featureMap.put(Constants.HEADER_TIMBRE_5_VARIANCE, timbre_5_var);
        featureMap.put(Constants.HEADER_TIMBRE_6_VARIANCE, timbre_6_var);
        featureMap.put(Constants.HEADER_TIMBRE_7_VARIANCE, timbre_7_var);
        featureMap.put(Constants.HEADER_TIMBRE_8_VARIANCE, timbre_8_var);
        featureMap.put(Constants.HEADER_TIMBRE_9_VARIANCE, timbre_9_var);
        featureMap.put(Constants.HEADER_TIMBRE_10_VARIANCE, timbre_10_var);
        featureMap.put(Constants.HEADER_TIMBRE_11_VARIANCE, timbre_11_var);
        featureMap.put(Constants.HEADER_TIMBRE_12_VARIANCE, timbre_12_var);

        return featureMap;

    }


    public static Instances createEmptyInstances(String[] headers, boolean isLabelRequired) {

        FastVector attrs = new FastVector();

        for (String header : headers) {
            attrs.addElement(new Attribute(header));
        }

        if (isLabelRequired) {
            FastVector fv = new FastVector();
            for (String classLabel : Constants.CLASS_LABELS) {
                fv.addElement(classLabel);
            }
            attrs.addElement(new Attribute(Constants.HEADER_CLASS_LABEL, fv));

        }

        String formattedDate = "";
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formattedDate = sdf.format(cal.getTime());
        }

        Instances data = new Instances(formattedDate, attrs, 10000);

        return data;
    }
}
