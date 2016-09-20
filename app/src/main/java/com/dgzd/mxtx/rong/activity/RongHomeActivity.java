package com.dgzd.mxtx.rong.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.rong.popuwindow.PopuItem;
import com.dgzd.mxtx.rong.popuwindow.PopuJar;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by nsd on 2016/2/22.
 * Notes:融云主页面
 */
public class RongHomeActivity extends FragmentActivity {
    Fragment mConversionList;
    private ViewPager mViewPager;
    FragmentPagerAdapter mFragmentPagerAdapter;
    List<Fragment> mFragment = new ArrayList<>();
    private TextView mLeftVeiw, mRightView;
    private ImageView mTopRight,mTopLeft;
    PopuJar mPopu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ronghome);
        initView();
    }

    private void initView() {
        mConversionList = initConcersionList();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLeftVeiw = (TextView) findViewById(R.id.conversionlist);
        mRightView = (TextView) findViewById(R.id.contact);
        mTopRight = (ImageView) findViewById(R.id.top_right);
        mTopLeft = (ImageView) findViewById(R.id.top_left);
        mFragment.add(mConversionList);
        mFragment.add(ContactFragment.getIntance());
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        mLeftVeiw.setBackgroundResource(R.drawable.ic_left_selected);
                        mLeftVeiw.setTextColor(getResources().getColor(R.color.ic_yellow));
                        mRightView.setBackgroundResource(R.drawable.ic_right_nomal);
                        mRightView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        mLeftVeiw.setBackgroundResource(R.drawable.ic_left_nomal);
                        mLeftVeiw.setTextColor(getResources().getColor(R.color.white));
                        mRightView.setBackgroundResource(R.drawable.ic_right_selected);
                        mRightView.setTextColor(getResources().getColor(R.color.ic_yellow));
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLeftVeiw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
//                mLeftVeiw.setBackgroundResource(R.drawable.ic_left_selected);
//                mLeftVeiw.setTextColor(getResources().getColor(R.color.ic_yellow));
//                mRightView.setBackgroundResource(R.drawable.ic_right_nomal);
//                mRightView.setTextColor(getResources().getColor(R.color.white));
            }
        });
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
//                mLeftVeiw.setBackgroundResource(R.drawable.ic_left_nomal);
//                mLeftVeiw.setTextColor(getResources().getColor(R.color.white));
//                mRightView.setBackgroundResource(R.drawable.ic_right_selected);
//                mRightView.setTextColor(getResources().getColor(R.color.ic_yellow));
            }
        });
        // //popuwindows
        PopuItem userItem = new PopuItem(0, "通讯录", null);
        userItem.setSticky(true);
        mPopu = new PopuJar(this, PopuJar.VERTICAL);
        mPopu.addPopuItem(userItem);
        mPopu.setOnPopuItemClickListener(new PopuJar.OnPopuItemClickListener() {
            @Override
            public void onItemClick(PopuJar source, int pos, int actionId) {
                PopuItem PopuItem = mPopu.getPopuItem(pos);
                if (actionId == 0) {
                    mPopu.dismiss();
                    startActivity(new Intent(RongHomeActivity.this, CreatDisscussActivity.class));
                }
            }
        });
        mTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopu.show(v);
            }
        });
        mTopLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private Fragment initConcersionList() {
        if (mConversionList == null) {
            ConversationListFragment listFragment = ConversationListFragment.getInstance();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论�?
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务�?
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//公共服务�?
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .build();
            listFragment.setUri(uri);
            return listFragment;
        } else {
            return mConversionList;
        }
    }
}
