package com.easyapp.mobilepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyapp.mobilepad.datacontract.Connection;

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

    public ConnectionsListFragment(){}

    public static ConnectionsListFragment newInstance(String profileName){
        ConnectionsListFragment fragment = new ConnectionsListFragment();
        // TODO: Add SQLite access for saved connections for profileName

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_connections_list, container, false);

        ListView connectionsList = (ListView)rootView.findViewById(R.id.connections_list);
        mAdapter = new ArrayAdapter<>(connectionsList.getContext(),
                R.layout.connection_list_item, MOCKUP_LIST);
        connectionsList.setAdapter(mAdapter);

        // on fab click
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.connections_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText host = new EditText(getActivity().getBaseContext());
                host.setInputType(InputType.TYPE_CLASS_NUMBER);
                (new AlertDialog.Builder(rootView.getContext()))
                        .setTitle(getString(R.string.add_host_dialog_title))
                        .setView(host)
                        .setPositiveButton(getString(R.string.add_host_dialog_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String hostName = host.getText().toString().trim();
                                if (hostName.matches("(?:\\d{1,3}\\.){3}\\d{1,3}(?::\\d+)?")){
                                    String[] parts = hostName.split(":");
                                    // TODO: Add Sqlite integration
                                    int port = 8887;
                                    if (parts.length > 1) {
                                        port = Integer.getInteger(parts[1],8887);
                                    }
                                    MOCKUP_LIST.add(parts[0]);
                                } else {
                                    Toast.makeText(getActivity().getBaseContext(),
                                            getString(R.string.add_host_dialog_err),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.add_host_dialog_Cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create().show();
            }
        });

        // on list item click
        connectionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String connection = (String)parent.getItemAtPosition(position);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, PresetsFragment.newInstance(connection))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public class ConnectionListAdapter extends ArrayAdapter<Connection> {

        private Context mContext;

        public ConnectionListAdapter(Context context, int resource, List<Connection> items){
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
            Connection item = getItem(position);
            if(item != null) {
                ((TextView)view.findViewById(R.id.connection_text)).setText(item.toString());
            }
            return view;
        }
    }

}
