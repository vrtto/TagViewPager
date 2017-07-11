package com.example.tagviewpager.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tagviewpager.R;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description：添加标签对话框
 * Author：lxl
 * Date： 2017/7/5 14:54
 */
public class AddTagDialog extends BaseDialog<AddTagDialog>  implements View.OnClickListener{
    @BindView(R.id.et_tag)
    EditText mEttag;
    @BindView(R.id.btn_cancel)
    TextView mBtnCancel;
    @BindView(R.id.btn_confirm)
    TextView mBtnConfirm;
    private InputListener inputListener;
    public AddTagDialog(Context context) {
        super(context);
    }
    public void setInputListener(InputListener inputListener){
        this.inputListener=inputListener;
    }
    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View view = View.inflate(mContext, R.layout.dialog_add_tag, null);
        ButterKnife.bind(this, view);
        view.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(0)));
        return view;
    }

    @Override
    public void setUiBeforShow() {
        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if(inputListener!=null)
                    inputListener.cancel();
                dismiss();
                break;
            case R.id.btn_confirm:
                String tag= mEttag.getText().toString().trim();
                if(inputListener!=null){
                    inputListener.confirm(tag);
                    dismiss();
                }
                break;
        }
    }

    public interface InputListener{
        void confirm(String tag);
        void cancel();
    }
}
