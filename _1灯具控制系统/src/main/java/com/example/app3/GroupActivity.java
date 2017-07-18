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

import com.example.view.DragGridViewGroup;
import com.justin.db.DBAdapter;
import com.justin.type.Group;

import java.util.ArrayList;
import java.util.List;


public class GroupActivity extends Activity {

	public static List<String> list = null;
	//�Զ���������
    public DragGridAdapterGroup adapter = null;
    public static TextView textview;
    public static ImageView imageview;
    public DragGridViewGroup dragGridViewGroup = null;
	
    /**
     * ��ȡ���ݿ⣬
     */
    public void readDB()
    {
    	list = new ArrayList<String>();
    	//��ȡ���ݿ�
    	
        DBAdapter db = new DBAdapter(GroupActivity.this);
        db.open();
        Group[] groups = db.getAllGroup();
        if(groups != null ){
        String[] ips = new String[groups.length];
        for(int i = 0; i< groups.length; i++)
        {
        	ips [i] = groups[i].getName();
        	list.add(ips[i]);
        }
        }
        else
        {
        	list = null;
        }
        db.close();
       
    }
    
    /**
     * ɾ��
     */
    public  void deleteOne(String ip)
    {
        /**
		 * ������
		 *
		 */
		MainActivity.tree.update();
		
		DBAdapter db = new DBAdapter(GroupActivity.this);
		db.open();
		db.deleteOneGroup(ip);
		db.close();
    }
    
    /**
     * �޸�
     */
    public void updateOne(final String name2)
    {
    	LayoutInflater myinflater = LayoutInflater.from(GroupActivity.this);
    	final View view = myinflater.inflate(R.layout.update_group, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Update Group");
    	TextView name_text = (TextView)view.findViewById(R.id.group_name_update);
		TextView ip_text   = (TextView)view.findViewById(R.id.group_ip_update);
    	
		name_text.setText(name2);
		DBAdapter db = new DBAdapter(GroupActivity.this);
		db.open();
		Group[] c = db.getAllGroup();
		db.close();
		Group this_c  = new Group();
		for(int i = 0 ; i < c.length ; i++)
		{
			if(c[i].getName().equals(name2))
			{
				this_c = c[i];
			}
		}
		if(this_c != null)
		{
			ip_text.setText(this_c.getID());
	    	
		}
    	
    	builder.setView(view);
    	builder.setPositiveButton("OK", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				TextView name_text = (TextView)view.findViewById(R.id.group_name_update);
				TextView ip_text   = (TextView)view.findViewById(R.id.group_ip_update);
				String name = name_text.getText().toString();
				String id = ip_text.getText().toString();
				
				
				
				if(name.length() > 0)
				{
					if(GroupActivity.this.adapter == null)
					{
						list = new ArrayList<String>();
						list.add(name);
			        	adapter = new DragGridAdapterGroup(GroupActivity.this, list);
			        	 dragGridViewGroup.setAdapter(adapter);
					}
					else
					{
						GroupActivity.this.adapter.remove(name2);
						GroupActivity.this.adapter.add(name);
						
						GroupActivity.this.dragGridViewGroup.setAdapter(GroupActivity.this.adapter);
					}
					//������
					DBAdapter db = new DBAdapter(GroupActivity.this);
					db.open();
					Group g = new Group();
					g.setID(id);
					g.setName(name);
					db.updateGroup(name2, g);//.addOneGroup(g);
					//db.deleteOneGroup(name2);
					db.close();
				

			    	/**
					 * ������
					 *
					 */
					MainActivity.tree.update();
					
				}
				   
			}
    		
    	});
    	builder.setNegativeButton("Cancel",null); 
    	builder.create();
    	builder.show();
    	
    	
    	
    }
    
    /**
     * ���
     * @param v
     */
    public void onClick(View v)
    {

    	LayoutInflater myinflater = LayoutInflater.from(GroupActivity.this);
    	final View view = myinflater.inflate(R.layout.add_group, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Add Group");
    	builder.setView(view);
    	builder.setPositiveButton("OK", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				TextView name_text = (TextView)view.findViewById(R.id.group_name_add);
				TextView ip_text   = (TextView)view.findViewById(R.id.group_ip_add);
				String name = name_text.getText().toString();
				String ip = ip_text.getText().toString();
				
				/**
				 * �ж�id�Ƿ��ظ�
				 */
				if(com.justin.common.Common.iSGroupIdExist(GroupActivity.this, ip))
				{
					Toast toast = Toast.makeText(getApplicationContext(),      "Failed! Id exist!", Toast.LENGTH_LONG); 
					   toast.setGravity(Gravity.CENTER, 0, 0);    toast.show(); 
					return;
				}
				
				if(name.length() > 0)
				{
					if(GroupActivity.this.adapter == null)
					{
						list = new ArrayList<String>();
						list.add(name);
			        	adapter = new DragGridAdapterGroup(GroupActivity.this, list);
			        	 dragGridViewGroup.setAdapter(adapter);
					}
					else
					{
						GroupActivity.this.adapter.getList().add(name);
						GroupActivity.this.dragGridViewGroup.setAdapter(GroupActivity.this.adapter);
					}
					//������
					DBAdapter db = new DBAdapter(GroupActivity.this);
					db.open();
					Group group = new Group();
					group.setName(name);
					group.setID(ip);
					db.addOneGroup(group);
					
					db.close();
					/**
					 * ������
					 *
					 */
					MainActivity.tree.update();
				}
			}
    	});
    	builder.setNegativeButton("Cancel",null); 
    	builder.create();
    	builder.show();
    	
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		//��ʼ������ȡ���ݿ�
		readDB();
		textview = (TextView)findViewById(R.id.text_delete_group);
		imageview = (ImageView)findViewById(R.id.image_delete);
				

		 //�����õ����Զ���GridView
        dragGridViewGroup = (DragGridViewGroup) findViewById(R.id.drag_grid_group);
        //list Ϊnull,���Ա����ˣ�����
        if(list == null)
        {
        	adapter = null;
        }
        else
        {
        	adapter = new DragGridAdapterGroup(this, list);
        }
        dragGridViewGroup.setAdapter(adapter);
       
        
	}
    
	/**
	 * �������ݵ�ά��ʹ�õ��ࣨadapter��
	 * @author justin
	 *
	 */
	public  class DragGridAdapterGroup extends ArrayAdapter<String>{
		
	    public DragGridAdapterGroup(Context context, List<String> objects) {
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
			
			//ɾ��һ��
			GroupActivity.this.deleteOne(object.toString().trim());
		}
		
	    
	    public void update(String str)
	    {
	    	GroupActivity.this.updateOne(str);
	    }
	    
		public List<String> getList(){
            return list;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view==null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.pivture_item_group, null);
            	}
          
            	Log.v("item", "------"+getItem(position));
               
                ImageView imageview= (ImageView)view.findViewById(R.id.drag_grid_item_image_group);
                TextView textview  = (TextView) view.findViewById(R.id.drag_grid_item_text_group);
                textview.setText(this.getList().get(position));
                
                imageview.setImageResource(R.drawable.zu);//ͼ��
         
            return view;
        }
    }
	
	
	class ViewHolder 
	{ 
	    public TextView title; 
	    public ImageView image; 
	} 

}
