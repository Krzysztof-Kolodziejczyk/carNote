package com.example.carnote;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button goToTankButton;
    private Button goToCollisionButton;
    private Button goToNewRepairButton;
    private Button goToNewCarButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laout);
        setTitle("Main Menu");

        initView();
    }

    private void initView() {
        goToTankButton = (Button) findViewById(R.id.go_to_tank_form_button);
        goToNewCarButton = (Button) findViewById(R.id.go_to_new_car_form_button);
        goToNewRepairButton = (Button) findViewById(R.id.go_to_repair_form_button);
        goToCollisionButton = (Button) findViewById(R.id.go_to_collison_form_button);


    }
}
