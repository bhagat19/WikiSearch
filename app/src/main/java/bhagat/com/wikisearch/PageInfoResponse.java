package bhagat.com.wikisearch;

import java.util.List;

public class PageInfoResponse {


    /**
     * batchcomplete : true
     * query : {"pages":[{"pageid":18630637,"ns":0,"title":"Translation","contentmodel":"wikitext","pagelanguage":"en","pagelanguagehtmlcode":"en","pagelanguagedir":"ltr","touched":"2018-07-08T12:06:23Z","lastrevid":849353572,"length":118195,"fullurl":"https://en.wikipedia.org/wiki/Translation","editurl":"https://en.wikipedia.org/w/index.php?title=Translation&action=edit","canonicalurl":"https://en.wikipedia.org/wiki/Translation"}]}
     */

    private boolean batchcomplete;
    private QueryBean query;

    public boolean isBatchcomplete() {
        return batchcomplete;
    }

    public void setBatchcomplete(boolean batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public QueryBean getQuery() {
        return query;
    }

    public void setQuery(QueryBean query) {
        this.query = query;
    }

    public static class QueryBean {
        private List<PagesBean> pages;

        public List<PagesBean> getPages() {
            return pages;
        }

        public void setPages(List<PagesBean> pages) {
            this.pages = pages;
        }

        public static class PagesBean {
            /**
             * pageid : 18630637
             * ns : 0
             * title : Translation
             * contentmodel : wikitext
             * pagelanguage : en
             * pagelanguagehtmlcode : en
             * pagelanguagedir : ltr
             * touched : 2018-07-08T12:06:23Z
             * lastrevid : 849353572
             * length : 118195
             * fullurl : https://en.wikipedia.org/wiki/Translation
             * editurl : https://en.wikipedia.org/w/index.php?title=Translation&action=edit
             * canonicalurl : https://en.wikipedia.org/wiki/Translation
             */

            private int pageid;
            private int ns;
            private String title;
            private String contentmodel;
            private String pagelanguage;
            private String pagelanguagehtmlcode;
            private String pagelanguagedir;
            private String touched;
            private int lastrevid;
            private int length;
            private String fullurl;
            private String editurl;
            private String canonicalurl;

            public int getPageid() {
                return pageid;
            }

            public void setPageid(int pageid) {
                this.pageid = pageid;
            }

            public int getNs() {
                return ns;
            }

            public void setNs(int ns) {
                this.ns = ns;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContentmodel() {
                return contentmodel;
            }

            public void setContentmodel(String contentmodel) {
                this.contentmodel = contentmodel;
            }

            public String getPagelanguage() {
                return pagelanguage;
            }

            public void setPagelanguage(String pagelanguage) {
                this.pagelanguage = pagelanguage;
            }

            public String getPagelanguagehtmlcode() {
                return pagelanguagehtmlcode;
            }

            public void setPagelanguagehtmlcode(String pagelanguagehtmlcode) {
                this.pagelanguagehtmlcode = pagelanguagehtmlcode;
            }

            public String getPagelanguagedir() {
                return pagelanguagedir;
            }

            public void setPagelanguagedir(String pagelanguagedir) {
                this.pagelanguagedir = pagelanguagedir;
            }

            public String getTouched() {
                return touched;
            }

            public void setTouched(String touched) {
                this.touched = touched;
            }

            public int getLastrevid() {
                return lastrevid;
            }

            public void setLastrevid(int lastrevid) {
                this.lastrevid = lastrevid;
            }

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }

            public String getFullurl() {
                return fullurl;
            }

            public void setFullurl(String fullurl) {
                this.fullurl = fullurl;
            }

            public String getEditurl() {
                return editurl;
            }

            public void setEditurl(String editurl) {
                this.editurl = editurl;
            }

            public String getCanonicalurl() {
                return canonicalurl;
            }

            public void setCanonicalurl(String canonicalurl) {
                this.canonicalurl = canonicalurl;
            }
        }
    }
}
