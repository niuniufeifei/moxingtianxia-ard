package com.dgzd.mxtx.fragment.activityFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.view.AddressAddModifyActivity;
import com.dgzd.mxtx.adapter.AddressManagementAdapter;
import com.dgzd.mxtx.entirety.AddressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddressManagementActivityFragment extends Fragment implements AddressManagementAdapter.addressAdapterListener {

    private View view;
    private ImageView ivBack;
    private TextView tvAddAddress;
    private ListView lvAddress;
    private List<AddressInfo> addressInfos;
    public AddressManagementActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_address_management_activity, container, false);

        initView();
        backClick();
        addAddressClick();
        return view;
    }

    private void initView() {
        ivBack = (ImageView)view.findViewById(R.id.iv_back);
        tvAddAddress = (TextView)view.findViewById(R.id.tv_add_address);
        lvAddress = (ListView)view.findViewById(R.id.lv_address);

        getAddressData();
        AddressManagementAdapter adapter = new AddressManagementAdapter(AddressManagementActivityFragment.this, addressInfos, 0);
        lvAddress.setAdapter(adapter);
    }

    private void backClick(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void addAddressClick(){
        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressAddModifyActivity.class));
            }
        });
    }

    private void getAddressData(){
        addressInfos = new ArrayList<AddressInfo>();
        for (int i=0; i<5; ++i){
            AddressInfo info = new AddressInfo();
            info.setAddress("123");
            info.seConsignee("小红");
            info.setContactWay("15954870918");
            info.setPostCode("266100");
            info.setShoppingAddress("山东青岛");
            addressInfos.add(info);
        }
    }

    @Override
    public void getSelectId(int id) {
        Toast.makeText(getActivity(), "selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getAddressModifyId(int id) {

    }
}
