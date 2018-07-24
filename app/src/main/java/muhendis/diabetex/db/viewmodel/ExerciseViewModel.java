package muhendis.diabetex.db.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import java.util.List;

import muhendis.diabetex.db.DataRepository;
import muhendis.diabetex.db.ExerciseDataRepository;
import muhendis.diabetex.db.entity.ExerciseEntity;
import muhendis.diabetex.model.Exercise;

/**
 * Created by muhendis on 27.01.2018.
 */

public class ExerciseViewModel extends AndroidViewModel {

    private ExerciseDataRepository mRepository;
    private LiveData<List<ExerciseEntity>> mAllExercises,mCompletedExercises,mUncompletedExercises;
    private int pid;

    public ExerciseViewModel(@NonNull Application application,int pid) {
        super(application);
        this.pid = pid;
        mRepository = new ExerciseDataRepository(application,pid);
        mAllExercises = mRepository.getAllExercises();
        mCompletedExercises=mRepository.getCompletedExercises();
        mUncompletedExercises = mRepository.getUncompletedExercises();
    }


    public LiveData<List<ExerciseEntity>> getAllExercises() { return mAllExercises; }

    public LiveData<List<ExerciseEntity>> getCompletedExercises() {
        return mCompletedExercises;
    }

    public LiveData<List<ExerciseEntity>> getUncompletedExercises() {
        return mUncompletedExercises;
    }

    public void insertExercise(ExerciseEntity exercise) { mRepository.insertExercise(exercise); }

    public static class ExerciseViewModelFactory implements ViewModelProvider.Factory {

        private final Application app;
        private int pid;

        public ExerciseViewModelFactory(Application app,int pid) {
            this.app = app;
            this.pid = pid;
        }

        @Override
        public ExerciseViewModel create(Class modelClass) {
            return new ExerciseViewModel(app,pid);
        }
    }


}
