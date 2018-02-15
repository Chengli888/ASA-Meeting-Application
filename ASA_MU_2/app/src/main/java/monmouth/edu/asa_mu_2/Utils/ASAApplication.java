package monmouth.edu.asa_mu_2.Utils;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by chengli on 2018/1/3.
 */

public class ASAApplication extends Application {
    private ArrayList<MyBean> list= new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public ArrayList<MyBean> getList() {
        return list;
    }

    public void setList(ArrayList<MyBean> list) {
        this.list = list;
    }
}
