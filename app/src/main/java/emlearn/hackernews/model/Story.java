package emlearn.hackernews.model;

import android.support.annotation.Nullable;

/**
 * @author eddymwenda
 * @since 29/04/2018.
 */

public class Story {
    private String by, title, text;
    private int id, score;
    private long time;

    public int getId() {
        return id;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Nullable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return String.format("Story[by=%s, title=%s]", this.by, this.title);
    }
}
