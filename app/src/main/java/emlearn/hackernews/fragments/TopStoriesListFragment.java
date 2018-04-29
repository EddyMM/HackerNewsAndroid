package emlearn.hackernews.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import emlearn.hackernews.Constants;
import emlearn.hackernews.HackerNewsService;
import emlearn.hackernews.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A {@link Fragment} subclass for handling display and manipulation of top news from HN
 */
public class TopStoriesListFragment extends Fragment {
    public static String TAG = TopStoriesListFragment.class.getSimpleName();

    public TopStoriesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_stories_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTopStories();
    }

    /**
     * Fetch top stories from HN and display them
     */
    private void loadTopStories() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HACKER_NEWS_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HackerNewsService hackerNewsService = retrofit.create(HackerNewsService.class);

        Call<List<Integer>> topStoriesIdsCall =  hackerNewsService.listTopStoriesIds();

        // TODO: Create a callback for the call
        topStoriesIdsCall.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<List<Integer>> call,
                                   @NonNull Response<List<Integer>> response) {
                List<Integer> topStoriesIds  = response.body();
                if(topStoriesIds != null) {
                    for(Integer id: topStoriesIds)
                        Log.i(TAG, "Top Story ID: " + id);
                } else {
                    Log.d(TAG, "Top Story IDs is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Integer>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "Problem fetching top stories ids:\n" + t);
            }
        });
    }
}
