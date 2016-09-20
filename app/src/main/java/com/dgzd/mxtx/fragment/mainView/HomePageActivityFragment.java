package com.dgzd.mxtx.fragment.mainView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.login.LoginActivity;
import com.dgzd.mxtx.fragment.mainView.bottomTab.HomePageFragment;
import com.dgzd.mxtx.fragment.mainView.bottomTab.HomePageMineFragment;
import com.dgzd.mxtx.fragment.mainView.bottomTab.HomePageShopCarFragment;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomePageActivityFragment extends Fragment {

    private Class fragmentArray[] = {HomePageFragment.class, HomePageShopCarFragment.class, HomePageMineFragment.class};
    private int[][] tabHostInfoArrays = {{R.drawable.home_page_selector, R.string.home_page},
            {R.drawable.shoppingcart_selector, R.string.shopping_cart},
            {R.drawable.personal_center_selector, R.string.mine}};

    private View view;
    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;

    public HomePageActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page_activity, container, false);
        initView();
        mineFragmentClick();
        return view;
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(getActivity());
        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getActivity().getSupportFragmentManager(), R.id.framelayout_content);

        int pageCount = fragmentArray.length;
        for (int i = 0; i < pageCount; ++i) {
            TabHost.TabSpec spec = mTabHost.newTabSpec(getString(tabHostInfoArrays[i][1])).setIndicator(getTabItemView(i));
            mTabHost.addTab(spec, fragmentArray[i], null);
        }
    }

    private View getTabItemView(int index) {
        View itemView = layoutInflater.inflate(R.layout.main_tab_item, null);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_icon);
        imageView.setImageResource(tabHostInfoArrays[index][0]);

        TextView textView = (TextView) itemView.findViewById(R.id.tv_content);
        textView.setText(getString(tabHostInfoArrays[index][1]));

        return itemView;
    }

    private void mineFragmentClick() {
        mTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSigned = MxtxApplictaion.getInstance().isSignedIn();
                if (!isSigned) {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), GlobalEntity.REQUEST_SHOPPING_CART_LOGIN);
                } else {
                    mTabHost.setCurrentTab(1);
                }
            }
        });

        mTabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSigned = MxtxApplictaion.getInstance().isSignedIn();
                if (!isSigned) {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), GlobalEntity.REQUEST_LOGIN);
                } else {
                    mTabHost.setCurrentTab(2);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalEntity.REQUEST_SHOPPING_CART_LOGIN) {
            if (resultCode == GlobalEntity.REQUEST_LOGIN_FAILED) {
                MxtxApplictaion.getInstance().logout();
            }else {
                mTabHost.setCurrentTab(1);
            }
        } else if (resultCode == GlobalEntity.REQUEST_LOGIN) {
            if (resultCode == GlobalEntity.REQUEST_LOGIN_FAILED) {
                MxtxApplictaion.getInstance().logout();
            }else {
                mTabHost.setCurrentTab(2);
            }

        }
    }
}
