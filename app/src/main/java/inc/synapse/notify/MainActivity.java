package inc.synapse.notify;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lv1;
    //private TextView tv1;
    private String PACKAGE_NAME;
    private String TAG;
    private String pref_LIST="LIST";
    private int list=0;
    ContentResolver contentResolver;
    String enabledNotificationListeners;
    ArrayList<String> listsummary=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private nReceiver nreceiver;
    private SharedPreferences app_store;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        IntentFilter filter= new IntentFilter();
        filter.addAction("inc.synapse.notify");
        registerReceiver(nreceiver, filter);
    }

    protected void onResume() {
        super.onResume();
        contentResolver = this.getContentResolver();
        enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(PACKAGE_NAME)){
            Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }
        read_pref();
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nreceiver);
    }

    private void init() {
        lv1= (ListView) findViewById(R.id.lv1);
//        tv1= (TextView) findViewById(R.id.tv1);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listsummary);
        lv1.setAdapter(adapter);
        nreceiver=new nReceiver();
        PACKAGE_NAME = getApplicationContext().getPackageName();
        TAG = this.getClass().getSimpleName();
        app_store=getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }


    class nReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            /*String tmp;
            tmp = "\n"+intent.getStringExtra("notification_event");
            tv1.append(tmp);
            tmp = intent.getStringExtra("summary_event");
            if(tmp!=null)
                adapter.add(tmp);*/
            read_pref();
        }
    }

    public void read_pref(){
        list=app_store.getInt(pref_LIST,0);
        Log.i(TAG, "reading_pref: list_size"+list);
        adapter.clear();
        for (int i=0; i<list; i++) {

            String tmp = app_store.getString("_" + i, null);
            if (tmp != null)
                adapter.add(tmp);
        }
    }

}
