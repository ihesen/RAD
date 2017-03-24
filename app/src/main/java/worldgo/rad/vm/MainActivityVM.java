package worldgo.rad.vm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.util.CommonUtils;
import worldgo.rad.R;
import worldgo.rad.databinding.ActivityMainBinding;
import worldgo.rad.entity.TabEntity;
import worldgo.rad.ui.MainActivity;
import worldgo.rad.ui.PagerItemFragment;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class MainActivityVM extends AbstractViewModel<MainActivity> {
    private String[] mTitles = {"妹子", "新闻", "更多"};
    private List<Fragment> mFragment = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.mipmap.ic_home_normal, R.mipmap.ic_girl_normal, R.mipmap.ic_care_normal};
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected, R.mipmap.ic_girl_selected, R.mipmap.ic_care_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ActivityMainBinding binding;

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
    }

    @Override
    public void onBindView(@NonNull MainActivity view) {
        super.onBindView(view);

        binding = view.getBinding();

        if (mFragment.size() == 0) {

            for (String mTitle : mTitles) {
                mFragment.add(PagerItemFragment.getInstance(mTitle));
            }
            for (int i = 0; i < mTitles.length; i++) {
                mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
            }
        }

        binding.mViewPager.setAdapter(new FragmentPagerAdapter(view.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }
        });


        binding.mTab.setTabData(mTabEntities);
        binding.mTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.mViewPager.setCurrentItem(position, false);
                //login status mock
                CommonUtils.loginEnable(position == 1);//TODO
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        binding.mViewPager.setOffscreenPageLimit(mTitles.length);
        binding.mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                binding.mTab.setCurrentTab(position);
            }
        });


    }

}