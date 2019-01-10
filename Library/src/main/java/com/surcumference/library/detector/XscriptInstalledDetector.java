package com.surcumference.library.detector;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;

import com.surcumference.library.inf.IXscriptDetectedListener;
import com.surcumference.library.inf.IXscriptDetector;

import java.util.List;

public class XscriptInstalledDetector implements IXscriptDetector {
    private final String TAG = XscriptInstalledDetector.class.getName();

    @Override
    public void detect(Context context, IXscriptDetectedListener listener) {
        try {
            Intent intent = new Intent("android.view.InputMethod", null);
            List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentServices(intent,0);
            if (resolveInfoList == null) {
                return;
            }
            for (ResolveInfo resolveInfo : resolveInfoList) {
                if (resolveInfo == null) {
                    continue;
                }
                if (resolveInfo.serviceInfo == null) {
                    continue;
                }
                if (TextUtils.isEmpty(resolveInfo.serviceInfo.name)) {
                    continue;
                }
                if (resolveInfo.serviceInfo.name.contains("com.surcumference.xscript.")) {
                    listener.onXscriptInstalledDetected(resolveInfo.serviceInfo.packageName);
                }
            }
            return;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return;
    }
}
