package com.zlodeuscomp.vdolgah.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity
public class Debtor implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "time_stamp")
    public long timestamp;
    @ColumnInfo(name = "sum")
    public String sum;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "date")
    public String date;

    public Boolean isChecked = false;

    public Debtor() {}

    protected Debtor(Parcel in) {
        uid = in.readInt();
        name = in.readString();
        timestamp = in.readLong();
        sum = in.readString();
        email = in.readString();
        date = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debtor debtor = (Debtor) o;
        return uid == debtor.uid &&
                timestamp == debtor.timestamp &&
                sum == debtor.sum &&
                Objects.equals(name, debtor.name) &&
                Objects.equals(email, debtor.email) &&
                Objects.equals(date, debtor.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, timestamp, sum, email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Debtor> CREATOR = new Creator<Debtor>() {
        @Override
        public Debtor createFromParcel(Parcel in) {
            return new Debtor(in);
        }

        @Override
        public Debtor[] newArray(int size) {
            return new Debtor[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(name);
        dest.writeLong(timestamp);
        dest.writeString(sum);
        dest.writeString(email);
        dest.writeString(date);
    }
}
