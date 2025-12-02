package com.example.floating;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class MyAssistiveTouchService extends AccessibilityService {

    public static final int ACTION_HOME = 1;
    public static final int ACTION_BACK = 2;

    private static MyAssistiveTouchService instance;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        instance = this;

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {}

    public static void performSystemAction(Context context, int action) {
        if (instance == null) return;

        switch (action) {
            case ACTION_HOME:
                instance.performGlobalAction(GLOBAL_ACTION_HOME);
                break;
            case ACTION_BACK:
                instance.performGlobalAction(GLOBAL_ACTION_BACK);
                break;
        }
    }
}
