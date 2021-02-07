package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ynzhxf.nd.xyfirecontrolapp.R;

/**
 * 统计
 * Created by nd on 2018-07-11.
 */

public class CompetentCountFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_compentent_count,null,false);

      return view;
    }

    public static CompetentCountFragment newInstance() {
        return new CompetentCountFragment();
    }
}
