package com.qurankarim.moshaf.Quran;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qurankarim.moshaf.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.qurankarim.moshaf.App.BOOKMARK_SURA_NUMBER;
import static com.qurankarim.moshaf.App.SHARED_PREFS;
import static com.qurankarim.moshaf.App.sharedPreferences;

public class JuzAdapter extends RecyclerView.Adapter<JuzAdapter.ViewHolder> {

private Context context;
private List<JuzModel> juzModelList;

private OnJuzListener onJuzListener;

private int checkedPosition = Integer.parseInt(sharedPreferences.getString(BOOKMARK_SURA_NUMBER,"-1")); // -1: no default selection , 0: 1st item selected



public interface OnJuzListener {
    void onJuzClick(String position);
}

    public JuzAdapter(Context context, List<JuzModel> juzModelList, JuzAdapter.OnJuzListener onJuzListener) {
        this.context = context;
        this.juzModelList = juzModelList;
        this.onJuzListener = onJuzListener;
    }

    @NonNull
    @Override
    public JuzAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.juz_card,
                parent, false);
        return new JuzAdapter.ViewHolder(view, onJuzListener);
    }

    @Override
    public void onBindViewHolder(@NonNull JuzAdapter.ViewHolder holder, int position) {
        String juzPosition = String.valueOf(position+1);
        holder.juzNumberText.setText(juzPosition);

        holder.bind(juzModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return juzModelList.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView juzNumberText;
    ImageView bookmarkView;
    ImageView bookmarkViewNot;
    CardView juzCard;

    JuzAdapter.OnJuzListener onJuzListener;

    public ViewHolder(@NonNull View itemView, JuzAdapter.OnJuzListener onJuzListener) {
        super(itemView);
        juzNumberText = (TextView) itemView.findViewById(R.id.juz_num);
        juzCard = (CardView) itemView.findViewById(R.id.juz_card);
        bookmarkView = itemView.findViewById(R.id.bookmark_view);
        bookmarkViewNot = itemView.findViewById(R.id.bookmark_view_not);
        juzCard.setOnClickListener(this);
        this.onJuzListener = onJuzListener;
    }

    void bind (final JuzModel juzModel){
        if (checkedPosition == -1){
            bookmarkView.setVisibility(View.GONE);
            bookmarkViewNot.setVisibility(View.VISIBLE);
        } else {
            if (checkedPosition == getAdapterPosition()){
                bookmarkView.setVisibility(View.VISIBLE);
                bookmarkViewNot.setVisibility(View.GONE);
            } else {
                bookmarkView.setVisibility(View.GONE);
                bookmarkViewNot.setVisibility(View.VISIBLE);
            }
        }

        bookmarkViewNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkViewNot.setVisibility(View.GONE);
                bookmarkView.setVisibility(View.VISIBLE);
                Toast.makeText(context, "تم حفظ علامة على الجزء "+Integer.parseInt(String.valueOf(getAdapterPosition()+1)), Toast.LENGTH_SHORT).show();
                saveBookMark(String.valueOf(getAdapterPosition()));
                if (checkedPosition != getAdapterPosition()){
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                }

            }
        });
    }
    public void saveBookMark (String suraNumber){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKMARK_SURA_NUMBER, suraNumber);
        editor.apply();
    }
    @Override
    public void onClick(View v) {
        String selectedJuzNumber = juzNumberText.getText().toString();
        onJuzListener.onJuzClick(selectedJuzNumber);
    }
}
public JuzModel getSelected(){
    if (checkedPosition != -1){
        return juzModelList.get(checkedPosition);
    }
    return null;
}

}
