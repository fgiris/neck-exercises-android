package muhendis.diabetex.db.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import muhendis.diabetex.db.DataRepository;
import muhendis.diabetex.db.entity.ProgramEntity;
import muhendis.diabetex.db.entity.StatisticsEntity;

/**
 * Created by muhendis on 29.01.2018.
 */

public class StatisticsViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private LiveData<List<StatisticsEntity>> mAllStatistics;

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllStatistics = mRepository.getAllStatistics();
    }

    public LiveData<List<StatisticsEntity>> getAllStatistics() { return mAllStatistics; }

    public void insertStatistics(StatisticsEntity statistics) { mRepository.insertStatistics(statistics); }

}
