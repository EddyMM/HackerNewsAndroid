package emlearn.hackernews.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import emlearn.hackernews.R;

/**
 * Uses the Fragment Manager to add a single fragment to an activity <br>
 * Simply subclass it and implement the {@link #createFragment()} method
 * such that it returns the target fragment
 * @author eddy.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.topNewsFrameLayout);

        if(fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.topNewsFrameLayout, createFragment())
                    .commit();
        }
    }

    /**
     * Creates a Fragment for the Single Fragment Activity
     * @return A Fragment instance
     */
    protected abstract Fragment createFragment();
}
