package timkranen.com.pinch.recyclerview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import timkranen.com.pinch.R;
import timkranen.com.pinch.model.GithubSubscriber;
import timkranen.com.pinch.recyclerview.viewholders.GithubSubscriberViewHolder;

/**
 * @author tim on [11/11/16]
 */
public class GithubSubscriberAdapter extends RecyclerView.Adapter<GithubSubscriberViewHolder> {
    private static final String TAG = GithubSubscriberAdapter.class.getSimpleName();

    private Context _context;
    private List<GithubSubscriber> _subscribers;

    public GithubSubscriberAdapter(@NonNull Context context, @NonNull List<GithubSubscriber> subscribers) {
        this._context = context;
        this._subscribers = subscribers;
    }

    @Override
    public GithubSubscriberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GithubSubscriberViewHolder(LayoutInflater.from(_context)
                .inflate(R.layout.subscriber_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GithubSubscriberViewHolder holder, int position) {
        holder.update(_context, _subscribers.get(position));
    }

    @Override
    public int getItemCount() {
        return _subscribers.size();
    }
}
