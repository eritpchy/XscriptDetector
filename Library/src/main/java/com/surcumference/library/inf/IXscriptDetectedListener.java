package com.surcumference.library.inf;

public interface IXscriptDetectedListener {
    void onXscriptRunningDetected(String detectorName);
    void onXscriptInstalledDetected(String packageName);
}
