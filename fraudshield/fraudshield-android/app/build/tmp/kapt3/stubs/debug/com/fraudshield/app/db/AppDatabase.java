package com.fraudshield.app.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&\u00a8\u0006\f"}, d2 = {"Lcom/fraudshield/app/db/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "patternDao", "Lcom/fraudshield/app/db/PatternDao;", "senderAllowlistDao", "Lcom/fraudshield/app/db/SenderAllowlistDao;", "smsLogDao", "Lcom/fraudshield/app/db/SmsLogDao;", "urlCacheDao", "Lcom/fraudshield/app/db/UrlCacheDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.fraudshield.app.db.SmsLogEntry.class, com.fraudshield.app.db.UrlCacheEntry.class, com.fraudshield.app.db.SenderAllowlistEntry.class, com.fraudshield.app.db.PatternEntry.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.fraudshield.app.db.AppDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.fraudshield.app.db.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.fraudshield.app.db.SmsLogDao smsLogDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.fraudshield.app.db.UrlCacheDao urlCacheDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.fraudshield.app.db.SenderAllowlistDao senderAllowlistDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.fraudshield.app.db.PatternDao patternDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0004H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/fraudshield/app/db/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/fraudshield/app/db/AppDatabase;", "getInstance", "context", "Landroid/content/Context;", "seedIfEmpty", "", "db", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.fraudshield.app.db.AppDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        /**
         * Today's quick-start seed so on-device checks work WITHOUT waiting for
         * Harshit's real blacklist feed or Navya's pattern data.
         * Replace/extend this once delta-sync (Step 10) is wired up.
         */
        private final void seedIfEmpty(com.fraudshield.app.db.AppDatabase db) {
        }
    }
}