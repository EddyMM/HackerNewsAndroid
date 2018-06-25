package emlearn.hackernews.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import emlearn.hackernews.Constants;
import emlearn.hackernews.HackerNewsService;
import emlearn.hackernews.R;
import emlearn.hackernews.RetrofitSingleton;
import emlearn.hackernews.activities.StoryActivity;
import emlearn.hackernews.model.Story;
import emlearn.hackernews.model.StoryKeeper;
import retrofit2.Call;


/**
 * A {@link Fragment} subclass for handling display and manipulation of top news from HN
 */
public class TopStoriesListFragment extends Fragment {
    private static final String TAG = TopStoriesListFragment.class.getSimpleName();

    HackerNewsService hns = RetrofitSingleton.getInstance().create(HackerNewsService.class);

    ProgressBar mLoadTopNewsProgressBar;
    Button mRefreshTopStoriesListButton;

    TopStoriesAdapter mTopStoriesAdapter;
    TopStoriesAsyncTask topStoriesAsyncTask;

    RecyclerView mTopStoriesRecyclerView;

    public TopStoriesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "** onCreate **");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "** onCreateView **");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.top_stories_list_fragment, container, false);

        initUI(v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "** onResume **");
        if(mTopStoriesAdapter.topStories.size() == 0) {
            loadTopStories();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topStoriesAsyncTask.cancel(true);
    }

    private void initUI(View v) {
        // Initialize the fragment views
        mTopStoriesRecyclerView = v.findViewById(R.id.topStoriesRecyclerView);
        mTopStoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTopStoriesAdapter = new TopStoriesAdapter();
        mTopStoriesRecyclerView.setAdapter(mTopStoriesAdapter);

        mLoadTopNewsProgressBar = v.findViewById(R.id.loading_top_stories_bar);



        mRefreshTopStoriesListButton = v.findViewById(R.id.btn_refresh_top_stories_list);
        mRefreshTopStoriesListButton.setOnClickListener(
                (btn) -> loadTopStories()
        );
    }

    private void loadTopStories() {
        mLoadTopNewsProgressBar.setVisibility(View.VISIBLE);
        mTopStoriesRecyclerView.setVisibility(View.INVISIBLE);
        topStoriesAsyncTask = new TopStoriesAsyncTask();
        topStoriesAsyncTask.execute();
    }

    /**
     * Adapter to create and bind Views/ViewHolders to the Top Story recycler view
     */
    private class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesViewHolder> {
        List<Story> topStories = new ArrayList<>();

        @NonNull
        @Override
        public TopStoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View topNewsListItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.top_stories_list_item_view,
                    parent,
                    false);

            return new TopStoriesViewHolder(topNewsListItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TopStoriesViewHolder holder, int position) {
            holder.mTitle.setText(topStories.get(position).getTitle());
            holder.mAuthor.setText(String.format(
                    "%s %s",
                    getString(R.string.story_author_label),
                    topStories.get(position).getBy()));
            holder.mScore.setText(String.valueOf(topStories.get(position).getScore()));

            holder.itemView.setOnClickListener(
                    (view) -> {
                        int storyId = topStories.get(position).getId();
                        Intent intent = StoryActivity.createIntent(getActivity(), storyId);
                        startActivity(intent);
                    }
            );
        }

        @Override
        public int getItemCount() {
            return topStories.size();
        }

        void setStories(List<Story> topStories) {
            this.topStories = topStories;
        }
    }

    /**
     * ViewHolder for the recyclerview displaying the top stories
     */
    private class TopStoriesViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mAuthor, mScore;

        TopStoriesViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_top_news_list_item_title);
            mAuthor = itemView.findViewById(R.id.tv_top_news_list_item_author);
            mScore = itemView.findViewById(R.id.tv_top_news_list_item_score);
        }
    }

    /**
     * Load the top stories IDs using the hacker News API and send them to
     * {@link #getTopStories(List)} to get the corresponding stories
     */
    @Nullable
    private List<Integer> getTopStoriesIds() {
        Call<List<Integer>> topStoriesIdsCall = hns.listTopStoriesIds();
        try {
            List<Integer> storiesIds = topStoriesIdsCall.execute().body();

            if(storiesIds != null) {
                return storiesIds.subList(0, Constants.storiesLimit);
            }
        } catch (UnknownHostException e) {
            Log.e(TAG, "Error retrieving top stories IDs: " + e);
            e.printStackTrace();
            Toast.makeText(
                    getActivity(),
                    "Ensure that you are connected to the internet",
                    Toast.LENGTH_LONG
            ).show();
        } catch (IOException e) {
            Log.e(TAG, "Error retrieving top stories IDs: " + e);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Loads the top stories from Hacker News
     */
    private List<Story> getTopStories(@Nullable List<Integer> storiesIds) {
        List<Story> topStories = new ArrayList<>();

        if(storiesIds != null) {
            for (int storyId : storiesIds) {
                Call<Story> storyCall = hns.getStoryInfo(storyId);
                try {
                    Story story = storyCall.execute().body();
                    if (story != null) {
                        topStories.add(story);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error retrieving stories: " + e);
                    e.printStackTrace();
                }
            }
        }

        return topStories;
    }

    /**
     * Handle fetching of top stories asynchronously
     */
    @SuppressLint("StaticFieldLeak")
    private class TopStoriesAsyncTask extends AsyncTask<Void, Void, List<Story>> {

        @Override
        protected List<Story> doInBackground(Void... voids) {
            List<Integer> storiesIds = getTopStoriesIds();
            return getTopStories(storiesIds);
        }

        @Override
        protected void onPostExecute(List<Story> topStories) {
            super.onPostExecute(topStories);

            mTopStoriesAdapter.setStories(topStories);
            StoryKeeper.getInstance().addStories(getTopStoriesHashMap(topStories));

            mTopStoriesAdapter.notifyDataSetChanged();
            mLoadTopNewsProgressBar.setVisibility(View.GONE);
            mTopStoriesRecyclerView.setVisibility(View.VISIBLE);
        }

        private LinkedHashMap<Integer, Story> getTopStoriesHashMap(@Nullable List<Story> topStories) {
            LinkedHashMap<Integer, Story> topStoriesHashTable = new LinkedHashMap<>();

            if(topStories != null) {
                for(Story story: topStories) {
                    topStoriesHashTable.put(story.getId(), story);
                }
            }
            return  topStoriesHashTable;
        }
    }
}
