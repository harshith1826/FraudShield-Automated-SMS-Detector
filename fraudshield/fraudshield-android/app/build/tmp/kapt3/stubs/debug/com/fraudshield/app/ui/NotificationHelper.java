package com.fraudshield.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\fJ.\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/fraudshield/app/ui/NotificationHelper;", "", "()V", "ACTION_FEEDBACK_FRAUD", "", "ACTION_FEEDBACK_SAFE", "CHANNEL_ID", "EXTRA_URL", "EXTRA_VERDICT", "actionIntent", "Landroid/app/PendingIntent;", "context", "Landroid/content/Context;", "action", "url", "verdict", "notificationId", "", "ensureChannel", "", "show", "state", "message", "app_debug"})
public final class NotificationHelper {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "fraudshield_verdicts";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_FEEDBACK_FRAUD = "com.fraudshield.app.FEEDBACK_FRAUD";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_FEEDBACK_SAFE = "com.fraudshield.app.FEEDBACK_SAFE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_URL = "extra_url";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_VERDICT = "extra_verdict";
    @org.jetbrains.annotations.NotNull()
    public static final com.fraudshield.app.ui.NotificationHelper INSTANCE = null;
    
    private NotificationHelper() {
        super();
    }
    
    public final void ensureChannel(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * state: "PENDING" (grey) | "SUSPICIOUS" (yellow) | "FRAUD" (red) | "SAFE" (green)
     * notificationId should be stable per-SMS so later calls UPDATE the same
     * notification instead of creating a new one (grey -> yellow -> red).
     */
    public final void show(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int notificationId, @org.jetbrains.annotations.NotNull()
    java.lang.String state, @org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    private final android.app.PendingIntent actionIntent(android.content.Context context, java.lang.String action, java.lang.String url, java.lang.String verdict, int notificationId) {
        return null;
    }
}