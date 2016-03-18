package com.android.base.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.base.app.activity.BaseActivity;
import com.android.base.assist.widget.LoadingView;
import com.android.base.assist.widget.TitleBarLayout;

// FIXME: 2016/3/18 dispose fragment's loading and error when it isn't match activity
public abstract class BaseFragment extends Fragment implements TitleBarLayout.TitleBackListener, LoadingView.IReloadDataDelegate, TitleBarLayout.ActionListener {

    protected View mView;
    protected BaseActivity mActivity;
    protected boolean mIsFinish = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        initBaseView(inflater);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsFinish = true;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * layout id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化View组件
     */
    protected abstract void initView();

    /**
     * 获取View
     */
    protected void initBaseView(LayoutInflater inflater) {
        mView = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
        initView();
        if (mActivity == null)
            return;
        mActivity.getLoadingView().setReloadDataDelegate(this);
    }

    protected void addGuideLayout(int layoutId) {
        if (mActivity == null)
            return;
        mActivity.addGuideLayout(layoutId);
    }

    protected void showGuide() {
        if (mActivity == null)
            return;
        mActivity.showGuide();
    }

    /**
     * hide titleView
     */
    protected void hideTitleView() {
        if (mActivity == null)
            return;
        mActivity.hideTitleView();
    }

    /**
     * 显示loadingView 子类可以重写
     */
    protected void showLoadingView() {
        if (mActivity == null)
            return;
        mActivity.showLoadingView();
    }

    /**
     * 隐藏loadingView 子类可以重写
     */
    protected void hideLoadingView() {
        if (mActivity == null)
            return;
        mActivity.hideLoadingView();
    }

    /**
     * 显示没有数据时的页面 子类有需要可以重写
     *
     * @param emptyTip               值为空默认为 R.string.has_no_data 提示的文字
     * @param emptyDrawableResorceId 值-1时默认为 R.drawable.no_data 提示icon
     */
    protected void showEmptyView(String emptyTip, int emptyDrawableResorceId) {
        if (mActivity == null)
            return;
        mActivity.showEmptyView(emptyTip, emptyDrawableResorceId);
    }

    /**
     * 显示请求错误页面，子类有需要可以重写
     *
     * @param tipString 提示文字 默认为R.string.ptrl_refresh_fail
     * @param tipIcon   提示icon 当值为-1时默认为 R.drawable.wait_view_retry
     */
    protected void showErrorView(String tipString, int tipIcon) {
        if (mActivity == null)
            return;
        mActivity.showEmptyView(tipString, tipIcon);
    }

    /**
     * 设置loadingView的背景颜色
     *
     * @param color
     */
    protected void setLoadingViewBG(int color) {
        if (mActivity == null)
            return;
        mActivity.setLoadingViewBG(color);
    }

    /**
     * show a progress dialog
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (mActivity == null)
            return;
        mActivity.showProgressDialog(message);
    }

    /**
     * show a progress dialog
     */
    protected void showProgressDialog(int messageRid) {
        if (mActivity == null)
            return;
        mActivity.showProgressDialog(messageRid);
    }

    /**
     * hide a progress dialog
     */
    protected void hideProgressDialog() {
        if (mActivity == null)
            return;
        mActivity.hideProgressDialog();
    }

    /**
     * 初始化titleView 子类有需要可以重写
     */
    protected void initTitleView(String titleText, boolean hasBack, boolean hasNav) {
        if (mActivity == null)
            return;
        mActivity.initTitleView(titleText, hasBack, hasNav);
        if (hasBack)
            mActivity.getTitleView().setTitleBackListener(this);
    }

    /**
     * 更新titleaction的名字
     * @param id action id
     * @param actionName name
     */
    /**
     * 更新titleaction的title
     *
     * @param id         action id
     * @param actionName name
     * @param clickable  是否可以点击
     */
    protected void updateTitleAction(int id, String actionName, boolean clickable) {
        if (mActivity == null)
            return;
        mActivity.updateTitleAction(id, actionName, clickable);
        if (clickable)
            mActivity.getTitleView().setActionListener(this);
        else
            mActivity.getTitleView().setActionListener(null);
    }


    /**
     * 初始化titleView 子类有需要可以重写
     */
    protected void initTitleView(int resid, boolean hasBack, boolean hasNav) {
        if (mActivity == null)
            return;
        mActivity.initTitleView(resid, hasBack, hasNav);
    }

    @Override
    public void onActionPerformed(int id) {

    }

    @Override
    public void onReload() {
        //loadingView点击重新加载数据，子类可以重写
    }

    @Override
    public void onBackClick() {
        mActivity.onBack();
    }

    @Override
    public void onCloseClick() {
        mActivity.onBack();
    }
}
