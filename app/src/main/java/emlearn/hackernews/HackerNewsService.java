package emlearn.hackernews;

import java.util.List;

import emlearn.hackernews.model.Story;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author eddymwenda
 * @since 29/04/2018.
 */

public interface HackerNewsService {
    String HACKER_NEWS_API_VERSION = "v0";

    @GET("/" + HACKER_NEWS_API_VERSION + "/topstories.json")
    Call<List<Integer>> listTopStoriesIds();

    @GET("/" + HACKER_NEWS_API_VERSION + "/item/{storyId}.json")
    Call<Story> getStoryInfo(@Path("storyId") int storyId);
}
