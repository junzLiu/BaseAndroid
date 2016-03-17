package com.android.base.app.activity;

import android.app.ProgressDialog;
import android.base.com.baseandroid.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.base.assist.widget.LoadingView;
import com.android.base.assist.widget.TitleBarLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2015/11/2.
 */
public abstract class BaseActivity extends FragmentActivity implements LoadingView.IReloadDataDelegate, TitleBarLayout.TitleBackListener,TitleBarLayout.ActionListener {
    private View mRootView;
    private View mView;
    private LinearLayout mContentLayout;

    private FrameLayout mGuideLayout;
    private List<Integer> mGuideLayoutRidList = new ArrayList<Integer>();
    private int mGuidePostion = 0;

    /**
     * loadingView
     */
    private LoadingView mLoadingView;

    /**
     * titleBar
     */
    protected TitleBarLayout mTitleBarLayout;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setContentView(getLayoutId());
        init();
    }

    /**
     * 退出页面方法，子类有需要可以重写
     */
    protected void onBack() {
        this.finish();
        hideProgressDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }

    private void initView(){
        mRootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        mTitleBarLayout = (TitleBarLayout) mView.findViewById(R.id.layout_title);
        mContentLayout = (LinearLayout)mView.findViewById(R.id.layout_content);
        mGuideLayout = (FrameLayout)mView.findViewById(R.id.layout_guide);
        setContentView(mView);
        mContentLayout.addView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        initView();
        showGuide();
        mGuideLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mGuideLayoutRidList.size() > 0) {
                    showGuide();
                }
            }
        });
    }

    /**
     * get LayoutId
     *
     * @return LayoutId
     */
    protected abstract int getLayoutId();

    /**
     * init Ui wedget and init Data
     */
    protected abstract void init();

    //mark ************************  guideLayout part ************************
    /**
     * 添加引导浮层
     * @param layoutId 浮层布局
     */
    protected void addGuideLayout(int layoutId){
        mGuideLayoutRidList.add(layoutId);
    }

    /**
     * 显示引导浮层
     */
    protected void showGuide() {
        if (mGuideLayoutRidList.size() > 0) {

            if(mGuidePostion>=mGuideLayoutRidList.size()){
                mGuideLayout.removeAllViews();
                mGuideLayout.setVisibility(View.GONE);
                return;
            }

            if (mGuideLayout.getChildAt(0) != null)
                mGuideLayout.removeAllViews();

            mGuideLayout.addView(
                    LayoutInflater.from(this).inflate(
                            mGuideLayoutRidList.get(mGuidePostion), null),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            mGuideLayout.setVisibility(View.VISIBLE);
            mGuidePostion++;


        } else {
            mGuideLayout.setVisibility(View.GONE);
        }
    }

    //mark ************************  loadingView part ************************

    /**
     * 初始化LoadingView 页面需要时在oncreate方法中调用
     */
    private void initLoadingView() {
        mLoadingView = (LoadingView) findViewById(R.id.layout_loading_view);
    }

    /**
     * 显示loadingView 子类可以重写
     */
    protected void showLoadingView() {
        if (mLoadingView == null) {
            initLoadingView();
        }
        mLoadingView.showLoadingView();
        mContentLayout.setVisibility(View.GONE);
    }

    /**
     * 设置loadingView的背景颜色
     * @param color
     */
    protected void setLoadingViewBG(int color) {
        if (mLoadingView == null) {
            initLoadingView();
        }
        mLoadingView.setBG(this.getResources().getColor(color));
        mContentLayout.setVisibility(View.VISIBLE);
    }

    protected LoadingView getLoadingView(){
        return mLoadingView;
    }

    /**
     *
     * @param resourceId
     */
    protected void setLoadingTextColor(int resourceId){
        if(mLoadingView == null){
            initLoadingView();
        }

        mLoadingView.setTipTextColor(resourceId);
    }

    /**
     * 隐藏loadingView 子类可以重写
     */
    protected void hideLoadingView() {
        if (mLoadingView == null){
            initLoadingView();
        }
        mLoadingView.hideLoadingView();
        mContentLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示没有数据时的页面 子类有需要可以重写
     *
     * @param emptyTip
     *            值为空默认为 R.string.has_no_data 提示的文字
     * @param emptyDrawableResorceId
     *            值-1时默认为 R.drawable.no_data 提示icon
     */
    protected void showEmptyView(String emptyTip, int emptyDrawableResorceId) {
        showEmptyView(emptyTip, emptyDrawableResorceId, false);
    }

    /**
     * 显示没有数据时的页面 子类有需要可以重写
     *
     * @param emptyTip
     *            值为空默认为 R.string.has_no_data 提示的文字
     * @param emptyDrawableResorceId
     *            值-1时默认为 R.drawable.no_data 提示icon
     */
    protected void showEmptyView(String emptyTip, int emptyDrawableResorceId,boolean showContentLayout) {
        if (mLoadingView == null) {
            initLoadingView();
        }
        mLoadingView.showEmptyView(emptyTip, emptyDrawableResorceId);
        if(showContentLayout){
            mContentLayout.setVisibility(View.VISIBLE);
        }else{
            mContentLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示请求错误页面，子类有需要可以重写
     *
     * @param tipString
     *            提示文字 默认为R.string.ptrl_refresh_fail
     * @param tipIcon
     *            提示icon 当值为-1时默认为 R.drawable.wait_view_retry
     * */
    protected void showErrorView(String tipString, int tipIcon) {
        showErrorView(tipString, tipIcon, false);
    }

    /**
     * 显示请求错误页面，子类有需要可以重写
     *
     * @param tipString
     *            提示文字 默认为R.string.ptrl_refresh_fail
     * @param tipIcon
     *            提示icon 当值为-1时默认为 R.drawable.wait_view_retry
     * @param showContentLayout 是否显示页面
     * */

    protected void showErrorView(String tipString,int tipIcon,boolean showContentLayout){
        if (mLoadingView == null) {
            initLoadingView();
        }
        mLoadingView.showErrorView(tipString, tipIcon);
        if(mLoadingView.getReloadDataDelegate() == null){
            mLoadingView.setReloadDataDelegate(this);
        }
        if(showContentLayout){
            mContentLayout.setVisibility(View.VISIBLE);
        }else{
            mContentLayout.setVisibility(View.GONE);
        }
    }

    //mark ************************  progressDialog part ************************

    /**
     * show a progress dialog
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * show a progress dialog
     *
     */
    protected void showProgressDialog(int messageRid) {
        showProgressDialog(getString(messageRid));
    }

    /**
     * hide a progress dialog
     */
    protected void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    //mark ************************  tiltleView part ************************

    /**
     * 初始化titleView 子类有需要可以重写
     *
     * @param titleText
     *            title
     * @param hasBack
     *            是否有返回按钮
     * @param hasNav
     *            是否有更多按钮
     */
    protected void initTitleView(String titleText, boolean hasBack,
                                 boolean hasNav) {
        mTitleBarLayout.setVisibility(View.VISIBLE);
        mTitleBarLayout.showBackIndicator(hasBack);
        mTitleBarLayout.showNavigation(hasNav);
        mTitleBarLayout.setTitleText(titleText);
        if(hasBack){
            mTitleBarLayout.setTitleBackListener(this);
        }
    }

    /**
     * 初始化titleView 子类有需要可以重写
     */
    protected void initTitleView(String titleText,boolean hasBack,boolean hasNav,boolean hasClose) {
        this.initTitleView(titleText, hasBack, hasNav);
        mTitleBarLayout.showCloseIndicator(hasClose);
    }

    /**
     * hide titleView
     */
    protected void hideTitleView(){
        if(mTitleBarLayout != null){
            mTitleBarLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 更新titleaction的title
     * @param id action id
     * @param actionName name
     * @param clickable 是否可以点击
     */
    protected void updateTitleAction(int id,String actionName,boolean clickable){
        if(!mTitleBarLayout.hasAction(id)){
            mTitleBarLayout.addTextAction(actionName, id, clickable);
        }else{
            mTitleBarLayout.setTextAction(id, actionName, clickable);
        }
        if(clickable){
            mTitleBarLayout.setActionListener(this);
        }else{
            mTitleBarLayout.setActionListener(null);
        }
    }

    /**
     * 更新titleaction 图片
     * @param id
     * @param imageResorceId 图片资源
     * @param clickable
     */
    protected void updateTitleAction(int id,int imageResorceId,boolean clickable){
        if(!mTitleBarLayout.hasAction(id)){
            mTitleBarLayout.addIconAction(imageResorceId, id, clickable);
        }else{
            mTitleBarLayout.setIconAction(id, imageResorceId);
        }
        if(clickable){
            mTitleBarLayout.setActionListener(this);
        }else{
            mTitleBarLayout.setActionListener(null);
        }
    }

    /**
     * 初始化titleView 子类有需要可以重写
     */
    protected void initTitleView(int resid,boolean hasBack,boolean hasNav) {
        mTitleBarLayout.setVisibility(View.VISIBLE);
        mTitleBarLayout.showBackIndicator(hasBack);
        mTitleBarLayout.showNavigation(hasNav);
        mTitleBarLayout.setTitleImageView(resid);
        if(hasBack){
            mTitleBarLayout.setTitleBackListener(this);
        }
    }


    @Override
    public void onReload() {

    }

    @Override
    public void onBackClick() {

    }
}
