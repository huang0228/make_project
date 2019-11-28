/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.sam.demo.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;

import com.sam.demo.MyApplication;
import com.sam.demo.R;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * Small helper class to manage text/icon around fingerprint authentication UI.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback {

    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;

    private final FingerprintManager mFingerprintManager;
    private TextView mErrorTextView = null;
    private SimpleAuthCallback mCallback=null;
    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled;

    LocalSharedPreference mLocalSharedPreference;

    private int purpose = KeyProperties.PURPOSE_ENCRYPT;

    private String data="myDataAutoLink";

    /**
     * Constructor for {@link FingerprintUiHelper}.
     */
    FingerprintUiHelper(FingerprintManager fingerprintManager, TextView errorTextView, SimpleAuthCallback callback,LocalSharedPreference mLocalSharedPreference) {
        mFingerprintManager = fingerprintManager;
        mErrorTextView = errorTextView;
        mCallback = callback;
        this.mLocalSharedPreference = mLocalSharedPreference;

    }

    public FingerprintUiHelper(Context context){
        mFingerprintManager = context.getSystemService(FingerprintManager.class);
    }


    //判断硬件是否支持
    public boolean isHardwareDetcted()
    {
        return mFingerprintManager.isHardwareDetected();
    }
    //手机是否录入指纹
    public boolean hasEnrolledFingerprints()
    {
        return mFingerprintManager.hasEnrolledFingerprints();
    }

    public boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject,int purpose) {
        this.purpose=purpose;
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        mFingerprintManager
                .authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
        mErrorTextView.setText(MyApplication.getInstance().getResources().getString(R.string.toast_finger_input));
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!mSelfCancelled) {
            showError(errString);
            mErrorTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCallback.onAuthError("验证失败");
                }
            }, ERROR_TIMEOUT_MILLIS);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        showError(helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        showError(MyApplication.getInstance().getResources().getString(
                R.string.toast_finger_error));
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        if (mCallback == null) {
            return;
        }
        if (result.getCryptoObject() == null) {
            mCallback.onAuthError("加密对象为空");
            return;
        }
        final Cipher cipher = result.getCryptoObject().getCipher();
        if (purpose == KeyProperties.PURPOSE_DECRYPT) {
            //取出secret key并返回
            String data = mLocalSharedPreference.getData(mLocalSharedPreference.dataKeyName);
            if (TextUtils.isEmpty(data)) {
                mCallback.onAuthError("无法获取本地KEY");
                return;
            }
            try {
                byte[] decrypted = cipher.doFinal(Base64.decode(data, Base64.URL_SAFE));
                mCallback.onAuthenticated(new String(decrypted));
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                mCallback.onAuthError(MyApplication.getInstance().getResources().getString(R.string.toast_finger_decrypt));
            }
        } else {
            //将前面生成的data包装成secret key，存入沙盒
            try {
                byte[] encrypted = cipher.doFinal(data.getBytes());
                byte[] IV = cipher.getIV();
                String se = Base64.encodeToString(encrypted, Base64.URL_SAFE);
                String siv = Base64.encodeToString(IV, Base64.URL_SAFE);
                if (mLocalSharedPreference.storeData(mLocalSharedPreference.dataKeyName, se) &&
                        mLocalSharedPreference.storeData(mLocalSharedPreference.IVKeyName, siv)) {
                    mCallback.onAuthenticated(se);
                }else{
                    mCallback.onAuthError("无法获取本地KEY");
                }
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                mCallback.onAuthError("加密异常");
            }
        }
    }

    private void showError(CharSequence error) {
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.actionsheet_red, null));
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }

    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.colorPrimaryDark, null));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.toast_finger_input));
        }
    };

    public interface SimpleAuthCallback {

        void onAuthenticated(String value);

        void onAuthError(String error);
    }
}
