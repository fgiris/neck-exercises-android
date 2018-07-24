package muhendis.diabetex.model;

/**
 * Created by muhendis on 26.01.2018.
 */

public interface User {
    int getId();

    int getUid();

    String getName();

    String getSurname();

    String getEmail();

    String getVideo();

    Boolean getLoggedIn();

    Boolean getSync();
}
