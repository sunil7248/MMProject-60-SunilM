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
import com.gramaurja.data.local.entity.ReportEntity;
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
public final class ReportDao_Impl implements ReportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReportEntity> __insertionAdapterOfReportEntity;

  public ReportDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReportEntity = new EntityInsertionAdapter<ReportEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `reports` (`id`,`issueType`,`description`,`imageUri`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReportEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getIssueType());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getImageUri());
        statement.bindLong(5, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insertReport(final ReportEntity report,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReportEntity.insert(report);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReportEntity>> observeReports() {
    final String _sql = "SELECT * FROM reports ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reports"}, new Callable<List<ReportEntity>>() {
      @Override
      @NonNull
      public List<ReportEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIssueType = CursorUtil.getColumnIndexOrThrow(_cursor, "issueType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<ReportEntity> _result = new ArrayList<ReportEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReportEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpIssueType;
            _tmpIssueType = _cursor.getString(_cursorIndexOfIssueType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new ReportEntity(_tmpId,_tmpIssueType,_tmpDescription,_tmpImageUri,_tmpTimestamp);
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
