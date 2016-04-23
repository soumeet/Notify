package inc.synapse.notify;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NLService extends NotificationListenerService {
    private String TAG=getClass().getSimpleName();
    private String PACKAGE_NAME;
    private String pref_LIST="LIST";
    private int list=0;

    private SharedPreferences app_store;
    private AudioManager aManager;
    public void onCreate() {
        super.onCreate();
        init();
        IntentFilter filter=new IntentFilter();
        filter.addAction("inc.synapse.notify");
    }

    private void init() {
        PACKAGE_NAME = getApplicationContext().getPackageName();
        aManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        app_store=getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        read_pref();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onNotificationPosted(StatusBarNotification sbn) {
        long t=sbn.getPostTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        super.onNotificationPosted(sbn);
        String pkg_name=sbn.getPackageName();
        String timestamp=sdf.format(new Date(t));
        String no_notf= String.valueOf(sbn.getNotification().number);
        Intent i = new  Intent("inc.synapse.notify");
        boolean f1=pkg_name.equalsIgnoreCase("com.whatsapp");
        boolean f2=pkg_name.equalsIgnoreCase("com.android.dialer");
        boolean f3=pkg_name.equalsIgnoreCase("com.android.messaging") || pkg_name.equalsIgnoreCase("com.android.mms");
        boolean f4=pkg_name.equalsIgnoreCase("com.facebook.katana");
        boolean f5=pkg_name.equalsIgnoreCase("com.google.android.gm");
        /*Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.WHATSAPP)+" : "+f1);
        Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.DAILER)+" : "+f2);
        Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.MESSAGING)+" : "+f3);
        Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.FACEBOOK)+" : "+f4);
        Log.d(TAG, ""+sbn.getPackageName()+"< >"+getString(R.string.GMAIL)+" : "+f5);*/

        if(f1 || f2 || f3 || f4 || f5) {
            if (aManager.getMode()==AudioManager.RINGER_MODE_SILENT || aManager.getMode()==AudioManager.RINGER_MODE_VIBRATE)
                aManager.setMode(AudioManager.RINGER_MODE_NORMAL);
            i.putExtra("summary_event", timestamp +" " + sbn.getPackageName()+" "+no_notf);
            i.putExtra("notification_event", timestamp +" Posted :" + sbn.getPackageName());
            write_pref(new Summary(pkg_name, timestamp, no_notf));
        }else {
            if(aManager.getMode()==AudioManager.RINGER_MODE_NORMAL || aManager.getMode()==AudioManager.RINGER_MODE_VIBRATE)
                aManager.setMode(AudioManager.RINGER_MODE_SILENT);
            this.cancelNotification(String.valueOf(sbn.getKey()));
            //i.putExtra("notification_event",timestamp+" Blocked :" + sbn.getPackageName());
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

    public void read_pref(){
        list=app_store.getInt(pref_LIST,0);
        /*Toast.makeText(getApplicationContext(), ""+list+"_", Toast.LENGTH_SHORT).show();*/
        Log.i(TAG, "reading_pref: list_"+list);
    }

    public void write_pref(Summary sm){
        //Toast.makeText(getApplicationContext(), " writing_pref: "+"_"+list+" < >"+sm.time_stamp+" "+sm.get_pckg_name()+" "+sm.no_notf, Toast.LENGTH_SHORT).show();
        //new  Intent("inc.synapse.notify").putExtra("notification_event",""+list+" writing_pref: "+"_"+list+" < >"+sm.time_stamp+" "+sm.get_pckg_name()+" "+sm.no_notf);
        app_store.edit().putString("_"+list++,sm.time_stamp+" "+sm.get_pckg_name()+" "+sm.no_notf).commit();
        app_store.edit().putInt(pref_LIST, list).commit();
        Log.i(TAG, ""+list+" writing_pref: "+"_"+list+" < >"+sm.time_stamp+" "+sm.get_pckg_name()+" "+sm.no_notf);
        Log.i(TAG, "writing_pref: list_size"+list);
    }
}
