package bhagat.com.wikisearch;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SuggestionsAdapter extends CursorAdapter{

    private static final String LOG_TAG = SuggestionsAdapter.class.getSimpleName();

    private static final int SUGGESTION_INDEX = 0;
    private static final int SUGGESTION_TITLE = 1;
    private static final int SUGGESTION_DETAIL = 2;
    private static final int SUGGESTION_ICON = 3;



    private LayoutInflater mLayoutInflater;
        private Context mContext;
        private SearchView searchView;


        public SuggestionsAdapter(Context context, Cursor cursor) {
            super(context, cursor, false);
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = mLayoutInflater.inflate(R.layout.search_item, parent, false);
            return v;
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            String title = cursor.getString(SUGGESTION_TITLE);
            final int id = cursor.getInt(SUGGESTION_INDEX);
            String detail = cursor.getString(SUGGESTION_DETAIL);
            String thumbPath = cursor.getString(SUGGESTION_ICON);


            TextView titleView = (TextView) view.findViewById(R.id.tv_name);
            TextView detailView = (TextView) view.findViewById(R.id.tv_detail);
            ImageView thumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);




            titleView.setText(title);
            detailView.setText(detail);
            if (thumbnail != null) {
                Picasso.with(mContext).load(thumbPath).into(thumbnail);
            }else {
                thumbnail.setImageDrawable(ActivityCompat.getDrawable(mContext,R.drawable.baseline_android_black_48));
            }


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //take next action based user selected item
////                    TextView dealText = (TextView) view.findViewById(R.id.tv_deal);
////                    searchView.setIconified(true);
//
//                    Log.d(LOG_TAG, "on item click id :"+id);
////                    Toast.makeText(context, "Selected suggestion "+dealText.getText(),
////                            Toast.LENGTH_LONG).show();
//
//                }
//            });

        }

}
