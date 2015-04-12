package com.example.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelper extends SQLiteOpenHelper {

	public SqlHelper(Context context) {
		super(context, "payrecord", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		String sql1 = "create table payrecord(_id integer primary key autoincrement"
				+ ",type varchar(10),price varchar(50),tag varchar(20),date date,time varchar(20),ps varchar(200))";
		arg0.execSQL(sql1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
}
