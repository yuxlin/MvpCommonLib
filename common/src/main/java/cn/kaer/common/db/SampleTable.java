package cn.kaer.common.db;/*
package cn.kaer.common.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "record")
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "heat")
    private float heat;//温度
    @ColumnInfo(name = "pic_path")
    private String picPath;
    @ColumnInfo(name = "warn_heat")
    private float warnigHeat;//警戒温度
    @ColumnInfo(name = "stamp")
    private long stamp;//时间戳
    @ColumnInfo(name = "ispost") //是否上传成功
    private boolean isPost;
    @ColumnInfo(name = "space1")
    private String space1;
    @ColumnInfo(name = "space2")
    private String space2;

    @Ignore
    public Record() {
    }

    public Record(float heat, String picPath, float warnigHeat, long stamp, boolean isPost) {
        this.heat = heat;
        this.picPath = picPath;
        this.warnigHeat = warnigHeat;
        this.stamp = stamp;
        this.isPost = isPost;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public float getHeat() {
        return heat;
    }

    public void setHeat(float heat) {
        this.heat = heat;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public float getWarnigHeat() {
        return warnigHeat;
    }

    public void setWarnigHeat(float warnigHeat) {
        this.warnigHeat = warnigHeat;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public String getSpace1() {
        return space1;
    }

    public void setSpace1(String space1) {
        this.space1 = space1;
    }

    public String getSpace2() {
        return space2;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public void setSpace2(String space2) {
        this.space2 = space2;
    }

    @Override
    public String toString() {
        return "Record{" +
                "_id=" + _id +
                ", heat=" + heat +
                ", picPath='" + picPath + '\'' +
                ", warnigHeat=" + warnigHeat +
                ", stamp=" + stamp +
                ", isPost=" + isPost +
                ", space1='" + space1 + '\'' +
                ", space2='" + space2 + '\'' +
                '}';
    }
}
*/
