package com.sam.demo.mine;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sam.demo.R;
import com.sam.demo.base.BaseActivity;
import com.sam.demo.base.LoginByFingerActivity;
import com.sam.demo.fingerprint.FingerprintAuthenticationDialogFragment;
import com.sam.demo.fingerprint.FingerprintUiHelper;
import com.sam.demo.util.MyLog;
import com.sam.demo.util.SharedPreferencesUtils;
import com.sam.demo.util.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

import static com.sam.demo.mine.BackRunSettingActivity.defaultTime;


public class SysSettingActivity extends BaseActivity {
    public static final String isfinger = "isFinger";
    public static final String currentTime = "currenttime";

    @Bind(R.id.tv_actionbar_title)
    TextView tvActionbarTitle;

    @Bind(R.id.viewBottomLine)
    View viewBottomLine;

    @Bind(R.id.li_finger_turn)
    LinearLayout li_finger_turn;

    @Bind(R.id.li_bg_run)
    LinearLayout li_bg_run;
    @Bind(R.id.tvBgRun)
    TextView tvBgRun;

    @Bind(R.id.switchFingerStatus)
    Switch switchFingerStatus;

    FingerprintUiHelper fingerprintUiHelper;
    KeyguardManager keyguardManager = null;

    private boolean isFingerOn = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_setting);
        tvActionbarTitle.setText(getString(R.string.sys_setting));
        viewBottomLine.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            li_finger_turn.setVisibility(View.GONE);
            li_bg_run.setVisibility(View.INVISIBLE);
            ToastUtil.showToast(SysSettingActivity.this,getResources().getString(R.string.toast_finger_unsupport));
            return;
        }
        try {
            keyguardManager = getSystemService(KeyguardManager.class);
            fingerprintUiHelper = new FingerprintUiHelper(SysSettingActivity.this);
            isFingerOn = (boolean) SharedPreferencesUtils.getParam(SysSettingActivity.this, isfinger, false);
            if (!fingerprintUiHelper.hasEnrolledFingerprints() || !keyguardManager.isKeyguardSecure()) {
                switchFingerStatus.setChecked(false);
                isFingerOn = false;
                SharedPreferencesUtils.setParam(SysSettingActivity.this,isfinger,isFingerOn);
            }
            if (isFingerOn) {
                li_bg_run.setVisibility(View.VISIBLE);
            } else {
                li_bg_run.setVisibility(View.INVISIBLE);
            }
            switchFingerStatus.setChecked(isFingerOn);
        } catch (Exception e) {
            MyLog.d(e.toString());
            ToastUtil.showToast(SysSettingActivity.this,getResources().getString(R.string.toast_finger_unsupport));
        }

        switchFingerStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!fingerprintUiHelper.hasEnrolledFingerprints()) {
                    Toast.makeText(SysSettingActivity.this,
                            "设置 -> 安全设置 中添加至少一个指纹", Toast.LENGTH_LONG).show();
                    switchFingerStatus.setChecked(false);
                    return;
                }
                if (!keyguardManager.isKeyguardSecure()) {
                    Toast.makeText(SysSettingActivity.this,
                            "请先设定指纹锁屏功能.\n"
                                    + "请在 '设定-> 安全设置 中添加指纹",
                            Toast.LENGTH_LONG).show();
                    switchFingerStatus.setChecked(false);
                    return;
                }
                if (b) {
                    loginByFinger();
                } else {
                    isFingerOn = false;
                    li_bg_run.setVisibility(View.INVISIBLE);
                    switchFingerStatus.setChecked(isFingerOn);
                    SharedPreferencesUtils.setParam(SysSettingActivity.this, isfinger, isFingerOn);
                }
            }
        });
    }

    //加密
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loginByFinger() {
        FingerprintAuthenticationDialogFragment.generateKey(true, KeyProperties.PURPOSE_ENCRYPT);
        FingerprintAuthenticationDialogFragment fragment
                = new FingerprintAuthenticationDialogFragment();
        fragment.setPurpose(KeyProperties.PURPOSE_ENCRYPT, 2);
        fragment.show(getFragmentManager(), LoginByFingerActivity.TAG);
    }

    //取消指纹设定
    public void onClickCancel() {
        switchFingerStatus.setChecked(false);
    }

    //指纹返回结果
    public void onAuthResultSuccess(boolean isChecked, String result) {
        ToastUtil.showToast(SysSettingActivity.this, getResources().getString(R.string.toast_finger_success));
        if (!isFingerOn) {
            isFingerOn = true;
        } else {
            isFingerOn = false;
        }
        li_bg_run.setVisibility(View.VISIBLE);
        switchFingerStatus.setChecked(isFingerOn);
        SharedPreferencesUtils.setParam(SysSettingActivity.this, isfinger, isFingerOn);
    }

    //指纹返回结果
    public void onAuthResultError(String errMsg) {
        ToastUtil.showToast(SysSettingActivity.this, errMsg);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        try {
            tvBgRun.setText((String) SharedPreferencesUtils.getParam(SysSettingActivity.this, BackRunSettingActivity.isBgRunFlag, defaultTime) + "分钟");
        } catch (Exception e) {
            ToastUtil.showToast(SysSettingActivity.this, getResources().getString(R.string.toast_finger_unsupport));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.li_finger_turn, R.id.li_bg_run})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.li_finger_turn:
                break;
            case R.id.li_bg_run://后台运行时间设定
                startActivity(new Intent(SysSettingActivity.this, BackRunSettingActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
