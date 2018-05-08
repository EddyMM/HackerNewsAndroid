package emlearn.hackernews.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import emlearn.hackernews.fragments.StoryFragment;

public class StoryActivity extends SingleFragmentActivity {
    public static final String EXTRA_STORY_ID = StoryActivity.class.getCanonicalName()
            + "extra_story_id";

    @Override
    protected Fragment createFragment() {
        int storyId = getIntent().getIntExtra(EXTRA_STORY_ID,-1);
        return StoryFragment.newInstance(storyId);
    }

    /**
     * Create an intent to open this activity while sending the story id it should display
     * @param packageContext Context associated with activity sending intent
     * @param storyId ID of story to display
     * @return Intent to start this activity
     */
    public static Intent createIntent(Context packageContext, int storyId) {
        Intent intent = new Intent(packageContext, StoryActivity.class);
        intent.putExtra(StoryActivity.EXTRA_STORY_ID, storyId);
        return  intent;
    }
}
