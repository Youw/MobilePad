package com.easyapp.mobilepad;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gasper on 14.12.2015
 */
public class ConnectionsListFragment extends Fragment {

    private ArrayAdapter<String> mAdapter;

    private final static List<String> MOCKUP_LIST = new ArrayList<>(
            Arrays.asList("Connection1", "Connection2", "Connection3", "Connection4")
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connections_list, container, false);

        ListView connectionsList = (ListView)rootView.findViewById(R.id.connections_list);
        mAdapter = new ArrayAdapter<>(connectionsList.getContext(),
                R.layout.connection_list_item, MOCKUP_LIST);
        connectionsList.setAdapter(mAdapter);

        // on list item click
        connectionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
