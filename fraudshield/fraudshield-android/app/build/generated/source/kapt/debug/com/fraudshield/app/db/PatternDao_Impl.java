package com.fraudshield.app.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class PatternDao_Impl implements PatternDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PatternEntry> __insertionAdapterOfPatternEntry;

  public PatternDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPatternEntry = new EntityInsertionAdapter<PatternEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `pattern_table` (`id`,`pattern`,`patternType`,`riskScore`,`category`,`language`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PatternEntry entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPattern() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPattern());
        }
        if (entity.getPatternType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPatternType());
        }
        statement.bindDouble(4, entity.getRiskScore());
        if (entity.getCategory() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCategory());
        }
        if (entity.getLanguage() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLanguage());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<PatternEntry> entries, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPatternEntry.insert(entries);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object getAll(final Continuation<? super List<PatternEntry>> arg0) {
    final String _sql = "SELECT * FROM pattern_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatternEntry>>() {
      @Override
      @NonNull
      public List<PatternEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "pattern");
          final int _cursorIndexOfPatternType = CursorUtil.getColumnIndexOrThrow(_cursor, "patternType");
          final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final List<PatternEntry> _result = new ArrayList<PatternEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatternEntry _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPattern;
            if (_cursor.isNull(_cursorIndexOfPattern)) {
              _tmpPattern = null;
            } else {
              _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
            }
            final String _tmpPatternType;
            if (_cursor.isNull(_cursorIndexOfPatternType)) {
              _tmpPatternType = null;
            } else {
              _tmpPatternType = _cursor.getString(_cursorIndexOfPatternType);
            }
            final float _tmpRiskScore;
            _tmpRiskScore = _cursor.getFloat(_cursorIndexOfRiskScore);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            _item = new PatternEntry(_tmpId,_tmpPattern,_tmpPatternType,_tmpRiskScore,_tmpCategory,_tmpLanguage);
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
  public Object count(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM pattern_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
