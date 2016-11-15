package timkranen.com.pinch.utils;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author tim on [11/14/16]
 *
 * Manages progress state and error state for assigned views. This can also be achieved trough abstraction
 * of the parent controller, probably a better way to do it.
 */
public class StateViewManager {
    private static final String TAG = StateViewManager.class.getSimpleName();

    private ProgressBar progressBar;
    private TextView errorTextView;
    private ViewGroup content;

    public StateViewManager(ViewGroup content, ProgressBar progressBar, TextView errorTextView) {
        this.content = content;
        this.progressBar = progressBar;
        this.errorTextView = errorTextView;
    }

    public void setErrorState(@NonNull String errorMessage) {
        this.content.setVisibility(View.GONE);
        this.errorTextView.setText(errorMessage);
        this.errorTextView.setVisibility(View.VISIBLE);
    }

    public void hideErrorState() {
        this.content.setVisibility(View.VISIBLE);
        this.errorTextView.setVisibility(View.GONE);
    }

    public boolean isErrorStateVisible() {
        return this.errorTextView.getVisibility() == View.VISIBLE;
    }

    public void setInProgress(boolean isInProgress) {
        if(isInProgress) {
            this.content.setVisibility(View.GONE);
            this.progressBar.setVisibility(View.VISIBLE);
        } else {
            this.content.setVisibility(View.VISIBLE);
            this.progressBar.setVisibility(View.GONE);
        }
    }
}
