package data.operations;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.StaticMusicType;
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


public class DataOperations {
    private static DataOperations dataOPS = new DataOperations();

    private DataOperations() {
    }

    /**
     * @return DataOperations
     * Returns the DataOperations singleton instance
     */
    public static DataOperations getInstance() {
        return dataOPS;
    }

    /**
     * @param content Writes JSON data to musicGenre file
     */
    public void writeToMusicGenreFile(String content) {
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/musicGenre.json");
            if (!file.exists())
                file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content + "\n");
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * @return JSONlist
     * @throws IOException Reads all the JSON data from the musicGenre file to a list of strings
     */
    public List<String> readFromMusicGenreFile() {

        BufferedReader br;
        List<String> JSONlist = new ArrayList<>();
        String line;
        try {
            String fpath = Environment.getExternalStorageDirectory().getPath()
                    + "/musicGenre.json";
            br = new BufferedReader(new FileReader(fpath));
            while ((line = br.readLine()) != null) {
                JSONlist.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JSONlist;
    }

    /**
     * @param EX
     * @return JSON
     * Converts a Genre object to a JSON string
     */
    public <T extends Genre> String genreToJSON(T EX) {
        Gson gson = new GsonBuilder().create();
        String JSONtemp = (gson.toJson(EX));
        return JSONtemp;
    }

    public List<Genre> getGenreListFromJSON(String genreType) {
        List<String> mTaskStringList = readFromMusicGenreFile();
        Gson gson = new GsonBuilder().create();
        List<Genre> mGenreList = new ArrayList<>();
        switch (genreType) {
            case StaticMusicType.rock:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType)) {
                        mGenreList.add(gson.fromJson(tp, Rock.class));
                        Log.i("LIST", mGenreList.get(0).toString());
                    }
                }
                break;
            case StaticMusicType.punk:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, Punk.class));

                }
                break;
            case StaticMusicType.folk:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, Folk.class));
                }
                break;
            case StaticMusicType.dance:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, Dance.class));
                }
                break;
            case StaticMusicType.hipHop:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, HipHop.class));
                }
                break;
            case StaticMusicType.pop:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, Pop.class));
                }
                break;
            case StaticMusicType.reggae:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, SoulReggae.class));
                }
                break;
            case StaticMusicType.metal:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, Metal.class));
                }
                break;
            case StaticMusicType.classical:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, Classical.class));
                }
                break;
            case StaticMusicType.jazz:
                for (String tp : mTaskStringList) {
                    if (tp.contains(genreType))
                        mGenreList.add(gson.fromJson(tp, Jazz.class));
                }
                break;
        }


        return mGenreList;
    }


    public String javascriptFeedbackToJson(String[] data) {

        Gson gson = new GsonBuilder().create();
        String JSONtemp = (gson.toJson(data));
        return JSONtemp;

    }


    public String[] prepareMusicDurationForJavascript(String taskType) {

        List<Genre> DL = getGenreListFromJSON(taskType);

        String[] mScoreStrings = new String[DL.size()];

        if (!DL.isEmpty())
            for (Genre ex : DL) {
                mScoreStrings[DL.indexOf(ex)] = Integer.toString(ex.getMusicDuration());
            }

        return mScoreStrings;
    }

    public String[] prepareAggregatedMusicDurationForJavascript() {
        List<String> genreList = new ArrayList<>();
        String[] mDurationStrings = new String[10];
        genreList.add(StaticMusicType.classical);
        genreList.add(StaticMusicType.reggae);
        genreList.add(StaticMusicType.rock);
        genreList.add(StaticMusicType.metal);
        genreList.add(StaticMusicType.folk);
        genreList.add(StaticMusicType.hipHop);
        genreList.add(StaticMusicType.dance);
        genreList.add(StaticMusicType.jazz);
        genreList.add(StaticMusicType.pop);
        genreList.add(StaticMusicType.punk);

        for(String genre : genreList){
            List<Genre> DL = getGenreListFromJSON(genre);
            int duration=0;
            if (!DL.isEmpty())
                for (Genre ex : DL) {
                    duration+=ex.getMusicDuration();
                }
            mDurationStrings[genreList.indexOf(genre)] = Integer.toString(duration);
        }

        return mDurationStrings;
    }

    public String getAggregateMusicGenreData(String genre){

        List<Genre> DL = getGenreListFromJSON(genre);
        int durationSum=0;
        if (!DL.isEmpty())
            for (Genre ex : DL) {
                durationSum+=ex.getMusicDuration();
            }
        return String.valueOf(durationSum);
    }
}
