package emlearn.hackernews.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import emlearn.hackernews.R;
import emlearn.hackernews.model.Story;
import emlearn.hackernews.model.StoryKeeper;

/**
 * @author eddy.
 */

public class StoryFragment extends Fragment {
    public static final String ARG_STORY_ID = StoryFragment.class.getCanonicalName() +
            "args_story_id";
    private static final String TAG = StoryFragment.class.getSimpleName();

    private int mStoryId;

    public static Fragment newInstance(int storyId) {
        Fragment fragment = new StoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_STORY_ID, storyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the story to display
        if(getArguments() != null) {
            this.mStoryId = getArguments().getInt(ARG_STORY_ID);
        } else {
            Log.e(TAG, "Story Fragment created without any story ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_story, container, false);

        Story story = StoryKeeper.getInstance().getStory(mStoryId);

        TextView tvStoryTitle = v.findViewById(R.id.tv_story_title);
        tvStoryTitle.setText(story.getTitle());

        TextView tvStoryAuthor = v.findViewById(R.id.tv_story_author);
        tvStoryAuthor.setText(story.getBy());

        TextView tvStoryDate = v.findViewById(R.id.tv_story_date);
        Date date = new Date(story.getTime() * 1000);
        SimpleDateFormat sf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        sf.applyLocalizedPattern("dd MMM, yyyy");
        String formatedDate = sf.format(date);

        tvStoryDate.setText(formatedDate);

        TextView tvStoryScore = v.findViewById(R.id.tv_story_score);
        tvStoryScore.setText(String.valueOf(story.getScore()));

        TextView tvStoryText = v.findViewById(R.id.tv_story_text);

        if(story.getText() != null) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                tvStoryText.setText(Html.fromHtml(story.getText(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvStoryText.setText(Html.fromHtml(story.getText()));
            }
        }

        return v;
    }
}
