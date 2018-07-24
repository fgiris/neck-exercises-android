package muhendis.diabetex.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import muhendis.diabetex.db.entity.UserEntity;
import muhendis.diabetex.model.User;

/**
 * Created by muhendis on 25.01.2018.
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    LiveData<List<UserEntity>> getAllUsers();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    LiveData<List<UserEntity>> loadAllUsersByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE name LIKE :name AND "
            + "surname LIKE :surname LIMIT 1")
    UserEntity findUserByName(String name, String surname);

    @Query("UPDATE user SET is_sync=1 WHERE id=:id")
    void updateFirstSurveyStatus(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);


    @Delete
    void deleteUser(UserEntity user);

    @Delete
    void deleteAllUsers(UserEntity... users);
}