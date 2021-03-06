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
import com.mingseal.data.db.DBInfo.TableLineStart;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.utils.ArraysComprehension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangjian
 * 
 */
public class GlueLineStartDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { TableLineStart._ID, TableLineStart.OUT_GLUE_TIME_PREV,
			TableLineStart.OUT_GLUE_TIME,
			TableLineStart.MOVE_SPEED,
			TableLineStart.GLUE_PORT };

	public GlueLineStartDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * @Title upDateGlueAlone
	 * @Description 更新一条独立点数据
	 * @author wj
	 * @param
	 * @return 影响的行数，0表示错误
	 */
	public int upDateGlueLineStart(PointGlueLineStartParam pointGlueLineStartParam,String taskname) {
		int rowid = 0;
		try {
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableLineStart.OUT_GLUE_TIME_PREV,
					pointGlueLineStartParam.getOutGlueTimePrev());
			values.put(TableLineStart.OUT_GLUE_TIME,
					pointGlueLineStartParam.getOutGlueTime());
			values.put(TableLineStart.MOVE_SPEED,
					pointGlueLineStartParam.getMoveSpeed());
			values.put(TableLineStart.GLUE_PORT,
					Arrays.toString(pointGlueLineStartParam.getGluePort()));
			rowid = db.update(DBInfo.TableLineStart.LINE_START_TABLE+taskname, values,
					TableLineStart._ID + "=?", new String[] { String
							.valueOf(pointGlueLineStartParam.get_id()) });
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
	 * 增加一条线起始点的数据
	 * 
	 * @param pointGlueLineStartParam
	 * @return 刚增加的这条数据的主键
	 */
	public long insertGlueLineStart(
			PointGlueLineStartParam pointGlueLineStartParam,String taskname) {
		long rowID = 0;
		db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();

			values = new ContentValues();
			values.put(TableLineStart._ID, pointGlueLineStartParam.get_id());
			values.put(TableLineStart.OUT_GLUE_TIME_PREV,
					pointGlueLineStartParam.getOutGlueTimePrev());
			values.put(TableLineStart.OUT_GLUE_TIME,
					pointGlueLineStartParam.getOutGlueTime());
			values.put(TableLineStart.MOVE_SPEED,
					pointGlueLineStartParam.getMoveSpeed());

			values.put(TableLineStart.GLUE_PORT,
					Arrays.toString(pointGlueLineStartParam.getGluePort()));

			rowID = db.insert(TableLineStart.LINE_START_TABLE+taskname,
					TableLineStart._ID, values);
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
	 * 取得所有线起始点的数据
	 * 
	 * @return
	 */
	public List<PointGlueLineStartParam> findAllGlueLineStartParams(String taskname) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueLineStartParam> startLists = null;
		PointGlueLineStartParam start = null;

		Cursor cursor = null;
		try {
			cursor = db.query(TableLineStart.LINE_START_TABLE+taskname, columns,
                    null, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
                startLists = new ArrayList<PointGlueLineStartParam>();
                while (cursor.moveToNext()) {
                    start = new PointGlueLineStartParam();
                    start.set_id(cursor.getInt(cursor
                            .getColumnIndex(TableLineStart._ID)));
                    start.setOutGlueTimePrev(cursor.getInt(cursor
                            .getColumnIndex(TableLineStart.OUT_GLUE_TIME_PREV)));
                    start.setOutGlueTime(cursor.getInt(cursor
                            .getColumnIndex(TableLineStart.OUT_GLUE_TIME)));
                    start.setMoveSpeed(cursor.getFloat(cursor
                            .getColumnIndex(TableLineStart.MOVE_SPEED)));

                    // start.setStopGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME_PREV)));
                    // start.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME)));
                    // start.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableLineStart.UP_HEIGHT)));
                    start.setGluePort(ArraysComprehension.boooleanParse(cursor
                            .getString(cursor
                                    .getColumnIndex(TableLineStart.GLUE_PORT))));

                    startLists.add(start);
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
		return startLists;
	}

	/**
	 * 通过主键id找到PointGlueLineStartParam参数
	 * 
	 * @param id
	 * @return PointGlueLineStartParam
	 */
	public PointGlueLineStartParam getPointGlueLineStartParamByID(int id,String taskname) {
		PointGlueLineStartParam param = new PointGlueLineStartParam();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(TableLineStart.LINE_START_TABLE+taskname, columns,
					TableLineStart._ID + "=?", new String[] { String.valueOf(id) },
					null, null, null);
			db.beginTransaction();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					param.set_id(cursor.getInt(cursor.getColumnIndex(TableLineStart._ID)));
					param.setOutGlueTimePrev(cursor.getInt(cursor
							.getColumnIndex(TableLineStart.OUT_GLUE_TIME_PREV)));
					param.setOutGlueTime(cursor.getInt(cursor
							.getColumnIndex(TableLineStart.OUT_GLUE_TIME)));
					param.setMoveSpeed(cursor.getFloat(cursor
							.getColumnIndex(TableLineStart.MOVE_SPEED)));

					param.setGluePort(ArraysComprehension.boooleanParse(cursor
							.getString(cursor
									.getColumnIndex(TableLineStart.GLUE_PORT))));

				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor!=null){
				cursor.close();
			}
			db.endTransaction();
			db.close();
		}
		return param;
	}

	/**
	 * 通过List<Integer> 列表来查找对应的 PointGlueLineStartParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueLineStartParam>
	 */
	public List<PointGlueLineStartParam> getPointGlueLineStartParamsByIDs(
			List<Integer> ids,String taskname) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueLineStartParam> params = new ArrayList<>();
		PointGlueLineStartParam param = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TableLineStart.LINE_START_TABLE+taskname,
						columns, TableLineStart._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueLineStartParam();
						param.set_id(cursor.getInt(cursor
								.getColumnIndex(TableLineStart._ID)));
						param.setOutGlueTimePrev(cursor.getInt(cursor
								.getColumnIndex(TableLineStart.OUT_GLUE_TIME_PREV)));
						param.setOutGlueTime(cursor.getInt(cursor
								.getColumnIndex(TableLineStart.OUT_GLUE_TIME)));

						param.setMoveSpeed(cursor.getFloat(cursor
								.getColumnIndex(TableLineStart.MOVE_SPEED)));
						// param.setStopGlueTimePrev(
						// cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME_PREV)));
						// param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME)));
						// param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableLineStart.UP_HEIGHT)));
						param.setGluePort(ArraysComprehension.boooleanParse(cursor.getString(cursor
								.getColumnIndex(TableLineStart.GLUE_PORT))));
						params.add(param);
					}
				}
				cursor.close();
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
	 * 通过参数方案找到当前方案的主键(是否出胶,停胶前延时,停胶后延时,抬起高度起始点是没有数据的)
	 * 
	 * @param pointGlueLineStartParam
	 * @return 当前方案的主键
	 */
	public int getLineStartParamIDByParam(
			PointGlueLineStartParam pointGlueLineStartParam,String taskname) {
		int id = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db
				.query(TableLineStart.LINE_START_TABLE+taskname,
						columns,
						TableLineStart.OUT_GLUE_TIME_PREV + "=? and "
								+ TableLineStart.OUT_GLUE_TIME + "=? and "
								+ TableLineStart.MOVE_SPEED + "=? and "
								+ TableLineStart.STOP_GLUE_TIME_PREV
								+ "=? and " + TableLineStart.STOP_GLUE_TIME
								+ "=? and " + TableLineStart.UP_HEIGHT
								+ "=? and " + TableLineStart.GLUE_PORT + "=?",
						new String[] {
								String.valueOf(pointGlueLineStartParam
										.getOutGlueTimePrev()),
								String.valueOf(pointGlueLineStartParam
										.getOutGlueTime()),
								String.valueOf(pointGlueLineStartParam
										.getMoveSpeed()),
								// String.valueOf(pointGlueLineStartParam.getStopGlueTimePrev()),
								String.valueOf(pointGlueLineStartParam
										.getOutGlueTime()),
								// String.valueOf(pointGlueLineStartParam.getUpHeight()),
								Arrays.toString(pointGlueLineStartParam
										.getGluePort()) }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex(TableLineStart._ID));
			}
		}
		db.close();
		if (-1 == id) {
			// 先查询除了是否出胶,停胶前延时,停胶后延时,抬起高度之外有没有相对应的方案参数
			db = dbHelper.getReadableDatabase();
			cursor = db
					.query(TableLineStart.LINE_START_TABLE+taskname,
							columns,
							TableLineStart.OUT_GLUE_TIME_PREV + "=? and "
									+ TableLineStart.OUT_GLUE_TIME + "=? and "
									+ TableLineStart.MOVE_SPEED + "=? and "
									+ TableLineStart.GLUE_PORT + "=?",
							new String[] {
									String.valueOf(pointGlueLineStartParam
											.getOutGlueTimePrev()),
									String.valueOf(pointGlueLineStartParam
											.getOutGlueTime()),
									String.valueOf(pointGlueLineStartParam
											.getMoveSpeed()),
									Arrays.toString(pointGlueLineStartParam
											.getGluePort()) }, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor
							.getColumnIndex(TableLineStart._ID));
				}
			}
			db.close();

			if (-1 == id) {
//				id = (int) insertGlueLineStart(pointGlueLineStartParam);
			}
		}
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
		}
		return id;

	}

	/**
	 * @Title deleteParam
	 * @Description 删除某一行数据
	 * @author wj
	 * @param pointGlueLineStartParam
	 * @return
	 */
	public int deleteParam(PointGlueLineStartParam pointGlueLineStartParam,String taskname) {
		db = dbHelper.getWritableDatabase();
		int rowID = db
				.delete(DBInfo.TableLineStart.LINE_START_TABLE+taskname,
						TableLineStart._ID + "=?", new String[] { String
								.valueOf(pointGlueLineStartParam.get_id()) });

		db.close();
		return rowID;
	}

}
