package lat.trust.trustdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import lat.trust.trustdemo.R;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_NORMAL = 1;
    List<SIMData> data;

    public HistoryAdapter() {
        if (Hawk.contains("HISTORY")) {
            data = Hawk.get("HISTORY");
            if (data.size() > 0)
                data.remove(0);
        } else {
            data = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {
        int itemViewType = getItemViewType(i);

        historyViewHolder.bind((itemViewType == TYPE_EMPTY) ? null : data.get(i));
    }

    @Override
    public int getItemViewType(int position) {
        return (data == null || data.isEmpty()) ? TYPE_EMPTY : TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return (data == null || data.isEmpty()) ? 1 : data.size();
    }

    public void update() {
        data = Hawk.get("HISTORY");
        if (data.size() > 0 && data.get(0).getType() == 0)
            data.remove(0);
        notifyDataSetChanged();
    }
}
