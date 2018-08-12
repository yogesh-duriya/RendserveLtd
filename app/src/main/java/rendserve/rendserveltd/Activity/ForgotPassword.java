package rendserve.rendserveltd.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import rendserve.rendserveltd.Pojo.LoginPojo;
import rendserve.rendserveltd.R;
import rendserve.rendserveltd.model.ApiClient;
import rendserve.rendserveltd.model.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.CustomProgressDialog;

public class ForgotPassword extends BaseActivity implements View.OnClickListener {

    private ImageView imageView2;
    private TextView tv_submit;
    private EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        setListener();
    }

    private void setListener() {
        imageView2.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    private void init() {
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        tv_submit  =(TextView)findViewById(R.id.tv_submit);
        et_email = (EditText)findViewById(R.id.et_email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView2:
                finish();
                break;
            case R.id.tv_submit:
                String email = et_email.getText().toString();
                if(email.isEmpty()){
                    showToast(getString(R.string.email_address));
                }else{
                    doForgotPassword(email);
                }
                break;
        }
    }
    private void doForgotPassword(String email) {
        final ProgressDialog d = CustomProgressDialog.showLoading(ForgotPassword.this);
        d.setCanceledOnTouchOutside(false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<LoginPojo> call = apiService.forgotPassword(email);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                if(response.body().getStatus().equals("1")){
                    showToast(response.body().getMessage());
                    finish();
                }
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
}
