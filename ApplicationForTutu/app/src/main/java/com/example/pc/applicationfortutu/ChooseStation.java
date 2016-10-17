package com.example.pc.applicationfortutu;


import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity "Выбор Станции"
 *
 * 1. Сортировка
 * 2. Поиск
 * 3. AlertDialog
 * 4. Информация по Станции
 * 5. setResult - intent
 */
public class ChooseStation extends ListActivity {
    public ArrayList<Station> arrayList = new ArrayList<>();
    public ArrayList<Station> arrStationFrom = new ArrayList<>();
    public ArrayList<Station> arrStationTo = new ArrayList<>();
    public ArrayList<Station> arrayListSort = new ArrayList<>();
    String FromTo;
    Station station_choose;
    EditText search_edit;
    Button search_button;
    StationAdapter stationAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_station);

        search_edit = (EditText) findViewById(R.id.search_edit);
        search_button = (Button) findViewById(R.id.search_Button);

        FromTo = (String) getIntent().getSerializableExtra("FromTo");

        createSpisok();                                                                             // Записование в список значения из БД
        arrayListSort = (ArrayList<Station>) sort();                                                // Сортировка

        arrStationFrom.clear();
        arrStationTo.clear();

        for (Station station1 : arrayListSort) {                                                    // Заполнение двух списков для Адаптера
            if((station1.getType()).equals("From")){
                arrStationFrom.add(station1);
            } else {
                arrStationTo.add(station1);
            }
        }

        stationAdapter = new StationAdapter(this,arrayList);

        switch (FromTo){                                                                            // Передача Адаптеру списка в зависимости от значения в Intent
            case "From":
                stationAdapter = new StationAdapter(this,arrStationFrom);
                break;
            case "To":
                stationAdapter = new StationAdapter(this,arrStationTo);
                break;
        }
        final ListView listView = getListView();
        listView.setAdapter(stationAdapter);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                   // Кнопка "Поиск"
                arrStationFrom.clear();
                arrStationTo.clear();

                List<Station> searchList = searchStation(String.valueOf(search_edit.getText()).toLowerCase());      // Список найденного путём сравнения в пониженном регистре с полем в БД в пониженном регистре (Пониженный регистр используется из за кирилицы)
                for (Station station1 : searchList) {
                    if (station1.getType().equals("From")) {
                        arrStationFrom.add(station1);
                    } else {
                        arrStationTo.add(station1);
                    }
                }
                switch (FromTo){
                    case "From":
                        stationAdapter.notifyDataSetChanged();
                        stationAdapter = new StationAdapter(ChooseStation.this, arrStationFrom);
                        listView.setAdapter(stationAdapter);
                        break;
                    case "To":
                        stationAdapter.notifyDataSetChanged();
                        stationAdapter = new StationAdapter(ChooseStation.this, arrStationTo);
                        listView.setAdapter(stationAdapter);
                        break;
                }
            }
        });
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {                     // Слушатель нажатия на элемент Списка
        super.onListItemClick(l, v, position, id);
        station_choose = getItem(position);

        onCreatedDialog();
    }

    public Station getItem(int position) {
        switch (FromTo){
            case "From":
                return arrStationFrom.get(position);
            case "To":
                return arrStationTo.get(position);
        }
        return null;
    }

    protected void onCreatedDialog(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Выбрать Станцию").
                setMessage("Вы хотите выбрать эту станцию?").
                setPositiveButton("Да", myClickListener).
                setNegativeButton("Нет", myClickListener).
                setNeutralButton("Инфо", myClickListener);
        AlertDialog alert = adb.create();
        alert.show();
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    Intent intent = new Intent(ChooseStation.this,Schedule.class);
                    intent.putExtra("POSITIVE_CHOOSE_Station", station_choose.getStationTitle());
                    intent.putExtra("POSITIVE_CHOOSE_Type", station_choose.getType());
                    switch (FromTo){
                        case "From":
                            setResult(1,intent);                                                    // Ответный intent
                            finish();
                            break;
                        case "To":
                            setResult(1,intent);
                            finish();
                            break;
                    }
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
                case Dialog.BUTTON_NEUTRAL:                                                         // По кнопке "Инфо" - отображается полная информация об адресе в новом диалоге
                    String info = station_choose.getCountryTitle() + "\n" +
                            station_choose.getCityTitle() + "\n" +
                            station_choose.getDistrictTitle() + "\n" +
                            station_choose.getRegionTitle() + "\n" +
                            station_choose.getStationTitle();
                    onCreatedDialog2(info);
                    break;
            }
        }
    };

    protected void onCreatedDialog2(String message){
        AlertDialog.Builder adb2 = new AlertDialog.Builder(this);
        adb2.setTitle("Информация по Станции").
                setMessage(message).
                setPositiveButton("Ок", myClickListener2);
        AlertDialog alert = adb2.create();
        alert.show();
    }

    DialogInterface.OnClickListener myClickListener2 = new DialogInterface.OnClickListener() {      // Слушатель для Диалога с Информацией
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    break;
            }
        }
    };

    private List<Station> searchStation (String string){
        return DataBaseManager.getInstance().search(string);
    }

    private List<Station> sort (){
        return DataBaseManager.getInstance().sort();
    }

    public void createSpisok() {
        HelperStation helperStation = OpenHelperManager.getHelper(this, HelperStation.class);
        Dao<Station, Long> dao = null;
        try {
            dao= helperStation.getDao(Station.class);
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
}
