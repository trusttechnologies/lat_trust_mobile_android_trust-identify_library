package lat.trust.trustdemo.ui.splash.adapter;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import lat.trust.trustdemo.R;
import lat.trust.trusttrifles.model.StringsModel;

public class SplashAdapter extends RecyclerView.Adapter<SplashAdapter.SplashViewHolder> {

    ArrayList<StringsModel> listStrings;
    ItemClickListener clickListener;

    public SplashAdapter(ArrayList<StringsModel> listButtons) {
        this.listStrings = listButtons;
    }

    @NonNull
    @Override
    public SplashViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_button, null, false);
        return new SplashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SplashViewHolder splashViewHolder, int i) {
        splashViewHolder.setData(listStrings.get(i));

    }

    @Override
    public int getItemCount() {
        return listStrings.size();
    }

    public class SplashViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cardView;
        TextView textView;

        public SplashViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.container_started);
            textView = itemView.findViewById(R.id.txtSplash);

        }

        public void setData(final StringsModel stringsModel) {
            textView.setText(stringsModel.getText());
            if (stringsModel.getColor().equals("red")) {
                textView.setTextColor(Color.RED);
            }
            if (stringsModel.getColor().equals("green")) {
                textView.setTextColor(Color.BLACK);
            }
        }
    }

}
