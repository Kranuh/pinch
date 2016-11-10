package timkranen.com.pinch.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tim on 7/6/16.
 */
public class RestAdapter {

    public static <T> T getAdapter(Class<T> cls, String url) {
        return getAdapter(cls, url, false);
    }

    public static<T> T getDebugAdapter(Class<T> cls, String url) {
        return getAdapter(cls, url, true);
    }

    private static <T> T getAdapter(Class<T> cls, String url, boolean isDebug) {
        if(isDebug) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(cls);
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(cls);
        }
    }


}
