# ColofulMusic

This is an Android application which handles music genre classification data 
when the feature is enabled and provides short and long 
term visualizations. 

All data collected is stored for later 
usage, for example visualizations and data mining. The 
complete architecture of the system is shown below.
The AudioClassificationService.class is a service, which 
initializes the audio processing when the user enables the 
feature from the HomeFragment.class. At first it registers 
the microphone and “listens” to the environment. The input 
of the microphone is sampled and the features are extracted. 

The extracted features are saved in .arff format, in order to 
be the input of the trained model, the 
DecisionClassifierTree.class. The model then predicts the 
genre of the sound and two processes take place. The 
DataOperations.class through the Scheduler.class handles 
the saving of this instance in the backend, in this case for 
prototyping purposes a JSON file. In parallel the 
AudioClassificationService.class sends with an HTTP 
request the prediction to the Arduino which in turn reads it, 
implemented in the sketch, and changes the color of the 
RGB LED. Then according to what genre the user wants to 
explore the relevant data are loaded from the JSON file and 
the Google Graphs API is employed on an Android webview
view in the HistoryFragment.class, to provide the graphical
visualizations. 

A more scientific view of this project's purposesis discussed in the report at this link : https://drive.google.com/open?id=0B4xOZ3AckVcxOFFtbHVBcmRzcGc
