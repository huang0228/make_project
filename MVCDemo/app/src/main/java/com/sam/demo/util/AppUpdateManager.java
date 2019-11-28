package com.sam.demo.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.sam.demo.MyApplication;
import com.sam.demo.eventbus.CheckVersionEvent;
import com.tencent.tmdownloader.sdkdownload.downloadclient.TMAssistantDownloadTaskState;
import com.tencent.tmselfupdatesdk.ITMSelfUpdateListener;
import com.tencent.tmselfupdatesdk.TMSelfUpdateConst;
import com.tencent.tmselfupdatesdk.TMSelfUpdateManager;
import com.tencent.tmselfupdatesdk.TMSelfUpdateTaskState;
import com.tencent.tmselfupdatesdk.YYBDownloadListener;
import com.tencent.tmselfupdatesdk.model.TMSelfUpdateUpdateInfo;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;


public class AppUpdateManager {
    private Context context;
    private boolean isShowToast = false;
    private AlertDialog.Builder mBuilder;
    private TMSelfUpdateManager manager;
    private static volatile AppUpdateManager instance;

    ProgressDialog mProgressDialog;

    /**
     * 应用宝渠道号，由产品对接分
     */
    private static final String SELF_UPDATE_CHANNEL = "1106780554";

    public static AppUpdateManager getInstance() {
        if (instance == null) {
            synchronized (AppUpdateManager.class) {
                if (instance == null) {
                    instance = new AppUpdateManager();
                }
            }
        }
        return instance;
    }

    public void setContext(Context context)
    {
        mProgressDialog=new ProgressDialog(context);
        this.context=context;
        this.mBuilder = new AlertDialog.Builder(context);
    }

    public AppUpdateManager() {
        this.manager = TMSelfUpdateManager.getInstance();
        initSelfUpdateSDK();
    }

    public void check(boolean isShowToast) {
        this.isShowToast=isShowToast;
        manager.switchLog(MyApplication.isDevelop);
        //检查更新
        manager.checkSelfUpdate();
    }

    /**
     * 初始化省流量更新SDK，传入的Context必须为ApplicationContext
     */
    private void initSelfUpdateSDK() {

        //附加参数的bundle，一般情况下传空，可以由外部传入场景信息等
        // 具体字段可参考示例{@link TMSelfUpdateConst}中BUNDLE_KEY_*的定义（详见注2）
        Bundle bundle = new Bundle();
        bundle.putString(TMSelfUpdateConst.BUNDLE_KEY_SCENE, "FOO");
        manager.init(MyApplication.getInstance(),
                SELF_UPDATE_CHANNEL, mSelfUpdateListener, mDownloadYYBCallback, bundle);
    }

