package cn.kaer.common.db;

import android.content.Context;

import androidx.room.RoomDatabase;

/**
 * User: yxl
 * Date: 2020/11/26
 */
public abstract class IDataBaseController<T extends RoomDatabase> {

    abstract T getDataBaseManager();

    public abstract T init(Context context, Class<T> tClass, String dbName);

}
