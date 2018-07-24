package muhendis.diabetex.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import muhendis.diabetex.db.dao.ExerciseDao;
import muhendis.diabetex.db.dao.ProgramDao;
import muhendis.diabetex.db.dao.StatisticsDao;
import muhendis.diabetex.db.dao.UserDao;
import muhendis.diabetex.db.entity.ExerciseEntity;
import muhendis.diabetex.db.entity.ProgramEntity;
import muhendis.diabetex.db.entity.StatisticsEntity;
import muhendis.diabetex.db.entity.UserEntity;
import muhendis.diabetex.model.Exercise;
import muhendis.diabetex.model.Statistics;
import muhendis.diabetex.model.User;

/**
 * Created by muhendis on 26.01.2018.
 */

public class DataRepository {
    private final String TAG = "DataRepository";
    private UserDao mUserDao;
    private ExerciseDao mExerciseDao;
    private StatisticsDao mStatisticsDao;
    private ProgramDao mProgramDao;
    private LiveData<List<UserEntity>> mAllUsers;
    private LiveData<List<StatisticsEntity>> mAllStatistics;
    private LiveData<List<ProgramEntity>> mAllPrograms;

    public DataRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mExerciseDao = db.exerciseDao();
        mStatisticsDao = db.statisticsDao();
        mProgramDao = db.programDao();
        mAllUsers = mUserDao.getAllUsers();
        mAllStatistics = mStatisticsDao.getAllStatistics();
        mAllPrograms = mProgramDao.getAllPrograms();
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return mAllUsers;
    }

    public LiveData<List<StatisticsEntity>> getAllStatistics() {
        return mAllStatistics;
    }

    public LiveData<List<ProgramEntity>> getAllPrograms() {
        return mAllPrograms;
    }

    public void insertUser (UserEntity user) {
        new insertUserAsyncTask(mUserDao).execute(user);
    }

    public void deleteUser (UserEntity user) {
        new deleteUserAsyncTask(mUserDao).execute(user);
    }


    public void updateFirstSurvey (Integer pid) {
        new updateFirstSurveyAsyncTask(mUserDao).execute(pid);
    }


    public void insertExercise (ExerciseEntity exercise) {
        new insertExerciseAsyncTask(mExerciseDao).execute(exercise);
    }

    public void insertStatistics (StatisticsEntity statistics) {
        new insertStatisticsAsyncTask(mStatisticsDao).execute(statistics);
    }

    public void insertProgram (ProgramEntity program) {
        new insertProgramAsyncTask(mProgramDao).execute(program);
    }

    private static class insertUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserEntity... params) {
            mAsyncTaskDao.insertUser(params[0]);
            return null;
        }
    }

    private static class deleteUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mAsyncTaskDao;

        deleteUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserEntity... params) {
            mAsyncTaskDao.deleteUser(params[0]);
            return null;
        }
    }

    private static class updateFirstSurveyAsyncTask extends AsyncTask<Integer, Void, Void> {

        private UserDao mAsyncTaskDao;

        updateFirstSurveyAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.updateFirstSurveyStatus(params[0].intValue());
            return null;
        }
    }
    private static class insertExerciseAsyncTask extends AsyncTask<ExerciseEntity, Void, Void> {

        private ExerciseDao mAsyncTaskDao;

        insertExerciseAsyncTask(ExerciseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ExerciseEntity... params) {
            mAsyncTaskDao.insertExercise(params[0]);
            return null;
        }
    }
    private static class insertStatisticsAsyncTask extends AsyncTask<StatisticsEntity, Void, Void> {

        private StatisticsDao mAsyncTaskDao;

        insertStatisticsAsyncTask(StatisticsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final StatisticsEntity... params) {
            mAsyncTaskDao.insertStatistics(params[0]);
            return null;
        }
    }

    private static class insertProgramAsyncTask extends AsyncTask<ProgramEntity, Void, Void> {

        private ProgramDao mAsyncTaskDao;

        insertProgramAsyncTask(ProgramDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ProgramEntity... params) {
            mAsyncTaskDao.insertProgram(params[0]);
            return null;
        }
    }
}
