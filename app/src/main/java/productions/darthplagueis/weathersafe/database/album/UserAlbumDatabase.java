package productions.darthplagueis.weathersafe.database.album;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import productions.darthplagueis.weathersafe.model.UserAlbum;

@Database(entities = {UserAlbum.class}, version = 2)
public abstract class UserAlbumDatabase extends RoomDatabase {

    public abstract UserAlbumDao userAlbumDao();

    private static UserAlbumDatabase INSTANCE;

    public static UserAlbumDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserAlbumDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserAlbumDatabase.class, "user_albums_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
