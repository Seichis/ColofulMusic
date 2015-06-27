package com.jupiter.on.tetsuo.colofulmusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.jupiter.on.tetsuo.colofulmusic.scheduler.Scheduler;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.Constants;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.DataInstance;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.DataInstanceList;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.FeatureGenerator;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.J48Wrapper;
import com.jupiter.on.tetsuo.colofulmusic.sensorProc.SlidingWindow;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.SilenceDetector;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.mfcc.MFCC;
import data.structure.Classical;
import data.structure.Dance;
import data.structure.Folk;
import data.structure.Genre;
import data.structure.HipHop;
import data.structure.Jazz;
import data.structure.Metal;
import data.structure.Pop;
import data.structure.Punk;
import data.structure.Rock;
import data.structure.SoulReggae;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class AudioClassificationService extends Service {
    final static String TAG = "MainActivity";
    final static String file1 = "test1.txt";
    final static String file2 = "test2.txt";
    final static String file3 = "test3.txt";
    public static boolean isOn;
    static int fileNumber = 1;
    private static MainRunnable mainRunnable; // Stores calculated feature to ARFF file, and optionally classifies instances
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
    SharedPreferences prefs;
    // Sliding window
    private SlidingWindow slidingWindow; // For extracting samples by window

    //    public AudioClassificationService() {
//    }
    @Override
    public void onCreate() {
        super.onCreate();
        isCollecting = false;
        changeSaveFile = false;
        mJ48Wrapper = new J48Wrapper();
        isOn = true;
        mainRunnable = new MainRunnable();
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
        isOn = false;
    }

    public void startDataCollection() {
        startCollectingTimestamp = System.currentTimeMillis();
        mTimer = new Timer();
        mHandler = new Handler();
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

    public void changeColorToMusic(int musicGenre) {
        prefs = MainActivity.getMainActivity().getSharedPreferences(Constants.MY_PREFS_NAME, 0);

        //Restore saved IP if there is one

        // get the pin number
        String parameterValue = String.valueOf(musicGenre);
        // get the ip address
        String ipAddress = "192.168.0.102";
        // get the port number
        String portNumber = "80";
        String restoredText = prefs.getString("ip", null);
        if (restoredText != null) {
            ipAddress = prefs.getString("ip", "192.168.0.102");//"192.168.0.102" is the default value.
            portNumber = prefs.getString("portNumber", "80"); // "80" is the default value.
        }

        // execute HTTP request
        if (ipAddress.length() > 0 && portNumber.length() > 0) {
            new HttpRequestAsyncTask(
                    MainActivity.getMainActivity(), parameterValue, ipAddress, portNumber, "genre"
            ).execute();
        }
    }

    /**
     * Description: Send an HTTP Get request to a specified ip address and port.
     * Also send a parameter "parameterName" with the value of "parameterValue".
     *
     * @param parameterValue the pin number to toggle
     * @param ipAddress      the ip address to send the request to
     * @param portNumber     the port number of the ip address
     * @param parameterName
     * @return The ip address' reply text, or an ERROR message is it fails to receive one
     */
    public String sendRequest(String parameterValue, String ipAddress, String portNumber, String parameterName) {
        String serverResponse = "ERROR";

        try {
            // create an HTTP client
            // define the URL e.g. http://myIpaddress:myport/?pin=13 (to toggle pin 13 for example)
            URL website = new URL("http://" + ipAddress + ":" + portNumber + "/?" + parameterName + "=" + parameterValue);
            HttpURLConnection conn = (HttpURLConnection) website.openConnection();
            conn.setRequestMethod("GET");

            System.out.println("Response Code: " + conn.getResponseCode());
            InputStream content = new BufferedInputStream(conn.getInputStream());


            BufferedReader in = new BufferedReader(new InputStreamReader(
                    content
            ));
            serverResponse = in.readLine();
            // Close the connection
            content.close();
        } catch (IOException e) {
            // IO error
            serverResponse = e.getMessage();
            e.printStackTrace();
        }
        // return the server's reply/response text
        return serverResponse;
    }

    public class MainRunnable extends TimerTask {

        private final static String TAG = "TimerThread";
        String prediction, previousPrediction;
        float tempPrediction;
        int genreDuration;
        int intPrediction;
        // Data collection (Always)
        private Instances instancesForDataCollection;

        public MainRunnable() {
            initializeSignalProcessing();
            this.prediction = "";
            this.previousPrediction = "";
            this.tempPrediction = 0;
            this.genreDuration = 0;

        }


        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isCollecting) {
                        currentTimestamp = System.currentTimeMillis();
                        if (currentTimestamp - startCollectingTimestamp >= 60000) {
                            genreDuration += 1;
                            if (fileNumber == 2) {
                                mainRunnable.saveInstancesToArff(mainRunnable.getInstances(), file3);
                                Instances mInstances = mJ48Wrapper.loadInstancesFromArffFile(file3);

                                for (int i = 1; i < mInstances.numInstances(); i++) {
                                    prediction = mJ48Wrapper.predict(mInstances.instance(i));
                                    tempPrediction = Float.parseFloat(prediction);
                                    previousPrediction = prediction;
                                    intPrediction = (int) tempPrediction;
                                    Log.e(TAG, "Prediction " + intPrediction);
                                }

                                fileNumber = 3;
                            } else if (fileNumber == 1) {
                                mainRunnable.saveInstancesToArff(mainRunnable.getInstances(), file2);
                                Instances mInstances = mJ48Wrapper.loadInstancesFromArffFile(file2);

                                for (int i = 1; i < mInstances.numInstances(); i++) {
                                    prediction = mJ48Wrapper.predict(mInstances.instance(i));
                                    tempPrediction = Float.parseFloat(prediction);
                                    intPrediction = (int) tempPrediction;
                                    Log.e(TAG, "Prediction " + intPrediction);

                                }
                                fileNumber = 2;
                            } else if (fileNumber == 3) {
                                mainRunnable.saveInstancesToArff(mainRunnable.getInstances(), file1);
                                Instances mInstances = mJ48Wrapper.loadInstancesFromArffFile(file1);

                                for (int i = 1; i < mInstances.numInstances(); i++) {
                                    prediction = mJ48Wrapper.predict(mInstances.instance(i));
                                    tempPrediction = Float.parseFloat(prediction);
                                    intPrediction = (int) tempPrediction;
                                    Log.e(TAG, "Prediction " + intPrediction);

                                }
                                fileNumber = 1;
                            }

                            instancesForDataCollection = null;
                            startCollectingTimestamp = System.currentTimeMillis();
                            if (!previousPrediction.equals(prediction) || genreDuration>12000) {
                                saveGenre(intPrediction, genreDuration);// save to the json file
                                changeColorToMusic(intPrediction);
                                genreDuration=0;
                                previousPrediction = prediction;
                            }
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

        //rock,punk,folk,pop,dance,metal,jazz,classical,hiphop,soulReggae
        private void saveGenre(int prediction, int duration) {
            switch (prediction) {
                case 0:
                    Genre rock = new Rock();
                    Scheduler.getInstance().activityStart(rock);
                    rock.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(rock);
                    break;
                case 1:
                    Genre punk = new Punk();
                    Scheduler.getInstance().activityStart(punk);
                    punk.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(punk);
                    break;
                case 2:
                    Genre folk = new Folk();
                    Scheduler.getInstance().activityStart(folk);
                    folk.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(folk);
                    break;
                case 3:
                    Genre dance = new Dance();
                    Scheduler.getInstance().activityStart(dance);
                    dance.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(dance);
                    break;

                case 4:
                    Genre pop = new Pop();
                    Scheduler.getInstance().activityStart(pop);
                    pop.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(pop);
                    break;
                case 5:
                    Genre metal = new Metal();
                    Scheduler.getInstance().activityStart(metal);
                    metal.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(metal);
                    break;
                case 6:
                    Genre jazz = new Jazz();
                    Scheduler.getInstance().activityStart(jazz);
                    jazz.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(jazz);
                    break;
                case 7:
                    Genre classical = new Classical();
                    Scheduler.getInstance().activityStart(classical);
                    classical.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(classical);
                    break;
                case 8:
                    Genre hiphop = new HipHop();
                    Scheduler.getInstance().activityStart(hiphop);
                    hiphop.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(hiphop);
                    break;
                case 9:
                    Genre reggae = new SoulReggae();
                    Scheduler.getInstance().activityStart(reggae);
                    reggae.setMusicDuration(duration);
                    Scheduler.getInstance().activityStop(reggae);
                    break;
                default:
                    return;
            }

        }

        public Instances getInstances() {
            return instancesForDataCollection;
        }

        public void initializeSignalProcessing() {
            // Sliding window

            slidingWindow = new SlidingWindow(Constants.WINDOW_SIZE, Constants.STEP_SIZE);
            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, 0);
            mfcc = new MFCC(bufferSize, sampleRate, 12, 40, 133.3334F, (float) sampleRate / 2);
            dispatcher.addAudioProcessor(mfcc);
            dispatcher.addAudioProcessor(new AudioProcessor() {

                @Override
                public void processingFinished() {
                }

                @Override
                public boolean process(AudioEvent audioEvent) {

                    mfcc.process(audioEvent);
                    Log.i("mfcc", "coefficients" + mfcc.getMFCC()[0] + "center frequencies  ");
                    DataInstance diAudio = new DataInstance(System.currentTimeMillis(), mfcc.getMFCC());
                    slidingWindow.input(diAudio);
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

    /**
     * An AsyncTask is needed to execute HTTP requests in the background so that they do not
     * block the user interface.
     */
    private class HttpRequestAsyncTask extends AsyncTask<Void, Void, Void> {

        // declare variables needed
        private String requestReply, ipAddress, portNumber;
        private Context context;
        //private AlertDialog alertDialog;
        private String parameter;
        private String parameterValue;

        /**
         * Description: The asyncTask class constructor. Assigns the values used in its other methods.
         *
         * @param context        the application context, needed to create the dialog
         * @param parameterValue the pin number to toggle
         * @param ipAddress      the ip address to send the request to
         * @param portNumber     the port number of the ip address
         */
        public HttpRequestAsyncTask(Context context, String parameterValue, String ipAddress, String portNumber, String parameter) {
            this.context = context;
            this.ipAddress = ipAddress;
            this.parameterValue = parameterValue;
            this.portNumber = portNumber;
            this.parameter = parameter;
        }

        /**
         * Name: doInBackground
         * Description: Sends the request to the ip address
         *
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            requestReply = sendRequest(parameterValue, ipAddress, portNumber, parameter);
            return null;
        }

        /**
         * Name: onPostExecute
         * Description: This function is executed after the HTTP request returns from the ip address.
         * The function sets the dialog's message with the reply text from the server and display the dialog
         * if it's not displayed already (in case it was closed by accident);
         *
         * @param aVoid void parameter
         */
        @Override
        protected void onPostExecute(Void aVoid) {
        }

        /**
         * Name: onPreExecute
         * Description: This function is executed before the HTTP request is sent to ip address.
         * The function will set the dialog's message and display the dialog.
         */
        @Override
        protected void onPreExecute() {

        }

    }
}




