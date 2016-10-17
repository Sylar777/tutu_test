package com.example.pc.applicationfortutu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class HelperStation extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "DBTutuStation";
    private static final int DATABASE_VERSION=1;
    private Dao<Station,Integer> stationDao;
    private RuntimeExceptionDao<Station,Integer> stationsRunTimeDao = null;

    public HelperStation(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Station.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

    Dao<Station,Integer> getStationDao(){
        if(null == stationDao){
            try {
                stationDao = getDao(Station.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return stationDao;
    }

    public RuntimeExceptionDao<Station,Integer> getStationRuntimeException() throws SQLException{
        if(stationsRunTimeDao == null){
            stationsRunTimeDao = getRuntimeExceptionDao(Station.class);
        }
        return stationsRunTimeDao;
    }

    @Override
    public void close() {
        super.close();
        stationDao = null;
        stationsRunTimeDao = null;
    }
}
