package data.structure;

import java.util.GregorianCalendar;

/**
 * Created by User1 on 20/6/2015.
 */
public abstract class Genre {

    String genreType;
    GregorianCalendar startTimestamp;
    GregorianCalendar endTimestamp;


    int musicDuration;

    public <T extends Genre> T createGenre() {
        this.startTimestamp = new GregorianCalendar();
        this.musicDuration = 0;
        return null;
    }

    public <T extends Genre> void concludeGenre() {
        startTimestamp = getStartTimestamp();
        this.endTimestamp = new GregorianCalendar();
        this.musicDuration = (int) ((endTimestamp.getTimeInMillis() - startTimestamp.getTimeInMillis())/60000);
    }

    public GregorianCalendar getStartTimestamp() {
        return this.startTimestamp;
    }

    public GregorianCalendar getEndTimestamp() {
        return this.endTimestamp;
    }

    public String getGenreType() {
        return this.genreType;
    }

    public int getMusicDuration() {
        return musicDuration;
    }
}
