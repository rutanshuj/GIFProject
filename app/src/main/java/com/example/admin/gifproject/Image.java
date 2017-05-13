package com.example.admin.gifproject;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {

    public Uri uri;
    public int orientation;

    public Image(Uri mUri, int mOrientation) {
        uri = mUri;
        orientation = mOrientation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.uri, 0);
        dest.writeInt(this.orientation);
    }}