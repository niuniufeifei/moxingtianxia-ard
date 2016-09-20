package com.dgzd.mxtx.activity.mainView.homePage;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.fragment.mainView.search.CommodityClassifyFragment;
import com.dgzd.mxtx.fragment.mainView.search.CommodityEntireFragment;
import com.dgzd.mxtx.tools.TipoDisp;

/**
 * @version V1.0 <功能>
 * @FileName: MotoTypeActivity.java
 * @author: Jessica
 * @date: 2016-01-21 16:47
 */

public class MotoTypeActivity extends AppCompatActivity {

    private Class commodityFragmentArray[] = {CommodityEntireFragment.class, CommodityClassifyFragment.class};
    private int[] tabCommodityInfoArray = {R.string.entire, R.string.classify};
    private boolean isClassifyTabClicked = true;
    private ImageView ivBack;
    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moto_type);
        initView();
        backClick();
    }

    private void initView() {

        ivBack = (ImageView) findViewById(R.id.iv_back);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        layoutInflater = LayoutInflater.from(this);
        replaceTab(commodityFragmentArray, tabCommodityInfoArray);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void replaceTab(Class[] fragmentList, int[] tabTitleList) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("clear", true);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.frame_content);
        int pageCount = fragmentList.length;

        for (int i = 0; i < pageCount; ++i) {
            TabHost.TabSpec spec = null;
            if (i == 1) {
                spec = mTabHost.newTabSpec(getString(tabTitleList[i])).
                        setIndicator(getTabItemView(getString(tabTitleList[i]).toString(), R.drawable.select_logo, true));
            } else {
                spec = mTabHost.newTabSpec(getString(tabTitleList[i])).
                        setIndicator(getTabItemView(getString(tabTitleList[i]).toString(), 0, false));
            }
            mTabHost.addTab(spec, fragmentList[i], bundle);
        }


        classifyTabListener();
        mTabHost.setCurrentTab(0);
        changeTabsLineColor(mTabHost.getTabWidget());
        tabHeightSet(mTabHost);
    }

    private View getTabItemView(String strText, int iconId, boolean isIconShow) {
        View itemView = layoutInflater.inflate(R.layout.search_tab_item, null);
        TextView textView = (TextView) itemView.findViewById(R.id.tv_content);
        textView.setTextColor(getResources().getColor(R.color.orange));
        textView.setText(strText);
        ImageView ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
        if (isIconShow) {
            ivIcon.setBackgroundResource(iconId);
            ivIcon.setVisibility(View.VISIBLE);
        } else {
            ivIcon.setVisibility(View.GONE);
        }
        return itemView;
    }

    private void changeTabsLineColor(TabWidget tabWidget) {
        int size = tabWidget.getChildCount();
        for (int i = 0; i < tabWidget.getChildCount(); ++i) {
            tabWidget.getChildAt(i).setBackgroundResource(R.drawable.tab_line_selector);
        }
    }

    private void tabHeightSet(TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            int alt = TipoDisp.alt_tabs(this);
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = alt;
        }
    }

    private void classifyTabListener() {
        mTabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOk = getSupportFragmentManager().executePendingTransactions();
//            if (isOk){//加上判断无法实现功能
                String tag = getString(tabCommodityInfoArray[1]).toString();
                CommodityClassifyFragment fragment = (CommodityClassifyFragment) getSupportFragmentManager().findFragmentByTag(tag);
                if (null != fragment)
                    fragment.setClassifyShowFlag(true);
//            }
                mTabHost.setCurrentTab(0);
            }
        });

        mTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClassifyTabClicked) {
                    mTabHost.setCurrentTab(1);
                } else {
                    boolean isOk = getSupportFragmentManager().executePendingTransactions();
//                    if (isOk) {//加上判断无法实现功能
                    String tag = getString(tabCommodityInfoArray[1]).toString();
                    CommodityClassifyFragment fragment = (CommodityClassifyFragment) getSupportFragmentManager().findFragmentByTag(tag);
                    if (null != fragment)
                        fragment.setClassifyVisibility();
//                    }
                }

            }
        });

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                String tag = mTabHost.getCurrentTabTag();
                if (tag == getString(tabCommodityInfoArray[1]).toString()) {
                    isClassifyTabClicked = false;
                } else {
                    isClassifyTabClicked = true;
                }

            }
        });

    }

}
