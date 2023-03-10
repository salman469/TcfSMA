package com.tcf.sma.utils;

import android.app.Activity;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActionBarToggle extends ActionBarDrawerToggle {

    private DrawerSlide drawerSlide;

    public ActionBarToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    public ActionBarToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    public void setOnDrawerSlide(DrawerSlide drawerSlide) {
        this.drawerSlide = drawerSlide;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        drawerSlide.onClosed();

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        drawerSlide.onSlide();

    }

    public interface DrawerSlide {
        void onSlide();

        void onClosed();
    }
}
