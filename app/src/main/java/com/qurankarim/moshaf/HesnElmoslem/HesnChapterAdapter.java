package com.qurankarim.moshaf.HesnElmoslem;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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


        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            hesnChapterContentText = itemView.findViewById(R.id.hesn_chapter_text);
            hesnChapterSourceText = itemView.findViewById(R.id.hesn_chapter_source_text);
            hesnChapterCopy = itemView.findViewById(R.id.copy_hesn_chapter);

            hesnChapterCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copied text", hesnChapterContentText.getText().toString() + "\n"
                            + hesnChapterSourceText.getText().toString()
                            +"\n\n"+ "يمكنك قراءة المزيد من الأدعية و الأذكار من خلال تحميل التطبيق" + "\n"+ "https://play.google.com/store/apps/details?id=com.qurankarim.moshaf");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "تم النسخ", Toast.LENGTH_SHORT).show();
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
