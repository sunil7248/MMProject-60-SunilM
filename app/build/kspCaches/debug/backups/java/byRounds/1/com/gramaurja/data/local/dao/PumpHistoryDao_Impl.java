package com.gramaurja.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gramaurja.data.local.entity.PumpHistoryEntity;
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
public final class PumpHistoryDao_Impl implements PumpHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PumpHistoryEntity> __insertionAdapterOfPumpHistoryEntity;

  public PumpHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPumpHistoryEntity = new EntityInsertionAdapter<PumpHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `pump_history` (`id`,`cropType`,`duration`,`timestamp`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PumpHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCropType());
        statement.bindString(3, entity.getDuration());
        statement.bindLong(4, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insertPumpHistory(final PumpHistoryEntity history,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPumpHistoryEntity.insert(history);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PumpHistoryEntity>> observePumpHistory() {
    final String _sql = "SELECT * FROM pump_history ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pump_history"}, new Callable<List<PumpHistoryEntity>>() {
      @Override
      @NonNull
      public List<PumpHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCropType = CursorUtil.getColumnIndexOrThrow(_cursor, "cropType");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PumpHistoryEntity> _result = new ArrayList<PumpHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PumpHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCropType;
            _tmpCropType = _cursor.getString(_cursorIndexOfCropType);
            final String _tmpDuration;
            _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PumpHistoryEntity(_tmpId,_tmpCropType,_tmpDuration,_tmpTimestamp);
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
