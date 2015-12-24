package com.easyapp.mobilepad;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Preset1Fragment extends Fragment {


    public Preset1Fragment() { }

    static public Preset1Fragment createPreset() {
        Preset1Fragment res = new Preset1Fragment();

        return res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preset1, container, false);
    }

}
