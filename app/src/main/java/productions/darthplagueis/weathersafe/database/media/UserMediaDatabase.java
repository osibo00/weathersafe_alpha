package productions.darthplagueis.weathersafe.database.media;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import productions.darthplagueis.weathersafe.model.UserMedia;

@Database(entities = {UserMedia.class}, version = 3)
public abstract class UserMediaDatabase extends RoomDatabase {

    public abstract UserMediaDao userMediaDao();

    private static UserMediaDatabase INSTANCE;

    public static UserMediaDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserMediaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserMediaDatabase.class, "user_media_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
