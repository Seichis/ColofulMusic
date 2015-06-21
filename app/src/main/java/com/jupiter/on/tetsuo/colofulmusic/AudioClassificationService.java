package com.jupiter.on.tetsuo.colofulmusic;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jupiter.on.tetsuo.colofulmusic.sensorProc.DataInstance;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.DataInstanceList;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.FeatureGenerator;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.SlidingWindow;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.SilenceDetector;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.mfcc.MFCC;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class AudioClassificationService extends Service {
    final static String TAG = "MainActivity";
    final static String file1 = "test1.txt";
    final static String file2 = "test2.txt";
    final static String file3 = "test3.txt";
    static int fileNumber = 1;
    int countSilence;
    int sampleRate = 44100;
    int bufferSize = 2048;
    Timer mTimer;
    Handler mHandler;
    boolean changeSaveFile;
    SilenceDetector mSilenceDetector;
    long startCollectingTimestamp, currentTimestamp;
    // Raw Data Acquisition
    AudioDispatcher dispatcher;
    Thread thread;
    MFCC mfcc;
    boolean isCollecting;
    J48Wrapper mJ48Wrapper;
    // Sliding window
    private SlidingWindow slidingWindow; // For extracting samples by window
    private MainRunnable mainRunnable; // Stores calculated feature to ARFF file, and optionally classifies instances


//    public AudioClassificationService() {
//    }
    @Override
    public void onCreate() {
        super.onCreate();
        isCollecting = false;
        countSilence = 0;
        changeSaveFile = false;
        mJ48Wrapper = new J48Wrapper();
        startDataCollection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        finishDataCollection();
    }
    public void startDataCollection() {
        startCollectingTimestamp = System.currentTimeMillis();
        mTimer = new Timer();
        mHandler = new Handler();
        mainRunnable = new MainRunnable();
        mTimer.scheduleAtFixedRate(mainRunnable, 0, Constants.DURATION_THREAD_SLEEP);
        isCollecting = true;
    }

    public void finishDataCollection() {

        // Cleaning
        mTimer.cancel();
        mTimer = null;
        dispatcher.stop();
        dispatcher = null;
        thread.interrupt();
        thread = null;

    }



    public class MainRunnable extends TimerTask {

        private final static String TAG = "TimerThread";

        // Data collection (Always)
        private Instances instancesForDataCollection;

        public MainRunnable() {
            initializeSignalProcessing();
        }


        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isCollecting) {
                        currentTimestamp = System.currentTimeMillis();
                        if (currentTimestamp - startCollectingTimestamp >= 20000) {
                            if (fileNumber == 2) {
                                mainRunnable.saveInstancesToArff(mainRunnable.getInstances(), file3);
                                Instances mInstances = mJ48Wrapper.loadInstancesFromArffFile(file3);

                                for (int i = 1; i < mInstances.numInstances(); i++) {
                                    String prediction = mJ48Wrapper.predict(mInstances.instance(i));
                                    Log.e(TAG, "Prediction " + prediction);
                                }

                                fileNumber = 3;
                            } else if (fileNumber == 1) {
                                mainRunnable.saveInstancesToArff(mainRunnable.getInstances(), file2);
                                Instances mInstances = mJ48Wrapper.loadInstancesFromArffFile(file2);

                                for (int i = 1; i < mInstances.numInstances(); i++) {
                                    String prediction = mJ48Wrapper.predict(mInstances.instance(i));
                                    Log.e(TAG, "Prediction " + prediction);

                                }
                                fileNumber = 2;
                            } else if (fileNumber == 3) {
                                mainRunnable.saveInstancesToArff(mainRunnable.getInstances(), file1);
                                Instances mInstances = mJ48Wrapper.loadInstancesFromArffFile(file1);

                                for (int i = 1; i < mInstances.numInstances(); i++) {
                                    String prediction = mJ48Wrapper.predict(mInstances.instance(i));
                                    Log.e(TAG, "Prediction " + prediction);

                                }
                                fileNumber = 1;
                            }

                            instancesForDataCollection = null;
                            startCollectingTimestamp = System.currentTimeMillis();
                        }
                        // Reusable buffer
                        DataInstanceList dlAudio;

                        // Fetching a slices of sliding window
                        dlAudio = slidingWindow.output();

                        if (dlAudio != null) {

                            // Calculate features (without class label)
                            HashMap<String, Float> featureMapAudio =
                                    FeatureGenerator.processAudio(dlAudio);

                            // Generating header
                            if (instancesForDataCollection == null) {
                                String[] featureHeader = Constants.LIST_FEATURES;
                                instancesForDataCollection =
                                        FeatureGenerator.createEmptyInstances(featureHeader, false); // makeClassLabel);
                            }

                            // Aggregate features in single Weka instance
                            int attributeSize = featureMapAudio.size() + 1;
                            Instance instance = new Instance(attributeSize); // including class classLabel

                            // Filling features for accelerometer
                            for (String feature : featureMapAudio.keySet()) {
                                float value = featureMapAudio.get(feature);
                                Attribute attr = instancesForDataCollection.attribute(feature);
                                instance.setValue(attr, value);
                            }

                            // Add generated Instance
                            instancesForDataCollection.add(instance); // Final calculated feature set
                        }
                    }
                }

            });
        } // End of run()

        public Instances getInstances() {
            return instancesForDataCollection;
        }

        public void initializeSignalProcessing() {
            // Sliding window

            slidingWindow = new SlidingWindow(Constants.WINDOW_SIZE, Constants.STEP_SIZE);
            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, 0);
            mfcc = new MFCC(bufferSize, sampleRate, 12, 30, 133.3334F, (float) sampleRate / 2);
            dispatcher.addAudioProcessor(mfcc);
            dispatcher.addAudioProcessor(new AudioProcessor() {
                long startStamp=System.currentTimeMillis();
                long currentStamp;

                @Override
                public void processingFinished() {

                }

                @Override
                public boolean process(AudioEvent audioEvent) {
                    currentStamp = System.currentTimeMillis();
                    if (audioEvent.isSilence(-70) && currentStamp-startStamp>10000 ) {
                        finishDataCollection();
                        isCollecting = false;
                        Log.i(TAG, "counter" + countSilence);
                    } else {
                        mfcc.process(audioEvent);
                        Log.i("mfcc", "coefficients" + mfcc.getMFCC()[0] + "center frequencies  ");
                        DataInstance diAudio = new DataInstance(System.currentTimeMillis(), mfcc.getMFCC());
                        slidingWindow.input(diAudio);
                    }

                    return true;
                }
            });

            thread = new Thread() {
                @Override
                public void run() {
                    dispatcher.run();
                }
            };
            thread.start();

        }

        public void saveInstancesToArff(Instances instances, String fileName) {

            try {
                ArffSaver saver = new ArffSaver();

                saver.setInstances(instances);
                String dirPath = Environment.getExternalStorageDirectory() + "/" + Constants.WORKING_DIR_NAME;
                String filePath = dirPath + "/" + fileName;

                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }

                saver.setFile(new File(filePath));
                saver.writeBatch();
                Log.i(TAG, "Arff saved : " + filePath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "saveInstancesToArff() error : " + e.getMessage());
            }
        }


    }
}




