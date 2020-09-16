package com.qurankarim.moshaf.QuranAudio;

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
import com.qurankarim.moshaf.Quran.Adapter;
import com.qurankarim.moshaf.R;

import java.util.ArrayList;
import java.util.List;

public class QariAdapterNew extends RecyclerView.Adapter<QariAdapterNew.ViewHolder> implements Filterable {

    private Context context;
    private List<QariModel> qariModelList;

    private List<QariModel> qariModelsFull;

    private DatabaseHelper favQariDb;

    public QariAdapterNew(Context context, List<QariModel> qariModelList) {
        this.context = context;
        this.qariModelList = qariModelList;

        qariModelsFull = new ArrayList<>(qariModelList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favQariDb = new DatabaseHelper(context);
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_qari, parent, false);
        return new QariAdapterNew.ViewHolder(view);
    }

    private void createTableOnFirstStart() {
       // favQariDb.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart1", false);
        editor.apply();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final QariModel qariModel = qariModelList.get(position);

        setImageIntoButton(holder.qariFavBtn, qariModel.getQariFavStatus(),position);
        readCursorData(qariModel, holder);
        holder.qariNameText.setText(qariModel.getQariName());
        holder.qatiPathText.setText(qariModel.getQariRelativePath());
    }

    // check is fav or not
    private void setImageIntoButton(Button buttonView, String isSelected, int surahPosition) {
        if (isSelected=="1") {
            buttonView.setBackgroundResource(R.drawable.ic_fav);

        } else {
            buttonView.setBackgroundResource(R.drawable.ic_fav_shadow);

        }
    }

    private void readCursorData(QariModel qariModel, ViewHolder holder) {
        Log.d("lllllk", "readCursorData: "+qariModel);

        Cursor cursor = favQariDb.read_all_data_qari(String.valueOf(qariModel.getQariId()));

        SQLiteDatabase db = favQariDb.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(favQariDb.FAVORITE_STATUS1));
                qariModel.setQariFavStatus(item_fav_status);

                //check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    holder.qariFavBtn.setBackgroundResource(R.drawable.ic_fav);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    holder.qariFavBtn.setBackgroundResource(R.drawable.ic_fav_shadow);
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
        return qariModelList.size();
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
                    QariModel qariModel = qariModelList.get(position);

                    if (qariModel.getQariFavStatus().equals("0")) {
                        qariModel.setQariFavStatus("1");
                        favQariDb.insertIntoTheDatabaseQari(String.valueOf(qariModel.getQariId()), qariModel.getQariName(),
                                qariModel.getQariRelativePath(), qariModel.getQariFavStatus());
                        qariFavBtn.setBackgroundResource(R.drawable.ic_fav);
                    } else {
                        qariModel.setQariFavStatus("0");
                        favQariDb.remove_fav_qari(String.valueOf(qariModel.getQariId()));
                        qariFavBtn.setBackgroundResource(R.drawable.ic_fav_shadow);
                    }
                }
            });

            qariCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String qariName = qariNameText.getText().toString();
                    String qariPath = qatiPathText.getText().toString();

                    Intent i = new Intent(context, QuranIndexAudio.class);
                    i.putExtra("qariname", qariName);
                    i.putExtra("qaripath", qariPath);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return QariFillter;
    }

    private Filter QariFillter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<QariModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(qariModelsFull);
            } else {
                // constraint the value that write in search text
                String filterPattern = constraint.toString().trim();
                for (QariModel item : qariModelsFull) {
                    if (item.getQariName().toLowerCase().trim().contains(filterPattern)) {
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
            qariModelList.clear();
            qariModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
