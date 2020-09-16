package com.qurankarim.moshaf.Quran;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qurankarim.moshaf.DatabaseHelper;
import com.qurankarim.moshaf.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private List<SurahModel> surahsList;
    private List<SurahModel> surahsListFull;
    private Context context;
    private DatabaseHelper favDB;
    private onSurahListener onSurahListener;



    public interface onSurahListener {
        void onSurahClick(String position, String selectedSurahName);
    }

    public Adapter(List<SurahModel> surahsList, Context context,onSurahListener onSurahListener) {
        this.surahsList = surahsList;
        this.context = context;
        this.onSurahListener = onSurahListener;

        surahsListFull = new ArrayList<>(surahsList) ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new DatabaseHelper(context);
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_quran_sura, parent, false);
        return new ViewHolder(view,onSurahListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SurahModel surahModel = surahsList.get(position);

        setImageIntoButton(holder.surahFavBtn,surahModel.getIsFav(),position);
        readCursorData(surahModel, holder);
        holder.surahNameText.setText(surahModel.getSurahName());
        holder.surahNumberText.setText(String.valueOf(surahModel.getSurahNumber()));
        holder.surahRevelationTypeText.setText(surahModel.getSurahRevelationType());
        holder.surahVersesNumberText.setText(String.valueOf(surahModel.getSurahVersesNumber()));
    }

    // check is fav or not
    private void setImageIntoButton(Button buttonView, String isSelected, int surahPosition) {
        if (isSelected=="1") {
            buttonView.setBackgroundResource(R.drawable.ic_fav);

        } else {
            buttonView.setBackgroundResource(R.drawable.ic_fav_shadow);

        }
    }

    private void readCursorData(SurahModel surahModel, ViewHolder holder) {
        Cursor cursor = favDB.read_all_data_surah(String.valueOf(surahModel.getSurahNumber()));
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(favDB.FAVORITE_STATUS));
                surahModel.setIsFav(item_fav_status);

                //check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    holder.surahFavBtn.setBackgroundResource(R.drawable.ic_fav);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    holder.surahFavBtn.setBackgroundResource(R.drawable.ic_fav_shadow);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
    }

    @Override
    public int getItemCount() {
        return surahsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView surahNameText;
        TextView surahNumberText;
        TextView surahVersesNumberText;
        TextView surahRevelationTypeText;
        CardView surahCard;
        Button surahFavBtn;

        onSurahListener onSurahListener;

        public ViewHolder(@NonNull final View itemView,onSurahListener onSurahListener) {
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
                    SurahModel surahModel = surahsList.get(position);

                    if (surahModel.getIsFav().equals("0")) {
                        surahModel.setIsFav("1");
                        favDB.insertIntoTheDatabaseSurah(surahModel.getSurahName(), surahModel.getSurahNumber(),
                                String.valueOf(surahModel.getSurahNumber()), surahModel.getIsFav()
                                , surahModel.getSurahRevelationType(), String.valueOf(surahModel.getSurahVersesNumber()));
                        surahFavBtn.setBackgroundResource(R.drawable.ic_fav);
                    } else {
                        surahModel.setIsFav("0");
                        favDB.remove_fav_surah(String.valueOf(surahModel.getSurahNumber()));
                        surahFavBtn.setBackgroundResource(R.drawable.ic_fav_shadow);
                    }
                }
            });
            this.onSurahListener = onSurahListener;
        }

        @Override
        public void onClick(View v) {
            String selectedSurah = (String) surahNumberText.getText().toString();
            String selectedSurahName = surahNameText.getText().toString();
            onSurahListener.onSurahClick(selectedSurah, selectedSurahName);
        }
    }


    private void createTableOnFirstStart() {
        //favDB.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    public Filter getFilter() {
        return SurahFillter;
    }

    private Filter SurahFillter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SurahModel> filteredList = new ArrayList<>();
            Log.d("kkkk", "performFiltering: "+surahsListFull);
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(surahsListFull);
            } else {
                // constraint the value that write in search text
                String filterPattern = constraint.toString().trim();
                for (SurahModel item : surahsListFull) {
                    if (item.getSurahName().toLowerCase().trim().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            surahsList.clear();
            surahsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
