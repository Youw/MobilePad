package com.easyapp.mobilepad;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.easyapp.mobilepad.datacontract.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gasper on 14.12.2015
 */
public class ConnectionsListFragment extends Fragment {

    private static DBConnection dbConnection = null;

    private int mProfileId = -1;
    private ArrayAdapter<Connection> mAdapter;
    private List<Connection> mConnectionList = new ArrayList<>();

    public ConnectionsListFragment(){}

    public static ConnectionsListFragment newInstance(int profileId, DBConnection connection) {
        dbConnection = connection;
        ConnectionsListFragment fragment = new ConnectionsListFragment();
        if (profileId != -1) {
            fragment.mProfileId = profileId;
            fragment.mConnectionList.addAll(dbConnection.getConnections(profileId));
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_connections_list, container, false);

        ListView connectionsList = (ListView)rootView.findViewById(R.id.connections_list);
        mAdapter = new ConnectionListAdapter(connectionsList.getContext(),
                R.layout.connection_list_item, mConnectionList);
        connectionsList.setAdapter(mAdapter);

        // on fab click
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.connections_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText host = new EditText(getActivity().getBaseContext());
                host.setInputType(InputType.TYPE_CLASS_TEXT);
                (new AlertDialog.Builder(rootView.getContext()))
                        .setTitle(getString(R.string.add_host_dialog_title))
                        .setView(host)
                        .setPositiveButton(getString(R.string.add_host_dialog_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String hostFullAddr = host.getText().toString().trim();
                                String[] parts = hostFullAddr.split(":");
                                String host = parts[0];
                                int port = 8887;
                                if (parts.length > 1) {
                                    port = Integer.getInteger(parts[1], 8887);
                                }
                                Connection connection = new Connection(mProfileId, host, port);
                                if (dbConnection.insert(connection)) {
                                    mConnectionList.add(connection);
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.add_host_dialog_Cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // default behaviour will cancel
                            }
                        })
                        .create().show();
            }
        });

        // on list item click
        connectionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Connection connection = (Connection)parent.getItemAtPosition(position);
                if (mConnectionClickListener != null) {
                    mConnectionClickListener.onClick(connection);
                }
            }
        });

        return rootView;
    }

    public interface ConnectionClickListener {
        void onClick(Connection connection);
    }
    private ConnectionClickListener mConnectionClickListener = null;
    public void setConnectionClickListener(ConnectionClickListener clickListener) {
        mConnectionClickListener = clickListener;
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
