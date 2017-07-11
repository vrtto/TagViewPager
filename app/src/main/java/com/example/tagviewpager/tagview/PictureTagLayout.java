package com.example.tagviewpager.tagview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.example.tagviewpager.bean.TagItem;
import com.example.tagviewpager.view.EditTagDialog;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Description：标签帧布局
 * Author：lxl
 * Date： 2017/7/3 17:09
 */
@SuppressLint("NewApi")
public class PictureTagLayout extends RelativeLayout implements OnTouchListener {
	private static final String TAG = "PictureTagLayout";

	private static final int CLICKRANGE = 5;
	int mStartX = 0;
	int mStartY = 0;
	int mStartTouchViewLeft = 0;
	int mStartTouchViewTop = 0;
	private PictureTagView mTouchView;
	private long mStartTime = 0;
	private long mEndTime = 0;

	int mCurrentPosition;

	Map<Integer, ArrayList<TagItem>> mAllTag = new HashMap<Integer,  ArrayList<TagItem>>();

	private Taglistener mListener;

	public PictureTagLayout(Context context) {
		super(context, null);
	}
	public PictureTagLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private void init(){
		this.setOnTouchListener(this);
	}
	public Map<Integer, ArrayList<TagItem>> getAllTag() {
		return mAllTag;
	}
	public void setonTaglistener(Taglistener mListener){
		this.mListener=mListener;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTouchView = null;
				mStartX = (int) event.getX();
				mStartY = (int) event.getY();
				if(hasView(mStartX,mStartY)){
					mStartTouchViewLeft = mTouchView.getLeft();
					mStartTouchViewTop = mTouchView.getTop();
					mStartTime = System.currentTimeMillis();
					return true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				moveView((int) event.getX(),
						(int) event.getY());
				break;
			case MotionEvent.ACTION_UP:
				mEndTime = System.currentTimeMillis();
				int endX = (int) event.getX();
				int endY = (int) event.getY();
				ArrayList<TagItem> tagItems = mAllTag.get(new Integer(mCurrentPosition));
				tagItems.get(mTouchView.getLocation()).setX(endX);
				tagItems.get(mTouchView.getLocation()).setY(endY);
				tagItems.get(mTouchView.getLocation()).setDx(mStartX -mStartTouchViewLeft);
				tagItems.get(mTouchView.getLocation()).setDy(mStartY -mStartTouchViewTop);
				Log.e(TAG,"ACTION_UP"+mTouchView.getLocation());
				//如果挪动的范围很小，并且时间间隔超过500，则判定为长按点击
				if(mTouchView!=null&& Math.abs(endX - mStartX)<CLICKRANGE&& Math.abs(endY - mStartY)<CLICKRANGE&&(mEndTime - mStartTime) > 500){
					removeItem();
				}else if (mTouchView!=null&& Math.abs(endX - mStartX)<CLICKRANGE&& Math.abs(endY - mStartY)<CLICKRANGE){
					editItem();
				}else{
					mTouchView = null;
				}
				break;
			}
			return false;

	}

	private void editItem() {
		ArrayList<TagItem> tagItems = mAllTag.get(new Integer(mCurrentPosition));
		ArrayList<String> mLabelArrayList=new ArrayList<>();
		for (TagItem item:tagItems){
			mLabelArrayList.add(item.getName());
		}
		EditTagDialog editTagDialog = new EditTagDialog(getContext(), mLabelArrayList,mTouchView.getTagdetail());
		editTagDialog.setInputListener(new EditTagDialog.InputListener() {
			@Override
			public void confirm(String tag) {
				if (mListener!=null){
					if (tag.equals(mTouchView.getTagdetail())) return;
					mListener.ontagEditlistener(mTouchView,mTouchView.getLocation(),tag);
				}
			}

			@Override
			public void cancel() {

			}
		});

		editTagDialog.show();
	}

