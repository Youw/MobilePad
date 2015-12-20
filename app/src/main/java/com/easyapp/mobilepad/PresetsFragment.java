package com.easyapp.mobilepad;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.easyapp.mobilepad.datacontract.Preset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gasper on 15.12.2015
 */
public class PresetsFragment extends Fragment {

    public interface PresetClickListener {
        void onClick(String presetName);
    }

    private static DBConnection dbConnection =  null;

    private ArrayAdapter<Preset> mAdapter;
    private List<Preset> mPresetList = new ArrayList<>();

    private PresetClickListener mClickListener = null;
    PresetsFragment setPresetClickListener(PresetClickListener ClickListener) {
        mClickListener = ClickListener;
        return this;
    }

    public PresetsFragment() { }

    public static PresetsFragment newInstance(int profileId, DBConnection connection) {
        if (dbConnection == null) {
            dbConnection = connection;
        }
        PresetsFragment fragment = new PresetsFragment();
        if (profileId != -1){
            fragment.mPresetList.addAll(dbConnection.getPresets(profileId));
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_presets, container, false);

        ListView presetsList = (ListView)rootView.findViewById(R.id.presets_list);
        mAdapter = new PresetListAdapter(presetsList.getContext(),
                R.layout.presets_list_item, mPresetList);
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
                Preset preset = (Preset)parent.getItemAtPosition(position);
                if (mClickListener != null) {
                    mClickListener.onClick(preset.getName());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public class PresetListAdapter extends ArrayAdapter<Preset> {

        private Context mContext;

        public PresetListAdapter(Context context, int resource, List<Preset> items) {
            super(context, resource, items);
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.presets_list_item, null);
            }
            Preset preset = getItem(position);
            if (preset != null) {
                ((TextView)view.findViewById(R.id.presets_item)).setText(preset.getName());
            }
            return view;
        }
    }
}
