package com.sam.demo.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.sam.demo.AppManager;
import com.sam.demo.MainActivity;
import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.constants.Data;
import com.sam.demo.constants.Flag;
import com.sam.demo.constants.StatusCode;
import com.sam.demo.constants.Urls;
import com.sam.demo.entity.UserResult;
import com.sam.demo.util.APKVersionUtils;
import com.sam.demo.util.AppSafeValidate;
import com.sam.demo.util.MyStringCallback;
import com.sam.demo.util.SharedPreferencesUtils;
import com.sam.demo.util.StringUtils;
import com.sam.demo.util.ToastUtil;
import com.sam.demo.util.Utils;
import com.lzy.okgo.OkGo;

import butterknife.Bind;

import static com.sam.demo.MyApplication.isDevelop;
import static com.sam.demo.MainActivity.fromTag;

/**
 * ================================================
 * 创建日期：2018/3/9
 * 描    述：闪屏页
 * 修订历史：
 * ================================================
 */

public class SplashActivity extends BaseActivity {

    @Bind(R.id.ll_splash_root)
    ImageView ll_splash_root;

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBarTint();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (isDevelop) {
            init();
        } else {
            apkInvalite();//完整性校验
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        Animation animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                boolean isFirstOpen = (boolean) SharedPreferencesUtils.getParam
                        (SplashActivity.this, Data.FIRST_OPEN, true);
                if (isFirstOpen) {
                    Intent intent = new Intent(SplashActivity.this, WelcomeGuideActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                Intent intent = new Intent(SplashActivity.this,
                        (boolean) SharedPreferencesUtils.getParam(SplashActivity.this, Data
                                .ISLOGIN, false) ? MainActivity.class : LoginActivity.class);
                intent.putExtra(fromTag, false);
                startActivity(intent);
                AppManager.getAppManager().finishActivity();
            }
        });
        //ll_splash_root.setAnimation(animation);
        ll_splash_root.startAnimation(animation);
    }

    private void apkInvalite() {
        String hashMd5 = AppSafeValidate.getApkHashValue(SplashActivity.this);
        Log.v("sam--", hashMd5);
        getApkHashByMD5(hashMd5);
    }

    /**
     * server端验证hash
     */
    private void getApkHashByMD5(String hashValue) {
        OkGo.<String>post(Urls.GET_HASH_MD5)
                .tag(SplashActivity.this)
                .params("os", "Android")
                .params("version", APKVersionUtils.getVerName(SplashActivity.this))
                .params("hash", hashValue)//hash
                .params("noToken", true)
                .params("manufacturer", Utils.getDeviceBrand() + "/" + Utils.getDeviceName() + "/" + Utils.getSystemVersion())//厂家操作系统名称和版本
                .execute(new MyStringCallback(SplashActivity.this, Flag.FIRST, false));
    }

    @Override
    public void onSuccess(String str, int flag) {
        super.onSuccess(str, flag);
        if (flag == Flag.FIRST) {
            UserResult userResult = new UserResult().toEntity(str);
            if (userResult.getCode() != StatusCode.SUCCESS) {
                Process.killProcess(Process.myPid()); // 验证失败则退出程序
            } else {
                init();
            }
        }
    }

    @Override
    public void onError(String str, int flag) {
        super.onError(str, flag);
        if (!StringUtils.isEmpty(str)) {
            if (str.contains(MyApplication.getInstance().getResources().getString(R.string.net_connection_error))) {
                ToastUtil.showToast(SplashActivity.this, MyApplication.getInstance().getResources().getString(R.string.net_connection_error));
            } else {
                Process.killProcess(Process.myPid()); // 验证失败则退出程序
            }
        } else {
            Process.killProcess(Process.myPid()); // 验证失败则退出程序
        }
    }
}
