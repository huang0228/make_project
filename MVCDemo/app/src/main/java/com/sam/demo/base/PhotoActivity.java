package com.sam.demo.base;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.sam.demo.MyApplication;
import com.sam.demo.constants.Common;
import com.sam.demo.util.FileUtil;
import com.sam.demo.util.ImageUtil;
import com.sam.demo.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2018/5/31.
 */

public class PhotoActivity extends BaseActivity {
    private String min_photo;
    private Activity activity;
    private File file;
    public static final String TYPE = "type";
    public static final String IMAGE_PHOTO_PATH = "imagePhotoPath";
    public static final int CAMERA = 1;
    public static final int EXTERNAL = 2;
    public static final int RESULT_REQUEST_CODE = 3;
    public static final int RESULT_REQUEST_CODE_BIG = 4;
    //是否需要剪裁  默认不需要
    private boolean isZoom;
    //剪切时 截大图用URI，小图用Bitmap。
    private boolean isBig;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        chuangJianMuLu();
        int type = getIntent().getIntExtra(TYPE, 0);
        isZoom = getIntent().getBooleanExtra("isZoom", false);
        isBig = getIntent().getBooleanExtra("isBig", false);
        if (type == CAMERA) {
            getCamera();
        } else if (type == EXTERNAL) {
            getExternal();
        }
    }

    private void chuangJianMuLu() {
        File appFirstFile = new File(Common.APP_SDCAR_PATH);// 新建一级主目录
        File dirFirstFile = new File(Common.CACHE_DIR);// 新建一级主目录
        if (!appFirstFile.exists()) {// 判断文件夹目录是否存在
            appFirstFile.mkdir();// 如果不存在则创建
        }
        if (!dirFirstFile.exists()) {// 判断文件夹目录是否存在
            dirFirstFile.mkdir();// 如果不存在则创建
        }
    }

    private void getExternal() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        startActivityForResult(intent, EXTERNAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA:
                    if (isZoom) {
                        if (isBig) {
                            imageUri = Uri.fromFile(file);
                            startPhotoZoomBig(Uri.fromFile(file));
                        } else {
                            startPhotoZoom(Uri.fromFile(file));
                        }
                    } else {
                        if (FileUtil.isFileExist(min_photo)) {
                            luBan(min_photo, true);
                        }

                    }
                    break;
                case EXTERNAL:
                    if (isZoom) {
                        if (isBig) {
                            imageUri = data.getData();
                            startPhotoZoomBig(data.getData());
                        } else {
                            startPhotoZoom(data.getData());
                        }
                    } else {
                        /**
                         * modify by 黄广帅
                         * date 2018.08.06
                         * content:调用系统图库在部分手机上会出现其他文件格式
                         */
                        if (data.getData()!=null){
                            String tempFormat=sendPicByUri(data.getData());
                            if (tempFormat.toLowerCase().contains(".png")||tempFormat.toLowerCase().contains(".jpeg")||tempFormat.toLowerCase().contains(".jpg"))
                            {
                                luBan(FileUtil.getPathByUri(activity, data.getData()), false);
                            }else if (tempFormat.toLowerCase().contains("not find")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(activity,"找不到图片资源");
                                        finish();
                                    }
                                });
                            }
                            else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(activity,"图片格式不正确");
                                        finish();
                                    }
                                });
                            }
                        }else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(activity,"无法获取图片资源");
                                    finish();
                                }
                            });

                        }
                        //---------------------------------------------------------------------------------

                        //luBan(FileUtil.getPathByUri(activity, data.getData()), false);

                    }
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        setImageToView(data);
                    }
                    break;
                case RESULT_REQUEST_CODE_BIG:
                    if (imageUri != null) {
                        Bitmap bitmap = decodeUriAsBitmap(imageUri);
                        saveBitmap(bitmap);
                    }
                    break;
            }
        } else {
            finish();
        }
    }


    /**
     * 获取图片路径
     *add by sam.huang
     * date:2018.08.08
     * @param selectedImage
     */
    protected String sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(activity,"无法获取图片资源");
                        finish();
                    }
                });
                return "not find";
            }
            return picturePath;
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(activity,"无法获取图片资源");
                        finish();
                    }
                });
                return "not find";

            }
           return file.getAbsolutePath();
        }
    }


        private void luBan(final String filePath, final boolean delFile) {
        //鲁班压缩图片
        Luban.with(this)
                .load(filePath)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(Common.CACHE_DIR)                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        if (delFile)
                            FileUtil.delFile(filePath);
                        resultData(file.getPath());

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getMessage();
                        resultData(filePath);

                    }
                }).launch();    //启动压缩
    }

    private void resultData(String filePath) {
        Intent intent = new Intent();
        intent.putExtra(IMAGE_PHOTO_PATH, filePath);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            saveBitmap(photo);
        }
    }

    public void saveBitmap(Bitmap mBitmap) {
        String IMAGE_FILE_NAME = Common.CACHE_DIR + "/" + ImageUtil.getImageName();
        File f = new File(IMAGE_FILE_NAME);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            File file = new File(IMAGE_FILE_NAME);
            if (file.exists()) {


                resultData(IMAGE_FILE_NAME);
            } else {
                ToastUtil.showLongToast(PhotoActivity.this, "剪切图片失败，请重试");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = ImageUtil.getPath(this, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }


    public void startPhotoZoomBig(Uri uri) {
        if (uri == null) {
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = ImageUtil.getPath(this, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // lol, negative boolean noFaceDetection

        startActivityForResult(intent, RESULT_REQUEST_CODE_BIG);
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public void getCamera() {
        min_photo = Common.CACHE_DIR + "/" + ImageUtil.getImageName();
        file = new File(min_photo);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (file != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(file));
        }
        activity.startActivityForResult(intent, CAMERA);
    }

    private static Uri getUriForFile(File file) {

        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(MyApplication.AppContent, "com.autobio.autolink.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
