/*
 * Copyright (c) 2016. WorldGo Technology Co., Ltd
 * DO NOT DIVULGE
 */

package worldgo.common.viewmodel.varyview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import worldgo.common.R;
import worldgo.common.viewmodel.varyview.anim.VaryViewAnimProvider;


/**
 * 功能：切换布局，用一个新的View覆盖原先的View
 */
public class OverlapViewHelper implements ICaseViewHelper {

    public ICaseViewHelper mHelper;
    public View mDataView;
    private int mVaryViewBackground;

    public OverlapViewHelper(View view) {
        this.mDataView = view;

        /*找到父View*/
        ViewGroup parent;
        if (view.getParent() != null) {
            parent = (ViewGroup) view.getParent();
        } else {
            parent = (ViewGroup) view.getRootView().findViewById(android.R.id.content);
        }
        if (parent == null) {
            throw new IllegalArgumentException(view.getClass().getSimpleName() + "找不到parent，可以嵌套一层FrameLayout或选择其它作为setStatusTargetView后尝试");
        }
        /*记录要显示的View在父View中的位置*/
        int childIndex = 0;
        int childCount = parent.getChildCount();
        for (int index = 0; index < childCount; index++) {
            if (view == parent.getChildAt(index)) {
                childIndex = index;
                break;
            }
        }

        /*重新将一个frameLayout添加进原来的View的位子中*/
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        FrameLayout frameLayout = new FrameLayout(view.getContext());
        parent.removeViewAt(childIndex);
        parent.addView(frameLayout, childIndex, layoutParams);

        /*在这个frameLayout中实现将新的View覆盖在原来的view上*/
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View floatView = new View(view.getContext());
        floatView.setId(R.id.varyView_holder_view);
        frameLayout.addView(view, params);
        frameLayout.addView(floatView, params);
        /*帧布局中有两层View，原始View与占位符View,占位符View负责切换*/
        mHelper = new ReplaceViewHelper(floatView);
    }

    @Override
    public View getCurrentView() {
        return mHelper.getCurrentView();
    }

    @Override
    public void restoreLayout() {
        mHelper.restoreLayout();
    }

    @Override
    public void showCaseLayout(View view) {
        view.setBackgroundColor(mVaryViewBackground);
        mHelper.showCaseLayout(view);
    }

    @Override
    public void showCaseLayout(int layoutId) {
        showCaseLayout(inflate(layoutId));
    }

    @Override
    public View inflate(int layoutId) {
        return mHelper.inflate(layoutId);
    }

    @Override
    public Context getContext() {
        return mHelper.getContext();
    }

    @Override
    public View getDataView() {
        return mDataView;
    }

    void setVaryViewBackground(int varyViewBackground) {
        mVaryViewBackground = varyViewBackground;
    }
    @Override
    public void setViewAnimProvider(VaryViewAnimProvider viewAnimProvider) {
        mHelper.setViewAnimProvider(viewAnimProvider);
    }
}