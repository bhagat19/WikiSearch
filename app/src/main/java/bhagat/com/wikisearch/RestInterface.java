package bhagat.com.wikisearch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestInterface {

    @GET("?action=query&format=json&prop=pageimages|pageterms&meta=&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail|name&" +
            "pithumbsize=50&pilimit=10&wbptterms=description")
    Call<SearchResponse> getSearchResults(@Query("gpssearch") String searchQuery);

    @GET("?action=query&prop=info&format=json&inprop=url&formatversion=2")
    Call<PageInfoResponse> getPageResult(@Query("pageids") int pageId);
}
