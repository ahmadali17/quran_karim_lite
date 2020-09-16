package com.qurankarim.moshaf.HesnElmoslem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qurankarim.moshaf.R;

import java.util.ArrayList;
import java.util.List;

public class HesnIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int TYPE = 1;
    private final Context context;
    private final List<HesnIndexModel> listRecyclerItem;

    private onChapterListener onChapterListener;

    private List<HesnIndexModel> listRecyclerItemFull;

    public HesnIndexAdapter(Context context, List<HesnIndexModel> listRecyclerItem, onChapterListener onChapterListener) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
        this.onChapterListener = onChapterListener;
        listRecyclerItemFull = new ArrayList<>(listRecyclerItem);
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView hesnChapterNameText;
        CardView hesnIndexCard;


        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            hesnChapterNameText = itemView.findViewById(R.id.hesn_chapter_name);
            hesnIndexCard = itemView.findViewById(R.id.hesn_index_card);

            hesnIndexCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChapterListener.onChapterClick(getAdapterPosition(),hesnChapterNameText.getText().toString());
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
                        R.layout.one_hesn_index, parent, false);

                return new HesnIndexAdapter.ItemViewHolder((layoutView));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE:
            default:
                HesnIndexAdapter.ItemViewHolder itemViewHolder = (HesnIndexAdapter.ItemViewHolder) holder;
                HesnIndexModel hesnIndexModel = (HesnIndexModel) listRecyclerItem.get(position);

                String chapterTitle = hesnIndexModel.getIndexTitle();

                for (int i = 0; i < listRecyclerItem.size(); i++) {
                    itemViewHolder.hesnChapterNameText.setText(chapterTitle);
                }
        }
    }

    public interface onChapterListener {
        void onChapterClick(int position, String chapterTitle);
    }
    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }

    @Override
    public Filter getFilter() {
        return QariFillter;
    }

    private Filter QariFillter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HesnIndexModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listRecyclerItemFull);
            } else {
                // constraint the value that write in search text
                String filterPattern = constraint.toString().trim();
                for (HesnIndexModel item : listRecyclerItemFull) {
                    if (item.getIndexTitle().toLowerCase().trim().contains(filterPattern)) {
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
            listRecyclerItem.clear();
            listRecyclerItem.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
