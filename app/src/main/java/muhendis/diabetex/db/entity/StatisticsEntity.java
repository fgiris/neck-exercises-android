package muhendis.diabetex.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import muhendis.diabetex.model.Statistics;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by muhendis on 23.01.2018.
 */

// Every statistics row is linked with ExerciseEntity class via eid key
// When the exercise is deleted the statistics rows which linked to that specific exercise
// are also deleted because of the foreign key
@Entity(tableName = "statistics", foreignKeys = @ForeignKey(entity = UserEntity.class,
        parentColumns = "id",
        childColumns = "pid", onDelete = CASCADE))
//@Entity(tableName = "statistics")
public class StatisticsEntity implements Statistics {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "eid")
    private int eid;

    @ColumnInfo(name = "pid")
    private int pid;

    @ColumnInfo(name = "finish_date")
    private String finishDate;

    //I am using this to save chronometer seconds not changing name!!!!
    @ColumnInfo(name = "difficulty_level")
    private int difficultyLevel;

    public StatisticsEntity(int eid, int pid, String finishDate, int difficultyLevel) {
        this.eid = eid;
        this.pid = pid;
        this.finishDate = finishDate;
        this.difficultyLevel = difficultyLevel;
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getPid()
    {
        return pid;
    }
    @Override
    public int getEid() {
        return eid;
    }
    @Override
    public String getFinishDate() {
        return finishDate;
    }
    @Override
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
