package emlearn.hackernews.fragments;


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

import java.util.ArrayList;
import java.util.List;

import emlearn.hackernews.Constants;
import emlearn.hackernews.HackerNewsService;
import emlearn.hackernews.R;
import emlearn.hackernews.RetrofitSingleton;
import emlearn.hackernews.model.Story;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A {@link Fragment} subclass for handling display and manipulation of top news from HN
 */
public class TopStoriesListFragment extends Fragment {
    private static final String TAG = TopStoriesListFragment.class.getSimpleName();
    HackerNewsService hns = RetrofitSingleton.getInstance().create(HackerNewsService.class);

    TopNewsAdapter mTopNewsAdapter;
    List<Story> mStories = new ArrayList<>();


    public TopStoriesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.top_stories_list_fragment, container, false);

        RecyclerView topNewsRecyclerView = v.findViewById(R.id.topNewsRecyclerView);
        topNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTopNewsAdapter = new TopNewsAdapter(mStories);
        topNewsRecyclerView.setAdapter(mTopNewsAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTopStoriesIds();
    }

    /**
     * Adapter to create and bind Views/ViewHolders to the Top Story recycler view
     */
    private class TopNewsAdapter extends RecyclerView.Adapter<TopNewsViewHolder> {

        private List<Story> topNews;

        TopNewsAdapter(List<Story> topNews) {
            this.topNews = topNews;
        }

        @NonNull
        @Override
        public TopNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View topNewsListItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.top_stories_list_item_view,
                    parent,
                    false);

            return new TopNewsViewHolder(topNewsListItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TopNewsViewHolder holder, int position) {
            holder.mTitle.setText(topNews.get(position).getTitle());
            holder.mAuthor.setText(String.format(
                    "%s: %s",
                    getString(R.string.author_label),
                    topNews.get(position).getBy()));
            holder.mScore.setText(String.valueOf(topNews.get(position).getScore()));
        }

        @Override
        public int getItemCount() {
            return topNews.size();
        }
    }

    private class TopNewsViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mAuthor, mScore;

        TopNewsViewHolder(View itemView) {
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
    private void loadTopStoriesIds() {
        Call<List<Integer>> topStoriesIdsCall = hns.listTopStoriesIds();
        topStoriesIdsCall.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<List<Integer>> call,
                                   @NonNull Response<List<Integer>> response) {
                List<Integer> storiesIds  = response.body();
                if(storiesIds != null) {
                    loadTopStories(storiesIds.subList(0, Constants.storiesLimit));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Integer>> call,
                                  @NonNull Throwable t) {

            }
        });
    }

    /**
     * Loads the top stories from Hacker News
     */
    private void loadTopStories(List<Integer> storiesIds) {
        mStories.clear();

        for(int storyId: storiesIds) {
            Call<Story> storyCall = hns.getStory(storyId);
            storyCall.enqueue(new Callback<Story>() {
                @Override
                public void onResponse(@NonNull Call<Story> call,
                                       @NonNull Response<Story> response) {
                    mStories.add(response.body());
                    mTopNewsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(@NonNull Call<Story> call,
                                      @NonNull Throwable t) {
                    Log.e(TAG, "Error loading top stories: " + t);
                }
            });
        }
    }
}
