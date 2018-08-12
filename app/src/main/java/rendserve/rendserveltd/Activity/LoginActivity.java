package rendserve.rendserveltd.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rendserve.rendserveltd.Pojo.LoginPojo;
import rendserve.rendserveltd.R;
import rendserve.rendserveltd.model.ApiClient;
import rendserve.rendserveltd.model.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.ConnectionDetector;
import utils.CustomProgressDialog;
import utils.RendserveConstant;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

   private EditText et_email, et_password;
   private TextView tv_forgot_password, tv_login, tv_sign_up;
   private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
        setListener();
    }

    private void setListener() {
        tv_forgot_password.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_sign_up.setOnClickListener(this);
    }

    private void initialize() {
        cd = new ConnectionDetector(this);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_forgot_password:
                if (cd.isConnectingToInternet()){

                }else
                    showToast("Internet connection appears to be offline");
                break;
            case R.id.tv_login:
                if (cd.isConnectingToInternet()){
                    validate();
                    //login();
                }else
                    showToast("Internet connection appears to be offline");
                break;
            case R.id.tv_sign_up:
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }

    }

    private void validate(){
        String email = et_email.getText().toString().trim();
        String pass = et_password.getText().toString().trim();
        if (email.equals("")){
            showToast("Please enter a Email Id");
        }
        else if (!email.equals("") && !isValidEmail(email)){
            showToast("Please enter a valid Email Id");
        }else if(pass.equals("")){
            showToast("Please enter the Password");
        }else {
            doLogin(email,pass,"");
        }

    }

    private void doLogin(String email_val, String pass,String token) {
        final ProgressDialog d = CustomProgressDialog.showLoading(LoginActivity.this);
        d.setCanceledOnTouchOutside(false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<LoginPojo> call = apiService.login(email_val, pass,token);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                if(response.body().getStatus().equals("1")){
                    String user_id = response.body().getData().getId();
                    String user_name = response.body().getData().getFull_name();
                    String email  = response.body().getData().getEmail();
                    saveIntoPrefs(RendserveConstant.USER_ID,user_id,email,user_name );
                    startActivity(new Intent(LoginActivity.this, MainDeshboardActivity.class));
                    finish();
                }else
                    showToast(response.body().getMessage());
                d.dismiss();
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                // Log error here since request failed
                System.out.println("retrofit hh failure " + t.getMessage());
                d.dismiss();
            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
