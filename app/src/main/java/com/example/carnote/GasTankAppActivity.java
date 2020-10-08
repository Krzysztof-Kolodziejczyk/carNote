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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carnote.model.AutoData;
import com.example.carnote.model.TankUpRecord;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class GasTankAppActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String NEW_TANKUP_RECORD = "NEW_TANKUP_RECORD";
    private static final String NEW_TANKUP = "NEW_TANKUP";
    private static final String AUTO_DATA_OBJ = "AUTO_DATA_OBJ";
    private EditText dateEditText;
    private EditText millageEditText;
    private EditText litersEditText;
    private EditText costEditText;

    private Button confirmButtom;

    private AutoData autodata;

    private DateFormat dateFormat;
    private TextView millageEditTextLabel;
    private TextView litersEditTextLabel;
    private TextView costEditTextLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
        {
            autodata = (AutoData) savedInstanceState.get(AUTO_DATA_OBJ);
        }
        setContentView(R.layout.gas_tank_app_layout);
        obtainExtras();
        setTitle(getResources().getString(R.string.new_tankup));
        viewInit();
    }

    private void obtainExtras() {
        autodata = (AutoData) getIntent().getExtras().getSerializable(MainMenuActivity.SPECIAL_DATA);
    }

    private void viewInit() {
        dateEditText = (EditText) findViewById(R.id.Date);
        millageEditText = (EditText) findViewById(R.id.millage);
        litersEditText = (EditText) findViewById(R.id.liters);
        costEditText = (EditText) findViewById(R.id.cost);

        millageEditTextLabel = (TextView) findViewById(R.id.millage_label);
        litersEditTextLabel = (TextView) findViewById(R.id.litersLabel);
        costEditTextLabel = (TextView) findViewById(R.id.costLabel);




        // ustawia defoultowo w polu "data" aktulaną datę
        dateEditText.setText(getCurrentDate());

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(GasTankAppActivity.this, GasTankAppActivity.this, year, month, day);

                datePickerDialog.show();
            }
        });

        millageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // wyjście z kontrolki
                if (! hasFocus)
                {
                    validateMIllage();
                }
            }
        });

        litersEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (! hasFocus)
                {
                    validateLiters();
                }
            }
        });

        costEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(! hasFocus)
                {
                    validateCost();
                }
            }
        });

        confirmButtom = (Button) findViewById(R.id.confirm);

        confirmButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (validateMIllage() && validateLiters() && validateCost()){
                    TankUpRecord tank = null;
                    try {
                        tank = new TankUpRecord(getdateDate(), getmillageInteger(), getlitersInteger(), getcostInteger(), null);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra(NEW_TANKUP_RECORD, tank);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                }
            }
        });
    }
    // koniec viewInit

    private boolean validateCost() {
        if(TextUtils.isEmpty(costEditText.getText().toString()))
        {
            costEditTextLabel.setText(getResources().getString(R.string.set_cost));
            costEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        if(Integer.parseInt(costEditText.getText().toString()) <= 0)
        {
            costEditTextLabel.setText(getResources().getString(R.string.given_cost_error));
            costEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        else
        {
            costEditTextLabel.setText(getResources().getString(R.string.cost));
            costEditTextLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }

    private boolean validateLiters() {
        if(TextUtils.isEmpty(litersEditText.getText().toString()))
        {
            litersEditTextLabel.setText(getResources().getString(R.string.set_liters));
            litersEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        if(Integer.parseInt(litersEditText.getText().toString()) <= 0)
        {
            litersEditTextLabel.setText(getResources().getString(R.string.given_liters_error));
            litersEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        else
        {
            litersEditTextLabel.setText(getResources().getString(R.string.liters));
            litersEditTextLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }

    private boolean validateMIllage() {

        if (TextUtils.isEmpty(millageEditText.getText().toString()))
        {
            millageEditTextLabel.setText(getResources().getString(R.string.set_millage));
            millageEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }

        if (Integer.parseInt(millageEditText.getText().toString()) <= 0)
        {
            millageEditTextLabel.setText(getResources().getString(R.string.given_millage_error));
            millageEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }

        int size = autodata.getTankUpRecord().size();
        if (size > 0)
        {
            Integer newMillage = Integer.valueOf(millageEditText.getText().toString());
            Integer oldBestMillage = -1; //= autodata.getTankUpRecord().get(size-1).getMiallage();
            for(int i=0; i<size; i++)
            {
                if(autodata.getTankUpRecord().get(i).getMiallage() > oldBestMillage) oldBestMillage = autodata.getTankUpRecord().get(i).getMiallage();
            }
            if ( newMillage <= oldBestMillage)
            {
                millageEditTextLabel.setText(getResources().getString(R.string.equals_millage_eror));
                millageEditTextLabel.setTextColor(getResources().getColor(R.color.red));
                return false;
            }
            else
            {
                millageEditTextLabel.setText(getResources().getString(R.string.millage));
                millageEditTextLabel.setTextColor(getResources().getColor(R.color.black));
                return true;
            }
        }
        return true;
    }


    private Integer getcostInteger() {
        return Integer.valueOf(costEditText.getText().toString());
    }

    private Integer getlitersInteger() {
        return Integer.valueOf(litersEditText.getText().toString());
    }

    private Integer getmillageInteger() {
        return Integer.valueOf(millageEditText.getText().toString());
    }

    private Date getdateDate() throws ParseException {
        return  dateFormat.parse(dateEditText.getText().toString());
    }

    /*
    oblicza koszt jednego litra paliwa
     */
    private String getOneLiterCost() {
        return String.valueOf(Double.valueOf((costEditText.getText().toString()))/Double.valueOf(litersEditText.getText().toString()));
    }


    // pobiera i zwraca aktualną datę
    private String getCurrentDate() {
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putSerializable(AUTO_DATA_OBJ, autodata);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year,month,dayOfMonth);
        dateEditText.setText(dateFormat.format(calendar.getTime()));
    }
}