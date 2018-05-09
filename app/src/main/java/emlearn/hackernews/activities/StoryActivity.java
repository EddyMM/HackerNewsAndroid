package emlearn.hackernews.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import emlearn.hackernews.R;
import emlearn.hackernews.fragments.StoryFragment;
import emlearn.hackernews.model.Story;
import emlearn.hackernews.model.StoryKeeper;

public class StoryActivity extends AppCompatActivity {
    public static final String EXTRA_STORY_ID = StoryActivity.class.getCanonicalName()
            + "extra_story_id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        int storyId = getIntent().getIntExtra(EXTRA_STORY_ID, -1);

        ViewPager storyViewPager = findViewById(R.id.storyViewPager);
        FragmentManager fm = getSupportFragmentManager();

        LinkedHashMap<Integer, Story> stories = StoryKeeper.getInstance().getStories();

        storyViewPager.setAdapter(new StoryViewPager(fm, stories));
        storyViewPager.setCurrentItem(getStoryPosition(stories, storyId));
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

    private int getStoryPosition(LinkedHashMap<Integer, Story> stories, int storyId) {
        List<Story> storiesList = new ArrayList<>();
        storiesList.addAll(stories.values());

        for(int pos=0; pos<storiesList.size(); pos++) {
            if(storiesList.get(pos).getId() == storyId) {
                return pos;
            }
        }
        return 0;
    }

    private class StoryViewPager extends FragmentPagerAdapter {
        private List<Story> mStories = new ArrayList<>();

        StoryViewPager(FragmentManager fm, LinkedHashMap<Integer, Story> stories) {
            super(fm);
            this.mStories.addAll(stories.values());
        }

        @Override
        public Fragment getItem(int position) {
            return createFragment(mStories.get(position).getId());
        }

        @Override
        public int getCount() {
            return mStories.size();
        }

        private Fragment createFragment(int storyId) {
            return StoryFragment.newInstance(storyId);
        }
    }
}
