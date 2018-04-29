package emlearn.hackernews.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import emlearn.hackernews.R;
import emlearn.hackernews.fragments.TopStoriesListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment topNewsFragment = fm.findFragmentById(R.id.topNewsFrameLayout);

        if(topNewsFragment == null) {
            topNewsFragment = new TopStoriesListFragment();

            fm.beginTransaction()
                    .add(R.id.topNewsFrameLayout, topNewsFragment)
                    .commit();
        }
    }
}
