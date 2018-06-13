package productions.darthplagueis.weathersafe.database.album;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import productions.darthplagueis.weathersafe.model.UserAlbum;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserAlbumDao {

    @Insert(onConflict = REPLACE)
    void insert(UserAlbum... userAlbums);

    @Query("DELETE from user_album_table")
    void deleteAll();

    @Query("SELECT * from user_album_table")
    LiveData<List<UserAlbum>> getAllAlbums();

    @Query("SELECT COUNT(*) from user_album_table")
    int countTotalAlbums();

    @Query("SELECT * from user_album_table ORDER BY album_timestamp DESC")
    LiveData<List<UserAlbum>> getDescendingDateOrder();

    @Delete
    void delete(UserAlbum userAlbum);
}
