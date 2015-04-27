package com.example.yumatsuzawa.googlehistorian;

import android.os.SystemClock;

import java.util.Date;

/**
 * Created by yumatsuzawa on 15/04/27.
 */
public class History {
    private String query = "";
    private String uri = "";
    private Long searchedAt;

    public History (String query, String uri) {
        this.query = query;
        this.uri = uri;
        this.searchedAt = System.currentTimeMillis();
    }

    public Long getSearchedAt() {
        return this.searchedAt;
    }

    public String getUri() {
        return this.uri;
    }

    public String toString() {
        return this.query;
    }
}