    /**
     * 自更新状态监听器（ITMSelfUpdateListener），是用于监听当前更新包的下载状态变化的回调接口
     */
    private ITMSelfUpdateListener mSelfUpdateListener = new ITMSelfUpdateListener() {
        /**
         * 更新时的下载状态回调
         * @param state 状态码
         * @param errorCode 错误码
         * @param errorMsg 错误信息
         */
        @Override
        public void onDownloadAppStateChanged(int state, int errorCode, String errorMsg) {
            //TODO 更新包下载状态变化的处理逻辑
            switch (state) {
                case TMAssistantDownloadTaskState.DownloadSDKTaskState_DOWNLOADING:
                    break;
                case TMAssistantDownloadTaskState.DownloadSDKTaskState_FAILED:
                    dismissProgressDialog();
                    break;
                case TMAssistantDownloadTaskState.DownloadSDKTaskState_PAUSED:
                    dismissProgressDialog();
                    break;
                case TMAssistantDownloadTaskState.DownloadSDKTaskState_WAITING:
                    dismissProgressDialog();
                    break;
                case TMAssistantDownloadTaskState.DownloadSDKTaskState_SUCCEED:
                    dismissProgressDialog();
                    break;
                case TMSelfUpdateTaskState.SelfUpdateSDKTaskState_SUCCESS:
                    dismissProgressDialog();
                    break;
                case TMSelfUpdateTaskState.SelfUpdateSDKTaskState_FAIURE:
                    dismissProgressDialog();
                    break;
                default:
                    break;
            }
        }

        /**
         * 点击普通更新时的下载进度回调
         * @param receiveDataLen 已经接收的数据长度
         * @param totalDataLen 全部需要接收的数据长度（如果无法获取目标文件的总长度，此参数返回-1）
         */
        @Override
        public void onDownloadAppProgressChanged(final long receiveDataLen, final long totalDataLen) {
            //TODO 更新包下载进度发生变化的处理逻辑
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.setTitle("普通更新中 ： " + (int) (receiveDataLen * 100 / (double)
                            totalDataLen) + "%");
                    mProgressDialog.show();
                }
            });
        }

        /**
         * 检查更新返回更新信息回调
         * @param tmSelfUpdateUpdateInfo 更新信息结构体
         */
        @Override
        public void onUpdateInfoReceived(TMSelfUpdateUpdateInfo tmSelfUpdateUpdateInfo) {
            //TODO 收到更新信息的处理逻辑
            if (null != tmSelfUpdateUpdateInfo) {
                int state = tmSelfUpdateUpdateInfo.getStatus();
                if (state == TMSelfUpdateUpdateInfo.STATUS_CHECKUPDATE_FAILURE) {
                    if (isShowToast) {
                        ToastUtil.showToast(context, "检查更新失败");
                    }
                    return;
                }

                switch (tmSelfUpdateUpdateInfo.getUpdateMethod()) {
                    case TMSelfUpdateUpdateInfo.UpdateMethod_NoUpdate:
                        //无更新
                        if (isShowToast) {
                            ToastUtil.showToast(context, "已经是最新版本"); //+ tmSelfUpdateUpdateInfo.versionname);
                        }
                        EventBus.getDefault().post(new CheckVersionEvent(true));
                        break;
                    case TMSelfUpdateUpdateInfo.UpdateMethod_Normal:
                        EventBus.getDefault().post(new CheckVersionEvent(false));
                        if (isXiaoMiBrand())
                        {
                            initXiaomiUpdate(context);
                        }else
                        {
                            //普通更新
                            showNormalUpdateDialog(tmSelfUpdateUpdateInfo);
                        }
                        break;
                    case TMSelfUpdateUpdateInfo.UpdateMethod_ByPatch:
                        EventBus.getDefault().post(new CheckVersionEvent(false));
                        if (isXiaoMiBrand())
                        {
                            initXiaomiUpdate(context);
                        }else
                        {
                            //省流量更新
                            if (TMSelfUpdateManager.getInstance().checkYYBInstallState() == TMAssistantDownloadTaskState
                                    .ALREADY_INSTALLED
                                    && tmSelfUpdateUpdateInfo.getPatchSize() != 0) {
                                showPatchUpdateDialog(tmSelfUpdateUpdateInfo);
                            } else {
                                showNormalUpdateDialog(tmSelfUpdateUpdateInfo);
                            }
                        }
                        break;
                    default:
                        //是否强制更新
                        Boolean isforceUpdate = Utils.forceUpdate(context, tmSelfUpdateUpdateInfo.versionname);
                        int yybInstallState = TMSelfUpdateManager.getInstance().checkYYBInstallState();

                        if (yybInstallState == TMAssistantDownloadTaskState.ALREADY_INSTALLED) {
                            //已安装应用宝，可以跳转到应用宝的指定页面  省流量更新

                        } else {
                            //未安装应用宝或者应用宝不支持跳转制定页面  普通更新
                        }
                        break;
                }
            }
        }
    };

    /**
     * 计算数据大小，将bit转换为k或M的单位输出
     *
     * @param dataSize
     * @return
     */
    private String getDataSize(long dataSize) {
        long kb = dataSize / 1024;  // 单位k
        if (kb < 1024) {
            return kb + "k";
        }
        double mb = dataSize / 1024.0 / 1024.0 + 0.05;    // 转换为M，保留一位小数，四舍五入
        return new DecimalFormat(".0").format(mb) + "M";
    }

    /**
     * 展示普通更新弹窗
     *
     * @param tmSelfUpdateUpdateInfo 更新信息结构体
     */
    private void showNormalUpdateDialog(TMSelfUpdateUpdateInfo tmSelfUpdateUpdateInfo) {
        mBuilder.setTitle("更新提示");
        mBuilder.setPositiveButton("普通更新(" + getDataSize(tmSelfUpdateUpdateInfo.getNewApkSize()) + "）",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //普通更新方式isUseYYB=false;
                        manager.startSelfUpdate(false);
                    }
                });
        mBuilder.create().show();
    }

    /**
     * 展示带有省流量更新的弹窗
     *
     * @param tmSelfUpdateUpdateInfo 更新信息结构体
     */
    private void showPatchUpdateDialog(TMSelfUpdateUpdateInfo tmSelfUpdateUpdateInfo) {
        mBuilder.setTitle("更新提示");
        mBuilder.setPositiveButton("普通更新(" + getDataSize(tmSelfUpdateUpdateInfo.getNewApkSize()) + "）",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //普通更新方式isUseYYB=false;
                        manager.startSelfUpdate(false);
                    }
                });
        mBuilder.setNegativeButton("省流量更新(" + getDataSize(tmSelfUpdateUpdateInfo.getPatchSize()) + "）",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //省流量更新方式isUseYYB=true;
                        manager.startSelfUpdate(true);
                    }
                });
        mBuilder.create().show();
    }


    /**
     * 应用宝下载状态监听（YYBDownloadListener），是用于监听应用宝的下载状态变化的回调接口
     */
    private YYBDownloadListener mDownloadYYBCallback = new YYBDownloadListener() {
        /**
         * 预下载应用宝状态变化回调
         *
         * @param url  指定任务的url
         * @param state 下载状态: 取自 TMAssistantDownloadTaskState.DownloadSDKTaskState_*
         * @param errorCode 错误码
         * @param errorMsg 错误描述，有可能为null
         */
        @Override
        public void onDownloadYYBStateChanged(String url, int state, int errorCode, String errorMsg) {
            //TODO 应用宝下载状态变化的处理逻辑
        }

        /**
         * 预下载应用宝进度回调
         *
         * @param url 当前任务的url
         * @param receiveDataLen 已经接收的数据长度
         * @param totalDataLen 全部需要接收的数据长度（如果无法获取目标文件的总长度，此参数返回 －1）
         */
        @Override
        public void onDownloadYYBProgressChanged(String url, long receiveDataLen, long totalDataLen) {
            //TODO 应用宝下载进度变化的处理逻辑
        }

        /**
         * 查询应用宝的下载状态：当用户调了查询应用宝状态时回调
         *
         * @param url 当前任务的url
         * @param state 下载状态: 取自 TMAssistantDownloadTaskState.DownloadSDKTaskState_*
         * @param receiveDataLen 已经接收的数据长度
         * @param totalDataLen 全部需要接收的数据长度（如果无法获取目标文件的总长度，此参数返回 －1）
         */
        @Override
        public void onCheckDownloadYYBState(String url, int state, long receiveDataLen, long totalDataLen) {
            //TODO 检查应用宝下载状态处理逻辑
        }

    };



    private void showToast(String msg) {
        ToastUtil.showToast(context, msg);
    }


    private void dismissProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        });
    }


    //是否小米更新
    private boolean isXiaoMiBrand()
    {
        String brand=Utils.getDeviceBrand();
        if (!StringUtils.isEmpty(brand)){
            if (brand.toLowerCase().equals("xiaomi"))
            {
                return true;
            }
        }
        return false;
    }
    /**
     * 初始化小米自动更新
     */
    private void initXiaomiUpdate(Context context) {
        XiaomiUpdateAgent.update(context);//小米自动更新
    }
}
