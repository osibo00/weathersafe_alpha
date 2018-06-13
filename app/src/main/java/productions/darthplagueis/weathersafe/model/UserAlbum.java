package productions.darthplagueis.weathersafe.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_album_table")
public class UserAlbum {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "album_file_directory")
    private String directoryName;

    @NonNull
    @ColumnInfo(name = "album_image")
    private String directoryTag;

    @NonNull
    @ColumnInfo(name = "album_date")
    private String directoryDate;

    @ColumnInfo(name = "album_timestamp")
    private long directoryTimestamp;

    public UserAlbum(@NonNull String directoryName,
                     @NonNull String directoryTag,
                     @NonNull String directoryDate,
                     long directoryTimestamp) {
        this.directoryName = directoryName;
        this.directoryTag = directoryTag;
        this.directoryDate = directoryDate;
        this.directoryTimestamp = directoryTimestamp;
    }

    @Ignore
    public UserAlbum(@NonNull String directoryName,
                     @NonNull String directoryDate,
                     long directoryTimestamp) {
        this.directoryName = directoryName;
        this.directoryTag = "";
        this.directoryDate = directoryDate;
        this.directoryTimestamp = directoryTimestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getDirectoryName() {
        return directoryName;
    }

    @NonNull
    public String getDirectoryTag() {
        return directoryTag;
    }

    @NonNull
    public String getDirectoryDate() {
        return directoryDate;
    }

    public long getDirectoryTimestamp() {
        return directoryTimestamp;
    }
}
