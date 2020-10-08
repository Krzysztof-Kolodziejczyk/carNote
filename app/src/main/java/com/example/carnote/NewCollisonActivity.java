package com.example.carnote;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class NewCollisonActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String NEW_COLLISION_RECORD = "NEW_COLLISION_RECORD";
    private EditText dateEditText;
    private EditText carNameEditText;
    private TextView dateEditTextLabel;
    private TextView carNameEditTextLabel;
    private Button newCollisonConfirmButton;


    private DateFormat dateFormat;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setTitle("new collsion");
        setContentView(R.layout.new_collision_layout);
        viewInit();
        obtainExtras();
        super.onCreate(savedInstanceState);
    }

    private void viewInit()
    {
        dateEditText = (EditText) findViewById(R.id.collisionDateEditText);
        carNameEditText = (EditText) findViewById(R.id.collsionCarEditText);

        dateEditTextLabel = (TextView) findViewById(R.id.collision_date);
        carNameEditTextLabel = (TextView) findViewById(R.id.colllison_car);

        newCollisonConfirmButton = (Button) findViewById(R.id.new_collsion_confirm_button);

        dateEditText.setText(getCurrentDate());

        // wyskoczy okno kalendarza do wyboru daty (fajne)
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewCollisonActivity.this, NewCollisonActivity.this, year, month, day);

                datePickerDialog.show();
            }
        });

        carNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // wyjście z kontrolki
                if (! hasFocus)
                {
                    validateCarName();
                }
            }

        });

        newCollisonConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateCarName())
                {
                    TankUpRecord tank = null;
                    try {
                        //todo do poprawki
                        tank = new TankUpRecord(getdateDate(), -1, -1, -1, carNameEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra(NEW_COLLISION_RECORD, tank);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    private boolean validateCarName()
    {
        if(TextUtils.isEmpty(carNameEditText.getText().toString()))
        {
            carNameEditTextLabel.setText(getResources().getString(R.string.set_car_name));
            carNameEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        else
        {
            carNameEditTextLabel.setText(getResources().getString(R.string.car_name));
            carNameEditTextLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }



    private void obtainExtras()
    {

    }

    // zwraca aktualną date w formacie mm-dd-rr w stringu
    private String getCurrentDate() {
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }



    // potrzbne żeby doimplementować do klasy NewCollison "DatePickerDialog.OnDateSetListener"
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year,month,dayOfMonth);
        dateEditText.setText(dateFormat.format(calendar.getTime()));
    }

    private Date getdateDate() throws ParseException {
        return  dateFormat.parse(dateEditText.getText().toString());
    }
}
