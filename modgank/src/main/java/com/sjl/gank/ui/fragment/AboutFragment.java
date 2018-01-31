package com.sjl.gank.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjl.gank.R;
import com.sjl.platform.base.BaseFragment;

/**
 * 我的Fragment
 *
 * @author SJL
 * @date 2017/11/30
 */
public class AboutFragment extends BaseFragment {
    private static final String TAG = "AboutFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }
}