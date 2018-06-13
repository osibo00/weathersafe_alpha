package productions.darthplagueis.weathersafe.database.media;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.model.UserMedia;

public class UserMediaRepository {

    private UserMediaDao userMediaDao;

    public UserMediaRepository(Application application) {
        UserMediaDatabase db = UserMediaDatabase.getDatabase(application);
        userMediaDao = db.userMediaDao();
    }

    public LiveData<List<UserMedia>> getAllMedia() {
        return userMediaDao.getAllMedia();
    }

    public LiveData<List<UserMedia>> getDescDateList() {
        return userMediaDao.getDescendingDateOrder();
    }

    public LiveData<List<UserMedia>> getAscDateList() {
        return userMediaDao.getAscendingDateOrder();
    }

    public LiveData<List<UserMedia>> getAlbumOrderList() {
        return userMediaDao.getAlbumOrder();
    }

    public LiveData<List<UserMedia>> getSingleEntityFromDirectory(String directoryName) {
        return userMediaDao.getSingleItemFromDirectory(directoryName);
    }

    public void insert(UserMedia userMedia) {
        new insertAsyncTask(userMediaDao).execute(userMedia);
    }

    public void delete(UserMedia userMedia) {
        new deleteAsyncTask(userMediaDao).execute(userMedia);
    }

    private static class insertAsyncTask extends AsyncTask<UserMedia, Void, Void> {

        private UserMediaDao asyncTaskDao;

        insertAsyncTask(UserMediaDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserMedia... userMedia) {
            asyncTaskDao.insert(userMedia[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<UserMedia, Void, Void> {

        private UserMediaDao asyncTaskDao;

        deleteAsyncTask(UserMediaDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserMedia... userMedia) {
            asyncTaskDao.delete(userMedia[0]);
            return null;
        }
    }
}
