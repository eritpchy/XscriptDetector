package com.surcumference.library.detector;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.surcumference.library.inf.IXscriptDetectedListener;
import com.surcumference.library.inf.IXscriptDetector;
import com.surcumference.library.util.FileUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class XscriptRsyncDetector implements IXscriptDetector {

    private static final String TAG = XscriptRsyncDetector.class.getName();

    @Override
    public void detect(Context context, final IXscriptDetectedListener listener) {
        new Thread(() -> {
            Socket socket = null;
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                socket = new Socket("127.0.0.1", 18730);
                socket.setSoTimeout(3000);
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String line = br.readLine();
                if (line.startsWith("@RSYNCD")) {
                    new Handler(Looper.getMainLooper()).post(() -> listener.onXscriptRunningDetected(TAG));
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            } finally {
                FileUtils.closeCloseable(br);
                FileUtils.closeCloseable(isr);
                FileUtils.closeCloseable(is);
                FileUtils.closeCloseable(socket);
            }
        }).start();
    }
}
