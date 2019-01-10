package com.surcumference.library.detector;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.surcumference.library.inf.IXscriptDetectedListener;
import com.surcumference.library.inf.IXscriptDetector;
import com.surcumference.library.util.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class XscriptDeamonCoreDetector implements IXscriptDetector {

    public static final String TAG = XscriptDeamonCoreDetector.class.getName();

    @Override
    public void detect(Context context, IXscriptDetectedListener listener) {
        new Thread(() -> {
            for (int uid = Process.FIRST_APPLICATION_UID; uid < Process.LAST_APPLICATION_UID; uid++) {
                if (socketDetect(uid)) {
                    int finalPid = uid;
                    new Handler(Looper.getMainLooper()).post(() -> listener.onXscriptRunningDetected(TAG + " uid: " + finalPid));
                }
            }
        }).start();
    }

    private static boolean socketDetect(int uid) {
        Socket socket = null;
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            socket = new Socket("127.0.0.1", uid);
            if (!socket.isConnected()) {
                return false;
            }
            socket.setSoTimeout(100);
            os = socket.getOutputStream();
            os.write("12\n".getBytes());
            os.flush();
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = br.readLine();
            return line.matches("\\d{1,2}");
        } catch (IOException e) {
            if (!e.getMessage().contains("Connection refused")) {
                Log.e(TAG, "", e);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            FileUtils.closeCloseable(os);
            FileUtils.closeCloseable(br);
            FileUtils.closeCloseable(isr);
            FileUtils.closeCloseable(is);
            FileUtils.closeCloseable(socket);
        }
        return false;
    }
}
