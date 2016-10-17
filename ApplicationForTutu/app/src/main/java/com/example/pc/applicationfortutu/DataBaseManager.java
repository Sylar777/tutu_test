package com.example.pc.applicationfortutu;

import android.util.Log;
import android.content.Context;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import java.sql.SQLException;
import java.util.List;

/**
 *  DataBase
 *
 *  1. Сохранение
 *  2. Поиск
 *  3. Сортировка
 *  4. Удаление
 */
public class DataBaseManager {

    static private DataBaseManager dataBaseManager;
    private HelperStation helperStation;

    private DataBaseManager(Context context) {                                                      //конструктор
        helperStation = new HelperStation(context);
    }

    static public DataBaseManager getInstance(){                                                    //getInstance
        return dataBaseManager;
    }

    private HelperStation getHelperStation(){
        return helperStation;
    }

    static public void init(Context context){
        if(null==dataBaseManager){
            dataBaseManager=new DataBaseManager(context);
        }
    }

    public void saveStation(final Station station){
        try {
            getHelperStation().getStationDao().create(station);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Station> search (final String string){
        List<Station> stationList = null;
        try {
            QueryBuilder<Station, Integer> queryBuilder =                                           // Конструктор запросов DAO
                    getHelperStation().getStationDao().queryBuilder();
            queryBuilder.where().like("stationTitleLow", "%" + string + "%").prepare();             // Поиск с текстом до и после ключевой строки
            PreparedQuery<Station> preparedQuery = queryBuilder.prepare();                          // Подготовка к SQL запросу
            stationList = getHelperStation().getStationDao().query(preparedQuery);                  // Возвращает все удовлетворяющие параметрам поиска записи
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stationList;
    }

    public List<Station> sort(){
        List<Station> stationList = null;

        try {
            QueryBuilder<Station, Integer> queryBuilder =
                    getHelperStation().getStationDao().queryBuilder();

            queryBuilder.orderBy("countryTitle", true);
            queryBuilder.orderBy("cityTitle", true);

            PreparedQuery<Station> preparedQuery = queryBuilder.prepare();
            stationList = getHelperStation().getStationDao().query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stationList;
    }

    public void clearBD(){
        try {
            List<Station> list = getHelperStation().getStationDao().queryForAll();
            Log.d("Delete", "" + list.size() + " " + list.get(0).getId());
            for (int i = 0;i<list.size();i++) {
                getHelperStation().getStationDao().deleteById((int)list.get(i).getId());
                Log.d("clearBD", "Удаление из БД : " + i + " ИЗ : " + list.size());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
