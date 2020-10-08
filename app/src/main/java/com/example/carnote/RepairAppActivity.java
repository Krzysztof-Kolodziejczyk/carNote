package com.example.carnote;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carnote.model.TankUpRecord;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/*
okno dodania nowej naprawy
 */
public class RepairAppActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String NEW_REPAIR_RECORD = "NEW_REPAIR_RECORD";
    private TextView repairDateEditTextLabel;
    private TextView repairCostEditTextLabel;
    private TextView repairCarNameDateEditTextLabel;

    private EditText repairDateEditText;
    private EditText repairCostEditText;
    private EditText repairCarNameDateEditText;

    private Button repairConfirmButton;

    private DateFormat dateFormat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reapir_car_layout);
        setTitle("car repair");
        viewInit();
        //obtainExtras();
    }

    private void viewInit()
    {
        repairDateEditTextLabel = (TextView) findViewById(R.id.repair_date);
        repairCostEditTextLabel = (TextView) findViewById(R.id.reapir_cost);
        repairCarNameDateEditTextLabel = (TextView) findViewById(R.id.rapair_car_name);

        repairDateEditText = (EditText) findViewById(R.id.reapir_date_edit);
        repairCostEditText = (EditText) findViewById(R.id.repair_cost_edit);
        repairCarNameDateEditText = (EditText) findViewById(R.id.reapir_car_name_edit);

        repairConfirmButton = (Button) findViewById(R.id.repair_confirm_button);


        repairDateEditText.setText(getCurrentDate());

        repairDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RepairAppActivity.this, RepairAppActivity.this, year, month, day);

                datePickerDialog.show();
            }
        });

        repairCostEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // wyjście z kontrolki
                if (! hasFocus)
                {
                    validateRepairCost();
                }
            }

        });

        repairCarNameDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // wyjście z kontrolki
                if (! hasFocus)
                {
                    validateRepairCarName();
                }
            }

        });

        repairConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateRepairCarName() && validateRepairCost())
                {
                    TankUpRecord tank = null;
                    try {
                        //todo do poprawki
                        tank = new TankUpRecord(getdateDate(), -1, Integer.valueOf(repairCostEditText.getText().toString()), -1, repairCarNameDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra(NEW_REPAIR_RECORD, tank);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });


    }

    private boolean validateRepairCarName()
    {
        if(TextUtils.isEmpty(repairCarNameDateEditText.getText().toString()))
        {
            repairCarNameDateEditTextLabel.setText(getResources().getString(R.string.set_reapir_car_name));
            repairCarNameDateEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        else
        {
            repairCarNameDateEditTextLabel.setText(getResources().getString(R.string.repair_car_name));
            repairCarNameDateEditTextLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }

    private boolean validateRepairCost()
    {
        if(TextUtils.isEmpty(repairCostEditText.getText().toString()))
        {
            repairCostEditTextLabel.setText(getResources().getString(R.string.set_repair_cost));
            repairCostEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        if(Integer.parseInt(repairCostEditText.getText().toString()) <= 0)
        {
            repairCostEditTextLabel.setText(getResources().getString(R.string.given_repair_cost_error));
            repairCostEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        else
        {
            repairCostEditTextLabel.setText(getResources().getString(R.string.repair_cost));
            repairCostEditTextLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }


    private String getCurrentDate() {
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year,month,dayOfMonth);
        repairDateEditText.setText(dateFormat.format(calendar.getTime()));
    }

    private Date getdateDate() throws ParseException {
        return  dateFormat.parse(repairDateEditText.getText().toString());
    }
}
