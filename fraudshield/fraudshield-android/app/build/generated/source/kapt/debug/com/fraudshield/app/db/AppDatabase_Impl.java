package com.fraudshield.app.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile SmsLogDao _smsLogDao;

  private volatile UrlCacheDao _urlCacheDao;

  private volatile SenderAllowlistDao _senderAllowlistDao;

  private volatile PatternDao _patternDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `sms_log` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `senderId` TEXT NOT NULL, `smsBody` TEXT NOT NULL, `extractedUrl` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `localVerdict` TEXT NOT NULL, `syncedToServer` INTEGER NOT NULL, `jobId` TEXT, `serverVerdict` TEXT, `verdictSource` TEXT NOT NULL, `userFeedback` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `url_cache` (`url` TEXT NOT NULL, `domain` TEXT NOT NULL, `status` TEXT NOT NULL, `confidence` REAL NOT NULL, `category` TEXT, `lastSynced` INTEGER NOT NULL, PRIMARY KEY(`url`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sender_allowlist` (`senderId` TEXT NOT NULL, `organisation` TEXT NOT NULL, `category` TEXT NOT NULL, `verified` INTEGER NOT NULL, `associatedDomains` TEXT NOT NULL, PRIMARY KEY(`senderId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pattern_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pattern` TEXT NOT NULL, `patternType` TEXT NOT NULL, `riskScore` REAL NOT NULL, `category` TEXT NOT NULL, `language` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a4286877c8a14509937dbd37a4f0dd44')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `sms_log`");
        db.execSQL("DROP TABLE IF EXISTS `url_cache`");
        db.execSQL("DROP TABLE IF EXISTS `sender_allowlist`");
        db.execSQL("DROP TABLE IF EXISTS `pattern_table`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSmsLog = new HashMap<String, TableInfo.Column>(11);
        _columnsSmsLog.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("senderId", new TableInfo.Column("senderId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("smsBody", new TableInfo.Column("smsBody", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("extractedUrl", new TableInfo.Column("extractedUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("localVerdict", new TableInfo.Column("localVerdict", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("syncedToServer", new TableInfo.Column("syncedToServer", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("jobId", new TableInfo.Column("jobId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("serverVerdict", new TableInfo.Column("serverVerdict", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("verdictSource", new TableInfo.Column("verdictSource", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsLog.put("userFeedback", new TableInfo.Column("userFeedback", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSmsLog = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSmsLog = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSmsLog = new TableInfo("sms_log", _columnsSmsLog, _foreignKeysSmsLog, _indicesSmsLog);
        final TableInfo _existingSmsLog = TableInfo.read(db, "sms_log");
        if (!_infoSmsLog.equals(_existingSmsLog)) {
          return new RoomOpenHelper.ValidationResult(false, "sms_log(com.fraudshield.app.db.SmsLogEntry).\n"
                  + " Expected:\n" + _infoSmsLog + "\n"
                  + " Found:\n" + _existingSmsLog);
        }
        final HashMap<String, TableInfo.Column> _columnsUrlCache = new HashMap<String, TableInfo.Column>(6);
        _columnsUrlCache.put("url", new TableInfo.Column("url", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUrlCache.put("domain", new TableInfo.Column("domain", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUrlCache.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUrlCache.put("confidence", new TableInfo.Column("confidence", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUrlCache.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUrlCache.put("lastSynced", new TableInfo.Column("lastSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUrlCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUrlCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUrlCache = new TableInfo("url_cache", _columnsUrlCache, _foreignKeysUrlCache, _indicesUrlCache);
        final TableInfo _existingUrlCache = TableInfo.read(db, "url_cache");
        if (!_infoUrlCache.equals(_existingUrlCache)) {
          return new RoomOpenHelper.ValidationResult(false, "url_cache(com.fraudshield.app.db.UrlCacheEntry).\n"
                  + " Expected:\n" + _infoUrlCache + "\n"
                  + " Found:\n" + _existingUrlCache);
        }
        final HashMap<String, TableInfo.Column> _columnsSenderAllowlist = new HashMap<String, TableInfo.Column>(5);
        _columnsSenderAllowlist.put("senderId", new TableInfo.Column("senderId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSenderAllowlist.put("organisation", new TableInfo.Column("organisation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSenderAllowlist.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSenderAllowlist.put("verified", new TableInfo.Column("verified", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSenderAllowlist.put("associatedDomains", new TableInfo.Column("associatedDomains", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSenderAllowlist = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSenderAllowlist = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSenderAllowlist = new TableInfo("sender_allowlist", _columnsSenderAllowlist, _foreignKeysSenderAllowlist, _indicesSenderAllowlist);
        final TableInfo _existingSenderAllowlist = TableInfo.read(db, "sender_allowlist");
        if (!_infoSenderAllowlist.equals(_existingSenderAllowlist)) {
          return new RoomOpenHelper.ValidationResult(false, "sender_allowlist(com.fraudshield.app.db.SenderAllowlistEntry).\n"
                  + " Expected:\n" + _infoSenderAllowlist + "\n"
                  + " Found:\n" + _existingSenderAllowlist);
        }
        final HashMap<String, TableInfo.Column> _columnsPatternTable = new HashMap<String, TableInfo.Column>(6);
        _columnsPatternTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternTable.put("pattern", new TableInfo.Column("pattern", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternTable.put("patternType", new TableInfo.Column("patternType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternTable.put("riskScore", new TableInfo.Column("riskScore", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternTable.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternTable.put("language", new TableInfo.Column("language", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPatternTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPatternTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPatternTable = new TableInfo("pattern_table", _columnsPatternTable, _foreignKeysPatternTable, _indicesPatternTable);
        final TableInfo _existingPatternTable = TableInfo.read(db, "pattern_table");
        if (!_infoPatternTable.equals(_existingPatternTable)) {
          return new RoomOpenHelper.ValidationResult(false, "pattern_table(com.fraudshield.app.db.PatternEntry).\n"
                  + " Expected:\n" + _infoPatternTable + "\n"
                  + " Found:\n" + _existingPatternTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a4286877c8a14509937dbd37a4f0dd44", "5200eeb5403c3210363b65085ee8856f");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "sms_log","url_cache","sender_allowlist","pattern_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `sms_log`");
      _db.execSQL("DELETE FROM `url_cache`");
      _db.execSQL("DELETE FROM `sender_allowlist`");
      _db.execSQL("DELETE FROM `pattern_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SmsLogDao.class, SmsLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UrlCacheDao.class, UrlCacheDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SenderAllowlistDao.class, SenderAllowlistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PatternDao.class, PatternDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SmsLogDao smsLogDao() {
    if (_smsLogDao != null) {
      return _smsLogDao;
    } else {
      synchronized(this) {
        if(_smsLogDao == null) {
          _smsLogDao = new SmsLogDao_Impl(this);
        }
        return _smsLogDao;
      }
    }
  }

  @Override
  public UrlCacheDao urlCacheDao() {
    if (_urlCacheDao != null) {
      return _urlCacheDao;
    } else {
      synchronized(this) {
        if(_urlCacheDao == null) {
          _urlCacheDao = new UrlCacheDao_Impl(this);
        }
        return _urlCacheDao;
      }
    }
  }

  @Override
  public SenderAllowlistDao senderAllowlistDao() {
    if (_senderAllowlistDao != null) {
      return _senderAllowlistDao;
    } else {
      synchronized(this) {
        if(_senderAllowlistDao == null) {
          _senderAllowlistDao = new SenderAllowlistDao_Impl(this);
        }
        return _senderAllowlistDao;
      }
    }
  }

  @Override
  public PatternDao patternDao() {
    if (_patternDao != null) {
      return _patternDao;
    } else {
      synchronized(this) {
        if(_patternDao == null) {
          _patternDao = new PatternDao_Impl(this);
        }
        return _patternDao;
      }
    }
  }
}
