package com.example.catmine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.adapter.PayRecordListAdapter;
import com.example.data.PayRecordData;
import com.example.sql.SqlUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

//记账薄
@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unused")
public class PayRecord extends Fragment implements OnClickListener {
	private View payrecord;
	// 控件相关
	private ListView listView;// 历史listview
	private View headView;// 头视图
	private RadioGroup group;// 类型
	private ImageButton calculator;// 计算器
	private EditText price;// 金额
	private TextView date;// 日期
	private TextView time;// 时间
	private Spinner spinner;// 标签
	private EditText ps;// 备注
	private Button history;// 历史
	private Button save;// 保存
	private ImageView timeicon;// 时间图标
	// 工具类相关
	private SqlUtils sqlUtils;// 数据库操作工具类
	// 参数相关
	private ArrayList<PayRecordData> list = new ArrayList<PayRecordData>();// 历史listview
	private PayRecordListAdapter adapter;// listview适配器
	private DateFormat df;// 日期时间解析类

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		payrecord = inflater.inflate(R.layout.pay_record, container, false);
		// 查找控件
		listView = (ListView) payrecord.findViewById(R.id.pay_record_listview);
		headView = getActivity().getLayoutInflater().inflate(
				R.layout.payrecord_headview, null);
		group = (RadioGroup) headView.findViewById(R.id.pay_headview_group);// 类型
		calculator = (ImageButton) headView
				.findViewById(R.id.pay_headview_calculator);// 计算器
		price = (EditText) headView.findViewById(R.id.pay_headview_price);// 金额
		date = (TextView) headView.findViewById(R.id.pay_headview_date);// 日期
		time = (TextView) headView.findViewById(R.id.pay_headview_time);// 时间
		spinner = (Spinner) headView.findViewById(R.id.pay_headview_spinner);// 标签
		ps = (EditText) headView.findViewById(R.id.pay_headview_ps);// 备注
		history = (Button) headView.findViewById(R.id.pay_headview_history);// 历史
		save = (Button) headView.findViewById(R.id.pay_headview_save);// 保存
		timeicon = (ImageView) headView
				.findViewById(R.id.pay_headview_time_icon);// 时间图标
		// 设置监听
		history.setOnClickListener(this);// 设置历史按钮监听
		save.setOnClickListener(this);// 设置保存按钮监听
		timeicon.setOnClickListener(this);// 设置时间按钮监听
		// 创建sqlitutils
		sqlUtils = new SqlUtils(getActivity());
		// 设置tag适配器
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.payrecord_spinner_item, getResources()
						.getStringArray(R.array.tag));
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerAdapter);
		// 获取当前日期
		df = new SimpleDateFormat("yyyy-MM-dd");
		String nowdate = df.format(new java.util.Date());
		// 设置日期
		date.setText(nowdate);
		// 获取当前时间
		df = new SimpleDateFormat("hh:mm:ss");
		String nowtime = df.format(new java.util.Date());
		// 设置时间
		time.setText(nowtime);
		// 查询当日记录
		ArrayList<PayRecordData> selectNow = sqlUtils.selectNow(nowdate);
		for (int i = 0; i < selectNow.size(); i++) {
			list.add(selectNow.get(i));
		}
		// 创建listview适配器
		adapter = new PayRecordListAdapter(list, getActivity());
		// 添加头视图
		listView.addHeaderView(headView);
		// 设置适配器
		listView.setAdapter(adapter);
		// 设置listview长按监听
		listView.setOnItemLongClickListener(new MyListViewOnLongClickListener());
		return payrecord;
	}

	// 监听
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.pay_headview_time_icon:// 时间图标
			// 获取当前时间
			df = new SimpleDateFormat("hh:mm:ss");
			String nowtime = df.format(new java.util.Date());
			// 设置时间
			time.setText(nowtime);
			break;
		case R.id.pay_headview_history:// 历史
			startActivity(new Intent().setClass(getActivity(),
					PayRecordHistory.class));
			break;
		case R.id.pay_headview_save:// 保存
			// 获取参数
			String priceString = "0";// 金额
			if (price.getText().toString().trim().equals("")) {
				priceString = "0";// 金额
			} else {
				priceString = price.getText().toString().trim();// 金额
			}
			String dateString = date.getText().toString().trim();// 日期
			String timeString = time.getText().toString().trim();// 时间
			String tagString = spinner.getSelectedItem().toString().trim();// 标签
			String psString = "没有备注";// 备注
			if (ps.getText().toString().trim().equals("")) {
				psString = "没有备注";// 备注
			} else {
				psString = ps.getText().toString().trim();// 备注
			}
			String typeString = "收入";
			if (group.getCheckedRadioButtonId() == R.id.pay_headview_radio0) {
				typeString = "收入";
			} else if (group.getCheckedRadioButtonId() == R.id.pay_headview_radio1) {
				typeString = "支出";
			}
			sqlUtils.insertRecord(typeString, priceString, tagString,
					dateString, timeString, psString);
			list.clear();
			ArrayList<PayRecordData> selectAll = sqlUtils.selectNow(date
					.getText().toString());
			for (int i = 0; i < selectAll.size(); i++) {
				list.add(selectAll.get(i));
			}
			// 重置界面
			adapter.notifyDataSetChanged();
			price.setText("");
			spinner.setSelection(0, true);
			ps.setText("");
			listView.setSelection(list.size());
			break;
		}
	}

	public class MyListViewOnLongClickListener implements
			OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int arg2, long arg3) {
			AlertDialog deleteDialog = new AlertDialog.Builder(getActivity())
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
									// 查询当日记录
									ArrayList<PayRecordData> selectNow = sqlUtils
											.selectNow(date.getText()
													.toString().trim());
									list.clear();
									for (int i = 0; i < selectNow.size(); i++) {
										list.add(selectNow.get(i));
									}
									adapter.notifyDataSetChanged();
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
}
