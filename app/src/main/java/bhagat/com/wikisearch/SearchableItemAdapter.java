package bhagat.com.wikisearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchableItemAdapter extends RecyclerView.Adapter {

    private ArrayList<SearchResponse.QueryBean.PagesBean> itemList = new ArrayList<>();
    private static final String LOG_TAG = SearchableItemAdapter.class.getSimpleName();
    private Context mContext;

    public SearchableItemAdapter(ArrayList<SearchResponse.QueryBean.PagesBean> itemList, Context context){
        this.itemList = itemList;
        mContext = context; }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);

        return new SearchableItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        SearchableItemViewHolder searchableItemViewHolder = (SearchableItemViewHolder) holder;
        searchableItemViewHolder.name.setText(itemList.get(position).getTitle());
        Log.d(LOG_TAG, "inside on bind"+itemList.get(position).getTerms().getDescription().toArray().toString());
        searchableItemViewHolder.detail.setText(Arrays.toString(itemList.get(position).getTerms().getDescription().toArray()));
        Picasso.with(mContext).load(itemList.get(position).getThumbnail().getSource()).into(searchableItemViewHolder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class SearchableItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView thumbnail;
        private TextView name, detail;

        public SearchableItemViewHolder(View itemView){
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
        }




    }
}
