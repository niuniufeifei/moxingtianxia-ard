package com.dgzd.mxtx.fragment.mineView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dgzd.mxtx.R;

/**
 * @version V1.0 <功能>
 * @FileName: EditNicknameFragment.java
 * @author: Jessica
 * @date: 2015-12-22 15:25
 */

public class EditNicknameFragment extends Fragment{
    private View view;
    private ImageView ivBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_nickname, container, false);
        initView();
        backClick();
        return view;
    }

    private void initView(){
        ivBack = (ImageView)view.findViewById(R.id.iv_back);
    }

    private void backClick(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
