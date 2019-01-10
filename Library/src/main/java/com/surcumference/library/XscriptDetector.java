package com.surcumference.library;

import android.content.Context;

import com.surcumference.library.detector.XscriptDeamonCoreDetector;
import com.surcumference.library.detector.XscriptInstalledDetector;
import com.surcumference.library.detector.XscriptRsyncDetector;
import com.surcumference.library.detector.XscriptScriptEngineDetector;
import com.surcumference.library.inf.IXscriptDetectedListener;
import com.surcumference.library.inf.IXscriptDetector;

import androidx.annotation.MainThread;

public class XscriptDetector {

    @MainThread
    public static void detect(Context context, IXscriptDetectedListener listener) {
        IXscriptDetector[] xscriptDetectors = getXscriptDetectors();
        for (IXscriptDetector xscriptDetector : xscriptDetectors) {
            xscriptDetector.detect(context, listener);
        }
    }

    private static IXscriptDetector[] getXscriptDetectors() {
        return new IXscriptDetector[]{
                new XscriptInstalledDetector(),
                new XscriptRsyncDetector(),
                new XscriptScriptEngineDetector(),
                new XscriptDeamonCoreDetector(),
        };
    }
}
