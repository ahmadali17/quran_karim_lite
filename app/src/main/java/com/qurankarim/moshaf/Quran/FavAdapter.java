package com.qurankarim.moshaf.Quran;

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

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private Context context;
    private List<FavSurahModel> favSurahList;
    private DatabaseHelper favDB;

    private OnFavSurahListener onFavSurahListener;

    public interface OnFavSurahListener {
        void onFavSurahClick(String position, String selectedSurahName);
    }

    public FavAdapter(Context context, List<FavSurahModel> favItemList, OnFavSurahListener onFavSurahListener) {
        this.context = context;
        this.favSurahList = favItemList;
        this.onFavSurahListener = onFavSurahListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_surah_fav,
                parent, false);
        favDB = new DatabaseHelper(context);
        return new ViewHolder(view, onFavSurahListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.surahNameText.setText(favSurahList.get(position).getSurahTitle());
        holder.surahNumberText.setText(favSurahList.get(position).getSurahNum());
        holder.surahRevelationTypeText.setText(favSurahList.get(position).getSurahRevelationType());
        holder.surahVersesNumberText.setText(String.valueOf(favSurahList.get(position).getSurahVersesNumber()));

    }

    @Override
    public int getItemCount() {
        return favSurahList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView surahNameText;
        TextView surahNumberText;
        TextView surahVersesNumberText;
        TextView surahRevelationTypeText;
        CardView surahCard;
        Button surahFavBtn;

        OnFavSurahListener onFavSurahListener;

        public ViewHolder(@NonNull View itemView, OnFavSurahListener onFavSurahListener) {
            super(itemView);
            surahNameText = (TextView) itemView.findViewById(R.id.sura_name_text);
            surahNumberText = (TextView) itemView.findViewById(R.id.sura_number_text);
            surahVersesNumberText = (TextView) itemView.findViewById(R.id.sura_verses_number);
            surahRevelationTypeText = (TextView) itemView.findViewById(R.id.sura_revelation_type_text);
            surahCard = (CardView) itemView.findViewById(R.id.surah_card);
            surahFavBtn = (Button) itemView.findViewById(R.id.is_fav_surah);

            surahCard.setOnClickListener(this);

            surahFavBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final FavSurahModel favSurahModel = favSurahList.get(position);
                    favDB.remove_fav_surah(favSurahModel.getSurahNum());
                    removeItem(position);
                }
            });
            this.onFavSurahListener = onFavSurahListener;
        }

        @Override
        public void onClick(View v) {
            String selectedSurah = (String) surahNumberText.getText().toString();
            String selectedSurahName = surahNameText.getText().toString();
            onFavSurahListener.onFavSurahClick(selectedSurah, selectedSurahName);
        }
    }

    private void removeItem(int position) {
        favSurahList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favSurahList.size());
    }
}
