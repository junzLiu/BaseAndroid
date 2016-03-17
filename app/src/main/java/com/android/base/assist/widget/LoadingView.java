package com.android.base.assist.widget;

import android.base.com.baseandroid.R;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.base.assist.tools.NetUtil;

/**
 * loadingView 加载页面
 * 
 * @author liumingjie
 * @date 2015.04.28
 */

public class LoadingView extends LinearLayout {

	// private View mWaitView;
	private ProgressBar mLargerProgressBar;
	private TextView mTipTextView;
	private IReloadDataDelegate reloadDataDelegate;
	private ImageView mImageView;
	private ProgressBar mLoadingImageView;
	private LinearLayout mProgressLayout;
	private TextView mLoadingTextView;

	//private AnimationDrawable anim;
	
	public LoadingView(Context context) {
		super(context);
		initView();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);
		addView(createImageView());
		addView(createProgressLayout());
	}

	private ImageView createImageView() {
		mImageView = new ImageView(this.getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		mImageView.setLayoutParams(layoutParams);
		return mImageView;
	}

	private LinearLayout createProgressLayout() {
		mProgressLayout = new LinearLayout(this.getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		mProgressLayout.setOrientation(VERTICAL);
		mProgressLayout.setLayoutParams(layoutParams);
		mProgressLayout.addView(createLoadingImageView());
		mProgressLayout.addView(createLargeProgressBar());
		mProgressLayout.addView(createTipTextView());
		mProgressLayout.addView(createLoadingTextView());
		return mProgressLayout;
	}

	private ProgressBar createLoadingImageView(){
		mLoadingImageView = new ProgressBar(this.getContext());

		return mLoadingImageView;
	}

	private ProgressBar createLargeProgressBar() {
		mLargerProgressBar = new ProgressBar(getContext());
		mLargerProgressBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, this.getResources()
				.getDimensionPixelOffset(R.dimen.loading_size)));
		mLargerProgressBar.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
		return mLargerProgressBar;
	}

	private TextView createTipTextView() {
		mTipTextView = new TextView(getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		mTipTextView.setLayoutParams(layoutParams);
		mTipTextView.setTextColor(getResources().getColor(R.color.text_color_tip));
		mTipTextView.setTextSize(14);
		return mTipTextView;
	}

	private TextView createLoadingTextView(){
		mLoadingTextView = new TextView(getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		layoutParams.topMargin = 10;
		mLoadingTextView.setLayoutParams(layoutParams);
		mLoadingTextView.setTextColor(getResources().getColor(R.color.text_color_load));
		mLoadingTextView.setTextSize(14);
		return mLoadingTextView;
	}

	public void showLoadingView() {
		mLoadingImageView.setVisibility(View.VISIBLE);
		LoadingView.this.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.GONE);
		mLoadingTextView.setVisibility(View.VISIBLE);
		mLoadingTextView.setText(R.string.loading_default);
		
		mLargerProgressBar.setVisibility(View.GONE);
		mTipTextView.setVisibility(View.GONE);
	}

	public void hideLoadingView() {
		LoadingView.this.setVisibility(View.GONE);
		mLoadingImageView.setVisibility(View.GONE);
	}

	/**
	 * 没有数据显示时页面
	 * 
	 * @param tipString  值为空默认为 R.string.has_no_data
	 *            提示的文字
	 * @param tipIcon  值-1时默认为  R.drawable.no_data
	 *            提示icon
	 */
	public void showEmptyView(String tipString, int tipIcon) {
		mLoadingImageView.setVisibility(View.GONE);
		LoadingView.this.setVisibility(View.VISIBLE);
		mLargerProgressBar.setVisibility(View.GONE);
		mTipTextView.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.VISIBLE);
		if(tipIcon == -1){
			tipIcon = R.drawable.loading_empty;
		}
		mImageView.setBackgroundResource(tipIcon);
		mImageView.setOnClickListener(null);
		if(TextUtils.isEmpty(tipString)){
			tipString = this.getResources().getString(R.string.loading_empty);
		}
		mTipTextView.setText(tipString);
		mTipTextView.setTextColor(getResources().getColor(R.color.text_grey));
		mLoadingTextView.setVisibility(View.GONE);
	}

	/**
	 * 加载数据错误时页面显示
	 * 
	 * @param tipString
	 *            提示文字 默认为R.string.ptrl_refresh_fail
	 * @param tipIcon
	 *            提示icon 当值为-1时默认为 R.drawable.wait_view_retry
	 * */
	public void showErrorView(String tipString, int tipIcon) {
		LoadingView.this.setVisibility(View.VISIBLE);
		if (TextUtils.isEmpty(tipString)) {
			if (!NetUtil.isNetworkConnected(this.getContext())) {
				tipString = getResources().getString(R.string.loading_net_retry);
			} else {
				tipString = getResources().getString(R.string.loading_fail_retry);
			}
		}
		mLoadingTextView.setVisibility(View.GONE);
		mTipTextView.setText(tipString);
		mTipTextView.setVisibility(View.VISIBLE);
		mLoadingImageView.setVisibility(View.GONE);
		mLargerProgressBar.setVisibility(View.GONE);
		mImageView.setVisibility(View.VISIBLE);
		if (tipIcon == -1) {
			mImageView.setBackgroundResource(R.drawable.loading_retry);
		} else {
			mImageView.setBackgroundResource(tipIcon);
		}
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLargerProgressBar.setVisibility(View.GONE);
				mLoadingImageView.setVisibility(View.VISIBLE);
				mLoadingTextView.setVisibility(View.GONE);
				mTipTextView.setText(R.string.loading_default);
				mImageView.setVisibility(View.GONE);
				LoadingView.this.setOnClickListener(null);
				if(reloadDataDelegate != null){
					reloadDataDelegate.onReload();
				}
			}
		});
		mTipTextView.setTextColor(getResources().getColor(R.color.text_color_tip));
	}

	public interface IReloadDataDelegate {
		/**
		 * 页面加载失败时点击重新加载回调接口
		 */
		void onReload();
	}

	public void setReloadDataDelegate(IReloadDataDelegate reloadDataDelegate) {
		this.reloadDataDelegate = reloadDataDelegate;
	}
	
	public IReloadDataDelegate getReloadDataDelegate() {
		return reloadDataDelegate;
	}
	
	public void setBG(int color) {
		setBackgroundColor(color);
		mLargerProgressBar.setVisibility(View.GONE);
		mLoadingImageView.setVisibility(View.VISIBLE);
		mTipTextView.setVisibility(View.GONE);
	}

	public void showLargeWaitting() {
		mLoadingImageView.setVisibility(View.GONE);
		mLargerProgressBar.setVisibility(View.VISIBLE);
		mTipTextView.setVisibility(View.GONE);
		LoadingView.this.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.GONE);
		mLoadingTextView.setVisibility(View.GONE);
	}
	
	public void setTipTextColor(int resourceId){
		mTipTextView.setTextColor(getResources().getColor(resourceId));
		mLoadingTextView.setTextColor(getResources().getColor(resourceId));
	}
}
