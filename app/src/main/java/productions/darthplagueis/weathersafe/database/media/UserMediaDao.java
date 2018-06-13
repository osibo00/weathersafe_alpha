package productions.darthplagueis.weathersafe.database.media;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.model.UserMedia;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserMediaDao {

    @Insert(onConflict = REPLACE)
    void insert(UserMedia... userMedia);

    @Query("DELETE from user_media_table")
    void deleteAll();

    @Query("SELECT * from user_media_table")
    LiveData<List<UserMedia>> getAllMedia();

    @Query("SELECT COUNT(*) from user_media_table")
    int countUserItemTotal();

    @Query("SELECT * from user_media_table ORDER BY media_time_stamp DESC")
    LiveData<List<UserMedia>> getDescendingDateOrder();

    @Query("SELECT * from user_media_table ORDER BY media_time_stamp ASC")
    LiveData<List<UserMedia>> getAscendingDateOrder();

    @Query("SELECT * from user_media_table ORDER BY media_file_directory ASC")
    LiveData<List<UserMedia>> getAlbumOrder();

    @Query("SELECT * from user_media_table WHERE media_file_directory LIKE :directory")
    LiveData<List<UserMedia>> getMediaFromDirectory(String directory);

    @Query("SELECT * from user_media_table WHERE media_file_directory LIKE :directory" + " ORDER BY media_time_stamp DESC LIMIT 1")
    LiveData<List<UserMedia>> getSingleItemFromDirectory(String directory);

    @Query("SELECT * from user_media_table WHERE media_tag LIKE :tag")
    LiveData<List<UserMedia>> getMediaFromTag(String tag);

    @Query("SELECT * from user_media_table WHERE media_upload_date LIKE :date")
    LiveData<List<UserMedia>> getMediaFromDate(String date);

    @Delete
    void delete(UserMedia userMedia);
}
