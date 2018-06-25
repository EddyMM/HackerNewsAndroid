package emlearn.hackernews;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author eddymwenda
 * @since 29/04/2018.
 */

public class RetrofitSingleton {
    private static Retrofit retrofit;

    private RetrofitSingleton() {}

    public static Retrofit getInstance() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HACKER_NEWS_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
