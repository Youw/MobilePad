package com.easyapp.mobilepad;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

    public class ConnectionListAdapter extends ArrayAdapter<String> {

        private Context mContext;

        public ConnectionListAdapter(Context context, int resource, List<String> items){
            super(context, resource, items);
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.connection_list_item, null);
            }
            String item = getItem(position);
            if(item != null) {
                ((TextView)view.findViewById(R.id.connection_text)).setText(item);
            }
            return view;
        }
    }

}
