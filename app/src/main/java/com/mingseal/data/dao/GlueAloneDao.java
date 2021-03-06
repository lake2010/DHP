package com.mingseal.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo;
import com.mingseal.data.db.DBInfo.TableAlone;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.utils.ArraysComprehension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangjian
 */
public class GlueAloneDao {
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private ContentValues values = null;
    String[] columns = {TableAlone._ID, TableAlone.DOT_GLUE_TIME, TableAlone.STOP_GLUE_TIME, TableAlone.UP_HEIGHT,
             TableAlone.IS_PAUSE, TableAlone.GLUE_PORT, TableAlone.DIPDISTANCE_Y, TableAlone.DIPDISTANCE_Z, TableAlone.DIPSPEED};

    public GlueAloneDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * @param pointGlueAloneParam
     * @return 影响的行数，0表示错误
     * @Title upDateGlueAlone
     * @Description 更新一条独立点数据
     * @author wj
     */
    public int upDateGlueAlone(PointGlueAloneParam pointGlueAloneParam,String taskname) {
        int rowid = 0;
        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            values = new ContentValues();
            values.put(TableAlone.DOT_GLUE_TIME, pointGlueAloneParam.getDotGlueTime());
            values.put(TableAlone.STOP_GLUE_TIME, pointGlueAloneParam.getStopGlueTime());
            values.put(TableAlone.UP_HEIGHT, pointGlueAloneParam.getUpHeight());
            values.put(TableAlone.IS_PAUSE, (boolean) pointGlueAloneParam.isPause() ? 1 : 0);
            values.put(TableAlone.GLUE_PORT, Arrays.toString(pointGlueAloneParam.getGluePort()));
            values.put(TableAlone.DIPDISTANCE_Y, pointGlueAloneParam.getnDipDistanceY());
            values.put(TableAlone.DIPDISTANCE_Z, pointGlueAloneParam.getnDipDistanceZ());
            values.put(TableAlone.DIPSPEED, pointGlueAloneParam.getnDipSpeed());
            rowid = db.update(DBInfo.TableAlone.ALONE_TABLE+taskname, values, TableAlone._ID + "=?", new String[]{String.valueOf(pointGlueAloneParam.get_id())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return rowid;
    }

    /**
     * 增加一条独立点数据
     *
     * @param pointGlueAloneParam
     * @return
     */
    public long insertGlueAlone(PointGlueAloneParam pointGlueAloneParam,String taskname) {
        long rowID = 0;
        db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            values = new ContentValues();
            values.put(TableAlone._ID, pointGlueAloneParam.get_id());
            values.put(TableAlone.DOT_GLUE_TIME, pointGlueAloneParam.getDotGlueTime());
            values.put(TableAlone.STOP_GLUE_TIME, pointGlueAloneParam.getStopGlueTime());
            values.put(TableAlone.UP_HEIGHT, pointGlueAloneParam.getUpHeight());
            values.put(TableAlone.IS_PAUSE, (boolean) pointGlueAloneParam.isPause() ? 1 : 0);
            values.put(TableAlone.GLUE_PORT, Arrays.toString(pointGlueAloneParam.getGluePort()));
            values.put(TableAlone.DIPDISTANCE_Y, pointGlueAloneParam.getnDipDistanceY());
            values.put(TableAlone.DIPDISTANCE_Z, pointGlueAloneParam.getnDipDistanceZ());
            values.put(TableAlone.DIPSPEED, pointGlueAloneParam.getnDipSpeed());
            rowID = db.insert(DBInfo.TableAlone.ALONE_TABLE+taskname, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // 释放资源
            db.close();
        }
        return rowID;
    }

    /**
     * 获得所有独立点的数据
     *
     * @return
     */
    public List<PointGlueAloneParam> findAllGlueAloneParams(String taskname) {
        db = dbHelper.getReadableDatabase();
        List<PointGlueAloneParam> aloneLists = null;
        PointGlueAloneParam alone = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TableAlone.ALONE_TABLE+taskname, columns, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                aloneLists = new ArrayList<PointGlueAloneParam>();
                while (cursor.moveToNext()) {
                    alone = new PointGlueAloneParam();
                    alone.set_id(cursor.getInt(cursor.getColumnIndex(TableAlone._ID)));
                    alone.setDotGlueTime(cursor.getInt(cursor.getColumnIndex(TableAlone.DOT_GLUE_TIME)));
                    alone.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableAlone.STOP_GLUE_TIME)));
                    alone.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableAlone.UP_HEIGHT)));
                    alone.setPause(cursor.getInt(cursor.getColumnIndex(TableAlone.IS_PAUSE)) == 0 ? false : true);

                    // System.out.println(cursor.getString(cursor.getColumnIndex(TableAlone.GLUE_PORT)));
                    alone.setGluePort(ArraysComprehension
                            .boooleanParse(cursor.getString(cursor.getColumnIndex(TableAlone.GLUE_PORT))));
                    alone.setnDipDistanceY(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPDISTANCE_Y)));
                    alone.setnDipDistanceZ(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPDISTANCE_Z)));
                    alone.setnDipSpeed(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPSPEED)));
                    aloneLists.add(alone);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return aloneLists;
    }

    /**
     * 删除某一行数据
     *
     * @param pointGlueAloneParam
     * @return 1为成功删除，0为未成功删除
     */
    public Integer deleteGlueAlone(PointGlueAloneParam pointGlueAloneParam,String taskname) {
        db = dbHelper.getWritableDatabase();
        int rowID = db.delete(DBInfo.TableAlone.ALONE_TABLE+taskname, TableAlone._ID + "=?",
                new String[]{String.valueOf(pointGlueAloneParam.get_id())});

        db.close();
        return rowID;
    }

    /**
     * 通过id找到独立点的参数
     *
     * @param id 主键
     * @return PointGlueAloneParam
     */
    public PointGlueAloneParam getPointGlueAloneParamById(int id,String taskname) {
        PointGlueAloneParam param = new PointGlueAloneParam();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TableAlone.ALONE_TABLE+taskname, columns, TableAlone._ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null);
            db.beginTransaction();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    param.set_id(cursor.getInt(cursor.getColumnIndex(TableAlone._ID)));
                    param.setDotGlueTime(cursor.getInt(cursor.getColumnIndex(TableAlone.DOT_GLUE_TIME)));
                    param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableAlone.STOP_GLUE_TIME)));
                    param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableAlone.UP_HEIGHT)));
                    param.setPause(cursor.getInt(cursor.getColumnIndex(TableAlone.IS_PAUSE)) == 0 ? false : true);
                    param.setGluePort(ArraysComprehension
                            .boooleanParse(cursor.getString(cursor.getColumnIndex(TableAlone.GLUE_PORT))));
                    param.setnDipDistanceY(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPDISTANCE_Y)));
                    param.setnDipDistanceZ(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPDISTANCE_Z)));
                    param.setnDipSpeed(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPSPEED)));
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
            db.close();
        }
        return param;
    }

    /**
     * 通过List<Integer> 列表来查找对应的PointGlueAloneParam集合
     *
     * @param ids
     * @return List<PointGlueAloneParam>
     */
    public List<PointGlueAloneParam> getGlueAloneParamsByIDs(List<Integer> ids,String taskname) {
        db = dbHelper.getReadableDatabase();
        List<PointGlueAloneParam> params = new ArrayList<>();
        PointGlueAloneParam param = null;
        Cursor cursor = null;
        try {
            db.beginTransaction();
            for (Integer id : ids) {
                cursor = db.query(TableAlone.ALONE_TABLE+taskname, columns, TableAlone._ID + "=?",
                        new String[]{String.valueOf(id)}, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        param = new PointGlueAloneParam();
                        param.set_id(cursor.getInt(cursor.getColumnIndex(TableAlone._ID)));
                        param.setDotGlueTime(cursor.getInt(cursor.getColumnIndex(TableAlone.DOT_GLUE_TIME)));
                        param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableAlone.STOP_GLUE_TIME)));
                        param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableAlone.UP_HEIGHT)));
                        param.setPause(cursor.getInt(cursor.getColumnIndex(TableAlone.IS_PAUSE)) == 0 ? false : true);
                        param.setGluePort(ArraysComprehension
                                .boooleanParse(cursor.getString(cursor.getColumnIndex(TableAlone.GLUE_PORT))));
                        param.setnDipDistanceY(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPDISTANCE_Y)));
                        param.setnDipDistanceZ(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPDISTANCE_Z)));
                        param.setnDipSpeed(cursor.getInt(cursor.getColumnIndex(TableAlone.DIPSPEED)));
                        params.add(param);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return params;
    }

    /**
     * 通过参数找到当前参数方案的主键(是否出胶是没有数据的)
     *
     * @param pointGlueAloneParam
     * @return 当前方案的主键
     */
    public int getAloneParamIdByParam(PointGlueAloneParam pointGlueAloneParam,String taskname) {
        int id = -1;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TableAlone.ALONE_TABLE+taskname, columns,
                TableAlone.DOT_GLUE_TIME + "=? and " + TableAlone.STOP_GLUE_TIME + "=? and " + TableAlone.UP_HEIGHT
                        + "=? and " + TableAlone.IS_PAUSE + "=? and " +
                        TableAlone.GLUE_PORT + "=? and " + TableAlone.DIPDISTANCE_Y + "=? and " + TableAlone.DIPDISTANCE_Z + "=? and " + TableAlone.DIPSPEED + "=?",
                new String[]{String.valueOf(pointGlueAloneParam.getDotGlueTime()),
                        String.valueOf(pointGlueAloneParam.getStopGlueTime()),
                        String.valueOf(pointGlueAloneParam.getUpHeight()),
                        String.valueOf(pointGlueAloneParam.isPause() ? 1 : 0),
                        Arrays.toString(pointGlueAloneParam.getGluePort()),
                        String.valueOf(pointGlueAloneParam.getnDipDistanceY()),
                        String.valueOf(pointGlueAloneParam.getnDipDistanceZ()),
                        String.valueOf(pointGlueAloneParam.getnDipSpeed())},
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(TableAlone._ID));
            }
        }
        db.close();
        if (-1 == id) {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(TableAlone.ALONE_TABLE+taskname, columns,
                    TableAlone.DOT_GLUE_TIME + "=? and " + TableAlone.STOP_GLUE_TIME + "=? and " + TableAlone.UP_HEIGHT
                            + "=? and " + TableAlone.IS_PAUSE + "=? and " + TableAlone.GLUE_PORT + "=? and " + TableAlone.DIPDISTANCE_Y + "=? and " + TableAlone.DIPDISTANCE_Z + "=? and " + TableAlone.DIPSPEED + "=?",
                    new String[]{String.valueOf(pointGlueAloneParam.getDotGlueTime()),
                            String.valueOf(pointGlueAloneParam.getStopGlueTime()),
                            String.valueOf(pointGlueAloneParam.getUpHeight()),
                            String.valueOf(pointGlueAloneParam.isPause() ? 1 : 0),
                            Arrays.toString(pointGlueAloneParam.getGluePort()),
                            String.valueOf(pointGlueAloneParam.getnDipDistanceY()),
                            String.valueOf(pointGlueAloneParam.getnDipDistanceZ()),
                            String.valueOf(pointGlueAloneParam.getnDipSpeed())},
                    null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex(TableAlone._ID));
                }
            }
            db.close();
            if (-1 == id) {
                // 说明源方案里面没有，需要重新添加
//                id = (int) insertGlueAlone(pointGlueAloneParam);
            }
        }
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
        }
        return id;
    }

    /**
     * @Title delsqlite_sequence
     * @Description 删除表的自增列, 都归零
     * @author wj
     */
    public void delsqlite_sequence() {
        db = dbHelper.getReadableDatabase();
        db.execSQL("DELETE FROM sqlite_sequence");
    }
}
