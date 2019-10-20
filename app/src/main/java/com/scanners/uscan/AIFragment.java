package com.scanners.uscan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AIFragment extends Fragment implements View.OnClickListener{
    View myView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.ai_fragment, container, false);
        return myView;

    }//End onCreateView
    public void onClick(View v) {


    }//End onClick
}
