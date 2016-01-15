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
        Bundle args = new Bundle();
        args.putSerializable("input_emulator", emulator);
        res.setArguments(args);
        return res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mEmulator = (RemoteInputEmulator) getArguments().get("input_emulator");
        }

        View v = inflater.inflate(R.layout.fragment_preset1, container, false);

        View myButton;

        myButton = v.findViewById(R.id.button_a);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmulator.mouseMove(-100, 0);
            }
        });

        myButton = v.findViewById(R.id.button_s);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmulator.mouseMove(0, 100);
            }
        });

        myButton = v.findViewById(R.id.button_d);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmulator.mouseMove(100, 0);
            }
        });

        myButton = v.findViewById(R.id.button_w);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmulator.mouseMove(0, -100);
            }
        });
        return v;
    }

}
