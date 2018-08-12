package rendserve.rendserveltd.Activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import rendserve.rendserveltd.R;
import utils.ConnectionDetector;
import utils.RendserveConstant;

public class SplashActivity extends BaseActivity {

    private static final int STOPSPLASH = 0;
    private static final int SPLASHTIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        Message msg = new Message();
        msg.what=STOPSPLASH;
        splashHandler.sendMessageDelayed(msg,SPLASHTIME);
    }
    private Handler splashHandler =new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case STOPSPLASH:
                    if (new ConnectionDetector(SplashActivity.this).isConnectingToInternet()) {
                        if (!getFromPrefs(RendserveConstant.USER_ID).equals("")) {
                            startActivity(new Intent(SplashActivity.this, MainDeshboardActivity.class));
                            finish();
                        }else {
                            startIntent(getApplicationContext(), LoginActivity.class);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                    }
            }
            super.handleMessage(msg);
        }
    };
}
