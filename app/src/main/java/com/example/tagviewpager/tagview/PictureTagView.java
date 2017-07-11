package com.example.tagviewpager.tagview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tagviewpager.R;

/**
 * Description：标签view
 * Author：lxl
 * Date： 2017/7/3 17:09
 */
public class PictureTagView extends RelativeLayout {
	private static final String TAG = "PictureTagView";


	private Context mContext;
	private TextView mTvLabelleft;
	private TextView mTvLabelright;
	private ImageView mPoint;
	public enum Direction{Left,Right}
	private Direction mDirection = Direction.Left;
	private String mTagdetail;//标记的描述
	private int mTaglocation;//标记的位置标记

	public PictureTagView(Context context, Direction direction) {
		super(context);
		this.mContext = context;
		this.mDirection = direction;
		initViews();
	}

	/** 初始化视图 **/
	protected void initViews(){
		LayoutInflater.from(mContext).inflate(R.layout.tag, this,true);
		mTvLabelleft = (TextView) findViewById(R.id.tv_left);//左边背景
		mTvLabelright = (TextView) findViewById(R.id.tv_right);//右边背景
		mPoint=(ImageView)findViewById(R.id.iv_icon);//中间园点
	}


	public void setTagDescribe(String tag) {
		mTagdetail=tag;
		directionChange();
		startAnimat();
	}
	public String getTagdetail() {
		return mTagdetail;
	}

	public void setLocation(int loc) {
		mTaglocation=loc;
	}
	public int getLocation() {
		return mTaglocation;
	}
	public void setmDirection(Direction mDirection) {
		this.mDirection = mDirection;
	}

	/**
	 * 指示点动画
	 */
	private void startAnimat() {
		AnimationSet as = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, ScaleAnimation.RELATIVE_TO_SELF,
				0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(600 * 3);
		sa.setRepeatCount(30);// 设置循环
		AlphaAnimation aniAlp = new AlphaAnimation(1, 0.1f);
		aniAlp.setRepeatCount(30);// 设置循环
		as.setDuration(600 * 3);
		as.addAnimation(sa);
		as.addAnimation(aniAlp);
		mPoint.startAnimation(as);
	}

	public void directionChange(){
		switch(mDirection){
		case Left:
			mTvLabelleft.setVisibility(VISIBLE);
			mTvLabelright.setVisibility(GONE);
			mTvLabelleft.setText(mTagdetail);
			break;
		case Right:
			mTvLabelright.setVisibility(VISIBLE);
			mTvLabelleft.setVisibility(GONE);
			mTvLabelright.setText(mTagdetail);
			break;
		}

	}

}
