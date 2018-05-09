package emlearn.hackernews.model;

import java.util.LinkedHashMap;

/**
 * @author eddy.
 */

public class StoryKeeper {
    private static StoryKeeper sStoryKeeper;

    private StoryKeeper() {}
    private static LinkedHashMap<Integer, Story> mStories = new LinkedHashMap<>();

    public static StoryKeeper getInstance() {
        if(sStoryKeeper == null) {
            sStoryKeeper = new StoryKeeper();
        }

        return sStoryKeeper;
    }

    public void addStories(LinkedHashMap<Integer, Story> stories) {
        mStories = stories;
    }

    public LinkedHashMap<Integer, Story> getStories() {
        return mStories;
    }

    public Story getStory(int storyId) {
        return mStories.get(storyId);
    }
}
