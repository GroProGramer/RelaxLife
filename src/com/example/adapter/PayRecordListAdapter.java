package com.example.adapter;

import java.util.ArrayList;

import com.example.catmine.R;
import com.example.data.PayRecordData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//记账薄适配器
public class PayRecordListAdapter extends BaseAdapter {
	private ArrayList<PayRecordData> list;
	private Context context;
	private LayoutInflater layoutInflater;

	public PayRecordListAdapter(ArrayList<PayRecordData> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (list == null || list.isEmpty()) {
			return 0;
		} else {
			return list.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		PayRecordHolder holder;
		if (arg1 == null) {
			arg1 = layoutInflater.inflate(R.layout.payrecord_list_item, null);
			holder = new PayRecordHolder();
			holder.type = (TextView) arg1
					.findViewById(R.id.payrecord_list_item_type);
			holder.price = (TextView) arg1
					.findViewById(R.id.payrecord_list_item_price);
			holder.tag = (TextView) arg1
					.findViewById(R.id.payrecord_list_item_tag);
			holder.ps = (TextView) arg1
					.findViewById(R.id.payrecord_list_item_ps);
			holder.date = (TextView) arg1
					.findViewById(R.id.payrecord_list_item_date);
			holder.time = (TextView) arg1
					.findViewById(R.id.payrecord_list_item_time);
			arg1.setTag(holder);
		} else {
			holder = (PayRecordHolder) arg1.getTag();
		}
		PayRecordData data = list.get(position);
		holder.type.setText(data.getType());
		holder.price.setText(data.getPrice());
		if (data.getType().equals("收入")) {
			holder.price.setTextColor(context.getResources().getColor(
					R.color.blue2));
		} else {
			holder.price.setTextColor(context.getResources().getColor(
					R.color.red1));
		}
		holder.tag.setText(data.getTag());
		holder.ps.setText(data.getPs());
		holder.date.setText(data.getDate());
		holder.time.setText(data.getTime());
		return arg1;
	}

	class PayRecordHolder {
		TextView type;// 类型(收入,支出)
		TextView price;// 金额
		TextView tag;// 标签
		TextView ps;// 备注
		TextView date;// 日期
		TextView time;// 时间
	}
}
