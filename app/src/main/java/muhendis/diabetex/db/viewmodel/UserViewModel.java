package muhendis.diabetex.db.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import muhendis.diabetex.db.DataRepository;
import muhendis.diabetex.db.entity.ProgramEntity;
import muhendis.diabetex.db.entity.UserEntity;

/**
 * Created by muhendis on 29.01.2018.
 */

public class UserViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private LiveData<List<UserEntity>> mAllUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllUsers = mRepository.getAllUsers();
    }

    public LiveData<List<UserEntity>> getAllUsers() { return mAllUsers; }

    public void insertUser(UserEntity user) { mRepository.insertUser(user); }

    public void deleteUser(UserEntity user) { mRepository.deleteUser(user); }

    public void updateFirstSurvey(Integer pid) { mRepository.updateFirstSurvey(pid); }


}
