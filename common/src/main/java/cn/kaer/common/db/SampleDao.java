package cn.kaer.common.db;/*
package cn.kaer.common.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RecordDao {
    */
/*    @Insert("INSERT INTO ")
        void insert(RecordDao tbl_test);*//*

    public static final String TABLENAME = "record";

    @Query("SELECT * FROM record")
    List<Record> getAll();

    @Delete
    void deleteByRecord(Record record);

    @Query("DELETE FROM record where _id = :id")
    void deleteById(int id);

    @Insert
    void insert(Record record);


    @Query("SELECT * FROM record where ispost = 0")
    List<Record> queryNotPostData();

    @Query("UPDATE record SET ispost = :isPostSuccess WHERE record._id = :id")
    void updatePostTAG(boolean isPostSuccess, int id);
}
*/
