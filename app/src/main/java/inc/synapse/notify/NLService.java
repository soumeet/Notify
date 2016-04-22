package inc.synapse.notify;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NLService extends NotificationListenerService {
    private String TAG=getClass().getSimpleName();
    private String PACKAGE_NAME;
    private String WHATS_APP="WHATSAPP";
    private NLSReceiver nlsReceiver;
    private SharedPreferences app_store;
    public void onCreate() {
        super.onCreate();
        init();
        IntentFilter filter=new IntentFilter();
        filter.addAction("inc.synapse.notify");
        registerReceiver(nlsReceiver,filter);
    }

    private void init() {
        nlsReceiver= new NLSReceiver();
        PACKAGE_NAME = getApplicationContext().getPackageName();
        app_store=getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlsReceiver);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onNotificationPosted(StatusBarNotification sbn) {
        long t=sbn.getPostTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        super.onNotificationPosted(sbn);
        String pkg_name=sbn.getPackageName();
        String timestamp=sdf.format(new Date(t));
        //String no_notf= String.valueOf(sbn.getNotification().number);
        Intent i = new  Intent("inc.synapse.notify");
        boolean f1=pkg_name.equalsIgnoreCase("com.whatsapp");
        boolean f2=pkg_name.equalsIgnoreCase("com.android.dialer");
        boolean f3=pkg_name.equalsIgnoreCase("com.android.messaging");
        boolean f4=pkg_name.equalsIgnoreCase("com.facebook.katana");
        boolean f5=pkg_name.equalsIgnoreCase("com.google.android.gm");
        /*Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.WHATSAPP)+" : "+f1);
        Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.DAILER)+" : "+f2);
        Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.MESSAGING)+" : "+f3);
        Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.FACEBOOK)+" : "+f4);
        Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.GMAIL)+" : "+f5);*/

        if(f1 || f2 || f3 || f4 || f5) {
            i.putExtra("summary_event", timestamp +" " + sbn.getPackageName() + "\n");
            i.putExtra("notification_event", timestamp +" Posted :" + sbn.getPackageName());
            //Summary tmp=new Summary(pkg_name, timestamp, no_notf);
        }else {
            this.cancelNotification(String.valueOf(sbn.getKey()));
            i.putExtra("notification_event",timestamp+" Blocked :" + sbn.getPackageName());
        }
        sendBroadcast(i);
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Intent i = new  Intent("inc.synapse.notify");
        i.putExtra("notification_event",sdf.format(sbn.getPostTime())+" Removed :" + sbn.getPackageName());
        sendBroadcast(i);
    }

    class NLSReceiver extends BroadcastReceiver{

        public void onReceive(Context context, Intent intent) {

        }
    }

    public void read_pref(){

    }

    public void write_pref(Summary sm){
//        app_store.edit().putString()
    }
}
