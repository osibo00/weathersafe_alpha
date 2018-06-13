package productions.darthplagueis.weathersafe.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import productions.darthplagueis.weathersafe.database.media.UserMediaRepository;
import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.model.UserMedia;

public class UserMediaViewModel extends AndroidViewModel {

    private UserMediaRepository mediaRepository;

    public UserMediaViewModel(@NonNull Application application) {
        super(application);
        mediaRepository = new UserMediaRepository(application);
    }

    public LiveData<List<UserMedia>> getAllMedia() {
        return mediaRepository.getAllMedia();
    }

    public LiveData<List<UserMedia>> getDescDateList() {
        return mediaRepository.getDescDateList();
    }

    public LiveData<List<UserMedia>> getAscDateList() {
        return mediaRepository.getAscDateList();
    }

    public LiveData<List<UserMedia>> getAlbumOrderList() {
        return mediaRepository.getAlbumOrderList();
    }

    public LiveData<List<UserMedia>> getSingleEntityFromDirectory(String albumName) {
        return mediaRepository.getSingleEntityFromDirectory(albumName);
    }

    public void insert(UserMedia userMedia) {
        mediaRepository.insert(userMedia);
    }

    public void delete(UserMedia userMedia) {
        mediaRepository.delete(userMedia);
    }
}
