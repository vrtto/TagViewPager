package com.example.tagviewpager;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.tagviewpager.adapter.ViewPagerAdapter;
import com.example.tagviewpager.bean.TagItem;
import com.example.tagviewpager.tagview.PictureTagLayout;
import com.example.tagviewpager.tagview.PictureTagView;
import com.example.tagviewpager.utils.AndroidUtils;
import com.example.tagviewpager.view.AddTagDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.vp_container)
    ViewPager mVpContainer;
    @BindView(R.id.pty_conver)
    PictureTagLayout mPtyConver;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private List<Integer> mUrls = new ArrayList<>();
    private ViewPagerAdapter mViewPagerAdapter;
    private int mCurrPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        StatusBarCompat.translucentStatusBar(this);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.black), 255);
        initToolbar();
        initViewPager();
        initTagView();
    }

    private void initToolbar() {
        mToolbar.getBackground().mutate().setAlpha(125);
        mToolbar.setTitle(mCurrPosition + 1 + "/" + mUrls.size());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewPager() {
        TypedArray images = getResources().obtainTypedArray(R.array.viewpager_imags);
        for (int i = 0; i < images.length(); i++)
            mUrls.add(images.getResourceId(i, 0));
        images.recycle();
        mVpContainer.setAdapter(mViewPagerAdapter = new ViewPagerAdapter(mUrls, this));
        mVpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mToolbar.setTitle((mCurrPosition = position) + 1 + "/" + mUrls.size());
                //处理标签
                mPtyConver.setCurrentPosition(position);
                mPtyConver.removeAllViews();//清空标签
                Map<Integer, ArrayList<TagItem>> allTag = mPtyConver.getAllTag();
                if (allTag.get(new Integer(position)) != null) {
                    ArrayList<TagItem> tagItems = allTag.get(new Integer(position));
                    for (int i = 0; i < tagItems.size(); i++) {
                        Log.e(TAG, "onPageSelected+setItem");
                        mPtyConver.setItem(tagItems.get(i).getX(), tagItems.get(i).getY(), tagItems.get(i).getName(),
                                i, tagItems.get(i).getDx(), tagItems.get(i).getDy());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerAdapter.setOnViewPagerClickListener(new ViewPagerAdapter.OnViewPagerClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                settingState();
            }
        });
    }

    private void initTagView() {
        mPtyConver.setonTaglistener(new PictureTagLayout.Taglistener() {
            @Override
            public void ontagDeletelistener(PictureTagView touchView, int id) {
                mPtyConver.removeView(touchView);
                mPtyConver.getAllTag().get(mCurrPosition).remove(id);
            }

            @Override
            public void ontagEditlistener(PictureTagView touchView, int id, String newname) {
                touchView.setTagDescribe(newname);
                mPtyConver.getAllTag().get(mCurrPosition).get(id).setName(newname);
            }

        });
    }

    private void settingState() {
        Animation exitanim = AnimationUtils.loadAnimation(this, R.anim.exit_from_top);
        Animation enteranim = AnimationUtils.loadAnimation(this, R.anim.enter_from_top);
        mToolbar.clearAnimation();
        if (mToolbar.getVisibility() == View.VISIBLE) {
            exitanim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mToolbar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
                mPtyConver.setVisibility(View.INVISIBLE);
                mToolbar.startAnimation(exitanim);
        } else {
                mToolbar.setVisibility(View.VISIBLE);
                mPtyConver.setVisibility(View.VISIBLE);
                mToolbar.startAnimation(enteranim);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_tag:
                addViewpagerTag();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加标签
     */
    private void addViewpagerTag() {
        AddTagDialog addTagDialog = new AddTagDialog(this);
        addTagDialog.setInputListener(new AddTagDialog.InputListener() {
            @Override
            public void confirm(String tag) {
                if (tag.equals("") || tag == null) return;
                int max = (int) AndroidUtils.getDeviceHight(MainActivity.this) / 2;
                int min = (int) AndroidUtils.getDeviceWidth(MainActivity.this) / 2;
                Random random = new Random();
                int randowsize = random.nextInt(max) % (max - min + 1) + min;
                Log.e(TAG, "addViewpagerTag+addItem");
                mPtyConver.addItem(randowsize, randowsize, tag, mCurrPosition);//添加标签
            }

            @Override
            public void cancel() {
            }
        });
        addTagDialog.show();
    }
}
