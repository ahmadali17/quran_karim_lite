package com.qurankarim.moshaf.Adiah;

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

import static android.widget.Toast.makeText;

public class AdiahAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE = 1;
    private static final String TAG = "AdiahAdapter";
    private final Context context;
    private final List<Object> listRecyclerItem;

    public AdiahAdapter(Context context, List<Object> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView doaaContent;
        TextView doaaReferance;
        ImageView copyDoaa;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            doaaContent = itemView.findViewById(R.id.doaa_text);
            doaaReferance = itemView.findViewById(R.id.doaa_reference_text);
            copyDoaa = itemView.findViewById(R.id.copy_doaa);

            copyDoaa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copied text", doaaContent.getText().toString() + "\n"
                            + doaaReferance.getText().toString()+ "\n\n"+ "يمكنك قراءة المزيد من الأدعية و الأذكار من خلال تحميل التطبيق" + "\n"+ "https://play.google.com/store/apps/details?id=com.qurankarim.moshaf");
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
                        R.layout.one_doaa, parent, false);

                return new AdiahAdapter.ItemViewHolder((layoutView));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE:
            default:

                AdiahAdapter.ItemViewHolder itemViewHolder = (AdiahAdapter.ItemViewHolder) holder;
                DoaaModel doaaModel = (DoaaModel) listRecyclerItem.get(position);

                String doaaId = doaaModel.getDoaaId();
                String doaaContent = doaaModel.getDoaaContent();
                String doaaReference = doaaModel.getDoaaRefrance();

                for (int i = 0; i < listRecyclerItem.size(); i++) {
                    itemViewHolder.doaaContent.setText(doaaContent);
                    itemViewHolder.doaaReferance.setText(String.valueOf(doaaReference));
                }


        }
    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
