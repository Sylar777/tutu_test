package com.example.pc.applicationfortutu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sosnovskiy D. A. on 15.10.2016 for Tutu.
 *
 * Главное Activity
 * 1. В ParseTask обрабатывается JSON
 * 2. Сохранение в БД
 */

public class MainActivity extends AppCompatActivity {
    private DataBaseManager databaseHelper = null;
    public ArrayList<Station> arrayList = new ArrayList<>();
    public Button schedule;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseManager.init(this);
        new ParseTask().execute();

        schedule = (Button) findViewById(R.id.schedule);
    }

    public void scheduleMainClick(View view) {                                                      // Кнопка "Расписание"
        Intent intent = new Intent(MainActivity.this, Schedule.class);
        startActivity(intent);
    }
    public void aboutMainClick(View view) {                                                         // Кнопка "О Приложении"
        Intent intent = new Intent(MainActivity.this, AboutApp.class);
        startActivity(intent);
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String url_now = "https://raw.githubusercontent.com/tutu-ru/hire_android_test/master/allStations.json";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Загрузка приложения.");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(url_now);

                urlConnection = (HttpURLConnection) url.openConnection();                           // Получения данных с интернет-ресурса
                urlConnection.setRequestMethod("GET");                                              // Requests data from a specified resource
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject dataJsonObj = null;
            try {
                dataJsonObj = new JSONObject(resultJson);                                           // Создание JSON Объекта
                JSONArray citiesFrom = dataJsonObj.getJSONArray("citiesFrom");                      // Запись в JSON список  по ключу "citiesFrom"
                JSONArray citiesTo = dataJsonObj.getJSONArray("citiesTo");                          // Запись в JSON список  по ключу "citiesTo"

                createSpisok();

                if(!arrayList.isEmpty()) {                                                          // Для обновления информации - Очищается БД (для дальнейшего сохранения значений из JSON)
                    clearBD();
                }

                if (arrayList.isEmpty()) {                                                          // Если актуальный список данных из БД пуст, то парсим JSON и производим сохрание в БД (внутри метода parseJSON)
                    parseJSON(citiesFrom, "From");
                    parseJSON(citiesTo, "To");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            mProgressDialog.dismiss();                                                              // Отключение ProgressDialog
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();                                                      // Завершение работы с БД
            databaseHelper = null;
        }
    }

    public void parseJSON(JSONArray citiesWhere, String where) {
        try {
            for (int i = 0; i < citiesWhere.length(); i++) {
                JSONObject obj_cities = citiesWhere.getJSONObject(i);
                JSONArray arr = obj_cities.getJSONArray("stations");

                for (int x = 0; x < arr.length(); x++) {
                    Station station = new Station();
                    JSONObject obj2 = arr.getJSONObject(x);

                    station.setCountryTitle(obj2.getString("countryTitle"));
                    station.setDistrictTitle(obj2.getString("districtTitle"));
                    station.setCityId(obj2.getString("cityId"));
                    station.setCityTitle(obj2.getString("cityTitle"));
                    station.setRegionTitle(obj2.getString("regionTitle"));
                    station.setStationId(obj2.getString("stationId"));
                    station.setStationTitle(obj2.getString("stationTitle"));

                    JSONObject obj3 = obj_cities.getJSONObject("point");

                    station.setLongitude(obj3.getString("longitude"));
                    station.setLatitude(obj3.getString("latitude"));

                    station.setType(where);                                                         // Параметр отвечающий за разделения станций по признаку From/To

                    station.setStationTitleLow(obj2.getString("stationTitle").toLowerCase());       // Поле хранящее название станции в пониженном регистре - требуется для регистронезависимого сравнение кирилицы

                    saveStation(station);                                                           // Сохранение в БД
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveStation(Station station) {
        DataBaseManager.getInstance().saveStation(station);
    }

    public void clearBD(){
        createSpisok();
        DataBaseManager.getInstance().clearBD();
        createSpisok();
    }

    public void createSpisok() {                                                                    // Актуализация Списка значений из БД
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
}
