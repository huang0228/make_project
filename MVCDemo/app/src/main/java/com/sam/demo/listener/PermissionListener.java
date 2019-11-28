package com.sam.demo.listener;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface PermissionListener {
    public void onGranted();
    //拒绝的权限列表
    public void onDenied(List<String> deniedPermission);
}
