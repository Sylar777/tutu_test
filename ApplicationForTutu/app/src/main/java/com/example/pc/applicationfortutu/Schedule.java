package com.example.pc.applicationfortutu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.widget.DatePicker;

/**
 * Activity "Расписание"
 *
 * 1. onActivityResult
 * 2. SharedPreferences
 */
public class Schedule extends Activity {
    public ArrayList<Station> arrayList = new ArrayList<>();
    public ArrayList<Station> arrStationFrom = new ArrayList<>();
    public ArrayList<Station> arrStationTo = new ArrayList<>();
    public Button CheckedTextViewFrom;
    public Button CheckedTextViewTo;
    public TextView TextViewFrom;
    public TextView TextViewTo;
    public TextView textView;
    String From = "From";
    String To = "To";
    int DIALOG_DATE = 1;
    int myYear;
    int myMonth;
    int myDay;

    SharedPreferences sPref;
    String savedText;
    final String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        createSpisok();

        for (Station station1 : arrayList) {
            if (station1.getType().equals("From")) {                                                // Разделяем Список БД на 2 списка (по признаку From и To)
                arrStationFrom.add(station1);
            } else {
                arrStationTo.add(station1);
            }
        }

        CheckedTextViewFrom = (Button) findViewById(R.id.CheckedTextViewFrom);
        CheckedTextViewTo = (Button) findViewById(R.id.CheckedTextViewTo);
        TextViewFrom = (TextView) findViewById(R.id.TextViewFrom);
        TextViewTo = (TextView) findViewById(R.id.TextViewTo);
        textView = (TextView) findViewById(R.id.textView);

        loadText();                                                                                 // Загрузка SharedPreferences для Даты
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {                 // присвоение название выбранной станции TextView
        if (resultCode == 1) {
            try {
                String BD_Station = data.getStringExtra("POSITIVE_CHOOSE_Station");                 // Название станции для отображения
                String BD_Type = data.getStringExtra("POSITIVE_CHOOSE_Type");                       // Тип станции From/To - для распознования какому TextView присваивать название
                if (BD_Type.equals("From")) {
                    TextViewFrom.setText(BD_Station);
                } else {
                    TextViewTo.setText(BD_Station);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void scheduleClickFrom(View view) {                                                      // Кнопка "Нажмите для выбора Станции Отправления"
        Intent intent = new Intent(Schedule.this, ChooseStation.class);
        intent.putExtra("FromTo", From);                                                            // Отправка значения Типа
        startActivityForResult(intent, 1);
    }

    public void scheduleClickTo(View view) {                                                        // Кнопка "Нажмите для выбора Станции Прибытия"
        Intent intent = new Intent(Schedule.this, ChooseStation.class);
        intent.putExtra("FromTo", To);                                                              // Отправка значения Типа
        startActivityForResult(intent, 2);
    }


    public void createSpisok() {
        HelperStation helperStation = OpenHelperManager.getHelper(this, HelperStation.class);
        Dao<Station, Long> dao = null;
        try {
            dao = helperStation.getDao(Station.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        arrayList = null;
        try {
            arrayList = (ArrayList) dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onclick(View view) {
        showDialog(DIALOG_DATE);
    }


    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear;
            myDay = dayOfMonth;
            textView.setText("Дата  " + myDay + "/" + myMonth + "/" + myYear);
            saveText();
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        loadText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);                                                       // Сохранение в SharedPreferences выбранной Даты
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, textView.getText().toString());
        ed.commit();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);                                                       // Загрузка из SharedPreferences выбранной Даты
        savedText = sPref.getString(SAVED_TEXT, "Выберите дату");
        textView.setText(savedText);
    }
}
