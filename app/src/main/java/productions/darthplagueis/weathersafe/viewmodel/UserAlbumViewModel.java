package productions.darthplagueis.weathersafe.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import productions.darthplagueis.weathersafe.database.album.UserAlbumRepository;
import productions.darthplagueis.weathersafe.model.UserAlbum;

public class UserAlbumViewModel extends AndroidViewModel {

    private UserAlbumRepository albumRepository;

    public UserAlbumViewModel(@NonNull Application application) {
        super(application);
        albumRepository = new UserAlbumRepository(application);
    }

    public LiveData<List<UserAlbum>> getAllAlbums() {
        return albumRepository.getAllAlbums();
    }

    public LiveData<List<UserAlbum>> getDescDateList() {
        return albumRepository.getDescDateList();
    }

    public void insert(UserAlbum userAlbum) {
        albumRepository.insert(userAlbum);
    }

    public void delete(UserAlbum userAlbum) {
        albumRepository.delete(userAlbum);
    }
}
