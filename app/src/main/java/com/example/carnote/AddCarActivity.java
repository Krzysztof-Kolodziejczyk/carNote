package com.example.carnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carnote.model.AutoData;

/*
okno dodania nowego auta
 */
public class AddCarActivity extends AppCompatActivity {

    private EditText makeEditText;
    private EditText modelEditText;
    private EditText colorEditText;

    private TextView modelEditTextLabel;
    private TextView colorEditTextLabel;
    private TextView makeEditTextLabel;

    private Switch masterSwitch;

    private Button confirmeButton;

    public static String AUTO_DATA_NEW_CAR = "AUTO_DATA_NEW_CAR";
    public static String IS_NEW_CAR_MASTER_CAR = "IS_NEW_CAR_MASTER_CAR";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_layout);

        makeEditText = (EditText) findViewById(R.id.makeEditText);
        modelEditText = (EditText) findViewById(R.id.modelEditText);
        colorEditText = (EditText) findViewById(R.id.colorEditText);

        modelEditTextLabel = (TextView) findViewById(R.id.modelEditTextLabel);
        colorEditTextLabel = (TextView) findViewById(R.id.colorEditTextLabel);
        makeEditTextLabel = (TextView) findViewById(R.id.makeEditTextLabel);


        masterSwitch = (Switch) findViewById(R.id.switch_confirme);

        confirmeButton = (Button) findViewById(R.id.confirmButton);


        modelEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // wyjście z kontrolki
                if (! hasFocus)
                {
                    validateModel();
                }
            }
        });


        makeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // wyjście z kontrolki
                if (! hasFocus)
                {
                    validateMake();
                }
            }
        });

        colorEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // wyjście z kontrolki
                if (! hasFocus)
                {
                    validateColor();
                }
            }
        });

        confirmeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateColor();
                validateMake();
                validateModel();
                if(validateModel() && validateMake() && validateColor())
                {
                    AutoData autoData = new AutoData(modelEditText.getText().toString() ,makeEditText.getText().toString(), colorEditText.getText().toString());
                    boolean isMasterCar = masterSwitch.isChecked();
                    Intent intent = new Intent();
                    intent.putExtra(AUTO_DATA_NEW_CAR, autoData);
                    intent.putExtra(IS_NEW_CAR_MASTER_CAR, isMasterCar);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            };
        });
    }

    private boolean validateModel() {
        if(TextUtils.isEmpty(modelEditText.getText().toString()))
        {
            modelEditTextLabel.setText(getResources().getString(R.string.set_model));
            modelEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        else
        {
            modelEditTextLabel.setText(getResources().getString(R.string.model));
            modelEditTextLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }

    private boolean validateMake() {
        if(TextUtils.isEmpty(makeEditText.getText().toString()))
        {
            makeEditTextLabel.setText(getResources().getString(R.string.set_make));
            makeEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        else
        {
            makeEditTextLabel.setText(getResources().getString(R.string.make));
            makeEditTextLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }

    private boolean validateColor() {
        if(TextUtils.isEmpty(colorEditText.getText().toString()))
        {
            colorEditTextLabel.setText(getResources().getString(R.string.set_color));
            colorEditTextLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        else
        {
            colorEditTextLabel.setText(getResources().getString(R.string.color));
            colorEditTextLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }

}
