package timkranen.com.pinch.api;

/**
 * @author tim on [11/9/16]
 */

public interface RequestListener {
    void onRequestCompleted();
    void onRequestFailed(Throwable e);
    void onRequestEmpty();
}
