package com.jupiter.on.tetsuo.colofulmusic.scheduler;

import android.util.Log;

import data.operations.DataOperations;
import data.structure.Genre;


/**
 * Created by User1 on 13/3/2015.
 */
public class Scheduler implements IScheduler {
    private static Scheduler scheduler = new Scheduler();

    public Scheduler() {
    }

    /**
     * @return Scheduler returns the singleton instance of Scheduler
     */
    public static Scheduler getInstance() {
        return scheduler;
    }

    @Override
    public <T extends Genre> T activityStart(T genre) {
        genre.createGenre();
        return genre;
    }

    @Override
    public <T extends Genre> void activityStop(T genre) {
        genre.concludeGenre();

        Log.i("Scheduler", "writing to json");
        String JSONstring = DataOperations.getInstance().genreToJSON(genre);

        DataOperations.getInstance().writeToMusicGenreFile(JSONstring);
        Log.i("Scheduler", "writing to json completed");
    }
}
