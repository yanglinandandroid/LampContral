package com.example.app3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.view.DragGridView;
import com.justin.db.DBAdapter;
import com.justin.type.Control;

import java.util.ArrayList;
import java.util.List;

public class ControllerActivity extends Activity {

	public static List<String> list = null;
	// 自定义适配器
	public DragGridAdapter adapter = null;
	public static TextView textview;
	public static ImageView imageview;
	public DragGridView dragGridView = null;

	/**
	 * 读取数据库，
	 */
	public void readDB() {
		list = new ArrayList<String>();
		// 读取数据库
		DBAdapter db = new DBAdapter(ControllerActivity.this);
		db.open();

		Control[] controls = db.getAllControl();
		if (controls != null) {
			String[] ips = new String[controls.length];
			for (int i = 0; i < controls.length; i++) {
				ips[i] = controls[i].getName();
				list.add(ips[i]);
			}
		} else {
			list = null;
		}

		db.close();

	}

	/**
	 * 删除
	 */
	public void deleteOne(String ip) {
		DBAdapter db = new DBAdapter(ControllerActivity.this);
		db.open();
		db.deleteOneControl(ip.trim());
		db.close();
	}

	/**
	 * 修改
	 */
	public void updateOne(final String name2) {
		LayoutInflater myinflater = LayoutInflater
				.from(ControllerActivity.this);
		final View view = myinflater.inflate(R.layout.update_controller, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Update Controller");
		// 加载数据
		TextView name_text = (TextView) view
				.findViewById(R.id.controller_name_update);
		TextView ip_text = (TextView) view
				.findViewById(R.id.controller_ip_update);
		name_text.setText(name2);
		DBAdapter db = new DBAdapter(ControllerActivity.this);
		db.open();
		Control[] c = db.getAllControl();
		db.close();
		Control this_c = new Control();
		for (int i = 0; i < c.length; i++) {
			if (c[i].getName().equals(name2)) {
				this_c = c[i];
			}
		}
		if (this_c != null) {
			ip_text.setText(this_c.getAddr());

		}

		builder.setView(view);
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				TextView name_text = (TextView) view
						.findViewById(R.id.controller_name_update);
				TextView ip_text = (TextView) view
						.findViewById(R.id.controller_ip_update);
				String name = name_text.getText().toString();
				String ip = ip_text.getText().toString();

				if (com.justin.common.Common.isAddr(ip) == false) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Failed! Addr error!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}

				if (name.length() > 0) {
					if (ControllerActivity.this.adapter == null) {
						list = new ArrayList<String>();
						list.add(name);
						adapter = new DragGridAdapter(ControllerActivity.this,
								list);
						dragGridView.setAdapter(adapter);
					} else {
						ControllerActivity.this.adapter.remove(name2);
						ControllerActivity.this.adapter.add(name);

						ControllerActivity.this.dragGridView
								.setAdapter(ControllerActivity.this.adapter);
					}
					// 添加入库
					DBAdapter db = new DBAdapter(ControllerActivity.this);
					db.open();
					Control con = new Control();
					con.setAddr(ip);
					con.setName(name);
					long w = db.updateControl(name2, con);
					System.out.println(w);
					db.close();

				}

			}

		});
		builder.setNegativeButton("Cancel", null);
		builder.create();
		builder.show();
	}

	/**
	 * 添加
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		LayoutInflater myinflater = LayoutInflater
				.from(ControllerActivity.this);
		final View view = myinflater.inflate(R.layout.add_controller, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Controller");
		builder.setView(view);
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				TextView name_text = (TextView) view
						.findViewById(R.id.controller_name_add);
				TextView ip_text = (TextView) view
						.findViewById(R.id.controller_ip_add);
				String name = name_text.getText().toString();
				String ip = ip_text.getText().toString();

				if (com.justin.common.Common.isAddr(ip) == false) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Addr error!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}

				if (name.length() > 0) {
					if (ControllerActivity.this.adapter == null) {
						list = new ArrayList<String>();
						list.add(name);
						adapter = new DragGridAdapter(ControllerActivity.this,
								list);
						dragGridView.setAdapter(adapter);
					} else {
						ControllerActivity.this.adapter.getList().add(name);
						ControllerActivity.this.dragGridView
								.setAdapter(ControllerActivity.this.adapter);
					}
					// 添加入库
					DBAdapter db = new DBAdapter(ControllerActivity.this);
					db.open();
					Control con = new Control();
					con.setAddr(ip);
					con.setName(name);
					db.addController(con);
					db.close();

				}

			}

		});
		builder.setNegativeButton("Cancel", null);
		builder.create();
		builder.show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		// 初始化，读取数据库
		readDB();
		textview = (TextView) findViewById(R.id.text_delete);
		imageview = (ImageView) findViewById(R.id.image_delete);

		// 后面用到的自定义GridView
		dragGridView = (DragGridView) findViewById(R.id.drag_grid);
		// list 为null,所以报错了！！！
		if (list == null) {
			adapter = null;
		} else {
			adapter = new DragGridAdapter(this, list);
		}
		dragGridView.setAdapter(adapter);

	}

	/**
	 * 进行数据的维护使用的类（adapter）
	 * 
	 * @author justin
	 * 
	 */
	public class DragGridAdapter extends ArrayAdapter<String> {

		public DragGridAdapter(Context context, List<String> objects) {
			super(context, 0, objects);
		}

		@Override
		public void add(String object) {
			// TODO Auto-generated method stub
			super.add(object);
		}

		@Override
		public void remove(String object) {
			// TODO Auto-generated method stub
			super.remove(object.toString().trim());

			// 删除一个,数据库
			ControllerActivity.this.deleteOne(object.toString().trim());
			this.getList().remove(object.toString().trim());
		}

		public void update(String object) {
			ControllerActivity.this.updateOne(object.toString());
		}

		public List<String> getList() {
			return list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(
						R.layout.picture_item, null);
			}

			Log.v("item", "------" + getItem(position));

			ImageView imageview = (ImageView) view
					.findViewById(R.id.drag_grid_item_image);
			TextView textview = (TextView) view
					.findViewById(R.id.drag_grid_item_text);
			textview.setText(this.getList().get(position));

			imageview.setImageResource(R.drawable.controller);

			return view;
		}
	}

	class ViewHolder {
		public TextView title;
		public ImageView image;
	}

}
