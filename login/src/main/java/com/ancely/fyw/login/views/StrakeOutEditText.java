package com.ancely.fyw.login.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ancely.fyw.aroute.utils.DrawableUtils;
import com.ancely.fyw.aroute.utils.SizeUtils;
import com.ancely.fyw.login.R;


/*
 */
public class StrakeOutEditText extends RelativeLayout implements TextWatcher, View.OnClickListener {
    private ImageView mLeftIcon;
    private ImageView deletIcon;
    private EditText mEditText;
    private String editValue = "";
    private Context mContext;

    public StrakeOutEditText(Context context) {
        this(context, null);
    }

    public StrakeOutEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrakeOutEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackground(DrawableUtils.creatDrable(context, R.color.color_ffffff, 4, 0.5f, R.color.color_0f66cc));
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrakeOutEditText);
        int leftIcon = a.getResourceId(R.styleable.StrakeOutEditText_left_icon, R.mipmap.login_user_center);
        String etHint = a.getString(R.styleable.StrakeOutEditText_et_hint);
        boolean leffIconVisibility = a.getBoolean(R.styleable.StrakeOutEditText_left_icon_visibility, false);
        int inputType = a.getInt(R.styleable.StrakeOutEditText_input_type, 1);
        a.recycle();

        mLeftIcon = new ImageView(context);
        addView(mLeftIcon);
        mLeftIcon.setImageAlpha(leftIcon);
        mLeftIcon.setVisibility(leffIconVisibility ? VISIBLE : GONE);
        mLeftIcon.setId(R.id.left_icon);
        LayoutParams leftIconParams = new LayoutParams(SizeUtils.px2dp(mContext,14), SizeUtils.px2dp(mContext,14));
        leftIconParams.leftMargin = SizeUtils.px2dp(mContext,14);
        leftIconParams.rightMargin = SizeUtils.px2dp(mContext,8);
        leftIconParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mLeftIcon.setLayoutParams(leftIconParams);


        mEditText = new EditText(context);
        addView(mEditText);
        mEditText.setId(R.id.middle_et);

        mEditText.setHintTextColor(ContextCompat.getColor(context, R.color.color_550f66cc));
        mEditText.setHint(etHint);
        mEditText.setCursorVisible(true);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mEditText.setTextColor(ContextCompat.getColor(context, R.color.color_0f66cc));
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtils.px2dpH(mContext,14));
        mEditText.setBackground(null);
        mEditText.setSingleLine(true);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        switch (inputType) {
            case 0:
                mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case 1:
                mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 2:
                mEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(11);
                mEditText.setFilters(new InputFilter.LengthFilter[]{lengthFilter});
                break;
        }

        LayoutParams middleEtParams = new LayoutParams(SizeUtils.px2dp(mContext,280), ViewGroup.LayoutParams.WRAP_CONTENT);
        middleEtParams.leftMargin = SizeUtils.px2dp(mContext,12);
        middleEtParams.addRule(RelativeLayout.CENTER_VERTICAL);
        middleEtParams.addRule(RelativeLayout.RIGHT_OF, R.id.left_icon);
        mEditText.setLayoutParams(middleEtParams);
        mEditText.addTextChangedListener(this);
        deletIcon = new ImageView(context);
        addView(deletIcon);
        deletIcon.setVisibility(GONE);
        deletIcon.setBackgroundResource(R.mipmap.login_search_delete_btn);
        LayoutParams deleteIconParams = new LayoutParams(SizeUtils.px2dp(mContext,14), SizeUtils.px2dp(mContext,14));
        deleteIconParams.rightMargin = SizeUtils.px2dp(mContext,10);
        deleteIconParams.addRule(RelativeLayout.CENTER_VERTICAL);
        deleteIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        deletIcon.setLayoutParams(deleteIconParams);
        deletIcon.setOnClickListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        editValue = s.toString().trim();
        if (s.toString().length() > 0) deletIcon.setVisibility(View.VISIBLE);
        else deletIcon.setVisibility(View.GONE);
        if (mListener != null) {
            mListener.onEditextChangedListener();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        mEditText.setText("");
    }


    public String getEditext() {
        return editValue;
    }

    public void setEditextChangedListener(EditextChangedListener listener) {
        mListener = listener;
    }

    public void setPasswordVisible(boolean visible) {
        if (visible) mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        else
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private EditextChangedListener mListener;

    public interface EditextChangedListener {
        void onEditextChangedListener();
    }
}
