package com.gramaurja.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gramaurja.data.local.entity.ZoneSelectionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ZoneSelectionDao_Impl implements ZoneSelectionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ZoneSelectionEntity> __insertionAdapterOfZoneSelectionEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearActiveSelections;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public ZoneSelectionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfZoneSelectionEntity = new EntityInsertionAdapter<ZoneSelectionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `zone_selections` (`id`,`district`,`village`,`zone`,`selectedAt`,`isActive`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ZoneSelectionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDistrict());
        statement.bindString(3, entity.getVillage());
        statement.bindString(4, entity.getZone());
        statement.bindLong(5, entity.getSelectedAt());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(6, _tmp);
      }
    };
    this.__preparedStmtOfClearActiveSelections = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE zone_selections SET isActive = 0";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM zone_selections";
        return _query;
      }
    };
  }

  @Override
  public Object insertSelection(final ZoneSelectionEntity selection,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfZoneSelectionEntity.insert(selection);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearActiveSelections(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearActiveSelections.acquire();
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
          __preparedStmtOfClearActiveSelections.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
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
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ZoneSelectionEntity> observeCurrentZone() {
    final String _sql = "SELECT * FROM zone_selections WHERE isActive = 1 ORDER BY selectedAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"zone_selections"}, new Callable<ZoneSelectionEntity>() {
      @Override
      @Nullable
      public ZoneSelectionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDistrict = CursorUtil.getColumnIndexOrThrow(_cursor, "district");
          final int _cursorIndexOfVillage = CursorUtil.getColumnIndexOrThrow(_cursor, "village");
          final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
          final int _cursorIndexOfSelectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedAt");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final ZoneSelectionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDistrict;
            _tmpDistrict = _cursor.getString(_cursorIndexOfDistrict);
            final String _tmpVillage;
            _tmpVillage = _cursor.getString(_cursorIndexOfVillage);
            final String _tmpZone;
            _tmpZone = _cursor.getString(_cursorIndexOfZone);
            final long _tmpSelectedAt;
            _tmpSelectedAt = _cursor.getLong(_cursorIndexOfSelectedAt);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new ZoneSelectionEntity(_tmpId,_tmpDistrict,_tmpVillage,_tmpZone,_tmpSelectedAt,_tmpIsActive);
          } else {
            _result = null;
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

  @Override
  public Flow<List<ZoneSelectionEntity>> observeZoneHistory() {
    final String _sql = "SELECT * FROM zone_selections ORDER BY selectedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"zone_selections"}, new Callable<List<ZoneSelectionEntity>>() {
      @Override
      @NonNull
      public List<ZoneSelectionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDistrict = CursorUtil.getColumnIndexOrThrow(_cursor, "district");
          final int _cursorIndexOfVillage = CursorUtil.getColumnIndexOrThrow(_cursor, "village");
          final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
          final int _cursorIndexOfSelectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedAt");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<ZoneSelectionEntity> _result = new ArrayList<ZoneSelectionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ZoneSelectionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDistrict;
            _tmpDistrict = _cursor.getString(_cursorIndexOfDistrict);
            final String _tmpVillage;
            _tmpVillage = _cursor.getString(_cursorIndexOfVillage);
            final String _tmpZone;
            _tmpZone = _cursor.getString(_cursorIndexOfZone);
            final long _tmpSelectedAt;
            _tmpSelectedAt = _cursor.getLong(_cursorIndexOfSelectedAt);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new ZoneSelectionEntity(_tmpId,_tmpDistrict,_tmpVillage,_tmpZone,_tmpSelectedAt,_tmpIsActive);
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
