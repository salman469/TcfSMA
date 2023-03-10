package com.tcf.sma.Models;

import android.graphics.drawable.Drawable;

/**
 * Created by Zubair Soomro on 1/9/2017.
 */

public class MenuItemsModel {
    public String item_name;
    public Drawable item_resource;

    public MenuItemsModel(String item_name, Drawable item_resource) {
        this.item_name = item_name;
        this.item_resource = item_resource;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Drawable getItem_resource() {
        return item_resource;
    }

    public void setItem_resource(Drawable item_resource) {
        this.item_resource = item_resource;
    }
}
