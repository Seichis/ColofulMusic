package com.jupiter.on.tetsuo.colofulmusic.scheduler;

import data.structure.Genre;


/**
 * Created by User1 on 13/3/2015.
 */
public interface IScheduler {
    /**
     * @param genre
     * @return exercise
     * This method receives an exercise instance and adds it to active genre list
     */
    public <T extends Genre> T activityStart(T genre);

    /**
     * @param genre
     *
     */
    public <T extends Genre> void activityStop(T genre);
}
