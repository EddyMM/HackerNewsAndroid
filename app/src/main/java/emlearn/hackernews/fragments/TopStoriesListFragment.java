package emlearn.hackernews.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import emlearn.hackernews.Constants;
import emlearn.hackernews.HackerNewsService;
import emlearn.hackernews.R;
import emlearn.hackernews.RetrofitSingleton;
import emlearn.hackernews.model.Story;
import retrofit2.Call;


/**
 * A {@link Fragment} subclass for handling display and manipulation of top news from HN
 */
public class TopStoriesListFragment extends Fragment {
    private static final String TAG = TopStoriesListFragment.class.getSimpleName();
    HackerNewsService hns = RetrofitSingleton.getInstance().create(HackerNewsService.class);

    TopStoriesAdapter mTopStoriesAdapter;
    TopStoriesAsyncTask topStoriesAsyncTask;

    public TopStoriesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.top_stories_list_fragment, container, false);

        RecyclerView topStoriesRecyclerView = v.findViewById(R.id.topStoriesRecyclerView);
        topStoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTopStoriesAdapter = new TopStoriesAdapter();
        topStoriesRecyclerView.setAdapter(mTopStoriesAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        topStoriesAsyncTask = new TopStoriesAsyncTask();
        topStoriesAsyncTask.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topStoriesAsyncTask.cancel(true);
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
                    "%s: %s",
                    getString(R.string.author_label),
                    topStories.get(position).getBy()));
            holder.mScore.setText(String.valueOf(topStories.get(position).getScore()));
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
     * {@link #loadTopStories(List)} to get the corresponding stories
     */
    private List<Integer> loadTopStoriesIds() {
        Call<List<Integer>> topStoriesIdsCall = hns.listTopStoriesIds();
        try {
            List<Integer> storiesIds = topStoriesIdsCall.execute().body();

            if(storiesIds != null) {
                return storiesIds.subList(0, Constants.storiesLimit);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error retrieving top stories IDs: " + e);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Loads the top stories from Hacker News
     */
    private List<Story> loadTopStories(List<Integer> storiesIds) {
        List<Story> topStories = new ArrayList<>();

        for(int storyId: storiesIds) {
            Call<Story> storyCall = hns.getStory(storyId);
            try {
                Story story = storyCall.execute().body();
                if(story != null) {
                    topStories.add(story);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error retrieving stories: " + e);
                e.printStackTrace();
            }
        }

        return topStories;
    }

    /**
     * Handle fetching of top stories asynchronously
     */
    private class TopStoriesAsyncTask extends AsyncTask<Void, Void, List<Story>> {

        @Override
        protected List<Story> doInBackground(Void... voids) {
            List<Integer> storiesIds = loadTopStoriesIds();
            return loadTopStories(storiesIds);
        }

        @Override
        protected void onPostExecute(List<Story> topStories) {
            super.onPostExecute(topStories);
            mTopStoriesAdapter.setStories(topStories);
            mTopStoriesAdapter.notifyDataSetChanged();
        }
    }
}
