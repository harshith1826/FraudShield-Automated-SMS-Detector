package com.fraudshield.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0016B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\bH\u0016J\u0018\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0016J \u0010\u0011\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\bH\u0002J\u0014\u0010\u0014\u001a\u00020\n2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/fraudshield/app/ui/SmsListAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/fraudshield/app/ui/SmsListAdapter$SmsViewHolder;", "items", "", "Lcom/fraudshield/app/db/SmsLogEntry;", "(Ljava/util/List;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "sendFeedback", "entry", "signal", "updateData", "newItems", "SmsViewHolder", "app_debug"})
public final class SmsListAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.fraudshield.app.ui.SmsListAdapter.SmsViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.fraudshield.app.db.SmsLogEntry> items;
    
    public SmsListAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fraudshield.app.db.SmsLogEntry> items) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.fraudshield.app.ui.SmsListAdapter.SmsViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.fraudshield.app.ui.SmsListAdapter.SmsViewHolder holder, int position) {
    }
    
    private final void sendFeedback(com.fraudshield.app.ui.SmsListAdapter.SmsViewHolder holder, com.fraudshield.app.db.SmsLogEntry entry, int signal) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void updateData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fraudshield.app.db.SmsLogEntry> newItems) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\u0015\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000eR\u0011\u0010\u0017\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000eR\u0011\u0010\u0019\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u000eR\u0011\u0010\u001b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u000e\u00a8\u0006\u001d"}, d2 = {"Lcom/fraudshield/app/ui/SmsListAdapter$SmsViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "btnFraud", "Landroid/widget/Button;", "getBtnFraud", "()Landroid/widget/Button;", "btnSafe", "getBtnSafe", "feedbackDoneText", "Landroid/widget/TextView;", "getFeedbackDoneText", "()Landroid/widget/TextView;", "feedbackLayout", "Landroid/widget/LinearLayout;", "getFeedbackLayout", "()Landroid/widget/LinearLayout;", "senderText", "getSenderText", "sourceText", "getSourceText", "timeText", "getTimeText", "urlText", "getUrlText", "verdictBadge", "getVerdictBadge", "app_debug"})
    public static final class SmsViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView senderText = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView urlText = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView verdictBadge = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView sourceText = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView timeText = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout feedbackLayout = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.Button btnFraud = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.Button btnSafe = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView feedbackDoneText = null;
        
        public SmsViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getSenderText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getUrlText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getVerdictBadge() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getSourceText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTimeText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getFeedbackLayout() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.Button getBtnFraud() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.Button getBtnSafe() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getFeedbackDoneText() {
            return null;
        }
    }
}