package info.androidhive.uplus.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.uplus.R;


public class ThreeFragment extends Fragment{
    FloatingActionButton btn;
    public ThreeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //String[] names={"EA Party Nyarutarama","The Ben Concert","YouthKonnect","Rwanda Day Toronto"};

        View view=inflater.inflate(R.layout.fragment_one, container, false);
        btn=(FloatingActionButton)view.findViewById(R.id.fabbtn);
        btn.setVisibility(View.INVISIBLE);
        return view;
    }
}




