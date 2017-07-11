package com.example.tagviewpager.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tagviewpager.R;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description：编辑标签对话框
 * Author：lxl
 * Date： 2017/7/6 11:38
 */
public class EditTagDialog extends BaseDialog<EditTagDialog> implements View.OnClickListener{


    @BindView(R.id.et_tag)
    EditText mEtTag;
    @BindView(R.id.tv_reset)
    TextView mTvReset;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout mIdFlowlayout;
    @BindView(R.id.btn_cancel)
    TextView mBtnCancel;
    @BindView(R.id.btn_confirm)
    TextView mBtnConfirm;

    private InputListener mInputListener;
    private ArrayList<String> mLabelArrayList;
    private Context mContext;
    LayoutInflater mInflater;
    String mCurrentTagDetail;

    public void setInputListener(InputListener inputListener) {
        this.mInputListener = inputListener;
    }
    public EditTagDialog(Context context, ArrayList<String> mLabelArrayList, String currenttext) {
        super(context);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mLabelArrayList = mLabelArrayList;
        mCurrentTagDetail = currenttext;
    }

    public EditTagDialog(Context context, boolean isPopupStyle) {
        super(context, isPopupStyle);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View view = View.inflate(mContext, R.layout.dialog_edit_tag, null);
        ButterKnife.bind(this, view);
        view.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(0)));
        return view;
    }

    @Override
    public void setUiBeforShow() {
        mEtTag.setText(mCurrentTagDetail);//设置默认显示的内容
        mEtTag.setSelection(mEtTag.getText().length());
        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mIdFlowlayout.setAdapter(new TagAdapter<String>(mLabelArrayList) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                View view = mInflater.inflate(R.layout.item_dialog_edittag, mIdFlowlayout, false);
                TextView tv=(TextView)view.findViewById(R.id.tv_tag);
                tv.setText(o);
                return view;
            }
        });
        mIdFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mEtTag.setText(mLabelArrayList.get(position));
                return false;
            }
        });

        mTvReset.setOnClickListener(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (mInputListener != null)
                    mInputListener.cancel();
                dismiss();
                break;
            case R.id.btn_confirm:
                String tag = mEtTag.getText().toString().trim();
                if (mInputListener != null) {
                    mInputListener.confirm(tag);
                    dismiss();
                }
            case R.id.tv_reset:
                mLabelArrayList.clear();
                mIdFlowlayout.setVisibility(View.GONE);
                break;
        }
    }
    public interface InputListener{
        void confirm(String tag);
        void cancel();
    }

}
