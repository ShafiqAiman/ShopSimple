package com.ruth.checkmeout.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Expense {
    @SerializedName("total")
    @Expose
    private int total;
    //private Date date=new Date();
    @SerializedName("localDate")
    @Expose
    private Date localDate;
    private String pushId;

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public Expense() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Expense(int total) {
        this.total = total;
        this.localDate = new Date();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getLocalDate() {
        return localDate;
    }

    public void setLocalDate(Date localDate) {
        this.localDate = localDate;
    }
}
