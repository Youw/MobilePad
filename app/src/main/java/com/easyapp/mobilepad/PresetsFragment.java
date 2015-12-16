package com.easyapp.mobilepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gasper on 15.12.2015
 */
public class PresetsFragment extends Fragment {

    private final static List<String> MOCKUP_LIST = new ArrayList<>(
        Arrays.asList("Preset1", "Preset2", "Preset3")
    );

    private ArrayAdapter<String> mAdapter = null;

    public PresetsFragment() { }

    public static PresetsFragment newInstance(String profileName) {
        PresetsFragment fragment = new PresetsFragment();
        // TODO: Add SQLite access for presets for profileName
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_presets, container, false);

        ListView presetsList = (ListView)rootView.findViewById(R.id.presets_list);
        mAdapter = new ArrayAdapter<>(presetsList.getContext(),
                R.layout.presets_list_item, MOCKUP_LIST);
        presetsList.setAdapter(mAdapter);

        // on fab click
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.presets_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new AlertDialog.Builder(rootView.getContext()))
                        .setMessage(getString(R.string.msg_missing_functionality)).create().show();
            }
        });

        // on item click
        presetsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String preset = (String)parent.getItemAtPosition(position);
                Bundle options = new Bundle();
                options.putSerializable("preset", preset);
                startActivity(new Intent(getActivity().getBaseContext(),LoginActivity.class), options);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
