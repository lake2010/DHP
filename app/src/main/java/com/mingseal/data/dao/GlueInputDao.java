/**
 * 
 */
package com.mingseal.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo;
import com.mingseal.data.db.DBInfo.TableInputIO;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.utils.ArraysComprehension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 商炎炳
 *
 */
public class GlueInputDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;

	String[] columns = { TableInputIO._ID, TableInputIO.GO_TIME_PREV, TableInputIO.GO_TIME_NEXT,
			TableInputIO.INPUT_PORT };

	public GlueInputDao(Context context) {
		dbHelper = new DBHelper(context);
	}
	/**
	 * @Title  upDateGlueLineMid
	 * @Description 更新一条独立点数据
	 * @author wj
	 * @param
	 * @return  影响的行数，0表示错误
	 */
	public int upDateGlueInput(PointGlueInputIOParam param,String taskname){
		int rowid = 0;
		try {
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableInputIO.GO_TIME_PREV, param.getGoTimePrev());
			values.put(TableInputIO.GO_TIME_NEXT, param.getGoTimeNext());
			values.put(TableInputIO.INPUT_PORT, Arrays.toString(param.getInputPort()));
			rowid = db.update(DBInfo.TableInputIO.INPUT_IO_TABLE+taskname, values,TableInputIO._ID +"=?", new String[]{String.valueOf(param.get_id())});
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
	 * 增加一条输入IO的数据
	 * 
	 * @param param
	 * @return 刚增加的这条数据的主键
	 */
	public long insertGlueInput(PointGlueInputIOParam param,String taskname) {
		long rowID = 0;
		db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableInputIO._ID, param.get_id());
			values.put(TableInputIO.GO_TIME_PREV, param.getGoTimePrev());
			values.put(TableInputIO.GO_TIME_NEXT, param.getGoTimeNext());
			values.put(TableInputIO.INPUT_PORT, Arrays.toString(param.getInputPort()));
			rowID = db.insert(TableInputIO.INPUT_IO_TABLE+taskname, TableInputIO._ID, values);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.endTransaction();
			// 释放资源
			db.close();
		}
		return rowID;
	}

	/**
	 * 取得所有输入IO的数据
	 * 
	 * @return List<PointGlueInputIOParam>
	 */
	public List<PointGlueInputIOParam> findAllGlueInputParams(String taskname) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueInputIOParam> outputIOParams = null;
		PointGlueInputIOParam output = null;

		Cursor cursor = null;
		try {
			cursor = db.query(TableInputIO.INPUT_IO_TABLE+taskname, columns, null, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
                outputIOParams = new ArrayList<PointGlueInputIOParam>();
                while (cursor.moveToNext()) {
                    output = new PointGlueInputIOParam();
                    output.set_id(cursor.getInt(cursor.getColumnIndex(TableInputIO._ID)));
                    output.setGoTimePrev(cursor.getInt(cursor.getColumnIndex(TableInputIO.GO_TIME_PREV)));
                    output.setGoTimeNext(cursor.getInt(cursor.getColumnIndex(TableInputIO.GO_TIME_NEXT)));
                    output.setInputPort(ArraysComprehension
                            .boooleanParse(cursor.getString(cursor.getColumnIndex(TableInputIO.INPUT_PORT))));

                    outputIOParams.add(output);
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor!=null){
				cursor.close();
			}
		}
		db.close();
		return outputIOParams;
	}

	/**
	 * 通过List<Integer> 列表来查找对应的PointGlueInputIOParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueInputIOParam>
	 */
	public List<PointGlueInputIOParam> getGlueInputParamsByIDs(List<Integer> ids,String taskname) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueInputIOParam> params = new ArrayList<>();
		PointGlueInputIOParam param = null;
		Cursor cursor = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				cursor = db.query(TableInputIO.INPUT_IO_TABLE+taskname, columns, TableInputIO._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueInputIOParam();
						param.set_id(cursor.getInt(cursor.getColumnIndex(TableInputIO._ID)));
						param.setGoTimePrev(cursor.getInt(cursor.getColumnIndex(TableInputIO.GO_TIME_PREV)));
						param.setGoTimeNext(cursor.getInt(cursor.getColumnIndex(TableInputIO.GO_TIME_NEXT)));
						param.setInputPort(ArraysComprehension
								.boooleanParse(cursor.getString(cursor.getColumnIndex(TableInputIO.INPUT_PORT))));

						params.add(param);
					}
				}
				if (cursor!=null){
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
	 * 通过参数方案寻找到当前方案的主键
	 * 
	 * @param pointGlueinputIOParam
	 * @return 当前方案的主键
	 */
	public int getInputParamIDByParam(PointGlueInputIOParam pointGlueinputIOParam,String taskname) {
		int id = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableInputIO.INPUT_IO_TABLE+taskname, columns,
				TableInputIO.GO_TIME_PREV + "=? and " + TableInputIO.GO_TIME_NEXT + "=? and " + TableInputIO.INPUT_PORT
						+ "=?",
				new String[] { String.valueOf(pointGlueinputIOParam.getGoTimePrev()),
						String.valueOf(pointGlueinputIOParam.getGoTimeNext()),
						Arrays.toString(pointGlueinputIOParam.getInputPort()) },
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex(TableInputIO._ID));
			}
		}
		db.close();
		if (-1 == id) {
//			id = (int) insertGlueInput(pointGlueinputIOParam);
		}
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
		}
		return id;
	}

	/**
	 * @Title getInputPointByID
	 * @Description 通过主键寻找到当前输入口的参数方案
	 * @param id
	 *            当前输入口主键
	 * @return PointGlueInputIOParam
	 */
	public PointGlueInputIOParam getInputPointByID(int id,String taskname) {
		db = dbHelper.getReadableDatabase();
		PointGlueInputIOParam param = null;
		Cursor cursor = null;
		try {
			db.beginTransaction();
			cursor = db.query(TableInputIO.INPUT_IO_TABLE+taskname, columns, TableInputIO._ID + "=?",
					new String[] { String.valueOf(id) }, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					param = new PointGlueInputIOParam();
					param.set_id(cursor.getInt(cursor.getColumnIndex(TableInputIO._ID)));
					param.setGoTimePrev(cursor.getInt(cursor.getColumnIndex(TableInputIO.GO_TIME_PREV)));
					param.setGoTimeNext(cursor.getInt(cursor.getColumnIndex(TableInputIO.GO_TIME_NEXT)));
					param.setInputPort(ArraysComprehension
							.boooleanParse(cursor.getString(cursor.getColumnIndex(TableInputIO.INPUT_PORT))));

				}
			}
			if (cursor!=null){
				cursor.close();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}

		return param;
	}

	public int deleteParam(PointGlueInputIOParam pointGlueInputIOParam,String taskname) {
		db = dbHelper.getWritableDatabase();
		int rowID = db.delete(DBInfo.TableInputIO.INPUT_IO_TABLE+taskname, TableInputIO._ID + "=?",
				new String[] { String.valueOf(pointGlueInputIOParam.get_id()) });

		db.close();
		return rowID;
	}
}
