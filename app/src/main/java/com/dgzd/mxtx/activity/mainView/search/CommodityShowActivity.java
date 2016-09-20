package com.dgzd.mxtx.activity.mainView.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.fragment.mainView.search.CommodityClassifyFragment;
import com.dgzd.mxtx.fragment.mainView.search.CommodityEntireFragment;
import com.dgzd.mxtx.fragment.mainView.search.HotVideoFragment;
import com.dgzd.mxtx.fragment.mainView.search.TimeVideoFragment;
import com.dgzd.mxtx.tools.TipoDisp;

public class CommodityShowActivity extends AppCompatActivity {

    private Class commodityFragmentArray[] = {CommodityEntireFragment.class, CommodityClassifyFragment.class};
    private Class videoFragmentArray[] = {HotVideoFragment.class, TimeVideoFragment.class};
    private int[] tabCommodityInfoArray = {R.string.entire, R.string.classify};
    private int[] tabVideoInfoArray = {R.string.hot, R.string.time};
    private boolean isCommodityPage = true;
    private boolean isClassifyTabClicked = true;

    private View popView;
    private ImageView ivBack;
    private PopupWindow popupWindow;
    private LinearLayout layoutSift;
    private TextView tvSift;
    private RelativeLayout layoutSearch;
    private InputMethodManager imm;

    private TextView tvSearch;
    private EditText etSearch;
    private TextView tvCancel;
    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_show);
        getBundleContent("isCommodityShow");
        initView();
        backClick();
        enterSearchClick();
        siftLayoutClick();
        searchClick();
        cancelClick();
    }

    private void getBundleContent(String key) {
        Intent intent = getIntent();
        if (intent.hasExtra(key)) {
            isCommodityPage = intent.getBooleanExtra(key, true);
        }
    }

    private void initView() {
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        layoutSift = (LinearLayout) findViewById(R.id.layout_sift);
        tvSift = (TextView) findViewById(R.id.tv_sift);
        layoutSearch = (RelativeLayout) findViewById(R.id.layout_search);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        layoutInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        if (isCommodityPage) {
            replaceTab(commodityFragmentArray, tabCommodityInfoArray);
        } else {
            replaceTab(videoFragmentArray, tabVideoInfoArray);
        }
    }

    private void replaceTab(Class[] fragmentList, int[] tabTitleList) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("clear", true);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.frame_content);
        int pageCount = fragmentList.length;

        if (isCommodityPage) {
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
            tvSift.setText(getString(R.string.commodity));
        } else {
            for (int i = 0; i < pageCount; ++i) {
                TabHost.TabSpec spec = mTabHost.newTabSpec(getString(tabTitleList[i])).
                        setIndicator(getTabItemView(getString(tabTitleList[i]).toString(), 0, false));
                mTabHost.addTab(spec, fragmentList[i], bundle);
            }
            tvSift.setText(getString(R.string.video));
        }

        classifyTabListener();
        mTabHost.setCurrentTab(0);
        changeTabsLineColor(mTabHost.getTabWidget());
        tabHeightSet(mTabHost);
