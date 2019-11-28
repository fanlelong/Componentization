package com.ancely.fyw.login.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ancely.fyw.aroute.bean.HttpResult;
import com.ancely.fyw.aroute.manager.RouterManager;
import com.ancely.fyw.aroute.model.bean.ResponseBean;
import com.ancely.fyw.aroute.utils.LogUtils;
import com.ancely.fyw.common.base.BaseModelFragment;
import com.ancely.fyw.login.LoginActivity;
import com.ancely.fyw.login.R;
import com.ancely.fyw.login.bean.RegisterBean;
import com.ancely.fyw.login.modelps.RegisterModelP;
import com.ancely.fyw.login.views.StrakeOutEditText;

import androidx.navigation.Navigation;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login.fragment
 *  @文件名:   RegisterFragment
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 5:59 PM
 *  @描述：    注册
 */
public class RegisterFragment extends BaseModelFragment<RegisterModelP,HttpResult<RegisterBean>> implements View.OnClickListener, StrakeOutEditText.EditextChangedListener {

    private StrakeOutEditText mFragRegisterAccount;
    private StrakeOutEditText mFragRegisterPassword;
    private StrakeOutEditText mFragRegisterPasswordAgain;
    private Button mFragRegisterRegister;
    private TextView mJumpToLogin;


    @Override
    public RegisterModelP getModelP() {
        return new RegisterModelP(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initEvent();
    }

    @Override
    protected void loadData() {

    }

    protected void initView() {

        mFragRegisterAccount = mContentView.findViewById(R.id.frag_register_account);
        mFragRegisterPassword = mContentView.findViewById(R.id.frag_register_password);
        mFragRegisterPasswordAgain = mContentView.findViewById(R.id.frag_register_password_again);
        mFragRegisterRegister = mContentView.findViewById(R.id.frag_register_register);
        mJumpToLogin = mContentView.findViewById(R.id.jump_to_login);
    }

    protected void initEvent() {
        mJumpToLogin.setOnClickListener(this);
        mFragRegisterRegister.setOnClickListener(this);
        mFragRegisterAccount.setEditextChangedListener(this);
        mFragRegisterPassword.setEditextChangedListener(this);
        mFragRegisterPasswordAgain.setEditextChangedListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.frag_register;
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.frag_register_register) {
           register();
        } else if (i == R.id.jump_to_login) {
            Navigation.findNavController(v).popBackStack();
        }
    }
    private void register() {
        mParams.put("username", mFragRegisterAccount.getEditext());
        mParams.put("password", mFragRegisterPassword.getEditext());
        mParams.put("repassword", mFragRegisterPasswordAgain.getEditext());
        mModelP.startRequestService(mParams);
    }

    @Override
    public void onEditextChangedListener() {
        mFragRegisterRegister.setEnabled(mFragRegisterAccount.getEditext().length() >= 6 && mFragRegisterPassword.getEditext().length() >= 6 && mFragRegisterPasswordAgain.getEditext().length() >= 6);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).setBarTitle(R.string.login_register_title);
            ((LoginActivity) getActivity()).setLeftIconIsShow(true);
        }
    }

    @Override
    public void accessSuccess(ResponseBean<HttpResult<RegisterBean>> responseBean) {
        RegisterBean loginBean = responseBean.body.getData();
        LogUtils.e("ancely_fyw", loginBean.getUsername());
        Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
        RouterManager.getInstance().build()
                .withResultString("username", loginBean.getUsername())
                .navigation(mContext, 2001);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
