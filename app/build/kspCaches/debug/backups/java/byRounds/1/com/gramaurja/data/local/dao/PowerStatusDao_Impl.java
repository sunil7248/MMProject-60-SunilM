package com.gramaurja.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gramaurja.data.local.entity.PowerStatusEntity;
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
public final class PowerStatusDao_Impl implements PowerStatusDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PowerStatusEntity> __insertionAdapterOfPowerStatusEntity;

  public PowerStatusDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPowerStatusEntity = new EntityInsertionAdapter<PowerStatusEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `power_status` (`id`,`status`,`timestamp`,`zone`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PowerStatusEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getStatus());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindString(4, entity.getZone());
      }
    };
  }

  @Override
  public Object insertStatus(final PowerStatusEntity status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPowerStatusEntity.insert(status);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<PowerStatusEntity> observeLatestStatus(final String zone) {
    final String _sql = "SELECT * FROM power_status WHERE zone = ? ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, zone);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"power_status"}, new Callable<PowerStatusEntity>() {
      @Override
      @Nullable
      public PowerStatusEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
          final PowerStatusEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpZone;
            _tmpZone = _cursor.getString(_cursorIndexOfZone);
            _result = new PowerStatusEntity(_tmpId,_tmpStatus,_tmpTimestamp,_tmpZone);
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
  public Flow<List<PowerStatusEntity>> observeRecentStatuses(final String zone, final int limit) {
    final String _sql = "SELECT * FROM power_status WHERE zone = ? ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, zone);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"power_status"}, new Callable<List<PowerStatusEntity>>() {
      @Override
      @NonNull
      public List<PowerStatusEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
          final List<PowerStatusEntity> _result = new ArrayList<PowerStatusEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PowerStatusEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpZone;
            _tmpZone = _cursor.getString(_cursorIndexOfZone);
            _item = new PowerStatusEntity(_tmpId,_tmpStatus,_tmpTimestamp,_tmpZone);
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

  @Override
  public Flow<List<PowerStatusEntity>> observeAllStatuses(final String zone) {
    final String _sql = "SELECT * FROM power_status WHERE zone = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, zone);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"power_status"}, new Callable<List<PowerStatusEntity>>() {
      @Override
      @NonNull
      public List<PowerStatusEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
          final List<PowerStatusEntity> _result = new ArrayList<PowerStatusEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PowerStatusEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpZone;
            _tmpZone = _cursor.getString(_cursorIndexOfZone);
            _item = new PowerStatusEntity(_tmpId,_tmpStatus,_tmpTimestamp,_tmpZone);
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

  @Override
  public Flow<List<PowerStatusEntity>> observeLatestStatusesAcrossZones() {
    final String _sql = "\n"
            + "        SELECT * FROM power_status\n"
            + "        WHERE id IN (\n"
            + "            SELECT MAX(latest.id)\n"
            + "            FROM power_status AS latest\n"
            + "            GROUP BY latest.zone\n"
            + "        )\n"
            + "        ORDER BY zone ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"power_status"}, new Callable<List<PowerStatusEntity>>() {
      @Override
      @NonNull
      public List<PowerStatusEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
          final List<PowerStatusEntity> _result = new ArrayList<PowerStatusEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PowerStatusEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpZone;
            _tmpZone = _cursor.getString(_cursorIndexOfZone);
            _item = new PowerStatusEntity(_tmpId,_tmpStatus,_tmpTimestamp,_tmpZone);
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
