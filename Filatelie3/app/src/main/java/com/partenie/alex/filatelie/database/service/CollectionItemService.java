package com.partenie.alex.filatelie.database.service;

import android.content.Context;
import android.os.AsyncTask;

import com.partenie.alex.filatelie.database.DatabaseManager;
import com.partenie.alex.filatelie.database.dao.CollectionItemDao;
import com.partenie.alex.filatelie.database.model.CollectionItem;

import java.util.List;

public class CollectionItemService {
    private static CollectionItemDao collectionItemDao;

    public static class GetAll
            extends AsyncTask<Void, Void, List<CollectionItem>> {

        public GetAll(Context context) {
            collectionItemDao = DatabaseManager
                    .getInstance(context)
                    .getCollectionItemDao();
        }

        @Override
        protected List<CollectionItem> doInBackground(Void... voids) {
            return collectionItemDao.getAll();
        }
    }

    public static class Insert extends
            AsyncTask<CollectionItem, Void, CollectionItem> {
        public Insert(Context context) {
            collectionItemDao = DatabaseManager
                    .getInstance(context)
                    .getCollectionItemDao();
        }

        @Override
        protected CollectionItem doInBackground(CollectionItem... collectionItems) {
            if (collectionItems == null || collectionItems.length != 1) {
                return null;
            }
            CollectionItem collectionItem = collectionItems[0];
            long id = collectionItemDao.insert(collectionItem);
            if (id != -1) {
                collectionItem.setId(id);
                return collectionItem;
            }
            return null;
        }
    }

    public static class Update extends
            AsyncTask<CollectionItem, Void, Integer> {
        public Update(Context context) {
            collectionItemDao = DatabaseManager
                    .getInstance(context)
                    .getCollectionItemDao();
        }

        @Override
        protected Integer doInBackground(CollectionItem... collectionItems) {
            if (collectionItems == null || collectionItems.length != 1) {
                return -1;
            }
            return collectionItemDao.update(collectionItems[0]);
        }
    }

    public static class Delete extends
            AsyncTask<CollectionItem, Void, Integer> {
        public Delete(Context context) {
            collectionItemDao = DatabaseManager
                    .getInstance(context)
                    .getCollectionItemDao();
        }

        @Override
        protected Integer doInBackground(CollectionItem... collectionItems) {
            if (collectionItems == null || collectionItems.length != 1) {
                return -1;
            }
            return collectionItemDao.delete(collectionItems[0]);
        }
    }
}