	private void removeItem() {
		final MaterialDialog dialog = new MaterialDialog(getContext());
		dialog.content(
				"删除“"+mTouchView.getTagdetail()+"”标签吗？")//
				.btnText("取消", "确定")//
				.show();
		dialog.setOnBtnClickL(
				new OnBtnClickL() {//left btn click listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
					}
				},
				new OnBtnClickL() {//right btn click listener
					@Override
					public void onBtnClick() {
						if (mListener!=null){
							mListener.ontagDeletelistener(mTouchView,mTouchView.getLocation());
						}
						dialog.dismiss();
					}
				}
		);

	}

	/**
	 * 添加标签
	 * @param x x轴坐标
	 * @param y y轴坐标
	 * @param tag 标签内容
	 * @param CurPosition 界面索引
     */
	public void addItem(int x, int y, String tag, int CurPosition){
		PictureTagView view = null;
		LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if(x>getWidth()*0.5){
			view = new PictureTagView(getContext(), PictureTagView.Direction.Right);
		}
		else{
			view = new PictureTagView(getContext(), PictureTagView.Direction.Left);
		}

		//-------------------------------------------------------
		if (mAllTag.get(new Integer(CurPosition))==null){
			ArrayList<TagItem> countTags=new ArrayList<>();
			countTags.add(new TagItem(tag,x, y, CurPosition));
			mAllTag.put(new Integer(CurPosition),countTags);
		}else{
			mAllTag.get(new Integer(CurPosition)).add(new TagItem(tag,x, y, CurPosition));
		}
		view.setLocation(mAllTag.get(new Integer(CurPosition)).size()-1);//设置当前标签的位置
		view.setTagDescribe(tag);//设置输入的内容
		int whMeasureSpec =MeasureSpec.makeMeasureSpec( (1 << 30) - 1, MeasureSpec.AT_MOST);
		view.measure(whMeasureSpec, whMeasureSpec);
		if(x>getWidth()*0.5){
			params.leftMargin = x ;

		}
		else{
			params.leftMargin = x- view.getMeasuredWidth();
		}
		params.topMargin = y;

		//限制子控件移动必须在视图范围内
		if (params.leftMargin<0){
			params.leftMargin = 0;
		}
		if ((params.leftMargin+view.getMeasuredWidth())>getWidth()){
			params.leftMargin = getWidth()-view.getMeasuredWidth();
		}
		if (params.topMargin<0){
			params.topMargin = 0;
		}
		if ((params.topMargin+view.getMeasuredHeight())>getHeight()){
			params.topMargin = getHeight()-view.getMeasuredHeight();
		}

		Log.e(TAG,"addItem"+"leftMargin"+params.leftMargin+"topMargin"+params.topMargin+tag);

		this.addView(view, params);

	}

	/**
	 * 设置标签
	 * @param x x轴坐标
	 * @param y y轴坐标
	 * @param tag 标签内容
	 * @param i   当前界面的第n个标签
     * @param dx  moveView时候x轴产生的移动偏差
     * @param dy  moveView时候y轴产生的移动偏差
     */
	public void setItem(int x, int y, String tag, int i,int dx,int dy){
		PictureTagView view = null;
		LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if(x>getWidth()*0.5){
			view = new PictureTagView(getContext(), PictureTagView.Direction.Right);
		}
		else{
			view = new PictureTagView(getContext(), PictureTagView.Direction.Left);
		}

		//-------------------------------------------------------
		view.setLocation(i);//设置当前标签的位置
		view.setTagDescribe(tag);//设置输入的内容
		int whMeasureSpec =MeasureSpec.makeMeasureSpec( (1 << 30) - 1, MeasureSpec.AT_MOST);
		view.measure(whMeasureSpec, whMeasureSpec);
		if (dy==0||dx==0){
			if(x>getWidth()*0.5){
				params.leftMargin = x ;

			}
			else{
				params.leftMargin = x- view.getMeasuredWidth();
			}
			params.topMargin = y;
		}else{
			params.topMargin = y-dy;
			params.leftMargin = x-dx;
		}
		//在重新设置标签位置的时候进行屏幕边界控制
		Log.e(TAG,"yyyy"+params.leftMargin+"yyy"+view.getMeasuredWidth()+"yy"+view.getWidth()+"y"+view.getLeft());
		if (params.leftMargin<0){
			params.leftMargin = 0;
		}
		if ((params.leftMargin+view.getMeasuredWidth())>getWidth()){
			params.leftMargin=getWidth()-view.getMeasuredWidth();
		}
		if (params.topMargin<0){
			params.topMargin = 0;
		}
		if ((params.topMargin+view.getMeasuredHeight())>getHeight()){
			params.topMargin = getHeight()-view.getMeasuredHeight();
		}
		Log.e(TAG,"setItem"+"leftMargin"+params.leftMargin+"topMargin"+params.topMargin+tag);
		this.addView(view, params);

	}

	/**
	 * 拖动标签
	 * @param x x轴坐标
	 * @param y y轴坐标
     */
	private void moveView(int x,int y){
		if(mTouchView == null) return;
		LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = y;
		if(x>getWidth()*0.5){
			mTouchView.setmDirection(PictureTagView.Direction.Right);
		}else{
			mTouchView.setmDirection(PictureTagView.Direction.Left);
		}
		//上下位置在视图内
		if(params.topMargin<0)params.topMargin =0;
		else if((params.topMargin+mTouchView.getHeight())>getHeight())params.topMargin = getHeight() - mTouchView.getWidth();
		params.leftMargin = x - mStartX + mStartTouchViewLeft;
		params.topMargin = y - mStartY + mStartTouchViewTop;
		//限制子控件移动必须在视图范围内
		Log.e(TAG,"yyy"+params.leftMargin+"yy"+mTouchView.getWidth()+"y"+getWidth());
		if(params.leftMargin<0||(params.leftMargin+mTouchView.getWidth())>getWidth())
			params.leftMargin = mTouchView.getLeft();
		if(params.topMargin<0||(params.topMargin+mTouchView.getHeight())>getHeight())
			params.topMargin = mTouchView.getTop();
		mTouchView.setLayoutParams(params);
		mTouchView.directionChange();//重新设置标签方位
		Log.e(TAG,"moveView"+"leftMargin"+params.leftMargin+"topMargin"+params.topMargin);
	}

	/**
	 * 判断触控点是否有标签
	 * @param x x轴坐标
	 * @param y y轴坐标
     * @return
     */
	private boolean hasView(int x,int y){
		//循环获取子view，判断xy是否在子view上，即判断是否按住了子view
		for(int index = 0; index < this.getChildCount(); index ++){
			PictureTagView view = (PictureTagView)this.getChildAt(index);
			int left = (int) view.getX();
			int top = (int) view.getY();
			int right = view.getRight();
			int bottom = view.getBottom();
			Rect rect = new Rect(left, top, right, bottom);
			boolean contains = rect.contains(x, y);
			//如果是与子view重叠则返回真,表示已经有了view不需要添加新view了
			if(contains){
				mTouchView = view;
				mTouchView.bringToFront();
				return true;
			}
		}
		mTouchView = null;
		return false;
	}

	public void setCurrentPosition(int position) {
		mCurrentPosition=position;
	}

	public interface Taglistener{
		void ontagDeletelistener(PictureTagView touchView, int id);

		void ontagEditlistener(PictureTagView touchView, int id, String newname);

	}

}
