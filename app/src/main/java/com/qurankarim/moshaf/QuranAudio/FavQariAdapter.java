package com.qurankarim.moshaf.QuranAudio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qurankarim.moshaf.DatabaseHelper;
import com.qurankarim.moshaf.R;

import java.util.List;

public class FavQariAdapter extends RecyclerView.Adapter<FavQariAdapter.ViewHolder> {

    private Context context;
    private List<FavQariModel> favQariModelList;
    private DatabaseHelper favQariDb;

    public FavQariAdapter(Context context, List<FavQariModel> favItemList) {
        this.context = context;
        this.favQariModelList = favItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_qari_fav,
                parent, false);
        favQariDb = new DatabaseHelper(context);
        return new FavQariAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.qariNameText.setText(favQariModelList.get(position).getQariName());
        holder.qatiPathText.setText(favQariModelList.get(position).getQariRelativePath());
    }

    @Override
    public int getItemCount() {
        return favQariModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView qariNameText;
        CardView qariCard;
        TextView qatiPathText;
        Button qariFavBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            qariNameText = itemView.findViewById(R.id.qari_name_text);
            qatiPathText = itemView.findViewById(R.id.qari_path);
            qariCard = itemView.findViewById(R.id.qari_card);
            qariFavBtn = itemView.findViewById(R.id.is_fav_qari);

            qariFavBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final FavQariModel favQariModel = favQariModelList.get(position);
                    favQariDb.remove_fav_qari(String.valueOf(favQariModel.getQariId()));
                    removeItem(position);
                }
            });

            qariCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String qariName = qariNameText.getText().toString();
                    String qariPath = qatiPathText.getText().toString();

                    Intent i = new Intent(context, QuranIndexAudio.class);
                    i.putExtra("qariposition", position);
                    i.putExtra("qariname", qariName);
                    i.putExtra("qaripath", qariPath);
                    context.startActivity(i);
                }
            });
        }
    }

    private void removeItem(int position) {
        favQariModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favQariModelList.size());
    }
}
