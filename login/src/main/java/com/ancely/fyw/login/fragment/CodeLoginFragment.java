package com.ancely.fyw.login.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ancely.fyw.login.LoginActivity;
import com.ancely.fyw.login.R;
import com.ancely.fyw.login.views.StrakeOutEditText;

import androidx.navigation.Navigation;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login.fragment
 *  @文件名:   CodeLoginFragment
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 6:03 PM
 *  @描述：    验证码
 */
public class CodeLoginFragment extends Fragment implements View.OnClickListener, StrakeOutEditText.EditextChangedListener {

    private StrakeOutEditText mFragLoginUsername;
    private StrakeOutEditText mFragLoginPassword;
    private TextView mFragCodeCode;
    private Button mFragLoginLogin;
    private TextView mFragCodeAccountLogin;
    private TextView mJumpToRegister;

    private View mContentView;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frag_code_login, container, false);

        return mContentView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initEvent();
    }

    protected void initView() {

        mFragLoginUsername = mContentView.findViewById(R.id.frag_login_username);
        mFragLoginPassword = mContentView.findViewById(R.id.frag_login_password);
        mFragCodeCode = mContentView.findViewById(R.id.frag_code_code);
        mFragLoginLogin = mContentView.findViewById(R.id.frag_login_login);
        mFragCodeAccountLogin = mContentView.findViewById(R.id.frag_code_account_login);
        mJumpToRegister = mContentView.findViewById(R.id.jump_to_register);
    }

    protected void initEvent() {
        mFragCodeAccountLogin.setOnClickListener(this);
        mJumpToRegister.setOnClickListener(this);
        mFragLoginUsername.setEditextChangedListener(this);
        mFragLoginPassword.setEditextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.frag_code_account_login) {
            Navigation.findNavController(v).popBackStack();

        } else if (i == R.id.jump_to_register) {
            Navigation.findNavController(v).navigate(R.id.action_codeLoginFragment_to_registerFragment);

        } else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).setBarTitle(R.string.login_code_title);
            ((LoginActivity) getActivity()).setLeftIconIsShow(true);
        }
    }

    @Override
    public void onEditextChangedListener() {
        mFragLoginLogin.setEnabled(mFragLoginUsername.getEditext().length() >= 6 && mFragLoginPassword.getEditext().length() >= 4);
    }
}
