package muhendis.diabetex.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import muhendis.diabetex.db.entity.ExerciseEntity;
import muhendis.diabetex.model.Exercise;

/**
 * Created by muhendis on 25.01.2018.
 */

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    LiveData<List<ExerciseEntity>> getAllExercises();

    //@Query("SELECT * FROM exercise INNER JOIN statistics ON date(statistics.finish_date) = :today WHERE exercise.id = statistics.eid")
    @Query("SELECT * FROM exercise WHERE id IN (SELECT eid FROM statistics WHERE date(finish_date) = :today)")
    LiveData<List<ExerciseEntity>> getCompletedExercises(String today);

    @Query("SELECT * FROM exercise WHERE id NOT IN (SELECT eid FROM statistics WHERE date(finish_date) = :today)")
    LiveData<List<ExerciseEntity>> getUncompletedExercises(String today);

    @Query("SELECT * FROM exercise WHERE id IN (:exerciseIds)")
    LiveData<List<ExerciseEntity>> loadAllExercisesByIds(int[] exerciseIds);

    @Query("SELECT * FROM exercise WHERE name LIKE :name LIMIT 1")
    ExerciseEntity findExerciseByName(String name);

    // One or more exercise should be able to be inserted for the sake of synchronization
    @Insert
    void insertExercise(ExerciseEntity... exercises);

    // One or more exercise should be able to be deleted for the sake of synchronization
    @Delete
    void deleteExercise(ExerciseEntity... exercises);
}
