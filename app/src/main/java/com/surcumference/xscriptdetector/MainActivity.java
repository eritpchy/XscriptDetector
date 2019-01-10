package com.surcumference.xscriptdetector;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.surcumference.library.XscriptDetector;
import com.surcumference.library.inf.IXscriptDetectedListener;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mTextView = findViewById(R.id.text);
        XscriptDetector.detect(this, new IXscriptDetectedListener() {
            @Override
            public void onXscriptRunningDetected(String detectorName) {
                mTextView.append("Xscript Running DETECTED! detector: " + detectorName + "\n");
            }

            @Override
            public void onXscriptInstalledDetected(String packageName) {
                mTextView.append("Xscript DETECTED!: " + packageName + "\n");
            }
        });
    }
}
