package com.sam.demo.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.sam.demo.constants.Common;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

public class FileUtil {

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File creatSDFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
        return file;
    }

    /**
     * 删除文件
     * @param filePath
     */
    public static void delFile(String filePath) {
        File delFile = new File(filePath);
        if (delFile.exists()) {
            Log.i("PATH", delFile.getAbsolutePath());
            delFile.delete();

        }
    }
    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public static File creatSDDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdirs();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFileExist(String fileName) {

        try {
            File file = new File(fileName);
            return file.exists();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File saveFile(File file, Bitmap photo) {

        FileOutputStream fOut = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fOut = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        photo.compress(Bitmap.CompressFormat.PNG, 100, fOut);// 把Bitmap对象解析成流
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static String getSizeName(double size) {
        if (String.valueOf(size).length() > 0) {
            DecimalFormat format = new DecimalFormat("0.00");

            if (size < 1024) {
                return size + "B";
            } else if (size >= 1024 && size < 1024 * 1024) {
                return format.format((double) size / 1024) + "KB";
            } else {
                return format.format((double) size / 1024 / 1024) + "M";

            }
        }
        return null;
    }

    /**
     * 用到cache目录时调用
     */
    public static void isCreatCache() {
        if (!new File(Common.CACHE_DIR).exists()) {
            new File(Common.CACHE_DIR).mkdirs();
        } else {
            return;
        }
    }

    /**
     * 删除本地文件
     */
    public static void deleleFile(String path) {
        if (checkSDCard() && !TextUtils.isEmpty(path)) {// 存在sdcard
            File file = new File(Common.CACHE_DIR, path);
            if (file.exists()) {
                file.delete();
                System.out.println("--执行本地删除成功--");
            }
        }
    }

    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

//	public static int freeSpaceOnSd() {
//		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
//		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / Common.MB;
//		return (int) sdFreeMB;
//	}

    /**
     * 文件重命名
     *
     */
    public static void makefilenewName(File orifile, String newName) {
        try {
            String newFileName = parseServerName(newName);
            File file = new File(orifile.getAbsolutePath());
            File newFile = new File(orifile.getParent(), newFileName);
            if (file.exists()) {
                file.renameTo(newFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String parseSeverstr(String str) {
        String newName = null;
        try {
            if (null != str && str.length() > 5) {
                JSONObject j = new JSONObject(str);
                newName = j.getString("filePath");
                return newName;
            }
        } catch (Exception e) {
        }
        return newName;
    }

    public static String parseServerName(String str) {
        String newFileName = null;
        if (null != str && !"".equals(str) && str.contains("/")) {
            newFileName = str.substring(str.lastIndexOf("/") + 1, str.length());
            return newFileName;
        }
        return newFileName;
    }

    public static void deleteFile(File file) {
        if (file.exists()) {                    //判断文件是否存在
            if (file.isFile()) {                    //判断是否是文件
                file.delete();                       //delete()
            } else if (file.isDirectory()) {              //否则如果它是一个目录
                File files[] = file.listFiles();               //声明目录下所有的文件 files[];
                if (files==null)
                    return;
                for (int i = 0; i < files.length; i++) {            //遍历目录下所有的文件
                    deleteFile(files[i]);             //把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            System.out.println("所删除的文件不存在！" + '\n');
        }
    }

    /**
     * @description save object(获取保存的实体对象)
     */
    public static boolean saveObject(Context context,Object ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * @description read object(读取保存的对象)
     */
    public static Object readObject(Context context,String file) {
        if (!isExistDataCache(context,file)) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * @description judge the cache file exit or not(判读缓存是否存在)
     */
    public static boolean isExistDataCache(Context context, String cachefile) {
        boolean exist = false;
        File data = context.getFileStreamPath(cachefile);

        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 通过URi获取path
     * @param context
     * @param uri
     * @return
     */
    public static String getPathByUri(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}