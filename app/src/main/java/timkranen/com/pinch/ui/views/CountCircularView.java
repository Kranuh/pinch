package timkranen.com.pinch.ui.views;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timkranen.com.pinch.R;

/**
 * @author tim on [11/12/16]
 *         <p>
 *         View that displays an integer and a title in a circular view
 */
public class CountCircularView extends LinearLayout {
    private static final String TAG = CountCircularView.class.getSimpleName();

    private String title;
    private int count;
    private
    @ColorInt
    int color;

    @BindView(R.id.count_title)
    protected TextView countTitle;

    @BindView(R.id.count_text)
    protected TextView countText;

    @BindView(R.id.count_background)
    protected View countBackground;

    public CountCircularView(Context context) {
        super(context);

        initialize();
    }

    public CountCircularView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    public CountCircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize();
    }

    private void initialize() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.count_circular_view, this, true);
        ButterKnife.bind(this, contentView);
    }

    private void updateView() {
        Drawable circleBackground = countBackground.getBackground();
        if(circleBackground instanceof GradientDrawable) {
            ((GradientDrawable) circleBackground).setColor(this.color);
        }

        countTitle.setText(this.title);
        countText.setText(String.valueOf(this.count));
    }

    public void setTitle(String title) {
        this.title = title;

        updateView();
    }

    public void setCount(@ColorInt int count) {
        this.count = count;

        updateView();
    }

    public void setColor(int color) {
        this.color = color;

        updateView();
    }
}
