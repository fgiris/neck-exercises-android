package muhendis.diabetex.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import muhendis.diabetex.db.entity.StatisticsEntity;
import muhendis.diabetex.model.Statistics;

/**
 * Created by muhendis on 25.01.2018.
 */

@Dao
public interface StatisticsDao {
    @Query("SELECT * FROM statistics")
    LiveData<List<StatisticsEntity>> getAllStatistics();

    @Query("SELECT * FROM statistics")
    List<StatisticsEntity> getAllStatisticsToday();

    @Query("SELECT date(finish_date) FROM statistics")
    List<String> getAllStatisticsTodayDate();
    @Insert
    void insertStatistics(StatisticsEntity... statistics);

    @Delete
    void deleteStatistics(StatisticsEntity... statistics);
}
