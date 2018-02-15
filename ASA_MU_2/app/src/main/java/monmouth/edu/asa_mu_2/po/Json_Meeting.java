package monmouth.edu.asa_mu_2.po;

import java.util.List;

/**
 * Created by chengli on 2017/11/5.
 */

public class Json_Meeting {
    private List<Meeting> meeting;
    public void setMeeting(List<Meeting> meeting) {
        this.meeting = meeting;
    }
    public List<Meeting> getMeeting() {
        return meeting;
    }
}
