package emlearn.hackernews;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import emlearn.hackernews.activities.TopStoriesListActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("emlearn.hackernews", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<TopStoriesListActivity> mActivityRule =
      new ActivityTestRule(TopStoriesListActivity.class);

    @Test
    public void refreshList() {
      onView(withId(R.id.btn_refresh_top_stories_list))
          .perform(click());
      onView(withId(R.id.topStoriesRecyclerView))
          .check(matches(isDisplayed()));
    }
}
