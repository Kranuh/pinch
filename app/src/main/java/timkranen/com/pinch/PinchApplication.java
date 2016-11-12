package timkranen.com.pinch;

import android.app.Application;

import io.paperdb.Paper;

/**
 * @author tim on [11/10/16]
 */
public class PinchApplication extends Application {
    private static final String TAG = PinchApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Paper.init(this);
    }
}
