package com.sam.demo.base;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.demo.AppManager;
import com.sam.demo.MainActivity;
import com.sam.demo.R;
import com.sam.demo.constants.Data;
import com.sam.demo.constants.Flag;
import com.sam.demo.constants.Urls;
import com.sam.demo.entity.User;
import com.sam.demo.fingerprint.FingerprintAuthenticationDialogFragment;
import com.sam.demo.fingerprint.FingerprintUiHelper;
import com.sam.demo.listener.PermissionListener;
import com.sam.demo.util.AlertDialogUtils;
import com.sam.demo.util.MD5Utils;
import com.sam.demo.util.MyStringCallback;
import com.sam.demo.util.PermissionUtils;
import com.sam.demo.util.SharedPreferencesUtils;
import com.sam.demo.util.ToastUtil;
import com.sam.demo.util.Utils;
import com.sam.demo.widget.ClearEditText;
import com.lzy.okgo.OkGo;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.sam.demo.MainActivity.fromTag;
import static com.sam.demo.mine.SysSettingActivity.isfinger;
import static com.sam.demo.util.Utils.getDeviceID;


/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/2/28
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.bt_login)
    Button bt_login;
    @Bind(R.id.et_username)
    ClearEditText etUserName;
    @Bind(R.id.et_password)
    EditText etPassWord;
    LoginActivity activity;


    @Bind(R.id.li_sign)
    LinearLayout li_sign;
    @Bind(R.id.imgIsCheck)
    ImageView imgIsCheck;
    @Bind(R.id.tvSignInfo)
    TextView tvSignInfo;

    private User user;
    private String userName;
    private String userPwd;

    private boolean isChecked = false;//默认选中用户协议

    private View dialogView;
    private AlertDialog showDialog;

    FingerprintUiHelper fingerprintUiHelper;
    KeyguardManager keyguardManager = null;

    /**
     * 子类可以重写决定是否使用透明状态栏
     */
    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_login);
        activity = this;

        String account = (String) SharedPreferencesUtils.getParam(activity, "account", "");
        if (!TextUtils.isEmpty(account)) {
            etUserName.setText(account);
        }
    }

    /**
     * 请求操作用户授权
     */
    private void waitUserGrant() {
        requestRunPermission(activity, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest
                        .permission.READ_EXTERNAL_STORAGE, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE},
                new PermissionListener() {
                    @Override
                    //表示所有权限都授权了
                    public void onGranted() {
                        doLogin(getDeviceID(activity));
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //未授权权限
                        PermissionUtils.openSettingActivity(activity, "电话");
                    }
                });
    }

    /**
     * 登录
     */
    private void doLogin(String devId) {
        OkGo.<String>post(Urls.LOGIN)
                .tag(activity)
                .params("UserLoginName", userName)
                .params("UserLoginPwd", MD5Utils.encode(userPwd))
                .params("TERMINAL_NAME", Utils.getDeviceName())//设备名称
                .params("TERMINAL_NO", devId)//设备序列号

                //.params("TERMINAL_NO", "yyy21")// TODO 设备序列号
                .params("TYPE", "2")//默认值 2
                .params("SYS_VERSION", "Android" + Utils.getSystemVersion())//设备系统版本号
                .params("noToken", true)
                .params("OS_TYPE", "Android")
                .params("Device_Token", (String) SharedPreferencesUtils.getParam(activity, Data
                        .DEVICE_TOKEN, ""))
                .execute(new MyStringCallback(activity, Flag.FIRST));
    }


    /**
     * 登录点击事件
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.bt_login, R.id.li_sign, R.id.tvSignInfo})
    public void login(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                userName = etUserName.getText().toString();
                userPwd = etPassWord.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    ToastUtil.showToast(activity, "请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(userPwd)) {
                    ToastUtil.showToast(activity, "请输入密码");
                    return;
                }
                waitUserGrant();
                break;
            case R.id.li_sign://用户协议
                if (isChecked) {
                    imgIsCheck.setImageBitmap(BitmapFactory.decodeResource(getResources(), R
                            .drawable.uncheck));
                    isChecked = false;
                } else {
                    imgIsCheck.setImageBitmap(BitmapFactory.decodeResource(getResources(), R
                            .drawable.checked));
                    isChecked = true;
                }
                break;
            case R.id.tvSignInfo://用户协议详情事件
                break;
        }

    }

    @Override
    public void onSuccess(String str, int flag) {
        super.onSuccess(str, flag);
        switch (flag) {
            // do login
            case Flag.FIRST:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    //指纹验证框
    private void fingerDialog() {
        showDialog = AlertDialogUtils.initAlertDialogView(activity, false);
        dialogView = AlertDialogUtils.initInflateView(activity, R.layout.alert_dialog_master_layout);
        AlertDialogUtils.setAlertDialogConfig(activity, showDialog, dialogView);
        ((TextView) dialogView.findViewById(R.id.tv_center_title)).setText(getResources().getString(R.string.toast_finger_dialog_title));
        ((TextView) dialogView.findViewById(R.id.tv_alert_content)).setText(getResources().getString(R.string.toast_finger_dialog_content));
        ((Button) dialogView.findViewById(R.id.btn_alert_cancel)).setText(getResources().getString(R.string.toast_finger_dialog_cancel));
        dialogView.findViewById(R.id.btn_alert_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(fromTag, true);
                startActivity(intent);
                finish();
                showDialog.dismiss();
            }
        });
        ((Button) dialogView.findViewById(R.id.btn_alert_conform)).setText(getResources().getString(R.string.toast_finger_dialog_confirm));
        dialogView.findViewById(R.id.btn_alert_conform).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                loginByFinger();
                showDialog.dismiss();
            }
        });
    }

    //加密
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loginByFinger() {
        FingerprintAuthenticationDialogFragment.generateKey(true, KeyProperties.PURPOSE_ENCRYPT);
        FingerprintAuthenticationDialogFragment fragment
                = new FingerprintAuthenticationDialogFragment();
        fragment.setPurpose(KeyProperties.PURPOSE_ENCRYPT, 3);
        fragment.show(getFragmentManager(), LoginByFingerActivity.TAG);
    }

    //指纹返回结果
    public void onAuthResultSuccess(boolean isChecked, String result) {
        ToastUtil.showToast(LoginActivity.this, getResources().getString(R.string.toast_finger_success));
        SharedPreferencesUtils.setParam(LoginActivity.this, isfinger, true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(fromTag, true);
        startActivity(intent);
        finish();
    }

    //指纹返回结果
    public void onAuthResultError(String errMsg) {
        ToastUtil.showToast(LoginActivity.this, errMsg);
    }

    /**
     * @description double click exit application
     */
    private long exitTime = 0;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showToast(this, R.string.two_click_exit);
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(this);
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
