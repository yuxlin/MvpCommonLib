package cn.kaer.common.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class __DataBaseController<T extends RoomDatabase> {

    private static __DataBaseController instance;
    private Context mContext;
    private T mRoomDatabase;

    private __DataBaseController() {

    }

    public static __DataBaseController create() {
        if (instance == null) {
            synchronized (__DataBaseController.class) {
                instance = new __DataBaseController();
            }
        }
        return instance;
    }

    //数据库升级
    private Migration Migration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        /*
           //增加新字段
           database.execSQL("ALTER TABLE users "
                       + " ADD COLUMN last_update INTEGER");*/
        }
    };

    public T getDataBaseManager() {
        return mRoomDatabase;
    }


    public T init(Context context, Class<T> tClass, String dbName) {
        if (mRoomDatabase != null)
            return mRoomDatabase;

        mContext = context.getApplicationContext();
        mRoomDatabase = (T) Room.databaseBuilder(mContext, tClass, dbName)
                .allowMainThreadQueries()
                // .addMigrations(Migration_1_2)
                .build();
        return mRoomDatabase;
    }

}
