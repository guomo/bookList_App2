package com.example.kathy.booklist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kathy on 11/22/2017.
 */

public class bookInfo implements Parcelable {
    Parcelable.Creator CREATOR;
    private String title;
    private String author;
    private String url;

    public bookInfo(String title, String author){
        this.title =title;
        this.author = author;
    }

    public bookInfo(String title, String author, String url){
        this.title =title;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}


