package rendserve.rendserveltd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import rendserve.rendserveltd.R;

public class AddSubContractActivity extends AppCompatActivity {

    private EditText et_name, et_subcontract, et_work_hour, et_travel_hour;
    private TextView tv_save, tv_reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_contract);

        initialize();

        setListener();
    }

    private void setListener() {
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("name" , et_name.getText().toString());
                intent.putExtra("subcontract" , et_subcontract.getText().toString());
                intent.putExtra("work_hour" , et_work_hour.getText().toString());
                intent.putExtra("travel_hour" , et_travel_hour.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_name.setText("");
                et_subcontract.setText("");
                et_work_hour.setText("");
                et_travel_hour.setText("");
            }
        });
    }

    private void initialize() {
        et_name = (EditText)findViewById(R.id.et_name);
        et_subcontract = (EditText)findViewById(R.id.et_subcontract);
        et_work_hour = (EditText)findViewById(R.id.et_work_hour);
        et_travel_hour = (EditText)findViewById(R.id.et_travel_hour);
        tv_reset = (TextView) findViewById(R.id.tv_reset);
        tv_save = (TextView) findViewById(R.id.tv_save);

    }
}
