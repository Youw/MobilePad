package com.easyapp.mobilepad;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConnectingProgress extends Fragment {

    public ConnectingProgress() { }

    public static ConnectingProgress newInstance() {
        ConnectingProgress fragment = new ConnectingProgress();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connecting_progress, container, false);
        return view;
    }

}
