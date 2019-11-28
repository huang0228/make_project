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

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sam.demo.R;
import com.sam.demo.base.LoginActivity;
import com.sam.demo.base.LoginByFingerActivity;
import com.sam.demo.mine.SysSettingActivity;
import com.sam.demo.util.ToastUtil;

import javax.crypto.Cipher;

/**
 * A dialog which uses fingerprint APIs to authenticate the user, and falls back to password
 * authentication if fingerprint is not available.
 */
public class FingerprintAuthenticationDialogFragment extends DialogFragment
        implements FingerprintUiHelper.SimpleAuthCallback {

    private Button mCancelButton;
    private TextView tvFingerprintStatus;

    private Stage mStage = Stage.FINGERPRINT;

    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    private Activity mActivity;

    private int fromwhere = 0;//来自哪个页面

    private int purpose = KeyProperties.PURPOSE_ENCRYPT;
    private LocalSharedPreference mLocalSharedPreference;
    private static LocalAndroidKeyStore mLocalAndroidKeyStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.getDialog().setCancelable(false);
        this.getDialog().setCanceledOnTouchOutside(false);
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keycode, KeyEvent keyEvent) {
                if (keycode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else
                    return false;
            }
        });

        View v = inflater.inflate(R.layout.alert_finger_print_layout, container, false);
        mCancelButton = (Button) v.findViewById(R.id.btnCancelFinger);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity = getActivity();
                if (fromwhere == 1) {
                } else if (fromwhere == 2) {
                    ((SysSettingActivity) mActivity).onClickCancel();
                } else if (fromwhere == 3) {
                }
                dismiss();
            }
        });
        tvFingerprintStatus = (TextView) v.findViewById(R.id.tv_fingerprint_status);
        mLocalSharedPreference = new LocalSharedPreference(getActivity());

        mFingerprintUiHelper = new FingerprintUiHelper(getActivity().getSystemService(FingerprintManager.class), tvFingerprintStatus
                , this, mLocalSharedPreference);
        updateStage();

        // If fingerprint authentication is not available, switch immediately to the backup
        // (password) screen.
        if (!mFingerprintUiHelper.isFingerprintAuthAvailable()) {
            goToBackup();
        }
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void generateKey(boolean invalidatedByBiometricEnrollment, int purpose) {
        //在keystore中生成加密密钥
        mLocalAndroidKeyStore = new LocalAndroidKeyStore();
        mLocalAndroidKeyStore.generateKey(LocalAndroidKeyStore.keyName, invalidatedByBiometricEnrollment, purpose);
    }


    public void setPurpose(int purpose, int pagewhere) {
        this.purpose = purpose;
        this.fromwhere = pagewhere;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        if (mStage == Stage.FINGERPRINT) {
            /**
             * Sets the crypto object to be passed in when authenticating with fingerprint.
             */
            if (purpose == KeyProperties.PURPOSE_DECRYPT) {
                String IV = mLocalSharedPreference.getData(mLocalSharedPreference.IVKeyName);
                mCryptoObject = mLocalAndroidKeyStore.getCryptoObject(Cipher.DECRYPT_MODE, Base64.decode(IV, Base64.URL_SAFE));
                if (mCryptoObject == null) {
                    ToastUtil.showToast(getActivity(), "请用密码认证");
                    return;
                }
            } else {
                mCryptoObject = mLocalAndroidKeyStore.getCryptoObject(Cipher.ENCRYPT_MODE, null);
                if (mCryptoObject == null) {
                    ToastUtil.showToast(getActivity(), "加密对象为空");
                    return;
                }
            }
            mFingerprintUiHelper.startListening(mCryptoObject, purpose);
        } else if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    public void setStage(Stage stage) {
        mStage = stage;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPause() {
        super.onPause();
        mFingerprintUiHelper.stopListening();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Switches to backup (password) screen. This either can happen when fingerprint is not
     * available or the user chooses to use the password authentication method by pressing the
     * button. This can also happen when the user had too many fingerprint attempts.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void goToBackup() {
        updateStage();
        // Fingerprint is not used anymore. Stop listening for it.
        mFingerprintUiHelper.stopListening();
    }


    private void updateStage() {
        switch (mStage) {
            case FINGERPRINT:
                mCancelButton.setText(R.string.cancel);
                break;
            case NEW_FINGERPRINT_ENROLLED:
                // Intentional fall through
                if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
                    tvFingerprintStatus.setVisibility(View.VISIBLE);
                    tvFingerprintStatus.setText("有新指纹信息");
                }
                break;
        }
    }

    @Override
    public void onAuthenticated(String value) {
        // Callback from FingerprintUiHelper. Let the activity know that authentication was
        // successful.
        mActivity = getActivity();
        if (mActivity == null) return;
        if (fromwhere == 1) {
            ((LoginByFingerActivity) mActivity).onAuthResultSuccess(true /* withFingerprint */, value);
        } else if (fromwhere == 2) {
            ((SysSettingActivity) mActivity).onAuthResultSuccess(true /* withFingerprint */, value);
        } else if (fromwhere == 3) {
            ((LoginActivity) mActivity).onAuthResultSuccess(true /* withFingerprint */, value);
        }
        dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAuthError(String msgError) {
        mActivity = getActivity();
        if (mActivity == null) return;
        if (fromwhere == 1) {
            ((LoginByFingerActivity) mActivity).onAuthResultError(msgError);
        } else if (fromwhere == 2) {
            ((SysSettingActivity) mActivity).onAuthResultError(msgError);
        } else if (fromwhere == 3) {
            ((LoginActivity) mActivity).onAuthResultError(msgError);
        }

        goToBackup();
    }

    /**
     * Enumeration to indicate which authentication method the user is trying to authenticate with.
     */
    public enum Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
    }
}
