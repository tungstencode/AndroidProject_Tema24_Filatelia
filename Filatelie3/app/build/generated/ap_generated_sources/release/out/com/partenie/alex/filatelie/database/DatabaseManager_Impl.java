package com.partenie.alex.filatelie.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.partenie.alex.filatelie.database.dao.CollectionItemDao;
import com.partenie.alex.filatelie.database.dao.CollectionItemDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class DatabaseManager_Impl extends DatabaseManager {
  private volatile CollectionItemDao _collectionItemDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `collectionItems` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `imgLocation` TEXT, `name` TEXT, `description` TEXT, `price` REAL, `manufacturedDate` INTEGER, `acquiredDate` INTEGER, `historicLocation` TEXT, `type` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"6dc962490c24676579d6f1646a96c966\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `collectionItems`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsCollectionItems = new HashMap<String, TableInfo.Column>(9);
        _columnsCollectionItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsCollectionItems.put("imgLocation", new TableInfo.Column("imgLocation", "TEXT", false, 0));
        _columnsCollectionItems.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsCollectionItems.put("description", new TableInfo.Column("description", "TEXT", false, 0));
        _columnsCollectionItems.put("price", new TableInfo.Column("price", "REAL", false, 0));
        _columnsCollectionItems.put("manufacturedDate", new TableInfo.Column("manufacturedDate", "INTEGER", false, 0));
        _columnsCollectionItems.put("acquiredDate", new TableInfo.Column("acquiredDate", "INTEGER", false, 0));
        _columnsCollectionItems.put("historicLocation", new TableInfo.Column("historicLocation", "TEXT", false, 0));
        _columnsCollectionItems.put("type", new TableInfo.Column("type", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCollectionItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCollectionItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCollectionItems = new TableInfo("collectionItems", _columnsCollectionItems, _foreignKeysCollectionItems, _indicesCollectionItems);
        final TableInfo _existingCollectionItems = TableInfo.read(_db, "collectionItems");
        if (! _infoCollectionItems.equals(_existingCollectionItems)) {
          throw new IllegalStateException("Migration didn't properly handle collectionItems(com.partenie.alex.filatelie.database.model.CollectionItem).\n"
                  + " Expected:\n" + _infoCollectionItems + "\n"
                  + " Found:\n" + _existingCollectionItems);
        }
      }
    }, "6dc962490c24676579d6f1646a96c966", "a58e2e3ddc063596ba6d2c05301ede44");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "collectionItems");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `collectionItems`");
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
  public CollectionItemDao getCollectionItemDao() {
    if (_collectionItemDao != null) {
      return _collectionItemDao;
    } else {
      synchronized(this) {
        if(_collectionItemDao == null) {
          _collectionItemDao = new CollectionItemDao_Impl(this);
        }
        return _collectionItemDao;
      }
    }
  }
}
