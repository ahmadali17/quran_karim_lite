package com.qurankarim.moshaf.Azkar;

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

import java.util.ArrayList;
import java.util.List;

public class AzkarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE = 1;
    private final Context context;
    private final List<Object> listRecyclerItem;

    String zekrBless;

    public AzkarAdapter(Context context, List<Object> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView zekrContentText;
        TextView zekrRepeatText;
        ImageView copyZekr;
        ImageView zekrBlessImage;
        ImageView zekrShare;

        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            zekrContentText = itemView.findViewById(R.id.zekr_text);
            zekrRepeatText = itemView.findViewById(R.id.zekr_repeat_text);
            copyZekr = itemView.findViewById(R.id.zekr_copy);
            zekrBlessImage = itemView.findViewById(R.id.zekr_info);
            zekrShare = itemView.findViewById(R.id.zekr_share);

            final ArrayList<String> zekrBlassList = AzkarShow.azkarblessList;
            copyZekr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copied text", zekrContentText.getText().toString()
                            + "\n\n" + "يمكنك قراءة القرآن الكريم كاملا والاستماع لأكثر من 50 مقراء مع تلاوات نادرة و لمزيد من الأدعية و الأذكار من خلال التطبيق" + "\n" + "https://play.google.com/store/apps/details?id=com.qurankarim.moshaf");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "تم النسخ", Toast.LENGTH_SHORT).show();
                }
            });
            zekrShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quran Lite");
                        String shareMessage = zekrContentText.getText().toString() + "\n\n" + "يمكنك قراءة القرآن الكريم كاملا والاستماع لأكثر من 50 مقراء مع تلاوات نادرة و لمزيد من الأدعية و الأذكار من خلال التطبيق" + "\n" + "https://play.google.com/store/apps/details?id=com.qurankarim.moshaf";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                }
            });
            zekrRepeatText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!zekrRepeatText.getText().toString().equals("0")) {
                        int zekrCounter = Integer.parseInt(zekrRepeatText.getText().toString());
                        zekrCounter--;
                        zekrRepeatText.setText(String.valueOf(zekrCounter));
                    }
                }
            });
            zekrBlessImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, zekrBlassList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
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
                        R.layout.one_zekr, parent, false);

                return new AzkarAdapter.ItemViewHolder((layoutView));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE:
            default:

                AzkarAdapter.ItemViewHolder itemViewHolder = (AzkarAdapter.ItemViewHolder) holder;
                ZekrModel zekrModel = (ZekrModel) listRecyclerItem.get(position);

                String zekrContent = zekrModel.getZekrContent();
                int zekrRepeat = zekrModel.getZekrRepeat();
                zekrBless = zekrModel.getZekrbless();

                for (int i = 0; i < listRecyclerItem.size(); i++) {
                    itemViewHolder.zekrContentText.setText(zekrContent);
                    itemViewHolder.zekrRepeatText.setText(String.valueOf(zekrRepeat));
                }

        }
    }


    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