//        updateTab(mTabHost);
    }

    private void updateTab(TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View popView = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.orange));
        }
    }

    private void tabHeightSet(TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            int alt = TipoDisp.alt_tabs(this);
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = alt;
        }
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

    /**
     * change tab line color
     *
     * @param tabWidget
     */
    private void changeTabsLineColor(TabWidget tabWidget) {
        int size = tabWidget.getChildCount();
        for (int i = 0; i < tabWidget.getChildCount(); ++i) {
            tabWidget.getChildAt(i).setBackgroundResource(R.drawable.tab_line_selector);
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

                if (isCommodityPage) {
                    if (isClassifyTabClicked) {
                        mTabHost.setCurrentTab(1);
                    } else {
                        boolean isOk = getSupportFragmentManager().executePendingTransactions();
//                        if (isOk){//加上判断无法实现功能
                        String tag = getString(tabCommodityInfoArray[1]).toString();
                        CommodityClassifyFragment fragment = (CommodityClassifyFragment) getSupportFragmentManager().findFragmentByTag(tag);
                        if (null != fragment)
                            fragment.setClassifyVisibility();
//                        }
                    }
                } else {
                    mTabHost.setCurrentTab(1);
                }
            }
        });

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (isCommodityPage) {
                    String tag = mTabHost.getCurrentTabTag();
                    if (tag == getString(tabCommodityInfoArray[1]).toString()) {
                        isClassifyTabClicked = false;
                    } else {
                        isClassifyTabClicked = true;
                    }
                } else {
                    isClassifyTabClicked = true;
                }
            }
        });

    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void siftLayoutClick() {
        layoutSift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
    }

    private void cancelClick() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchChange(false);

                String strSearch = etSearch.getText().toString();
                boolean isOk = getSupportFragmentManager().executePendingTransactions();
                String curTag = mTabHost.getCurrentTabTag();
                if (curTag == getString(tabCommodityInfoArray[0]).toString()) {

                    CommodityEntireFragment fragment = (CommodityEntireFragment) getSupportFragmentManager().findFragmentByTag(curTag);
                    if (null != fragment)
                        fragment.resetSearchKeyString();
                } else if (curTag == getString(tabCommodityInfoArray[1]).toString()) {

                    CommodityClassifyFragment fragment = (CommodityClassifyFragment) getSupportFragmentManager().findFragmentByTag(curTag);
                    if (null != fragment)
                        fragment.resetSearchKeyString();
                } else if (curTag == getString(tabVideoInfoArray[0]).toString()) {

                    HotVideoFragment fragment = (HotVideoFragment) getSupportFragmentManager().findFragmentByTag(curTag);
                    if (null != fragment)
                        fragment.resetSearchKeyString();

                } else if (curTag == getString(tabVideoInfoArray[1]).toString()) {

                    TimeVideoFragment fragment = (TimeVideoFragment) getSupportFragmentManager().findFragmentByTag(curTag);
                    if (null != fragment)
                        fragment.resetSearchKeyString();

                }
            }
        });
    }

    private void enterSearchClick() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    String strSearch = etSearch.getText().toString();
                    boolean isOk = getSupportFragmentManager().executePendingTransactions();
                    String curTag = mTabHost.getCurrentTabTag();
                    if (curTag == getString(tabCommodityInfoArray[0]).toString()) {

                        CommodityEntireFragment fragment = (CommodityEntireFragment) getSupportFragmentManager().findFragmentByTag(curTag);
                        if (null != fragment)
                            fragment.searchCommodityData(strSearch);
                    } else if (curTag == getString(tabCommodityInfoArray[1]).toString()) {

                        CommodityClassifyFragment fragment = (CommodityClassifyFragment) getSupportFragmentManager().findFragmentByTag(curTag);
                        if (null != fragment)
                            fragment.searchCommodityData(strSearch);
                    } else if (curTag == getString(tabVideoInfoArray[0]).toString()) {

                        HotVideoFragment fragment = (HotVideoFragment) getSupportFragmentManager().findFragmentByTag(curTag);
                        if (null != fragment)
                            fragment.searchVideoData(strSearch);
                    } else if (curTag == getString(tabVideoInfoArray[1]).toString()) {

                        TimeVideoFragment fragment = (TimeVideoFragment) getSupportFragmentManager().findFragmentByTag(curTag);
                        if (null != fragment)
                            fragment.searchVideoData(strSearch);
                    }
                    return true;
                }
                return false;
            }
        });
    }


    private void searchClick() {
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchChange(true);
            }
        });
    }

    private void searchChange(boolean isShow) {
        if (isShow) {
            tvSearch.setVisibility(View.GONE);
            tvCancel.setVisibility(View.VISIBLE);
            etSearch.setVisibility(View.VISIBLE);
            etSearch.setFocusable(true);
            etSearch.setFocusableInTouchMode(true);
            etSearch.requestFocus();
            etSearch.findFocus();
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        } else {
            tvSearch.setVisibility(View.VISIBLE);
            etSearch.setText("");
            etSearch.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(etSearch.getApplicationWindowToken(), 0);
            }
        }
    }

    private void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popView = layoutInflater.inflate(R.layout.commodity_select, null);
            TextView tvCommodity = (TextView) popView.findViewById(R.id.tv_commodity);
            tvCommodity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                    mTabHost.removeAllViews();
                    mTabHost.clearAllTabs();
                    isCommodityPage = true;
                    replaceTab(commodityFragmentArray, tabCommodityInfoArray);
                }
            });

            TextView tvVideo = (TextView) popView.findViewById(R.id.tv_video);
            tvVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                    mTabHost.removeAllViews();
                    mTabHost.clearAllTabs();
                    isCommodityPage = false;
                    replaceTab(videoFragmentArray, tabVideoInfoArray);
                }
            });
            popupWindow = new PopupWindow(popView, parent.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.showAsDropDown(parent, 0, 0);

    }
}
