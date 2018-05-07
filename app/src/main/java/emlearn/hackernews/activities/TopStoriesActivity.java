package emlearn.hackernews.activities;

import android.support.v4.app.Fragment;

import emlearn.hackernews.fragments.TopStoriesListFragment;

public class TopStoriesActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TopStoriesListFragment();
    }
}
