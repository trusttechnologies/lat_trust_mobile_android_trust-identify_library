package lat.trust.trustdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import lat.trust.trustdemo.Utils.Utils;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView data;
    private TextView date;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        data = itemView.findViewById(R.id.data);
        date = itemView.findViewById(R.id.date);
    }

    public void bind(SIMData simData) {
        title.setText((simData != null) ? ((simData.getType() == 0) ? "SIM" : "IMEI") : "SIN CAMBIOS");
        data.setText((simData != null) ? simData.getId() : "");
        date.setText((simData != null) ? Utils.formatDate(new Date(simData.getTimestamp())) : "");

    }
}
