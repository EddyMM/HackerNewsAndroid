package emlearn.hackernews.model;

import java.util.Hashtable;
import java.util.List;

/**
 * @author eddy.
 */

public class StoryKeeper {
    private static StoryKeeper sStoryKeeper;

    private StoryKeeper() {}
    private static Hashtable<Integer, Story> mStories;

    public StoryKeeper getInstance() {
        if(sStoryKeeper == null) {
            sStoryKeeper = new StoryKeeper();
        }

        return sStoryKeeper;
    }

    public static void addStories(Hashtable<Integer, Story> stories) {
        mStories = stories;
    }

    public static Hashtable<Integer, Story> getStories() {
        return mStories;
    }

    public static Story getStory(int storyId) {
        return mStories.get(storyId);
    }
}
