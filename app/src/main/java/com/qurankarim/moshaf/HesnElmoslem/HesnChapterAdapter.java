package com.qurankarim.moshaf.HesnElmoslem;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qurankarim.moshaf.R;

import java.util.List;

public class HesnChapterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<Object> listRecyclerItem;

    public HesnChapterAdapter(Context context, List<Object> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView hesnChapterContentText;
        TextView hesnChapterSourceText;
        ImageView hesnChapterCopy;
        ImageView hesnChapterShare;


        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            hesnChapterContentText = itemView.findViewById(R.id.hesn_chapter_text);
            hesnChapterSourceText = itemView.findViewById(R.id.hesn_chapter_source_text);
            hesnChapterCopy = itemView.findViewById(R.id.copy_hesn_chapter);
            hesnChapterShare = itemView.findViewById(R.id.share_hesn_chapter);

            hesnChapterCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copied text", hesnChapterContentText.getText().toString() + "\n"
                            + hesnChapterSourceText.getText().toString()
                            +"\n\n"+ "يمكنك قراءة القرآن الكريم كاملا والاستماع لأكثر من 50 مقراء مع تلاوات نادرة و لمزيد من الأدعية و الأذكار من خلال التطبيق" + "\n"+ "https://play.google.com/store/apps/details?id=com.qurankarim.moshaf");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "تم النسخ", Toast.LENGTH_SHORT).show();
                }
            });

            hesnChapterShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quran Lite");
                        String shareMessage = hesnChapterContentText.getText().toString() + "\n"
                                + hesnChapterSourceText.getText().toString() + "\n\n" + "يمكنك قراءة القرآن الكريم كاملا والاستماع لأكثر من 50 مقراء مع تلاوات نادرة و لمزيد من الأدعية و الأذكار من خلال التطبيق" + "\n" + "https://play.google.com/store/apps/details?id=com.qurankarim.moshaf";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE:

            default:

                View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.one_chapter_card, parent, false);

                return new HesnChapterAdapter.ItemViewHolder((layoutView));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE:
            default:
                HesnChapterAdapter.ItemViewHolder itemViewHolder = (HesnChapterAdapter.ItemViewHolder) holder;
                HesnChapterModel hesnChapterModel = (HesnChapterModel) listRecyclerItem.get(position);

                String hesnChapterContent = hesnChapterModel.getChapterText();
                String hesnChapterSource = hesnChapterModel.getChapterSource();

                for (int i = 0; i < listRecyclerItem.size(); i++) {
                    itemViewHolder.hesnChapterContentText.setText(hesnChapterContent);
                    itemViewHolder.hesnChapterSourceText.setText(hesnChapterSource);
                }
        }
    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
