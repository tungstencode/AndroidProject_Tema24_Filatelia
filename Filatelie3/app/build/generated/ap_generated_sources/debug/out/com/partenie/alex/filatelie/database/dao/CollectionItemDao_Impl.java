package com.partenie.alex.filatelie.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.partenie.alex.filatelie.database.model.CollectionItem;
import com.partenie.alex.filatelie.util.DataConverter;
import java.lang.Float;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public final class CollectionItemDao_Impl implements CollectionItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfCollectionItem;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfCollectionItem;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfCollectionItem;

  public CollectionItemDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCollectionItem = new EntityInsertionAdapter<CollectionItem>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `collectionItems`(`id`,`imgLocation`,`name`,`description`,`price`,`manufacturedDate`,`acquiredDate`,`historicLocation`,`type`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CollectionItem value) {
        stmt.bindLong(1, value.id);
        if (value.imgLocation == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.imgLocation);
        }
        if (value.name == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.name);
        }
        if (value.description == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.description);
        }
        if (value.price == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindDouble(5, value.price);
        }
        final Long _tmp;
        _tmp = DataConverter.fromDate(value.manufacturedDate);
        if (_tmp == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = DataConverter.fromDate(value.acquiredDate);
        if (_tmp_1 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, _tmp_1);
        }
        if (value.historicLocation == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.historicLocation);
        }
        if (value.type == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.type);
        }
      }
    };
    this.__deletionAdapterOfCollectionItem = new EntityDeletionOrUpdateAdapter<CollectionItem>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `collectionItems` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CollectionItem value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfCollectionItem = new EntityDeletionOrUpdateAdapter<CollectionItem>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `collectionItems` SET `id` = ?,`imgLocation` = ?,`name` = ?,`description` = ?,`price` = ?,`manufacturedDate` = ?,`acquiredDate` = ?,`historicLocation` = ?,`type` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CollectionItem value) {
        stmt.bindLong(1, value.id);
        if (value.imgLocation == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.imgLocation);
        }
        if (value.name == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.name);
        }
        if (value.description == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.description);
        }
        if (value.price == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindDouble(5, value.price);
        }
        final Long _tmp;
        _tmp = DataConverter.fromDate(value.manufacturedDate);
        if (_tmp == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = DataConverter.fromDate(value.acquiredDate);
        if (_tmp_1 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, _tmp_1);
        }
        if (value.historicLocation == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.historicLocation);
        }
        if (value.type == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.type);
        }
        stmt.bindLong(10, value.id);
      }
    };
  }

  @Override
  public long insert(CollectionItem collectionItem) {
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfCollectionItem.insertAndReturnId(collectionItem);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(CollectionItem collectionItem) {
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfCollectionItem.handle(collectionItem);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(CollectionItem collectionItem) {
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__updateAdapterOfCollectionItem.handle(collectionItem);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<CollectionItem> getAll() {
    final String _sql = "select * from collectionItems";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfImgLocation = _cursor.getColumnIndexOrThrow("imgLocation");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("price");
      final int _cursorIndexOfManufacturedDate = _cursor.getColumnIndexOrThrow("manufacturedDate");
      final int _cursorIndexOfAcquiredDate = _cursor.getColumnIndexOrThrow("acquiredDate");
      final int _cursorIndexOfHistoricLocation = _cursor.getColumnIndexOrThrow("historicLocation");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final List<CollectionItem> _result = new ArrayList<CollectionItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CollectionItem _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpImgLocation;
        _tmpImgLocation = _cursor.getString(_cursorIndexOfImgLocation);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Float _tmpPrice;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmpPrice = null;
        } else {
          _tmpPrice = _cursor.getFloat(_cursorIndexOfPrice);
        }
        final Date _tmpManufacturedDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfManufacturedDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfManufacturedDate);
        }
        _tmpManufacturedDate = DataConverter.fromTimestamp(_tmp);
        final String _tmpHistoricLocation;
        _tmpHistoricLocation = _cursor.getString(_cursorIndexOfHistoricLocation);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _item = new CollectionItem(_tmpId,_tmpImgLocation,_tmpName,_tmpDescription,_tmpPrice,_tmpManufacturedDate,_tmpHistoricLocation,_tmpType);
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfAcquiredDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfAcquiredDate);
        }
        _item.acquiredDate = DataConverter.fromTimestamp(_tmp_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<CollectionItem> getType(String tip) {
    final String _sql = "select * from collectionItems where type LIKE ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tip == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tip);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfImgLocation = _cursor.getColumnIndexOrThrow("imgLocation");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("price");
      final int _cursorIndexOfManufacturedDate = _cursor.getColumnIndexOrThrow("manufacturedDate");
      final int _cursorIndexOfAcquiredDate = _cursor.getColumnIndexOrThrow("acquiredDate");
      final int _cursorIndexOfHistoricLocation = _cursor.getColumnIndexOrThrow("historicLocation");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final List<CollectionItem> _result = new ArrayList<CollectionItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CollectionItem _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpImgLocation;
        _tmpImgLocation = _cursor.getString(_cursorIndexOfImgLocation);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Float _tmpPrice;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmpPrice = null;
        } else {
          _tmpPrice = _cursor.getFloat(_cursorIndexOfPrice);
        }
        final Date _tmpManufacturedDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfManufacturedDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfManufacturedDate);
        }
        _tmpManufacturedDate = DataConverter.fromTimestamp(_tmp);
        final String _tmpHistoricLocation;
        _tmpHistoricLocation = _cursor.getString(_cursorIndexOfHistoricLocation);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _item = new CollectionItem(_tmpId,_tmpImgLocation,_tmpName,_tmpDescription,_tmpPrice,_tmpManufacturedDate,_tmpHistoricLocation,_tmpType);
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfAcquiredDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfAcquiredDate);
        }
        _item.acquiredDate = DataConverter.fromTimestamp(_tmp_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<CollectionItem> getOver(Float value) {
    final String _sql = "select * from collectionItems where price > ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (value == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindDouble(_argIndex, value);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfImgLocation = _cursor.getColumnIndexOrThrow("imgLocation");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("price");
      final int _cursorIndexOfManufacturedDate = _cursor.getColumnIndexOrThrow("manufacturedDate");
      final int _cursorIndexOfAcquiredDate = _cursor.getColumnIndexOrThrow("acquiredDate");
      final int _cursorIndexOfHistoricLocation = _cursor.getColumnIndexOrThrow("historicLocation");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final List<CollectionItem> _result = new ArrayList<CollectionItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CollectionItem _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpImgLocation;
        _tmpImgLocation = _cursor.getString(_cursorIndexOfImgLocation);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Float _tmpPrice;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmpPrice = null;
        } else {
          _tmpPrice = _cursor.getFloat(_cursorIndexOfPrice);
        }
        final Date _tmpManufacturedDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfManufacturedDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfManufacturedDate);
        }
        _tmpManufacturedDate = DataConverter.fromTimestamp(_tmp);
        final String _tmpHistoricLocation;
        _tmpHistoricLocation = _cursor.getString(_cursorIndexOfHistoricLocation);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _item = new CollectionItem(_tmpId,_tmpImgLocation,_tmpName,_tmpDescription,_tmpPrice,_tmpManufacturedDate,_tmpHistoricLocation,_tmpType);
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfAcquiredDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfAcquiredDate);
        }
        _item.acquiredDate = DataConverter.fromTimestamp(_tmp_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
