package com.vinpin.sample.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 多条目对象
 *
 * @author zwp
 *         create at 2018/01/24 10:18
 */
public class MultiItemTypeInfo implements Parcelable {

    public String content;
    public int type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeInt(this.type);
    }

    public MultiItemTypeInfo() {
    }

    protected MultiItemTypeInfo(Parcel in) {
        this.content = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<MultiItemTypeInfo> CREATOR = new Parcelable.Creator<MultiItemTypeInfo>() {
        @Override
        public MultiItemTypeInfo createFromParcel(Parcel source) {
            return new MultiItemTypeInfo(source);
        }

        @Override
        public MultiItemTypeInfo[] newArray(int size) {
            return new MultiItemTypeInfo[size];
        }
    };
}
