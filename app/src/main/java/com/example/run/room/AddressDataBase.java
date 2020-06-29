package com.example.run.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database( entities = {Address.class},version = 1,exportSchema = false)
public abstract  class AddressDataBase extends RoomDatabase {
    private static AddressDataBase addressDataBase;

    public static AddressDataBase getInstance(Context context){
        if(addressDataBase == null){
            addressDataBase = Room.databaseBuilder(context.getApplicationContext(),AddressDataBase.class,"address")
                    .build();
        }
        return addressDataBase;
    }

    abstract AddressDao getAddressDao();
}
