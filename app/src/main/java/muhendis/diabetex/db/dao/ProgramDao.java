package muhendis.diabetex.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import muhendis.diabetex.db.entity.ProgramEntity;
import muhendis.diabetex.model.Program;

/**
 * Created by muhendis on 29.01.2018.
 */

@Dao
public interface ProgramDao {
    @Query("SELECT * FROM program")
    LiveData<List<ProgramEntity>> getAllPrograms();

    @Query("SELECT * FROM program WHERE id IN (:programIds)")
    LiveData<List<ProgramEntity>> loadAllProgramsByIds(int[] programIds);

    @Query("SELECT * FROM program WHERE name LIKE :name LIMIT 1")
    ProgramEntity findProgramByName(String name);

    @Query("SELECT * FROM program WHERE id=:id")
    ProgramEntity findProgramById(int id);

    @Query("SELECT * FROM program WHERE pid LIKE :pid LIMIT 1")
    ProgramEntity findProgramByPid(int pid);

    // One or more exercise should be able to be inserted for the sake of synchronization
    @Insert
    void insertProgram(ProgramEntity... programs);

    // One or more exercise should be able to be deleted for the sake of synchronization
    @Delete
    void deleteProgram(ProgramEntity... programs);
}
