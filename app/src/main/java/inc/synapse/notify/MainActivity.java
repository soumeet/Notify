package inc.synapse.notify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private String PACKAGE_NAME;
    private String TAG;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        tv1.setText(""+PACKAGE_NAME+":"+TAG);
    }

    private void init() {
        tv1= (TextView) findViewById(R.id.tv1);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        TAG = this.getClass().getSimpleName();
    }


}
