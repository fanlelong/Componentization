package com.ancely.fyw.login.fragment;

import android.text.TextUtils;
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
import com.ancely.fyw.login.bean.LoginBean;
import com.ancely.fyw.login.modelps.LoginModelP;
import com.ancely.fyw.login.views.StrakeOutEditText;

import androidx.navigation.Navigation;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login.fragment
 *  @文件名:   LoginFragment
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 5:14 PM
 *  @描述：    登陆
 */
public class LoginFragment extends BaseModelFragment<LoginModelP, HttpResult<LoginBean>> implements View.OnClickListener, StrakeOutEditText.EditextChangedListener {

    private StrakeOutEditText mFragLoginUsername;
    private StrakeOutEditText mFragLoginPassword;
    private Button mFragLoginLogin;
    private TextView mFragLoginCodeLogin;
    private TextView mJumpToRegister;


    @Override
    public LoginModelP getModelP() {
        return new LoginModelP(this);
    }

    @Override
    protected void loadData() {

    }


    public void initEvent() {
        mFragLoginUsername.setEditextChangedListener(this);
        mFragLoginPassword.setEditextChangedListener(this);

        mFragLoginLogin.setOnClickListener(this);
        mFragLoginCodeLogin.setOnClickListener(this);
        mJumpToRegister.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.frag_login;
    }

    public void initView() {
        mFragLoginUsername = mContentView.findViewById(R.id.frag_login_username);
        mFragLoginPassword = mContentView.findViewById(R.id.frag_login_password);
        mFragLoginLogin = mContentView.findViewById(R.id.frag_login_login);
        mFragLoginCodeLogin = mContentView.findViewById(R.id.frag_login_code_login);
        mJumpToRegister = mContentView.findViewById(R.id.jump_to_register);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.frag_login_login) {
            login();
        } else if (i == R.id.frag_login_code_login) {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_codeLoginFragment);

        } else if (i == R.id.jump_to_register) {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);

        }
    }

    private void login() {
        if (TextUtils.isEmpty(mFragLoginUsername.getEditext())) {
            Toast.makeText(getContext(), R.string.login_accound_error_tips, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mFragLoginPassword.getEditext())) {
            Toast.makeText(getContext(), R.string.login_psw_error_tips, Toast.LENGTH_SHORT).show();
            return;
        }
        mParams.put("username", mFragLoginUsername.getEditext());
        mParams.put("password", mFragLoginPassword.getEditext());
        mModelP.startRequestService(mParams);
    }

    @Override
    public void onEditextChangedListener() {
        mFragLoginLogin.setEnabled(mFragLoginUsername.getEditext().length() >= 6 && mFragLoginPassword.getEditext().length() >= 6);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).setBarTitle(R.string.login);
            ((LoginActivity) getActivity()).setLeftIconIsShow(false);
        }
    }

    @Override
    public void accessSuccess(ResponseBean<HttpResult<LoginBean>> responseBean) {
        super.accessSuccess(responseBean);
        LoginBean loginBean = responseBean.body.getData();
        LogUtils.e("ancely_fyw", loginBean.getUsername());
        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
        RouterManager.getInstance().build()
                .withResultString("username", loginBean.getUsername())
                .navigation(mContext, 2001);

        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
