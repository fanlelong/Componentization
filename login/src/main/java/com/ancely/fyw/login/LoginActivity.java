package com.ancely.fyw.login;

import android.os.Bundle;
import android.view.View;

import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.login.views.BaseTitle;

import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import con.ancely.fyw.annotation.apt.ARouter;

@ARouter(path = "/login/LoginActivity")
public class LoginActivity extends BaseActivity implements BaseTitle.OnLeftBackListener {

//    @Parameter
//    String username;

    private boolean mPopBackStack;
    private BaseTitle mActLoginTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActLoginTitle = findViewById(R.id.act_login_title);
        mActLoginTitle.setBackVisbility(false);


        mActLoginTitle.setOnLeftBackListener(this);
        setupNavigation();
        addWindowsView();
//        startActivity(new Intent(getContext(),LoginTestActivity.class));
    }

    public void jumpToApp(View view) {
//        RouterManager.getInstance().finish(this).withString("call","login_app").navigation(this,20);
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_login);
        NavGraph navSimple = navHostFragment.getNavController().getNavInflater().inflate(R.navigation.login_navigation);
        Bundle bundle = new Bundle();
        bundle.putString("key","flag");
        navSimple.setDefaultArguments(bundle);
        navSimple.setStartDestination(R.id.codeLoginFragment);
        navHostFragment.getNavController().setGraph(navSimple);
    }

    public void setBarTitle(int resId) {
        mActLoginTitle.setTitle(resId);
    }

    public void setTitle(String title) {
        mActLoginTitle.setTitle(title);
    }

    public void setLeftIconIsShow(boolean isShow) {
        mActLoginTitle.setBackVisbility(isShow);
    }


    @Override
    public boolean openChangerSkin() {
        return true;
    }

    @Override
    public void leftBack() {
        mPopBackStack = Navigation.findNavController(this, R.id.fragment_navigation_login).popBackStack();
        if (!mPopBackStack) {
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_window_in_left, R.anim.anim_window_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {

        return Navigation.findNavController(this, R.id.fragment_navigation_login).navigateUp();
    }
}
