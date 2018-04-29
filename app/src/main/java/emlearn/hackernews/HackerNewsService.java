package emlearn.hackernews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author eddymwenda
 * @since 29/04/2018.
 */

public interface HackerNewsService {
    static String HACKER_NEWS_API_VERSION = "v0";

    @GET("/" + HACKER_NEWS_API_VERSION + "/topstories.json")
    Call<List<Integer>> listTopStoriesIds();
}
