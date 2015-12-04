package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.content.Context;

/**
 * Created by User on 12/4/2015.
 */
public class AppContext extends Application {

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
