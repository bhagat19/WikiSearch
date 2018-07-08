package bhagat.com.wikisearch;


import android.content.Context;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementing a Singleton behavior for one time initialisation of Retrofit
 */

public class RestInterfaceService {

    private static final String BASE_URl = "https://en.wikipedia.org//w/api.php/";

    private static RestInterfaceService mInstance;
    private static Retrofit RETROFIT;




    private RestInterfaceService(Context context){
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level

            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient httpClient = new OkHttpClient.
                Builder().cache(cache).
                addInterceptor(logging).
                build();
        // add your other interceptors …

        // add logging as last interceptor
//        httpClient.addInterceptor(logging);  // <-- this is the important line!

       RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

    }

    public static RestInterfaceService getInstance(Context context){
        if (mInstance == null){
            mInstance= new RestInterfaceService(context);
        }

        return mInstance;
    }





//    private static final RestInterface SERVICE = RETROFIT.create(RestInterface.class);

    public RestInterface getService() {

        return RETROFIT.create(RestInterface.class);
    }


}
