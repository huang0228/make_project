package com.sam.demo.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.constants.Flag;
import com.sam.demo.entity.BaseResult;
import com.sam.demo.entity.User;
import com.sam.demo.constants.Common;
import com.sam.demo.base.BaseFragment;
import com.sam.demo.util.AlertDialogUtils;
import com.sam.demo.util.FileUtil;
import com.sam.demo.util.ToastUtil;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 牛世杰
 * 2018-3-13 17:24:13
 * 个人中心
 */

public class MainMeFragment extends BaseFragment {

    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.userDept)
    TextView userDept;
    @Bind(R.id.userPosition)
    TextView userPosition;
    @Bind(R.id.userArea)
    TextView userArea;
    @Bind(R.id.userHeadImg)
    ImageView userHeadImg;

    private View dialogView;
    private AlertDialog showDialog;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(this, R.layout.fragment_main_me);
        initData();
    }

    private void initData() {
        User user = MyApplication.getInstance().getUser();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    // 计算缓存
    public int calculateCacheSize() {
        return 0;
    }

    // 计算消息数
    public int calculateMsgCount() {
        return 0;
    }

    /**
     * 隐藏软键盘(适用于Activity，Fragment)
     */
    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onError(String str, int flag) {
        super.onError(str, flag);

    }

    @Override
    public void onSuccess(String str, int flag) {
        super.onSuccess(str, flag);
        switch (flag) {
            case Flag.FIRST: // 修改密码
                BaseResult result = new BaseResult().toEntity(str);
                break;
        }
    }
}
