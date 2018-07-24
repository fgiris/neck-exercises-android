package muhendis.diabetex.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import muhendis.diabetex.model.User;

/**
 * Created by muhendis on 23.01.2018.
 */
@Entity(tableName = "user")
public class UserEntity implements User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    // uid is a field which is user id in the web DB
    @ColumnInfo(name = "uid")
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "surname")
    private String surname;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "video")
    private String video;

    @ColumnInfo(name = "is_logged_in")
    private Boolean isLoggedIn;

    @ColumnInfo(name = "is_sync")
    private Boolean isSync;

    public UserEntity(int uid, String name, String surname, String email, String video, Boolean isLoggedIn, Boolean isSync) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.video = video;
        this.isLoggedIn = isLoggedIn;
        this.isSync = isSync;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public int getUid() {
        return uid;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getSurname() {
        return surname;
    }
    @Override
    public String getEmail() {
        return email;
    }
    @Override
    public String getVideo() {
        return video;
    }
    @Override
    public Boolean getLoggedIn() {
        return isLoggedIn;
    }
    @Override
    public Boolean getSync() {
        return isSync;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
    }
}
