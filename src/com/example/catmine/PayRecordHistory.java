package com.example.catmine;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.adapter.PayRecordListAdapter;
import com.example.data.PayRecordData;
import com.example.sql.SqlUtils;

//记账薄历史
@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unused")
public class PayRecordHistory extends Activity implements OnClickListener {
	// 控件相关
	private EditText editText;// 备注输入框
	private Button endbtn;// 结束时间button
	private Button startbtn;// 开始时间button
	private TextView starttext;// 开始时间textview
	private TextView endtext;// 结束时间textview
	private Spinner tagspinner;// 标签spinner
	private Button searchbtn;// 搜索button
	private DatePicker datePicker;// 日期控件
	// 工具类相关
	private SqlUtils sqlUtils;// 数据库操作工具类
	// 参数相关
	private DateFormat df;// 日期时间解析类
	private ListView listView;// 显示记录listview
	private PayRecordListAdapter adapter;// listview 适配器
	private View headview;// 头视图
	private TextView income;// 头视图-收入
	private TextView spending;// 头视图-支出
	private TextView balance;// 头视图-结余
	private boolean type = false;// 用于区分开始按钮和结束按钮
	private MyDatePickerDialog myDatePickerDialog;// 日期监听
	private DatePickerDialog datePickerDialog;// 日期选择框
	private ArrayList<PayRecordData> list;// 查询信息显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pay_record_history);
		// 查找控件
		editText = (EditText) findViewById(R.id.pay_history_edittext);// 备注输入框
		endbtn = (Button) findViewById(R.id.pay_history_endbtn);// 结束时间button
		startbtn = (Button) findViewById(R.id.pay_history_startbtn);// 开始时间button
		starttext = (TextView) findViewById(R.id.pay_history_starttime);// 开始时间textview
		endtext = (TextView) findViewById(R.id.pay_history_endtime);// 结束时间textview
		tagspinner = (Spinner) findViewById(R.id.pay_history_spinner);// 标签spinner
		searchbtn = (Button) findViewById(R.id.pay_history_search);// 搜索button
		listView = (ListView) findViewById(R.id.pay_history_listview);// 显示记录listview
		headview = getLayoutInflater().inflate(R.layout.payhistory_headview,
				null);// 头视图
		income = (TextView) headview
				.findViewById(R.id.payhistory_headview_income);// 头视图-收入
		spending = (TextView) headview
				.findViewById(R.id.payhistory_headview_spending);// 头视图-支出
		balance = (TextView) headview
				.findViewById(R.id.payhistory_headview_balance);// 头视图-结余
		// 设置监听
		searchbtn.setOnClickListener(this);// 设置搜索按钮监听
		startbtn.setOnClickListener(this);// 设置开始时间按钮监听
		endbtn.setOnClickListener(this);// 设置结束时间按钮监听
		// 设置标签spinner
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				R.layout.payrecord_spinner_item, getResources().getStringArray(
						R.array.tag_all));
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tagspinner.setAdapter(spinnerAdapter);
		// 设置日期
		df = new SimpleDateFormat("yyyy-MM-dd");
		String nowdate = df.format(new java.util.Date());
		starttext.setText("--");
		endtext.setText(nowdate);
		// 查询全部信息显示
		sqlUtils = new SqlUtils(this);
		list = sqlUtils.selectAll();
		// 创建listview适配器
		adapter = new PayRecordListAdapter(list, PayRecordHistory.this);
		// 添加头视图
		listView.addHeaderView(headview);
		// 设置listview适配器
		listView.setAdapter(adapter);
		// 设置长按监听
		listView.setOnItemLongClickListener(new deleteHistory());
		// 日期控件
		datePicker = new DatePicker(PayRecordHistory.this);
		datePicker.init(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), null);
		// 日期监听
		myDatePickerDialog = new MyDatePickerDialog();
		// 设置收入支出结余显示视图
		getCombined(list);
	}

	// 监听
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.pay_history_search:// 搜索按钮
			String trim = starttext.getText().toString().trim();
			if (trim.equals("--")) {
				trim = "1970-01-01";
			}
			ArrayList<PayRecordData> selectHistory = sqlUtils.selectHistory(
					trim, endtext.getText().toString().trim(), tagspinner
							.getSelectedItem().toString().trim(), editText
							.getText().toString().trim());
			list.clear();
			if (selectHistory.isEmpty()) {
				// 设置收入支出结余显示视图
				getCombined(list);
				adapter.notifyDataSetChanged();
			} else {
				for (int i = 0; i < selectHistory.size(); i++) {
					list.add(selectHistory.get(i));
				}
				// 设置收入支出结余显示视图
				getCombined(list);
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.pay_history_startbtn:// 开始时间按钮
			type = false;
			datePickerDialog = new DatePickerDialog(PayRecordHistory.this,
					myDatePickerDialog, datePicker.getYear(),
					datePicker.getMonth(), datePicker.getDayOfMonth());
			datePickerDialog.show();
			break;
		case R.id.pay_history_endbtn:// 结束时间按钮
			type = true;
			datePickerDialog = new DatePickerDialog(PayRecordHistory.this,
					myDatePickerDialog, datePicker.getYear(),
					datePicker.getMonth(), datePicker.getDayOfMonth());
			datePickerDialog.show();
			break;
		}
	}

	// 长按监听事件
	public class deleteHistory implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int arg2, long arg3) {
			AlertDialog deleteDialog = new AlertDialog.Builder(
					PayRecordHistory.this)
					.setTitle("提示")
					.setMessage("是否要删除该记录?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// 删除该项记录
									sqlUtils.deleteRecord(list.get(arg2 - 1)
											.getId());
									// 检索信息
									String trim = starttext.getText()
											.toString().trim();
									ArrayList<PayRecordData> selectAll = null;
									if (trim.equals("--")) {
										trim = "1970-01-01";
									}
									selectAll = sqlUtils
											.selectHistory(trim, endtext
													.getText().toString()
													.trim(), tagspinner
													.getSelectedItem()
													.toString().trim(),
													editText.getText()
															.toString().trim());
									list.clear();
									for (int i = 0; i < selectAll.size(); i++) {
										list.add(selectAll.get(i));
									}
									// 刷新适配器
									adapter.notifyDataSetChanged();
									// 设置结余
									getCombined(list);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									return;
								}
							}).create();
			deleteDialog.show();
			return false;
		}
	}

	// 计算收入支出并显示
	private void getCombined(ArrayList<PayRecordData> arrayList) {
		int incomeCount = 0;// 收入次数
		int spendingCount = 0;// 支出次数
		double incomeSum = 0;// 收入总金额
		double spendingSum = 0;// 支出总金额
		int sumCount = 0;// 合计总次数
		double sum = 0;// 结余总金额
		DecimalFormat format = new DecimalFormat("0.00");
		for (int i = 0; i < arrayList.size(); i++) {
			PayRecordData data = arrayList.get(i);
			if (data.getType().equals("收入")) {
				incomeCount++;
				incomeSum = incomeSum + Double.parseDouble(data.getPrice());
			} else if (data.getType().equals("支出")) {
				spendingCount++;
				spendingSum = spendingSum + Double.parseDouble(data.getPrice());
			}
		}
		// 显示支出收入结余信息
		income.setText(format.format(incomeSum) + "(" + incomeCount + ")");
		spending.setText(format.format(spendingSum) + "(" + spendingCount + ")");
		balance.setText(format.format((incomeSum - spendingSum)) + "("
				+ (incomeCount + spendingCount) + ")");
	}

	// 日期选择框确定监听
	public class MyDatePickerDialog implements
			DatePickerDialog.OnDateSetListener {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			datePicker.updateDate(year, monthOfYear, dayOfMonth); // 更新日期值
			String month = "";
			if ((monthOfYear + 1) < 10) {
				month = "0" + (monthOfYear + 1);
			} else {
				month = "" + (monthOfYear + 1);
			}
			String day = "";
			if (dayOfMonth < 10) {
				day = "0" + dayOfMonth;
			} else {
				day = "" + dayOfMonth;
			}
			if (type == false) {// 开始时间按钮打开的日期选择框
				starttext.setText(year + "-" + month + "-" + day);
			} else if (type == true) {// 结束时间按钮打开的日期选择框
				endtext.setText(year + "-" + month + "-" + day);
			}
		}
	}

}
