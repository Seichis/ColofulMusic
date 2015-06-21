package com.jupiter.on.tetsuo.colofulmusic;

public class Constants {
    // Parameters
    public final static String[] CLASS_LABELS = {"rock", "pop", "punk", "folk","dance","metal","jazz","classical","hiphop","soulReggae"}; // Should be predefined before collecting data
    public final static String[] ARFF_FILE_NAMES = {"rock.txt", "pop.txt", "punk.txt", "folk.txt","dance.txt","metal.txt","jazz.txt","classical.txt","hiphop.txt","soulReggae.txt"}; // Will be used for model building after merging
    public final static String WORKING_DIR_NAME = "musicRecognition"; // Folder name in SD card


    public final static int DURATION_THREAD_SLEEP = 250; // ms (Should be smaller than step_size, otherwise bottle neck occurs)
    public final static int WINDOW_SIZE = 5000; // ms
    public final static int STEP_SIZE = 2500; // ms

    // File name prefix
    public final static String PREFIX_RAW_DATA = "1_raw_data_";
    public final static String PREFIX_FEATURES = "2_features_";
    public final static String PREFIX_MODEL = "3_model_"; // Not used for this application
    public final static String PREFIX_RESULT = "4_result_";


    // Optional fields
    public final static String HEADER_CLASS_LABEL = "label";
    public final static String HEADER_UNIXTIME = "unix_time";

    // Features (Timbre)
    public final static String HEADER_TIMBRE_1_MEAN = "timbre_1_mean";
    public final static String HEADER_TIMBRE_2_MEAN = "timbre_2_mean";
    public final static String HEADER_TIMBRE_3_MEAN = "timbre_3_mean";
    public final static String HEADER_TIMBRE_4_MEAN = "timbre_4_mean";
    public final static String HEADER_TIMBRE_5_MEAN = "timbre_5_mean";
    public final static String HEADER_TIMBRE_6_MEAN = "timbre_6_mean";
    public final static String HEADER_TIMBRE_7_MEAN = "timbre_7_mean";
    public final static String HEADER_TIMBRE_8_MEAN = "timbre_8_mean";
    public final static String HEADER_TIMBRE_9_MEAN = "timbre_9_mean";
    public final static String HEADER_TIMBRE_10_MEAN = "timbre_10_mean";
    public final static String HEADER_TIMBRE_11_MEAN = "timbre_11_mean";
    public final static String HEADER_TIMBRE_12_MEAN = "timbre_12_mean";

    public final static String HEADER_TIMBRE_1_VARIANCE = "timbre_1_variance";
    public final static String HEADER_TIMBRE_2_VARIANCE = "timbre_2_variance";
    public final static String HEADER_TIMBRE_3_VARIANCE = "timbre_3_variance";
    public final static String HEADER_TIMBRE_4_VARIANCE = "timbre_4_variance";
    public final static String HEADER_TIMBRE_5_VARIANCE = "timbre_5_variance";
    public final static String HEADER_TIMBRE_6_VARIANCE = "timbre_6_variance";
    public final static String HEADER_TIMBRE_7_VARIANCE = "timbre_7_variance";
    public final static String HEADER_TIMBRE_8_VARIANCE = "timbre_8_variance";
    public final static String HEADER_TIMBRE_9_VARIANCE = "timbre_9_variance";
    public final static String HEADER_TIMBRE_10_VARIANCE = "timbre_10_variance";
    public final static String HEADER_TIMBRE_11_VARIANCE = "timbre_11_variance";
    public final static String HEADER_TIMBRE_12_VARIANCE = "timbre_12_variance";

    // List of Features
    public final static String[] LIST_FEATURES = {
            HEADER_TIMBRE_1_MEAN,
            HEADER_TIMBRE_2_MEAN,
            HEADER_TIMBRE_3_MEAN,
            HEADER_TIMBRE_4_MEAN,
            HEADER_TIMBRE_5_MEAN,
            HEADER_TIMBRE_6_MEAN,
            HEADER_TIMBRE_7_MEAN,
            HEADER_TIMBRE_8_MEAN,
            HEADER_TIMBRE_9_MEAN,
            HEADER_TIMBRE_10_MEAN,
            HEADER_TIMBRE_11_MEAN,
            HEADER_TIMBRE_12_MEAN,

            HEADER_TIMBRE_1_VARIANCE,
            HEADER_TIMBRE_2_VARIANCE,
            HEADER_TIMBRE_3_VARIANCE,
            HEADER_TIMBRE_4_VARIANCE,
            HEADER_TIMBRE_5_VARIANCE,
            HEADER_TIMBRE_6_VARIANCE,
            HEADER_TIMBRE_7_VARIANCE,
            HEADER_TIMBRE_8_VARIANCE,
            HEADER_TIMBRE_9_VARIANCE,
            HEADER_TIMBRE_10_VARIANCE,
            HEADER_TIMBRE_11_VARIANCE,
            HEADER_TIMBRE_12_VARIANCE,
    };
}
