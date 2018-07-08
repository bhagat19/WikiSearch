package bhagat.com.wikisearch;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.SearchManager.SUGGEST_COLUMN_ICON_1;
import static android.app.SearchManager.SUGGEST_COLUMN_TEXT_2;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String[] sAutocompleteColNames = new String[] {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SUGGEST_COLUMN_TEXT_2,
            SUGGEST_COLUMN_ICON_1
    };

    private WebView mWebview ;
    private LinearLayout homeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeView = findViewById(R.id.ll_wikiSearch);
        progressBar = findViewById(R.id.progressBar);


        mWebview  = new WebView(this);


//        mSuggestionsAdapter = new SearchableItemAdapter(suggestionItemList,this);
        handleIntent(getIntent());

        mWebview = (WebView) findViewById(R.id.webview);
        // Configure related browser settings
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        mWebview.setWebViewClient(new MyBrowser());

        // Load the initial URL

    }

    // Manages the behavior when URLs are loaded
    private class MyBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }


    }

    MatrixCursor suggestionCursor = new MatrixCursor(sAutocompleteColNames);
    SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setOnQueryTextListener(onQueryTextListener);

        mSearchView.setSuggestionsAdapter(new SuggestionsAdapter(this,suggestionCursor));

        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                Utility.hideSoftKeyboard(MainActivity.this,mSearchView);

                Cursor cursor = (Cursor) mSearchView.getSuggestionsAdapter().getItem(i);
                int pageId = cursor.getInt(0);
                Log.d(LOG_TAG, "page id: "+pageId);
                cursor.close();

                progressBar.setVisibility(View.VISIBLE);
                setView(true);
                Utility.hideSoftKeyboard(MainActivity.this,mSearchView);


                RestInterface service = RestInterfaceService.getInstance(MainActivity.this).getService();
                service.getPageResult(pageId).enqueue(new Callback<PageInfoResponse>() {
                    @Override
                    public void onResponse(Call<PageInfoResponse> call, Response<PageInfoResponse> response) {
                        if (response.isSuccessful()) {



                            progressBar.setVisibility(View.GONE);
                            PageInfoResponse pageInfoResponse = response.body();
                            Log.d(LOG_TAG, "response: " + new Gson().toJson(pageInfoResponse));
                            String url = pageInfoResponse.getQuery().getPages().get(0).getFullurl();
                            Log.d(LOG_TAG, "url " + url);
                            mWebview.loadUrl(url);

                        }

                    }

                    @Override
                    public void onFailure(Call<PageInfoResponse> call, Throwable t) {

                        Log.d(LOG_TAG, "response: fail"+t.toString());
                    }
                });


                return true;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                return onSuggestionSelect(i);
            }
        });

        return true;
    }

//    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(LOG_TAG, "inside on new intent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {



        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.d(LOG_TAG, "search query " + query);

            if (query.length() >= 3) {
                if (Utility.isNetworkAvailable(this))
                    searchQuery(query);
                else
                    Toast.makeText(this, "Internet not available !",Toast.LENGTH_SHORT).show();
            }else {
                mSearchView.getSuggestionsAdapter().changeCursor(null);

            }


        }
    }


    ProgressBar progressBar;

    public void searchQuery(String query) {

        suggestionCursor = new MatrixCursor(sAutocompleteColNames);

        RestInterface service = RestInterfaceService.getInstance(MainActivity.this).getService();

        service.getSearchResults(query).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                if (response.isSuccessful()) {

                    SearchResponse wikiResponse = response.body();
                    Log.d(LOG_TAG, "wikiresponse: "+new Gson().toJson(wikiResponse));



                    ArrayList<SearchResponse.QueryBean.PagesBean> list =
                            (ArrayList<SearchResponse.QueryBean.PagesBean>) wikiResponse.getQuery().getPages();
                    for (int i=0 ;i <list.size(); i++) {
                        int index = list.get(i).getPageid();
                        String thumbnail = null;
                        String detail = null;
                        String title = null;

                        if (list.get(i).getTerms() != null && list.get(i).getTerms().getDescription() != null){
                           detail = Arrays.toString(list.get(i).getTerms().getDescription().toArray());
                        }

                        if (list.get(i).getTitle() != null){
                            title = list.get(i).getTitle();
                        }

                        if (list.get(i).getThumbnail() != null) {
                            thumbnail = list.get(i).getThumbnail().getSource();
                        }

                        Object[] row = new Object[] {index,title,detail,thumbnail};

                        suggestionCursor.addRow(row);
                    }
                    mSearchView.getSuggestionsAdapter().changeCursor(suggestionCursor);
                    mSearchView.getSuggestionsAdapter().notifyDataSetChanged();


                } else {
                    Log.d(LOG_TAG, "error code response: " + response.code());
                    mSearchView.getSuggestionsAdapter().changeCursor(null);

                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d(LOG_TAG, "on failure: " + t.toString());
                t.printStackTrace();
                mSearchView.getSuggestionsAdapter().changeCursor(null);

            }
        });
    }

    private SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    setView(true);
                    Utility.hideSoftKeyboard(MainActivity.this,mSearchView);
                    if (timer != null) {
                        timer.cancel();
                    }
                    if (query.length() >=3) {
                        if (Utility.isNetworkAvailable(MainActivity.this))
                            searchQuery(query);
                        else
                            Toast.makeText(MainActivity.this, "Internet not available !",Toast.LENGTH_SHORT).show();
                    }else {
                        mSearchView.getSuggestionsAdapter().changeCursor(null);
                    }
                    return true;
                }

                private Timer timer;

                @Override
                public boolean onQueryTextChange(final String newText) {

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (newText.length() >=3)
                                if (Utility.isNetworkAvailable(MainActivity.this))
                                    searchQuery(newText);
                                else
                                    Toast.makeText(MainActivity.this, "Internet not available !",Toast.LENGTH_SHORT).show();
                                }
                    }, 200);

                    return true;
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        Utility.hideSoftKeyboard(this,mSearchView);
        if (mWebview.getVisibility() == View.VISIBLE){
           setView(false);
        }else {
            super.onBackPressed();
        }

    }

    private void setView(boolean isWebView){
        if (isWebView){
            mWebview.setVisibility(View.VISIBLE);
            homeView.setVisibility(View.GONE);
        }else {
            mWebview.setVisibility(View.GONE);
            homeView.setVisibility(View.VISIBLE);
        }
    }
}
