package com.surcumference.library.detector;

import android.content.Context;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.surcumference.library.inf.IXscriptDetectedListener;
import com.surcumference.library.inf.IXscriptDetector;
import com.surcumference.library.util.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class XscriptScriptEngineDetector implements IXscriptDetector {

    public static final String TAG = XscriptScriptEngineDetector.class.getName();

    @Override
    public void detect(Context context, IXscriptDetectedListener listener) {
        new Thread(() -> {
            for (int pid = 1; pid <= 65535; pid++) {
                if (socketDetect(pid)) {
                    int finalPid = pid;
                    new Handler(Looper.getMainLooper()).post(() -> listener.onXscriptRunningDetected(TAG + " pid: " + finalPid));
                }
            }
        }).start();
    }

    private static boolean socketDetect(int pid) {
        String name = "xs:" + pid;
        LocalSocketAddress address = new LocalSocketAddress(name, LocalSocketAddress.Namespace.ABSTRACT);
        LocalSocket socket = new LocalSocket();
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            socket.connect(address);
            if (!socket.isConnected()) {
                return false;
            }
            socket.setSoTimeout(100);
            os = socket.getOutputStream();
            os.write("1\n".getBytes());
            os.flush();
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = br.readLine();
            return line.startsWith("1");
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
