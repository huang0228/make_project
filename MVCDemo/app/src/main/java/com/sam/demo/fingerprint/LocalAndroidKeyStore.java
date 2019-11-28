package com.sam.demo.fingerprint;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;

public class LocalAndroidKeyStore {

    private KeyStore mStore;
    public static final String keyName = "key";

    LocalAndroidKeyStore() {
        try {
            mStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param keyAlias
     * @param invalidatedByBiometricEnrollment 是否校验指纹库有变化
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    void generateKey(String keyAlias,boolean invalidatedByBiometricEnrollment,int purposeParam) {
        //这里使用AES + CBC + PADDING_PKCS7，并且需要用户验证方能取出
        try {
            if (purposeParam==KeyProperties.PURPOSE_ENCRYPT)
            {
                final KeyGenerator generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                final int purpose = KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT;
                final KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyAlias, purpose);
                builder.setUserAuthenticationRequired(true);
                builder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
                builder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
                // This is a workaround to avoid crashes on devices whose API level is < 24
                // because KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment is only
                // visible on API level +24.
                // Ideally there should be a compat library for KeyGenParameterSpec.Builder but
                // which isn't available yet.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
                }
                generator.init(builder.build());
                generator.generateKey();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    FingerprintManager.CryptoObject getCryptoObject(int purpose, byte[] IV) {
        try {
            mStore.load(null);
            final SecretKey key = (SecretKey) mStore.getKey(keyName, null);
            if (key == null) {
                return null;
            }
            final Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC
                    + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            if (purpose == KeyProperties.PURPOSE_ENCRYPT) {
                cipher.init(purpose, key);
            } else {
                cipher.init(purpose, key, new IvParameterSpec(IV));
            }
            return new FingerprintManager.CryptoObject(cipher);
        } catch (KeyPermanentlyInvalidatedException e) {
            return null;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isKeyProtectedEnforcedBySecureHardware() {
        try {
            //这里随便生成一个key，检查是不是受保护即可
            generateKey("temp",true,KeyProperties.PURPOSE_ENCRYPT);
            final SecretKey key = (SecretKey) mStore.getKey("temp", null);
            if (key == null) {
                return false;
            }
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyInfo keyInfo;
            keyInfo = (KeyInfo) factory.getKeySpec(key, KeyInfo.class);
            return keyInfo.isInsideSecureHardware() && keyInfo.isUserAuthenticationRequirementEnforcedBySecureHardware();
        } catch (Exception e) {
            // Not an Android KeyStore key.
            return false;
        }
    }
}
