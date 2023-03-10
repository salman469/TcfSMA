package com.tcf.sma.Interfaces;

import androidx.fragment.app.Fragment;

/**
 * Created by Zubair Soomro on 1/11/2017.
 */

public interface FragmentChangeListener {
    /**
     * load fragment from another fragment simply
     *
     * @param fragment fragment you want to load
     * @param Title    fragment title
     */
    void Replacefragment(Fragment fragment, String Title);
}
