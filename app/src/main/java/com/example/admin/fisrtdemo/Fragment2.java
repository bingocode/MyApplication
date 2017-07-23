package com.example.admin.fisrtdemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2017/7/9.
 */

public class Fragment2 extends Fragment {@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    View view=inflater.inflate(R.layout.layout2, container, false);
    return view;
}

}
