package emlearn.hackernews.activities;

import android.support.v4.app.Fragment;

import emlearn.hackernews.fragments.TopStoriesListFragment;

public class TopStoriesListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TopStoriesListFragment();
    }
}
