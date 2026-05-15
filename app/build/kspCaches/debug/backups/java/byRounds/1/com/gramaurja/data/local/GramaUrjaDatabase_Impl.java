package com.gramaurja.data.local;

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
import com.gramaurja.data.local.dao.AlertDao;
import com.gramaurja.data.local.dao.AlertDao_Impl;
import com.gramaurja.data.local.dao.PowerStatusDao;
import com.gramaurja.data.local.dao.PowerStatusDao_Impl;
import com.gramaurja.data.local.dao.PumpHistoryDao;
import com.gramaurja.data.local.dao.PumpHistoryDao_Impl;
import com.gramaurja.data.local.dao.ReportDao;
import com.gramaurja.data.local.dao.ReportDao_Impl;
import com.gramaurja.data.local.dao.UserDao;
import com.gramaurja.data.local.dao.UserDao_Impl;
import com.gramaurja.data.local.dao.ZoneSelectionDao;
import com.gramaurja.data.local.dao.ZoneSelectionDao_Impl;
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
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GramaUrjaDatabase_Impl extends GramaUrjaDatabase {
  private volatile UserDao _userDao;

  private volatile PowerStatusDao _powerStatusDao;

  private volatile ReportDao _reportDao;

  private volatile PumpHistoryDao _pumpHistoryDao;

  private volatile AlertDao _alertDao;

  private volatile ZoneSelectionDao _zoneSelectionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `phone` TEXT NOT NULL, `village` TEXT NOT NULL, `zone` TEXT NOT NULL, `password` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `power_status` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `status` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `zone` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reports` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `issueType` TEXT NOT NULL, `description` TEXT NOT NULL, `imageUri` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pump_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cropType` TEXT NOT NULL, `duration` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `alerts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `message` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `zone_selections` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `district` TEXT NOT NULL, `village` TEXT NOT NULL, `zone` TEXT NOT NULL, `selectedAt` INTEGER NOT NULL, `isActive` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'eec87478286c519c50bfa1f0fedec6b2')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `power_status`");
        db.execSQL("DROP TABLE IF EXISTS `reports`");
        db.execSQL("DROP TABLE IF EXISTS `pump_history`");
        db.execSQL("DROP TABLE IF EXISTS `alerts`");
        db.execSQL("DROP TABLE IF EXISTS `zone_selections`");
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
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(6);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("village", new TableInfo.Column("village", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("zone", new TableInfo.Column("zone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.gramaurja.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsPowerStatus = new HashMap<String, TableInfo.Column>(4);
        _columnsPowerStatus.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPowerStatus.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPowerStatus.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPowerStatus.put("zone", new TableInfo.Column("zone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPowerStatus = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPowerStatus = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPowerStatus = new TableInfo("power_status", _columnsPowerStatus, _foreignKeysPowerStatus, _indicesPowerStatus);
        final TableInfo _existingPowerStatus = TableInfo.read(db, "power_status");
        if (!_infoPowerStatus.equals(_existingPowerStatus)) {
          return new RoomOpenHelper.ValidationResult(false, "power_status(com.gramaurja.data.local.entity.PowerStatusEntity).\n"
                  + " Expected:\n" + _infoPowerStatus + "\n"
                  + " Found:\n" + _existingPowerStatus);
        }
        final HashMap<String, TableInfo.Column> _columnsReports = new HashMap<String, TableInfo.Column>(5);
        _columnsReports.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReports.put("issueType", new TableInfo.Column("issueType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReports.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReports.put("imageUri", new TableInfo.Column("imageUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReports.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReports = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReports = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReports = new TableInfo("reports", _columnsReports, _foreignKeysReports, _indicesReports);
        final TableInfo _existingReports = TableInfo.read(db, "reports");
        if (!_infoReports.equals(_existingReports)) {
          return new RoomOpenHelper.ValidationResult(false, "reports(com.gramaurja.data.local.entity.ReportEntity).\n"
                  + " Expected:\n" + _infoReports + "\n"
                  + " Found:\n" + _existingReports);
        }
        final HashMap<String, TableInfo.Column> _columnsPumpHistory = new HashMap<String, TableInfo.Column>(4);
        _columnsPumpHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPumpHistory.put("cropType", new TableInfo.Column("cropType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPumpHistory.put("duration", new TableInfo.Column("duration", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPumpHistory.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPumpHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPumpHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPumpHistory = new TableInfo("pump_history", _columnsPumpHistory, _foreignKeysPumpHistory, _indicesPumpHistory);
        final TableInfo _existingPumpHistory = TableInfo.read(db, "pump_history");
        if (!_infoPumpHistory.equals(_existingPumpHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "pump_history(com.gramaurja.data.local.entity.PumpHistoryEntity).\n"
                  + " Expected:\n" + _infoPumpHistory + "\n"
                  + " Found:\n" + _existingPumpHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsAlerts = new HashMap<String, TableInfo.Column>(3);
        _columnsAlerts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlerts.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlerts.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAlerts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAlerts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAlerts = new TableInfo("alerts", _columnsAlerts, _foreignKeysAlerts, _indicesAlerts);
        final TableInfo _existingAlerts = TableInfo.read(db, "alerts");
        if (!_infoAlerts.equals(_existingAlerts)) {
          return new RoomOpenHelper.ValidationResult(false, "alerts(com.gramaurja.data.local.entity.AlertEntity).\n"
                  + " Expected:\n" + _infoAlerts + "\n"
                  + " Found:\n" + _existingAlerts);
        }
        final HashMap<String, TableInfo.Column> _columnsZoneSelections = new HashMap<String, TableInfo.Column>(6);
        _columnsZoneSelections.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsZoneSelections.put("district", new TableInfo.Column("district", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsZoneSelections.put("village", new TableInfo.Column("village", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsZoneSelections.put("zone", new TableInfo.Column("zone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsZoneSelections.put("selectedAt", new TableInfo.Column("selectedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsZoneSelections.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysZoneSelections = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesZoneSelections = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoZoneSelections = new TableInfo("zone_selections", _columnsZoneSelections, _foreignKeysZoneSelections, _indicesZoneSelections);
        final TableInfo _existingZoneSelections = TableInfo.read(db, "zone_selections");
        if (!_infoZoneSelections.equals(_existingZoneSelections)) {
          return new RoomOpenHelper.ValidationResult(false, "zone_selections(com.gramaurja.data.local.entity.ZoneSelectionEntity).\n"
                  + " Expected:\n" + _infoZoneSelections + "\n"
                  + " Found:\n" + _existingZoneSelections);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "eec87478286c519c50bfa1f0fedec6b2", "6e77df6b617274130a0ca4e0eae7452a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","power_status","reports","pump_history","alerts","zone_selections");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `power_status`");
      _db.execSQL("DELETE FROM `reports`");
      _db.execSQL("DELETE FROM `pump_history`");
      _db.execSQL("DELETE FROM `alerts`");
      _db.execSQL("DELETE FROM `zone_selections`");
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
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PowerStatusDao.class, PowerStatusDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReportDao.class, ReportDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PumpHistoryDao.class, PumpHistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AlertDao.class, AlertDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ZoneSelectionDao.class, ZoneSelectionDao_Impl.getRequiredConverters());
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
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public PowerStatusDao powerStatusDao() {
    if (_powerStatusDao != null) {
      return _powerStatusDao;
    } else {
      synchronized(this) {
        if(_powerStatusDao == null) {
          _powerStatusDao = new PowerStatusDao_Impl(this);
        }
        return _powerStatusDao;
      }
    }
  }

  @Override
  public ReportDao reportDao() {
    if (_reportDao != null) {
      return _reportDao;
    } else {
      synchronized(this) {
        if(_reportDao == null) {
          _reportDao = new ReportDao_Impl(this);
        }
        return _reportDao;
      }
    }
  }

  @Override
  public PumpHistoryDao pumpHistoryDao() {
    if (_pumpHistoryDao != null) {
      return _pumpHistoryDao;
    } else {
      synchronized(this) {
        if(_pumpHistoryDao == null) {
          _pumpHistoryDao = new PumpHistoryDao_Impl(this);
        }
        return _pumpHistoryDao;
      }
    }
  }

  @Override
  public AlertDao alertDao() {
    if (_alertDao != null) {
      return _alertDao;
    } else {
      synchronized(this) {
        if(_alertDao == null) {
          _alertDao = new AlertDao_Impl(this);
        }
        return _alertDao;
      }
    }
  }

  @Override
  public ZoneSelectionDao zoneSelectionDao() {
    if (_zoneSelectionDao != null) {
      return _zoneSelectionDao;
    } else {
      synchronized(this) {
        if(_zoneSelectionDao == null) {
          _zoneSelectionDao = new ZoneSelectionDao_Impl(this);
        }
        return _zoneSelectionDao;
      }
    }
  }
}
