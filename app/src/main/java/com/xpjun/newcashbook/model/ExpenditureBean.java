package com.xpjun.newcashbook.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by U-nookia on 2016/8/18.
 */
public class ExpenditureBean extends BmobObject {
    private String thing,price,note;

    public String getThing() {
        return thing;
    }

    public void setThing(String thing) {
        this.thing = thing;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
