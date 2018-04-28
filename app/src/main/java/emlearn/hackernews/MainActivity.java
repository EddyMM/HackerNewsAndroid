package emlearn.hackernews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment topNewsFragement = fm.findFragmentById(R.id.topNewsFrameLayout);

        if(topNewsFragement == null) {
            topNewsFragement = new TopNewsListFragment();

            fm.beginTransaction()
                    .add(R.id.topNewsFrameLayout, topNewsFragement)
                    .commit();
        }
    }
}
