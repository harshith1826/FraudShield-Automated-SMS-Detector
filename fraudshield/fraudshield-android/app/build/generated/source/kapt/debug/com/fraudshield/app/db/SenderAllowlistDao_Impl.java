package com.fraudshield.app.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SenderAllowlistDao_Impl implements SenderAllowlistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SenderAllowlistEntry> __insertionAdapterOfSenderAllowlistEntry;

  public SenderAllowlistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSenderAllowlistEntry = new EntityInsertionAdapter<SenderAllowlistEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sender_allowlist` (`senderId`,`organisation`,`category`,`verified`,`associatedDomains`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SenderAllowlistEntry entity) {
        if (entity.getSenderId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getSenderId());
        }
        if (entity.getOrganisation() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getOrganisation());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCategory());
        }
        final int _tmp = entity.getVerified() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getAssociatedDomains() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAssociatedDomains());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<SenderAllowlistEntry> entries,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSenderAllowlistEntry.insert(entries);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object find(final String senderId, final Continuation<? super SenderAllowlistEntry> arg1) {
    final String _sql = "SELECT * FROM sender_allowlist WHERE senderId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (senderId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, senderId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SenderAllowlistEntry>() {
      @Override
      @Nullable
      public SenderAllowlistEntry call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfOrganisation = CursorUtil.getColumnIndexOrThrow(_cursor, "organisation");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "verified");
          final int _cursorIndexOfAssociatedDomains = CursorUtil.getColumnIndexOrThrow(_cursor, "associatedDomains");
          final SenderAllowlistEntry _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSenderId;
            if (_cursor.isNull(_cursorIndexOfSenderId)) {
              _tmpSenderId = null;
            } else {
              _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            }
            final String _tmpOrganisation;
            if (_cursor.isNull(_cursorIndexOfOrganisation)) {
              _tmpOrganisation = null;
            } else {
              _tmpOrganisation = _cursor.getString(_cursorIndexOfOrganisation);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVerified);
            _tmpVerified = _tmp != 0;
            final String _tmpAssociatedDomains;
            if (_cursor.isNull(_cursorIndexOfAssociatedDomains)) {
              _tmpAssociatedDomains = null;
            } else {
              _tmpAssociatedDomains = _cursor.getString(_cursorIndexOfAssociatedDomains);
            }
            _result = new SenderAllowlistEntry(_tmpSenderId,_tmpOrganisation,_tmpCategory,_tmpVerified,_tmpAssociatedDomains);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
