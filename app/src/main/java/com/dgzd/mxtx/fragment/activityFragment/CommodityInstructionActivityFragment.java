package com.dgzd.mxtx.fragment.activityFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dgzd.mxtx.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommodityInstructionActivityFragment extends Fragment {

    private View view;
    private ImageView ivBack;
    public CommodityInstructionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_instruction_activity, container, false);

        initView();
        backClick();
        return view;
    }

    private void initView() {
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
