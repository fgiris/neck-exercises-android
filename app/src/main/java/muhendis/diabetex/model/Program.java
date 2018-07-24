package muhendis.diabetex.model;

import java.util.Date;

/**
 * Created by muhendis on 29.01.2018.
 */

public interface Program {

    int getId();

    int getUid();

    int getPid();

    String getProgramName();

    String getDoctorName();

    String getExp();

    int getDuration();

    int getCompletePercentage();

    Date getStartDate();

    Date getFinishDate();

}
