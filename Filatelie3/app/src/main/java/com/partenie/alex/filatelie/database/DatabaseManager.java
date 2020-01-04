package com.partenie.alex.filatelie.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.partenie.alex.filatelie.database.dao.CollectionItemDao;
import com.partenie.alex.filatelie.database.model.CollectionItem;
import com.partenie.alex.filatelie.util.DataConverter;

@Database(entities = {CollectionItem.class},
        exportSchema = false,
        version = 1)
@TypeConverters({DataConverter.class})
public abstract class DatabaseManager extends RoomDatabase {

    private static final String DB_NAME = "filantelie_db";
    private static DatabaseManager databaseManager;

    public static DatabaseManager getInstance(
            Context context) {
        if (databaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (databaseManager == null) {
                    databaseManager = Room
                            .databaseBuilder(context,
                                    DatabaseManager.class,
                                    DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                    return databaseManager;
                }
            }
        }
        return databaseManager;
    }

    public abstract CollectionItemDao getCollectionItemDao();
}
