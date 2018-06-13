package productions.darthplagueis.weathersafe.database.album;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import productions.darthplagueis.weathersafe.model.UserAlbum;

public class UserAlbumRepository {

    private UserAlbumDao userAlbumDao;

    public UserAlbumRepository(Application application) {
        UserAlbumDatabase db = UserAlbumDatabase.getDatabase(application);
        userAlbumDao = db.userAlbumDao();
    }

    public LiveData<List<UserAlbum>> getAllAlbums() {
        return userAlbumDao.getAllAlbums();
    }

    public LiveData<List<UserAlbum>> getDescDateList() {
        return userAlbumDao.getDescendingDateOrder();
    }

    public void insert(UserAlbum userAlbum) {
        new insertAsyncTask(userAlbumDao).execute(userAlbum);
    }

    public void delete(UserAlbum userAlbum) {
        new deleteAsyncTask(userAlbumDao).execute(userAlbum);
    }

    private static class insertAsyncTask extends AsyncTask<UserAlbum, Void, Void> {

        private UserAlbumDao asyncTaskDao;

        insertAsyncTask(UserAlbumDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserAlbum... userAlbums) {
            asyncTaskDao.insert(userAlbums[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<UserAlbum, Void, Void> {

        private UserAlbumDao asyncTaskDao;

        deleteAsyncTask(UserAlbumDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserAlbum... userAlbums) {
            asyncTaskDao.delete(userAlbums[0]);
            return null;
        }
    }
}
