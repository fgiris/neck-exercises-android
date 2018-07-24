package muhendis.diabetex.db.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import muhendis.diabetex.db.DataRepository;
import muhendis.diabetex.db.entity.ProgramEntity;

/**
 * Created by muhendis on 29.01.2018.
 */

public class ProgramViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private LiveData<List<ProgramEntity>> mAllPrograms;

    public ProgramViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllPrograms = mRepository.getAllPrograms();
    }

    public LiveData<List<ProgramEntity>> getAllPrograms() { return mAllPrograms; }

    public void insertProgram(ProgramEntity program) { mRepository.insertProgram(program); }

}
