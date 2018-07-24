package muhendis.diabetex.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import muhendis.diabetex.db.converter.DateConverter;
import muhendis.diabetex.db.dao.ExerciseDao;
import muhendis.diabetex.db.dao.ProgramDao;
import muhendis.diabetex.db.dao.StatisticsDao;
import muhendis.diabetex.db.dao.UserDao;
import muhendis.diabetex.db.entity.ExerciseEntity;
import muhendis.diabetex.db.entity.ProgramEntity;
import muhendis.diabetex.db.entity.StatisticsEntity;
import muhendis.diabetex.db.entity.UserEntity;

/**
 * Created by muhendis on 25.01.2018.
 */

@Database(entities = {UserEntity.class, ExerciseEntity.class, StatisticsEntity.class, ProgramEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ExerciseDao exerciseDao();
    public abstract StatisticsDao statisticsDao();
    public abstract ProgramDao programDao();
    private final String TAG="AppDatabase";
    private final static String DB_NAME="diabetex_db";

    private static AppDatabase INSTANCE;

    /*
    * Make sure that singleton pattern is applied since
    * INSTANCE object is expensive
    * */
    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            //Since migration is not needed for this app db
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }



    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);

                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ProgramDao mProgramDao;
        private final ExerciseDao mExerciseDao;
        private final UserDao mUserDao;
        private final StatisticsDao mStatisticsDao;
        private final String TAG="AppDatabase";

        PopulateDbAsync(AppDatabase db) {
            mExerciseDao = db.exerciseDao();
            mProgramDao = db.programDao();
            mUserDao = db.userDao();
            mStatisticsDao = db.statisticsDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            UserEntity user = new UserEntity(1,"Ali","Aydın","aliaydın@xxx.com","",true,true);
            //mUserDao.insertUser(user);

            for(int i=0;i<10;i++)
            {
                //ProgramEntity program = new ProgramEntity(1,i+1,"Ayak Egzersizleri","Eren Yılmaz","Eren Yılmaz",new Date(2017-1900,1,10),new Date(2017-1900,2,10),30,15);
                //mProgramDao.insertProgram(program);

                ExerciseEntity exercise = new ExerciseEntity(1,i,1,"Ayak Egzersizleri","Eren Yılmaz","photo","video",1,1,1,1,1,1);
                mExerciseDao.insertExercise(exercise);
                Log.d(TAG,"EXERCISES ARE INSERTED");

                /*String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                StatisticsEntity statistics = new StatisticsEntity(1, today,1);
                mStatisticsDao.insertStatistics(statistics);

                List<StatisticsEntity> mStat=mStatisticsDao.getAllStatisticsToday();
                List<String> mDates = mStatisticsDao.getAllStatisticsTodayDate();
                for (String s:mDates
                     ) {
                    Log.d("STATISTICS TODAY DONE",s+"  FINISH DATE");
                }*/
            }



            Log.d("AppDB","inserted");

            return null;
        }
    }

}
