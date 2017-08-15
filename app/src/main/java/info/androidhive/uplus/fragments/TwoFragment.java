package info.androidhive.uplus.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.androidhive.uplus.Contact;
import info.androidhive.uplus.ContactFetcher;
import info.androidhive.uplus.ContactsAdapter;
import info.androidhive.uplus.R;
import info.androidhive.uplus.UserProfile;


public class TwoFragment extends Fragment{
    ArrayList<Contact> listContacts;
    ListView lvContacts;
    FloatingActionButton btn;
    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_two, container, false);

        listContacts = new ContactFetcher(getActivity()).fetchAll();
        lvContacts = (ListView) view.findViewById(R.id.lvContacts);
        ContactsAdapter adapterContacts = new ContactsAdapter(getActivity(), listContacts);
        lvContacts.setAdapter(adapterContacts);
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent in1 = new Intent(getContext(), UserProfile.class);

                TextView c = (TextView) view.findViewById(R.id.tvMobile);
                TextView d = (TextView) view.findViewById(R.id.tvPhone);
                TextView e = (TextView) view.findViewById(R.id.tvName);
                String state = c.getText().toString();
                String name = e.getText().toString();
                String phone = d.getText().toString();
                in1.putExtra("user",name);
                in1.putExtra("state",state);
                in1.putExtra("phone",phone);
                startActivity(in1);
            }
        });

        return view;

    }

}
