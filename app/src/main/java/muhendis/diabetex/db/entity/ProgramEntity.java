package muhendis.diabetex.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import muhendis.diabetex.model.Program;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by muhendis on 29.01.2018.
 */

@Entity(tableName = "program",foreignKeys = @ForeignKey(entity = UserEntity.class,
        parentColumns = "id",
        childColumns = "user_id", onDelete = CASCADE))

public class ProgramEntity implements Program {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    private int uid;

    @ColumnInfo(name = "pid")
    private int pid;

    @ColumnInfo(name = "name")
    private String programName;

    @ColumnInfo(name = "exp")
    private String exp;

    @ColumnInfo(name = "start_date")
    private Date startDate;

    @ColumnInfo(name = "finish_date")
    private Date finishDate;

    @ColumnInfo(name = "complete_percentage")
    private int completePercentage;

    @ColumnInfo(name = "duration")
    private int duration;

    @ColumnInfo(name = "doctor_name")
    private String doctorName;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getUid() {
        return uid;
    }

    @Override
    public int getPid() {
        return pid;
    }

    @Override
    public String getProgramName() {
        return programName;
    }

    @Override
    public String getDoctorName() {
        return doctorName;
    }

    @Override
    public String getExp() {
        return exp;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getCompletePercentage() {
        return completePercentage;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }


    public ProgramEntity(int uid, int pid, String programName, String exp, String doctorName, Date startDate, Date finishDate, int duration, int completePercentage) {
        this.uid = uid;
        this.pid = pid;
        this.programName = programName;
        this.exp = exp;
        this.doctorName = doctorName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.duration = duration;
        this.completePercentage = completePercentage;
    }

    @Override
    public Date getFinishDate() {
        return finishDate;
    }
}
