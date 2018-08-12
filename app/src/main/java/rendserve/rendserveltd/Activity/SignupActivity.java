package rendserve.rendserveltd.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

public class SignupActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_full_name,et_email,et_password, et_company, et_company_size, et_number;
    private TextView tv_submit;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
        setListener();
    }

    private void initialize() {

        et_full_name = (EditText)findViewById(R.id.et_full_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        et_company = (EditText)findViewById(R.id.et_company);
        et_company_size = (EditText)findViewById(R.id.et_company_size);
        et_number = (EditText)findViewById(R.id.et_number);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
    }
    private void setListener() {
        tv_submit.setOnClickListener(this);
        imageView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageView2:
                finish();
                break;
            case R.id.tv_submit:
                String user_names = et_full_name.getText().toString().trim();
                String email_address = et_email.getText().toString().trim();
                String Pass = et_password.getText().toString().trim();
                String company_ = et_company.getText().toString().trim();
                String companysize = et_company_size.getText().toString().trim();
                String phoneNumber = et_number.getText().toString().trim();
                if(user_names.length() == 0)
                {
                    showToast("Please enter username");
                }
                else if(email_address.length() == 0)
                {
                    showToast("Please enter email");
                }
                else if (!email_address.equals("") && !isValidEmail(email_address)){
                    showToast("Please enter a valid Email Id");
                }
                else if(Pass.length() == 0)
                {
                    showToast("Please enter password");
                }
                else if(company_.length() == 0)
                {
                    showToast("Please enter company");
                }
                else if(companysize.length() == 0)
                {
                    showToast("Please select company size");
                }
                else if(phoneNumber.length() == 0)
                {
                    showToast("Please enter phone number");
                }
                else {
                    createAccount(user_names,email_address,Pass,company_,companysize,phoneNumber);
                }
                break;
        }
    }

    private void createAccount(String user_names, String email_address, String pass, String company_, String companysize, String phoneNumber) {
        final ProgressDialog d = CustomProgressDialog.showLoading(SignupActivity.this);
        d.setCanceledOnTouchOutside(false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<LoginPojo> call = apiService.signup(user_names, email_address,pass,company_,companysize,phoneNumber);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
               if(response.body().getStatus().equals("1")){
                   showToast(response.body().getMessage());
                   //startActivity(new Intent(getApplicationContext(),MainDeshboardActivity.class));
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

    //  startActivity(new Intent(getApplicationContext(),MainDeshboardActivity.class));


    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
