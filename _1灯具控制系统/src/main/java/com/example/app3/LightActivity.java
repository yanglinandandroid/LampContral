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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.view.DragGridViewLight;
import com.justin.communicate.server.Data;
import com.justin.communicate.server.ServerHandler;
import com.justin.db.DBAdapter;
import com.justin.type.Control;
import com.justin.type.Group;
import com.justin.type.Light;
import com.justin.type.Scene;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.util.ConcurrentHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class LightActivity extends Activity {

	public static List<String> list = null;
	//自定义适配器
    public DragGridAdapterLight adapter = null;
    public static TextView textview;
    public static ImageView imageview;
    public DragGridViewLight dragGridViewLight = null;
	
    /**
     * 读取数据库，
     */
    public void readDB()
    {
    	list = new ArrayList<String>();
    	//读取数据库
    	
        DBAdapter db = new DBAdapter(LightActivity.this);
        db.open();
        
        Light[] lights = db.getAllLight();
        if(lights != null ){
        String[] ips = new String[lights.length];
        for(int i = 0; i< lights.length; i++)
        {
        	list.add(lights[i].getName());
        }
        }
        else
        {
        	list = null;
        }
        
      
        db.close();
       
    }
    
    /**
     * 删除
     */
    public  void deleteOne(String name)
    {
        DBAdapter db = new DBAdapter(LightActivity.this);
        db.open();
        Light[] lights = db.getAllLight();
        db.deleteOneLight(name);
        
     
		Control[] controls = db.getAllControl();
		Group[] groups = db.getAllGroup();
        
        db.close();
        
        

		/**
		 * 通信，终端设备信息删除
		 */
        /*
        Light light = new Light();
		String control_name = "";
		String control_addr = "";
		String light_addr   = "";
		//获得所有灯地址
		Set<String> light_addr_set = new ConcurrentHashSet<String>();
		for(Light l : lights)
		{
			if(l.getName().equals(name))
			{
				light = l;
				control_name = l.getControlName();
			}
		}
		//获得控制器地址
		for(Control c:controls)
		{
			if(c.getName().equals(control_name))
				control_addr = c.getAddr();
		}
		
		Data data = new Data();
		byte [] datas = data.deleteTerminalInfo(control_addr,light.getAddr());
		IoBuffer buffer = IoBuffer.allocate(datas.length);
		buffer.put(datas);
		TreeActivity.addInfo("删除终端设备信息~");
		TreeActivity.addInfo("数据:["+com.justin.common.Common.bytes2HexString(datas)+"]");
		ServerHandler.iosession.write(buffer.flip());
        */
		/**
		 * 通信，删除终端设备组
		 * 
		 */
		/*
		Group gp = new Group();
		for(Group g : groups)
		{
			if(g.getName().equals(light.getGroupName()))
			{
				gp   = g;
			}
		}
		datas = data.delTerGroup(control_addr, light.getAddr(),Integer.parseInt(gp.getID()));
		buffer = IoBuffer.allocate(datas.length);
		buffer.put(datas);
		TreeActivity.addInfo("删除终端设备组~");
		TreeActivity.addInfo("数据:["+com.justin.common.Common.bytes2HexString(datas)+"]");
		ServerHandler.iosession.write(buffer.flip());
		*/
		
        /**
		 * 更新树
		 *
		 */
		MainActivity.tree.update();
		/**
		 * 维护场景
		 */
	//	this.deleteFromScene(name);
    }

    /**
     * 从场景中删除
     * @param name
     */
     public void deleteFromScene(String name)
     {
    	 DBAdapter db = new DBAdapter(LightActivity.this);
    	 db.open();
    	 Scene[] scenes = db.getAllScene();
    	 if(scenes == null || scenes.length == 0)
    		 return;
    	 for(int i = 0 ; i <scenes.length ; i++)
    	 {
    		  Scene s = scenes[i];
    		  String value = s.getValue();
    		  String[] a  = value.split(";");
    		  for(int j = 0 ;j<a.length ; j++)
    		  {
    			  String[] b = a[j].split(",");
    			  if(b[0].equals(name))
    			  {
    				  a[j] = "";
    			  }
    		  }
    		  
    		  String value2 = "";
    		  for(int m = 0 ; m<a.length;m++)
    		  {
    			  value2 += a[m];
    		  }
    		  
    		  s.setValue(value2);
    		  db.updateOneScene(s.getName(), s);
    		  
    	 }
    	 db.close();
     }
     
     /**
      * 从场景中修改灯
      */
     public void updateFromScene(String name,Light l)
     {
    	 DBAdapter db = new DBAdapter(LightActivity.this);
    	 db.open();
    	 Scene[] scenes = db.getAllScene();
    	 if(scenes == null || scenes.length == 0)
    		 return;
    	 for(int i = 0 ; i <scenes.length ; i++)
    	 {
    		  Scene s = scenes[i];
    		  String value = s.getValue();
    		  String[] a  = value.split(";");
    		  for(int j = 0 ;j<a.length ; j++)
    		  {
    			  String[] b = a[j].split(",");
    			  if(b[0].equals(name))
    			  {
    				  a[j] = l.getName()+","+l.getAddr()+","+b[2];
    			  }
    		  }
    		  
    		  String value2 = "";
    		  for(int m = 0 ; m<a.length;m++)
    		  {
    			  value2 += a[m]+";" ;
    		  }
    		  
    		  s.setValue(value2);
    		  db.updateOneScene(s.getName(), s);
    		  
    	 }
    	 db.close();
     }
     
     /**
      * 从场景中添加
      */
     public void addFromScene(Light l)
     {
    	 DBAdapter db = new DBAdapter(LightActivity.this);
    	 db.open();
    	 Scene[] scenes = db.getAllScene();
    	 if(scenes == null || scenes.length == 0)
    		 return;
    	 for(int i = 0 ; i <scenes.length ; i++)
    	 {
    		  Scene s = scenes[i];
    		  String value = s.getValue();
    		 
    		  
    		  String value2 = l.getName()+","+l.getAddr()+","+0+";";
    		  s.setValue(value + value2);
    		  db.updateOneScene(s.getName(), s);
    		  
    	 }
    	 db.close();
     }
    
    /**
     * 修改
     */
    public void updateOne(final String ip2)
    {
    	LayoutInflater myinflater = LayoutInflater.from(LightActivity.this);
    	final View view = myinflater.inflate(R.layout.update_lightt, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Update Light");
    	
    	EditText text_id   = (EditText) view.findViewById(R.id.light_id_update);
    	EditText text_name = (EditText) view.findViewById(R.id.light_name_update);
    	
    	text_name.setText(ip2);
		DBAdapter db3 = new DBAdapter(LightActivity.this);
		db3.open();
		Light[] l = db3.getAllLight();
		db3.close();
		Light this_c  = new Light();
		for(int i = 0 ; i < l.length ; i++)
		{
			if(l[i].getName().equals(ip2))
			{
				this_c = l[i];
			}
		}
		if(this_c != null)
		{
			text_id.setText(this_c.getAddr());
	    	
		}
    	
    	
    	builder.setView(view);
    	
    	
    	
    	//添加组到下拉框
    	//添加组
    	
    	int position_group = 0;
        DBAdapter db = new DBAdapter(LightActivity.this);
        db.open();
        Group[] groups = db.getAllGroup();
        db.close();
        if(groups != null ){
        String[] g = new String[groups.length];
        for(int i = 0; i< groups.length; i++)
        {
        	g [i] = groups[i].getName();
        	if(g[i].equals(this_c.getGroupName()))
        	{
        		position_group = i;
        	}
        }
    	ArrayAdapter<String> adapter_group;  
    	adapter_group = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,g);
    	adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
    	 Spinner spinner_group = (Spinner) view.findViewById(R.id.spinner_group_update);
         //将adapter 添加到spinner中  
         spinner_group.setAdapter(adapter_group);  
         spinner_group.setSelection(position_group);
        // spinner_group.setVisibility(View.VISIBLE);
         
         //添加控制器到下拉框
         DBAdapter db2 = new DBAdapter(LightActivity.this);
         db2.open();
         Control[] controls = db2.getAllControl();
         db2.close();
         if(controls != null ){
         String[] c = new String[controls.length];
         int position = 0;
         for(int i = 0; i< controls.length; i++)
         {
         	c [i] = controls[i].getName();
         	if(c[i].equals(this_c.getControlName()))
         	{
         		position = i;
         	}
         }
     	ArrayAdapter<String> adapter_control;  
     	adapter_control = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,c);
     	adapter_control.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
     	 Spinner spinner_control = (Spinner) view.findViewById(R.id.spinner_control_update);
          //将adapter 添加到spinner中  
     	 spinner_control.setSelection(position);
     	spinner_control.setAdapter(adapter_control);  
     	//spinner_control.setVisibility(View.VISIBLE);
         
         

    	builder.setPositiveButton("OK", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//入库
				
				EditText text_id   = (EditText) view.findViewById(R.id.light_id_update);
		    	EditText text_name = (EditText) view.findViewById(R.id.light_name_update);
		    	
		    	Spinner group = (Spinner) view.findViewById(R.id.spinner_group_update);
		    	Spinner control = (Spinner) view.findViewById(R.id.spinner_control_update);
				Light light = new Light();
				String name = text_name.getText().toString();
				light.setAddr(text_id.getText().toString());
				light.setName(text_name.getText().toString());
				
				light.setGroupName(group.getSelectedItem().toString());
				light.setControlName(control.getSelectedItem().toString());
				
				//TreeActivity.addInfo("id="+light.getID()+",name="+light.getName()+",control="+light.getControlName()+",group="+light.getGroupName());
				DBAdapter db = new DBAdapter(LightActivity.this);
				db.open();
				db.updateLight(ip2, light);//.addOneLight(light);
				
				Light[] lights = db.getAllLight();
				Control[] controls = db.getAllControl();
				Group[] groups   = db.getAllGroup();
				db.close();
				
				String group_name =  light.getGroupName();
				
				/**
				 * 通信，给控制器设置终端信息
				 */
				String control_name = light.getControlName();
				String control_addr = "";
				//获得所有灯地址
				Set<String> light_addr_set = new ConcurrentHashSet<String>();
				for(Light l : lights)
				{
					if(l.getControlName().equals(control_name))
						light_addr_set.add(l.getAddr());
				}
				//获得控制器地址
				for(Control c:controls)
				{
					if(c.getName().equals(control_name))
						control_addr = c.getAddr();
				}
				Data data = new Data();
				byte [] datas = data.initTerminalInfo(control_addr, light_addr_set);
				IoBuffer buffer = IoBuffer.allocate(datas.length);
        		buffer.put(datas);
        		TreeActivity.addInfo("设置控制器终端信息~");
        		TreeActivity.addInfo("数据:["+com.justin.common.Common.bytes2HexString(datas)+"]");
        		ServerHandler.iosession.write(buffer.flip());
				
        		/**
        		 * 通信，设置终端设备组
        		 * 
        		 */
        		Group gp = new Group();
        		for(Group g : groups)
        		{
        			if(g.getName().equals(group_name))
        				gp = g;
        		}
        		datas = data.delTerGroup(control_addr, light.getAddr(), Integer.parseInt(gp.getID()));
        		buffer = IoBuffer.allocate(datas.length);
        		buffer.put(datas);
        		TreeActivity.addInfo("设置终端设备组~");
        		TreeActivity.addInfo("数据:["+com.justin.common.Common.bytes2HexString(datas)+"]");
        		ServerHandler.iosession.write(buffer.flip());
        		
				/**
				 * 维护场景
				 */
			//	updateFromScene(ip2, light);
				
				/**
				 * 更新树
				 *
				 */
				MainActivity.tree.update();
				
				if(LightActivity.this.adapter == null)
				{
					list = new ArrayList<String>();
					list.add(light.getName());
		        	adapter = new DragGridAdapterLight(LightActivity.this, list);
		        	
		        	 dragGridViewLight.setAdapter(adapter);
				}
				else
				{
					
					LightActivity.this.adapter.getList().add(light.getName());
					LightActivity.this.adapter.getList().remove(ip2);
					LightActivity.this.dragGridViewLight.setAdapter(LightActivity.this.adapter);
				}
			}
    		
    	}); 
    	builder.setNegativeButton("Cancel",null); 
    	builder.create();
    	builder.show();
        }
        }
    	
    }
    
    /**
     * 添加
     * @param v
     */
    public void onClick(View v)
    {
    	LayoutInflater myinflater = LayoutInflater.from(LightActivity.this);
    	final View view = myinflater.inflate(R.layout.add_light, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Add Light");
    	builder.setView(view);
    	
    	//添加组到下拉框
    	//添加组
        DBAdapter db = new DBAdapter(LightActivity.this);
        db.open();
        Group[] groups = db.getAllGroup();
        db.close();
        if(groups != null ){
        String[] g = new String[groups.length];
        for(int i = 0; i< groups.length; i++)
        {
        	g [i] = groups[i].getName();
        }
    	ArrayAdapter<String> adapter_group;  
    	adapter_group = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,g);
    	adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
    	 Spinner spinner_group = (Spinner) view.findViewById(R.id.spinner_group);
         //将adapter 添加到spinner中  
         spinner_group.setAdapter(adapter_group);  
       //  spinner_group.setVisibility(View.VISIBLE);
         
         //添加控制器到下拉框
         DBAdapter db2 = new DBAdapter(LightActivity.this);
         db2.open();
         Control[] controls = db2.getAllControl();
         db2.close();
         if(controls != null ){
         String[] c = new String[controls.length];
         for(int i = 0; i< controls.length; i++)
         {
         	c [i] = controls[i].getName();
         }
     	ArrayAdapter<String> adapter_control;  
     	adapter_control = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,c);
     	adapter_control.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
     	 Spinner spinner_control = (Spinner) view.findViewById(R.id.spinner_control);
          //将adapter 添加到spinner中  
     	spinner_control.setAdapter(adapter_control);  
     	//spinner_control.setVisibility(View.VISIBLE);
         
         

    	builder.setPositiveButton("OK", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//入库
				
				EditText text_id   = (EditText) view.findViewById(R.id.light_id_add);
		    	EditText text_name = (EditText) view.findViewById(R.id.light_name_add);
		    	
		    	Spinner group = (Spinner) view.findViewById(R.id.spinner_group);
		    	Spinner control = (Spinner) view.findViewById(R.id.spinner_control);
				Light light = new Light();
				String name = text_name.getText().toString();
				light.setAddr(text_id.getText().toString());
				light.setName(text_name.getText().toString());
				
				light.setGroupName(group.getSelectedItem().toString());
				light.setControlName(control.getSelectedItem().toString());
				
				/**
				 * 判断id是否重复
				 */
				if(com.justin.common.Common.isLightIdExidt(LightActivity.this, light.getAddr()))
				{
					Toast toast = Toast.makeText(getApplicationContext(),      "Failed! Id exist!", Toast.LENGTH_LONG); 
					   toast.setGravity(Gravity.CENTER, 0, 0);    toast.show(); 
					return;
				}
				
				
				//TreeActivity.addInfo("id="+light.getID()+",name="+light.getName()+",control="+light.getControlName()+",group="+light.getGroupName());
				DBAdapter db = new DBAdapter(LightActivity.this);
				db.open();
				db.addOneLight(light);
				Light[] lights = db.getAllLight();
				Control[] controls = db.getAllControl();
				Group[] groups = db.getAllGroup();
				db.close();
				
				/**
				 * 通信，给控制器设置终端信息
				 */
				String control_name = light.getControlName();
				String control_addr = "";
				//获得所有灯地址
				Set<String> light_addr_set = new ConcurrentHashSet<String>();
				for(Light l : lights)
				{
					if(l.getControlName().equals(control_name))
						light_addr_set.add(l.getAddr());
				}
				//获得控制器地址
				for(Control c:controls)
				{
					if(c.getName().equals(control_name))
						control_addr = c.getAddr();
				}
				Data data = new Data();
				byte [] datas = data.initTerminalInfo(control_addr, light_addr_set);
				IoBuffer buffer = IoBuffer.allocate(datas.length);
        		buffer.put(datas);
        		TreeActivity.addInfo("设置控制器终端信息~");
        		TreeActivity.addInfo("数据:["+com.justin.common.Common.bytes2HexString(datas)+"]");
        		ServerHandler.iosession.write(buffer.flip());
				
        		/**
        		 * 通信，设置终端设备组
        		 * 
        		 */
        		Group gp = new Group();
        		for(Group g : groups)
        		{
        			if(g.getName().equals(light.getGroupName()))
        				gp = g;
        		}
        		datas = data.delTerGroup(control_addr, light.getAddr(), Integer.parseInt(gp.getID()));
        		buffer = IoBuffer.allocate(datas.length);
        		buffer.put(datas);
        		TreeActivity.addInfo("设置终端设备组~");
        		TreeActivity.addInfo("数据:["+com.justin.common.Common.bytes2HexString(datas)+"]");
        		ServerHandler.iosession.write(buffer.flip());
        		
				/**
				 * 维护场景
				 */
		//		addFromScene(light);
				
				/**
				 * 更新树
				 *
				 */
				MainActivity.tree.update();
				
				if(LightActivity.this.adapter == null)
				{
					list = new ArrayList<String>();
					list.add(light.getName());
		        	adapter = new DragGridAdapterLight(LightActivity.this, list);
		        	
		        	 dragGridViewLight.setAdapter(adapter);
				}
				else
				{
					
					LightActivity.this.adapter.getList().add(light.getName());
					LightActivity.this.dragGridViewLight.setAdapter(LightActivity.this.adapter);
				}
			}
    		
    	}); 
    	builder.setNegativeButton("Cancel",null); 
    	builder.create();
    	builder.show();
        }
        }
    	
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light);
		//初始化，读取数据库
		readDB();
		textview = (TextView)findViewById(R.id.text_delete_light);
		imageview = (ImageView)findViewById(R.id.image_delete);
		 //后面用到的自定义GridView
        dragGridViewLight = (DragGridViewLight) findViewById(R.id.drag_grid_light);
        //list 为null,所以报错了！！！
        if(list == null)
        {
        	adapter = null;
        }
        else
        {
        	adapter = new DragGridAdapterLight(this, list);
        }
        dragGridViewLight.setAdapter(adapter);
       
        
	}
    
	/**
	 * 进行数据的维护使用的类（adapter）
	 * @author justin
	 *
	 */
	public  class DragGridAdapterLight extends ArrayAdapter<String>{
		
	    public DragGridAdapterLight(Context context, List<String> objects) {
            super(context, 0, objects);
        }
        @Override
		public void add(String object) {
			// TODO Auto-generated method stub
			super.add(object);
		}
        public void update(String str)
        {
        	LightActivity.this.updateOne(str.toString().trim());
        }
		@Override
		public void remove(String object) {
			// TODO Auto-generated method stub
			super.remove(object.toString().trim());
			
			//删除一个
			LightActivity.this.deleteOne(object.toString().trim());
		}
		public List<String> getList(){
            return list;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view==null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.picture_item_light, null);
            	}
          
            	Log.v("item", "------"+getItem(position));
               
                ImageView imageview= (ImageView)view.findViewById(R.id.drag_grid_item_image_light);
                TextView textview  = (TextView) view.findViewById(R.id.drag_grid_item_text_light);
                textview.setText(this.getList().get(position));
                
                imageview.setImageResource(R.drawable.deng);//图标
         
            return view;
        }
    }
	
	
	class ViewHolder 
	{ 
	    public TextView title; 
	    public ImageView image; 
	} 

}