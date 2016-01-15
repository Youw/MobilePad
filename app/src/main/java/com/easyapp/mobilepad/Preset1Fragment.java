package com.easyapp.mobilepad;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class Preset1Fragment extends Fragment {

    private RemoteInputEmulator mEmulator = null;

    public Preset1Fragment() { }

    static public Preset1Fragment newInstance(RemoteInputEmulator emulator) {
        Preset1Fragment res = new Preset1Fragment();
        res.mEmulator = emulator;
        return res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preset1, container, false);
        View myButton = v.findViewById(R.id.button_a);
        myButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {

                    default:
                        return false;
                }
            }
        });
        return v;
    }

}
