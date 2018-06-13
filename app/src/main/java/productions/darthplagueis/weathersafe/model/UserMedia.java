package productions.darthplagueis.weathersafe.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_media_table")
public class UserMedia {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "media_file_name")
    private String mediaFileName;

    @NonNull
    @ColumnInfo(name = "media_file_path")
    private String mediaFilePath;

    @NonNull
    @ColumnInfo(name = "media_file_directory")
    private String mediaFileDirectory;

    @NonNull
    @ColumnInfo(name = "media_tag")
    private String mediaTag;

    @NonNull
    @ColumnInfo(name = "media_upload_date")
    private String mediaUploadDate;

    @ColumnInfo(name = "media_time_stamp")
    private long mediaTimeStamp;

    public UserMedia(@NonNull String mediaFileName,
                     @NonNull String mediaFilePath,
                     @NonNull String mediaFileDirectory,
                     @NonNull String mediaTag,
                     @NonNull String mediaUploadDate,
                     long mediaTimeStamp) {
        this.mediaFileName = mediaFileName;
        this.mediaFilePath = mediaFilePath;
        this.mediaFileDirectory = mediaFileDirectory;
        this.mediaTag = mediaTag;
        this.mediaUploadDate = mediaUploadDate;
        this.mediaTimeStamp = mediaTimeStamp;
    }

    @Ignore
    public UserMedia(@NonNull String mediaFileName,
                     @NonNull String mediaFilePath,
                     @NonNull String mediaFileDirectory,
                     @NonNull String mediaUploadDate,
                     long mediaTimeStamp) {
        this.mediaFileName = mediaFileName;
        this.mediaFilePath = mediaFilePath;
        this.mediaFileDirectory = mediaFileDirectory;
        this.mediaTag = "";
        this.mediaUploadDate = mediaUploadDate;
        this.mediaTimeStamp = mediaTimeStamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getMediaFileName() {
        return mediaFileName;
    }

    @NonNull
    public String getMediaFilePath() {
        return mediaFilePath;
    }

    @NonNull
    public String getMediaFileDirectory() {
        return mediaFileDirectory;
    }

    @NonNull
    public String getMediaTag() {
        return mediaTag;
    }

    @NonNull
    public String getMediaUploadDate() {
        return mediaUploadDate;
    }

    public long getMediaTimeStamp() {
        return mediaTimeStamp;
    }
}
