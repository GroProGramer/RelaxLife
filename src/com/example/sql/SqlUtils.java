package com.example.sql;

import java.util.ArrayList;

import com.example.data.PayRecordData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//数据库操作工具类
@SuppressWarnings("unused")
public class SqlUtils {
	private Context context;
	private SqlHelper helper;

	public SqlUtils(Context context) {
		super();
		this.context = context;
		helper = new SqlHelper(context);
	}

	// 查询历史记录
	public ArrayList<PayRecordData> selectHistory(String starttime,
			String endtime, String tagString, String psString) {
		ArrayList<PayRecordData> list = null;
		PayRecordData data;
		String sql = "";
		if (tagString.equals("全部")) {
			list = new ArrayList<PayRecordData>();
			sql = "select * from payrecord where date between (?) and (?) and ps like (?)";
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { starttime, endtime,
					"%" + psString + "%" });
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String type = cursor.getString(cursor.getColumnIndex("type"));
				String price = cursor.getString(cursor.getColumnIndex("price"));
				String tag = cursor.getString(cursor.getColumnIndex("tag"));
				String date = cursor.getString(cursor.getColumnIndex("date"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				String ps = cursor.getString(cursor.getColumnIndex("ps"));
				data = new PayRecordData(id, type, price, tag, date, time, ps);
				list.add(data);
			}
		} else {
			list = new ArrayList<PayRecordData>();
			sql = "select * from payrecord where date between (?) and (?) and tag = (?)  and ps like (?)";
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { starttime, endtime,
					tagString, "%" + psString + "%" });
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String type = cursor.getString(cursor.getColumnIndex("type"));
				String price = cursor.getString(cursor.getColumnIndex("price"));
				String tag = cursor.getString(cursor.getColumnIndex("tag"));
				String date = cursor.getString(cursor.getColumnIndex("date"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				String ps = cursor.getString(cursor.getColumnIndex("ps"));
				data = new PayRecordData(id, type, price, tag, date, time, ps);
				list.add(data);
			}
		}
		return list;
	}

	// 删除记录
	public void deleteRecord(int id) {
		String sql = "delete from payrecord where _id = (?)";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[] { id });
		db.close();
	}

	// 插入记录
	public void insertRecord(String type, String price, String tag,
			String date, String time, String ps) {
		String sql = "insert into payrecord (type,price,tag,date,time,ps) values (?,?,?,?,?,?)";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[] { type, price, tag, date, time, ps });
		db.close();
	}

	// 查找当日记录
	public ArrayList<PayRecordData> selectNow(String dateString) {
		ArrayList<PayRecordData> list = new ArrayList<PayRecordData>();
		PayRecordData data;
		String sql = "select * from payrecord where date = (?)";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] { dateString });
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String tag = cursor.getString(cursor.getColumnIndex("tag"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String time = cursor.getString(cursor.getColumnIndex("time"));
			String ps = cursor.getString(cursor.getColumnIndex("ps"));
			data = new PayRecordData(id, type, price, tag, date, time, ps);
			list.add(data);
		}
		return list;
	}

	// 查找记录
	public ArrayList<PayRecordData> selectAll() {
		ArrayList<PayRecordData> list = new ArrayList<PayRecordData>();
		PayRecordData data;
		String sql = "select * from payrecord";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String tag = cursor.getString(cursor.getColumnIndex("tag"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String time = cursor.getString(cursor.getColumnIndex("time"));
			String ps = cursor.getString(cursor.getColumnIndex("ps"));
			data = new PayRecordData(id, type, price, tag, date, time, ps);
			list.add(data);
		}
		return list;
	}
}
