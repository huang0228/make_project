package com.sam.demo.base;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.constants.Data;
import com.sam.demo.entity.User;
import com.sam.demo.fingerprint.FingerprintAuthenticationDialogFragment;
import com.sam.demo.fingerprint.FingerprintUiHelper;
import com.sam.demo.util.GlideManager;
import com.sam.demo.util.SharedPreferencesUtils;
import com.sam.demo.util.StringUtils;
import com.sam.demo.util.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

import static com.sam.demo.mine.SysSettingActivity.currentTime;
import static com.sam.demo.mine.SysSettingActivity.isfinger;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2019/2/23
 * <p>
 * 描述：
 */
public class LoginByFingerActivity extends BaseActivity {
    public static final String TAG = "LoginByFingerActivity";
    public static final String TagGo="secret";

    @Bind(R.id.iv_actionbar_return)
    View iv_actionbar_return;
    @Bind(R.id.tv_actionbar_title)
    TextView tvActionbarTitle;

    @Bind(R.id.viewBottomLine)
    View viewBottomLine;

    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.imgUserHeader)
    ImageView imgUserHeader;

    @Bind(R.id.tvLoginByPwd)
    TextView tvLoginByPwd;
    @Bind(R.id.li_finger_container)
    LinearLayout li_finger_container;

    FingerprintUiHelper fingerprintUiHelper;
    KeyguardManager keyguardManager = null;

    FingerprintAuthenticationDialogFragment fragment=null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_finger);
        tvActionbarTitle.setText(getString(R.string.login));
        viewBottomLine.setVisibility(View.GONE);
        iv_actionbar_return.setVisibility(View.GONE);
        User user = MyApplication.getInstance().getUser();
        if (user != null) {
            tvUserName.setText(user.getLOCAL_NAME() + ",你好");
            if (!StringUtils.isEmpty(user.getHEADIMGURL())) {
                GlideManager.displayCircleImage(user.getHEADIMGURL(), imgUserHeader);
            }
        }
        fingerprintUiHelper = new FingerprintUiHelper(LoginByFingerActivity.this);
        keyguardManager = getSystemService(KeyguardManager.class);
        loginDecryptyByFinger();
    }

    //解密
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loginDecryptyByFinger() {
        if (!fingerprintUiHelper.isHardwareDetcted() || !fingerprintUiHelper.hasEnrolledFingerprints()
                || !keyguardManager.isKeyguardSecure()) {
            ToastUtil.showToast(LoginByFingerActivity.this, "指纹信息有变动");
            SharedPreferencesUtils.setParam(LoginByFingerActivity.this, isfinger, false);
            SharedPreferencesUtils.setParam(LoginByFingerActivity.this, Data.ISLOGIN, false);
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
            return;
        }
        FingerprintAuthenticationDialogFragment.generateKey(true,KeyProperties.PURPOSE_DECRYPT);
        fragment= new FingerprintAuthenticationDialogFragment();
        fragment.setPurpose(KeyProperties.PURPOSE_DECRYPT,1);
        fragment.show(getFragmentManager(), TAG);
    }

    //指纹返回结果
    public void onAuthResultSuccess(boolean isChecked, String result) {
        //ToastUtil.showToast(LoginByFingerActivity.this, result);
        ToastUtil.showToast(LoginByFingerActivity.this, getResources().getString(R.string.toast_finger_success));
        SharedPreferencesUtils.setParam(this,currentTime,Long.parseLong("0"));//应用在前台时清除时间记录
        this.finish();
    }

    //指纹返回结果
    public void onAuthResultError(String errMsg) {
        if (errMsg.contains(getResources().getString(R.string.toast_finger_decrypt)))
        {
            ToastUtil.showToast(LoginByFingerActivity.this,getResources().getString(R.string.toast_finger_change));
            if (fragment!=null){
                fragment.getDialog().dismiss();
            }
            SharedPreferencesUtils.setParam(LoginByFingerActivity.this, isfinger,false);
            SharedPreferencesUtils.setParam(this, Data.ISLOGIN, false);
        }else
        {
            ToastUtil.showToast(LoginByFingerActivity.this, errMsg);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.tvLoginByPwd,R.id.li_finger_container})
    public void OnClick(View view)
    {
        switch (view.getId()){
            case R.id.tvLoginByPwd:
                startActivity(new Intent(LoginByFingerActivity.this,LoginActivity.class));
                break;
            case R.id.li_finger_container:
                loginDecryptyByFinger();
                break;
        }
    }
}
