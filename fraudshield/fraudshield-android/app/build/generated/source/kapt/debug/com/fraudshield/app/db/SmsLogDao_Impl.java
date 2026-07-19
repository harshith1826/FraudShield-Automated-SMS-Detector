package com.fraudshield.app.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SmsLogDao_Impl implements SmsLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SmsLogEntry> __insertionAdapterOfSmsLogEntry;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfUpdateServerVerdict;

  private final SharedSQLiteStatement __preparedStmtOfSetUserFeedback;

  public SmsLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSmsLogEntry = new EntityInsertionAdapter<SmsLogEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sms_log` (`id`,`senderId`,`smsBody`,`extractedUrl`,`timestamp`,`localVerdict`,`syncedToServer`,`jobId`,`serverVerdict`,`verdictSource`,`userFeedback`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SmsLogEntry entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getSenderId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getSenderId());
        }
        if (entity.getSmsBody() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSmsBody());
        }
        if (entity.getExtractedUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getExtractedUrl());
        }
        statement.bindLong(5, entity.getTimestamp());
        if (entity.getLocalVerdict() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLocalVerdict());
        }
        final int _tmp = entity.getSyncedToServer() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getJobId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getJobId());
        }
        if (entity.getServerVerdict() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getServerVerdict());
        }
        if (entity.getVerdictSource() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getVerdictSource());
        }
        if (entity.getUserFeedback() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getUserFeedback());
        }
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sms_log SET syncedToServer = 1, jobId = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateServerVerdict = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sms_log SET serverVerdict = ?, verdictSource = 'AI_ML' WHERE jobId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUserFeedback = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sms_log SET userFeedback = ? WHERE extractedUrl = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SmsLogEntry entry, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSmsLogEntry.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object markSynced(final int id, final String jobId,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSynced.acquire();
        int _argIndex = 1;
        if (jobId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, jobId);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkSynced.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object updateServerVerdict(final String jobId, final String verdict,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateServerVerdict.acquire();
        int _argIndex = 1;
        if (verdict == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, verdict);
        }
        _argIndex = 2;
        if (jobId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, jobId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateServerVerdict.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object setUserFeedback(final String url, final int feedback,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetUserFeedback.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, feedback);
        _argIndex = 2;
        if (url == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, url);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetUserFeedback.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object getUnsynced(final Continuation<? super List<SmsLogEntry>> arg0) {
    final String _sql = "SELECT * FROM sms_log WHERE syncedToServer = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SmsLogEntry>>() {
      @Override
      @NonNull
      public List<SmsLogEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSmsBody = CursorUtil.getColumnIndexOrThrow(_cursor, "smsBody");
          final int _cursorIndexOfExtractedUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedUrl");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLocalVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "localVerdict");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final int _cursorIndexOfJobId = CursorUtil.getColumnIndexOrThrow(_cursor, "jobId");
          final int _cursorIndexOfServerVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "serverVerdict");
          final int _cursorIndexOfVerdictSource = CursorUtil.getColumnIndexOrThrow(_cursor, "verdictSource");
          final int _cursorIndexOfUserFeedback = CursorUtil.getColumnIndexOrThrow(_cursor, "userFeedback");
          final List<SmsLogEntry> _result = new ArrayList<SmsLogEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmsLogEntry _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSenderId;
            if (_cursor.isNull(_cursorIndexOfSenderId)) {
              _tmpSenderId = null;
            } else {
              _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            }
            final String _tmpSmsBody;
            if (_cursor.isNull(_cursorIndexOfSmsBody)) {
              _tmpSmsBody = null;
            } else {
              _tmpSmsBody = _cursor.getString(_cursorIndexOfSmsBody);
            }
            final String _tmpExtractedUrl;
            if (_cursor.isNull(_cursorIndexOfExtractedUrl)) {
              _tmpExtractedUrl = null;
            } else {
              _tmpExtractedUrl = _cursor.getString(_cursorIndexOfExtractedUrl);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpLocalVerdict;
            if (_cursor.isNull(_cursorIndexOfLocalVerdict)) {
              _tmpLocalVerdict = null;
            } else {
              _tmpLocalVerdict = _cursor.getString(_cursorIndexOfLocalVerdict);
            }
            final boolean _tmpSyncedToServer;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _tmpSyncedToServer = _tmp != 0;
            final String _tmpJobId;
            if (_cursor.isNull(_cursorIndexOfJobId)) {
              _tmpJobId = null;
            } else {
              _tmpJobId = _cursor.getString(_cursorIndexOfJobId);
            }
            final String _tmpServerVerdict;
            if (_cursor.isNull(_cursorIndexOfServerVerdict)) {
              _tmpServerVerdict = null;
            } else {
              _tmpServerVerdict = _cursor.getString(_cursorIndexOfServerVerdict);
            }
            final String _tmpVerdictSource;
            if (_cursor.isNull(_cursorIndexOfVerdictSource)) {
              _tmpVerdictSource = null;
            } else {
              _tmpVerdictSource = _cursor.getString(_cursorIndexOfVerdictSource);
            }
            final Integer _tmpUserFeedback;
            if (_cursor.isNull(_cursorIndexOfUserFeedback)) {
              _tmpUserFeedback = null;
            } else {
              _tmpUserFeedback = _cursor.getInt(_cursorIndexOfUserFeedback);
            }
            _item = new SmsLogEntry(_tmpId,_tmpSenderId,_tmpSmsBody,_tmpExtractedUrl,_tmpTimestamp,_tmpLocalVerdict,_tmpSyncedToServer,_tmpJobId,_tmpServerVerdict,_tmpVerdictSource,_tmpUserFeedback);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getRecent(final Continuation<? super List<SmsLogEntry>> arg0) {
    final String _sql = "SELECT * FROM sms_log ORDER BY id DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SmsLogEntry>>() {
      @Override
      @NonNull
      public List<SmsLogEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSmsBody = CursorUtil.getColumnIndexOrThrow(_cursor, "smsBody");
          final int _cursorIndexOfExtractedUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedUrl");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLocalVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "localVerdict");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final int _cursorIndexOfJobId = CursorUtil.getColumnIndexOrThrow(_cursor, "jobId");
          final int _cursorIndexOfServerVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "serverVerdict");
          final int _cursorIndexOfVerdictSource = CursorUtil.getColumnIndexOrThrow(_cursor, "verdictSource");
          final int _cursorIndexOfUserFeedback = CursorUtil.getColumnIndexOrThrow(_cursor, "userFeedback");
          final List<SmsLogEntry> _result = new ArrayList<SmsLogEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmsLogEntry _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSenderId;
            if (_cursor.isNull(_cursorIndexOfSenderId)) {
              _tmpSenderId = null;
            } else {
              _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            }
            final String _tmpSmsBody;
            if (_cursor.isNull(_cursorIndexOfSmsBody)) {
              _tmpSmsBody = null;
            } else {
              _tmpSmsBody = _cursor.getString(_cursorIndexOfSmsBody);
            }
            final String _tmpExtractedUrl;
            if (_cursor.isNull(_cursorIndexOfExtractedUrl)) {
              _tmpExtractedUrl = null;
            } else {
              _tmpExtractedUrl = _cursor.getString(_cursorIndexOfExtractedUrl);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpLocalVerdict;
            if (_cursor.isNull(_cursorIndexOfLocalVerdict)) {
              _tmpLocalVerdict = null;
            } else {
              _tmpLocalVerdict = _cursor.getString(_cursorIndexOfLocalVerdict);
            }
            final boolean _tmpSyncedToServer;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _tmpSyncedToServer = _tmp != 0;
            final String _tmpJobId;
            if (_cursor.isNull(_cursorIndexOfJobId)) {
              _tmpJobId = null;
            } else {
              _tmpJobId = _cursor.getString(_cursorIndexOfJobId);
            }
            final String _tmpServerVerdict;
            if (_cursor.isNull(_cursorIndexOfServerVerdict)) {
              _tmpServerVerdict = null;
            } else {
              _tmpServerVerdict = _cursor.getString(_cursorIndexOfServerVerdict);
            }
            final String _tmpVerdictSource;
            if (_cursor.isNull(_cursorIndexOfVerdictSource)) {
              _tmpVerdictSource = null;
            } else {
              _tmpVerdictSource = _cursor.getString(_cursorIndexOfVerdictSource);
            }
            final Integer _tmpUserFeedback;
            if (_cursor.isNull(_cursorIndexOfUserFeedback)) {
              _tmpUserFeedback = null;
            } else {
              _tmpUserFeedback = _cursor.getInt(_cursorIndexOfUserFeedback);
            }
            _item = new SmsLogEntry(_tmpId,_tmpSenderId,_tmpSmsBody,_tmpExtractedUrl,_tmpTimestamp,_tmpLocalVerdict,_tmpSyncedToServer,_tmpJobId,_tmpServerVerdict,_tmpVerdictSource,_tmpUserFeedback);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public LiveData<List<SmsLogEntry>> getAllLive() {
    final String _sql = "SELECT * FROM sms_log ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"sms_log"}, false, new Callable<List<SmsLogEntry>>() {
      @Override
      @Nullable
      public List<SmsLogEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSmsBody = CursorUtil.getColumnIndexOrThrow(_cursor, "smsBody");
          final int _cursorIndexOfExtractedUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedUrl");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLocalVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "localVerdict");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final int _cursorIndexOfJobId = CursorUtil.getColumnIndexOrThrow(_cursor, "jobId");
          final int _cursorIndexOfServerVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "serverVerdict");
          final int _cursorIndexOfVerdictSource = CursorUtil.getColumnIndexOrThrow(_cursor, "verdictSource");
          final int _cursorIndexOfUserFeedback = CursorUtil.getColumnIndexOrThrow(_cursor, "userFeedback");
          final List<SmsLogEntry> _result = new ArrayList<SmsLogEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmsLogEntry _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSenderId;
            if (_cursor.isNull(_cursorIndexOfSenderId)) {
              _tmpSenderId = null;
            } else {
              _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            }
            final String _tmpSmsBody;
            if (_cursor.isNull(_cursorIndexOfSmsBody)) {
              _tmpSmsBody = null;
            } else {
              _tmpSmsBody = _cursor.getString(_cursorIndexOfSmsBody);
            }
            final String _tmpExtractedUrl;
            if (_cursor.isNull(_cursorIndexOfExtractedUrl)) {
              _tmpExtractedUrl = null;
            } else {
              _tmpExtractedUrl = _cursor.getString(_cursorIndexOfExtractedUrl);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpLocalVerdict;
            if (_cursor.isNull(_cursorIndexOfLocalVerdict)) {
              _tmpLocalVerdict = null;
            } else {
              _tmpLocalVerdict = _cursor.getString(_cursorIndexOfLocalVerdict);
            }
            final boolean _tmpSyncedToServer;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _tmpSyncedToServer = _tmp != 0;
            final String _tmpJobId;
            if (_cursor.isNull(_cursorIndexOfJobId)) {
              _tmpJobId = null;
            } else {
              _tmpJobId = _cursor.getString(_cursorIndexOfJobId);
            }
            final String _tmpServerVerdict;
            if (_cursor.isNull(_cursorIndexOfServerVerdict)) {
              _tmpServerVerdict = null;
            } else {
              _tmpServerVerdict = _cursor.getString(_cursorIndexOfServerVerdict);
            }
            final String _tmpVerdictSource;
            if (_cursor.isNull(_cursorIndexOfVerdictSource)) {
              _tmpVerdictSource = null;
            } else {
              _tmpVerdictSource = _cursor.getString(_cursorIndexOfVerdictSource);
            }
            final Integer _tmpUserFeedback;
            if (_cursor.isNull(_cursorIndexOfUserFeedback)) {
              _tmpUserFeedback = null;
            } else {
              _tmpUserFeedback = _cursor.getInt(_cursorIndexOfUserFeedback);
            }
            _item = new SmsLogEntry(_tmpId,_tmpSenderId,_tmpSmsBody,_tmpExtractedUrl,_tmpTimestamp,_tmpLocalVerdict,_tmpSyncedToServer,_tmpJobId,_tmpServerVerdict,_tmpVerdictSource,_tmpUserFeedback);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
