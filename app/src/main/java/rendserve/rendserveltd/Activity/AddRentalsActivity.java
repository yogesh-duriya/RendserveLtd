package rendserve.rendserveltd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import rendserve.rendserveltd.R;

public class AddRentalsActivity extends AppCompatActivity {

    private EditText et_equipment, et_hour, et_cost, et_total_cost;
    private TextView tv_save, tv_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rentals);

        initialize();

        setListener();
    }

    private void setListener() {
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("equipment" , et_equipment.getText().toString());
                intent.putExtra("cost" , et_cost.getText().toString());
                intent.putExtra("work_hour" , et_hour.getText().toString());
                intent.putExtra("total_cost" , et_total_cost.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_equipment.setText("");
                et_cost.setText("");
                et_hour.setText("");
                et_total_cost.setText("");
            }
        });
    }

    private void initialize() {
        et_equipment = (EditText)findViewById(R.id.et_equipment);
        et_cost = (EditText)findViewById(R.id.et_cost);
        et_hour = (EditText)findViewById(R.id.et_hour);
        et_total_cost = (EditText)findViewById(R.id.et_total_cost);
        tv_reset = (TextView) findViewById(R.id.tv_reset);
        tv_save = (TextView) findViewById(R.id.tv_save);

    }
}
