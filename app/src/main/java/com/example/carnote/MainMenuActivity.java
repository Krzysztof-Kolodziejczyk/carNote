package com.example.carnote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carnote.historylist.HistoryAdapter;
import com.example.carnote.model.AutoData;
import com.example.carnote.model.TankUpRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


/*
    Okono menu Głównego
*/

public class MainMenuActivity extends AppCompatActivity {

    public static final String SPECIAL_DATA = "Special Data";
    private static final String AUTO_PREF = "AUTO_PREF";
    private static final int NEW_REPAIR_REQUEST_CODE = 112;
    private int NEW_COLLISION_REQUEST_CODE = 6789;

    private Button goToTankFormButton;
    private Button goToNewCarButton;
    private Button goToRepairButton;
    private Button goToCollisionButton;
    private ImageView removeSingleCarButton;

    private Spinner autoChooseSpinner;

    private ArrayList<AutoData> autoList;

    private int NEW_CAR_REQUEST_CODE = 1234;
    private int FUEL_REQUEST_CODE = 1241;
    private RecyclerView historyRecycleView;

    private RecyclerView.Adapter historyAdapter;
    private RecyclerView.LayoutManager historyLayoutManager;
    private ArrayAdapter<AutoData> spinnerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);

        setTitle("Car Note");

        initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);
        }
        else
        {
            Intent intent = new Intent(this, GpsActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(this, GpsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // zapisujemy dane
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(AUTO_PREF, gson.toJson(autoList));
        editor.apply();
    }

    private void initViews() {

        goToTankFormButton = (Button) findViewById(R.id.go_to_tank_form_button);
        goToNewCarButton = (Button) findViewById(R.id.go_to_new_car_form_button);
        goToRepairButton = (Button) findViewById(R.id.go_to_repair_form_button);
        goToCollisionButton = (Button) findViewById(R.id.go_to_collison_form_button);
        removeSingleCarButton = (ImageView) findViewById(R.id.trash_bin);

        autoChooseSpinner = (Spinner) findViewById(R.id.auto_choose_spinner);

        historyRecycleView = (RecyclerView) findViewById(R.id.historyRecycleView);


        initAutoList();
        initArrayAdapter();
        autoChooseSpinner.setAdapter(spinnerAdapter);
        initRecyclerView();

        goToTankFormButton.setOnClickListener(goToTankupActivity());
        goToNewCarButton.setOnClickListener(goToNewCarActivity());
        goToRepairButton.setOnClickListener(goToRepairActivity());
        goToCollisionButton.setOnClickListener(goToCollisionActivity());
        removeSingleCarButton.setOnClickListener(removeSingleCarActivity());

        autoChooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(autoList.isEmpty())
        {
            Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
            startActivityForResult(intent, NEW_CAR_REQUEST_CODE);
        }
    }

    private View.OnClickListener removeSingleCarActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!autoList.isEmpty())
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainMenuActivity.this);
                    builder1.setMessage(R.string.car_remove_allert);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int position = autoChooseSpinner.getSelectedItemPosition();
                                    autoList.get(position).getTankUpRecord().clear();
                                    autoList.remove(position);
                                    spinnerAdapter.notifyDataSetChanged();
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        };
    }



    private void initRecyclerView() {
        historyLayoutManager = new LinearLayoutManager(this);
        historyRecycleView.setLayoutManager(historyLayoutManager);

        historyRecycleView.setHasFixedSize(true);

        historyAdapter = new HistoryAdapter(this, getCurrentAuto()!=null?getCurrentAuto().getTankUpRecord():new ArrayList<TankUpRecord>());
        historyRecycleView.setAdapter(historyAdapter);
    }


    private View.OnClickListener goToRepairActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!autoList.isEmpty()) {
                    Intent intent = new Intent(MainMenuActivity.this, RepairAppActivity.class);
                    startActivityForResult(intent, NEW_REPAIR_REQUEST_CODE);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                    builder.setMessage("ustaw auto");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
                            startActivityForResult(intent, NEW_CAR_REQUEST_CODE);
                        }
                    });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
            }
        };
    }


    private View.OnClickListener goToTankupActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // wrzuca na stos koleją aktywność i przechodzi do nowego okna
                if(!autoList.isEmpty())
                {
                    Intent intent = new Intent(MainMenuActivity.this, GasTankAppActivity.class);
                    intent.putExtra(SPECIAL_DATA, getCurrentAuto());
                    startActivityForResult(intent, FUEL_REQUEST_CODE);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                    builder.setMessage("ustaw auto");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
                            startActivityForResult(intent, NEW_CAR_REQUEST_CODE);
                        }
                    });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
            }
        };
    }

    private View.OnClickListener goToCollisionActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!autoList.isEmpty()) {
                    Intent intent = new Intent(MainMenuActivity.this, NewCollisonActivity.class);
                    startActivityForResult(intent, NEW_COLLISION_REQUEST_CODE);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                    builder.setMessage("ustaw auto");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
                            startActivityForResult(intent, NEW_CAR_REQUEST_CODE);
                        }
                    });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
            }
        };
    }

    private View.OnClickListener goToNewCarActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // wrzuca na stos koleją aktywność i przechodzi do nowego okna
                Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
                startActivityForResult(intent, NEW_CAR_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_CAR_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    AutoData newAutoData = (AutoData) data.getExtras().get(AddCarActivity.AUTO_DATA_NEW_CAR);
                    Boolean isNewCarMasterCar = (Boolean) data.getExtras().get(AddCarActivity.IS_NEW_CAR_MASTER_CAR);
                    int position = 0;
                    if(!autoList.isEmpty())
                    {
                        position = autoChooseSpinner.getSelectedItemPosition();
                    }
                    if (newAutoData != null && isNewCarMasterCar != null && isNewCarMasterCar)
                    {
                        autoList.add(0,newAutoData);
                        autoChooseSpinner.setAdapter(spinnerAdapter);
                        autoChooseSpinner.setSelection(0, true);
                        if(autoList.size() == 1)
                        {
                            autoChooseSpinner.setSelection(0);
                        }
                        else
                        {
                            autoChooseSpinner.setSelection(position+1);
                        }
                    }
                    else
                    {
                        autoList.add(newAutoData);
                        autoChooseSpinner.setAdapter(spinnerAdapter);
                        autoChooseSpinner.setSelection(autoList.size() -1 , true);
                        autoChooseSpinner.setSelection(position);
                    }
                }
            }
        }

        if (requestCode == FUEL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    TankUpRecord newTankUp = (TankUpRecord) data.getExtras().get(GasTankAppActivity.NEW_TANKUP_RECORD);
                    if (newTankUp != null)
                    {
                        getCurrentAuto().getTankUpRecord().add(0,newTankUp);
                        historyAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        if(requestCode == NEW_COLLISION_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if(data != null)
                {
                    TankUpRecord newTankUp = (TankUpRecord) data.getExtras().get(NewCollisonActivity.NEW_COLLISION_RECORD);
                    if (newTankUp != null)
                    {
                        getCurrentAuto().getTankUpRecord().add(0,newTankUp);
                        historyAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        if(requestCode == NEW_REPAIR_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if(data != null)
                {
                    TankUpRecord newTankUp = (TankUpRecord) data.getExtras().get(RepairAppActivity.NEW_REPAIR_RECORD);
                    if (newTankUp != null)
                    {
                        getCurrentAuto().getTankUpRecord().add(0,newTankUp);
                        historyAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void initArrayAdapter() {
        spinnerAdapter = new ArrayAdapter<AutoData>(this, android.R.layout.simple_spinner_dropdown_item, autoList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }



    private String getCurrentDate() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }


    private void initAutoList() {

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String string = sharedPreferences.getString(AUTO_PREF, null);
        Gson gson = new Gson();
        ArrayList<AutoData> newAutoList = gson.fromJson(string, new TypeToken<ArrayList<AutoData>>()
        {
        }.getType());

        if (newAutoList != null)
        {
            autoList = newAutoList;
        }
        else
        {
            autoList = new ArrayList<>();
        }
    }


    private AutoData getCurrentAuto() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }

}
