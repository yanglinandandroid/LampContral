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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.view.DragGridViewScene;
import com.justin.communicate.server.Data;
import com.justin.communicate.server.ServerHandler;
import com.justin.db.DBAdapter;
import com.justin.type.Control;
import com.justin.type.Light;
import com.justin.type.Scene;
import com.justin.type.SceneLight;

import org.apache.mina.core.buffer.IoBuffer;

import java.util.ArrayList;
import java.util.List;

//import sun.applet.Main;
//import sun.security.pkcs11.Secmod.DbMode;


public class SceneActivity extends Activity {

	public static List<String> list = null;
	//锟皆讹拷锟斤拷锟斤拷锟斤拷锟斤拷
    public DragGridAdapterScene adapter = null;
    public static TextView textview;
    public static ImageView imageview;
    public DragGridViewScene dragGridViewScene = null;
	
    /**
     * 锟斤拷取锟斤拷菘猓?
     */
    public void readDB()
    {
    	list = new ArrayList<String>();
    	//锟斤拷取锟斤拷菘锟?
    	
        DBAdapter db = new DBAdapter(SceneActivity.this);
        db.open();
        
        Scene[] scenes = db.getAllScene();
        if(scenes != null ){
        String[] names = new String[scenes.length];
        for(int i = 0; i< scenes.length; i++)
        {
        	list.add(scenes[i].getName());
        }
        }
        else
        {
        	list = null;
        }
        db.close();
       
    }
    
    /**
     *删除一个场景
     */
    public  void deleteOne(String name)
    {
        DBAdapter db = new DBAdapter(SceneActivity.this);
        db.open();
        db.deleteOneScene(name);
        Light[] lights = db.getAllLight();
        Control[] controls = db.getAllControl();
        Scene[] scenes = db.getAllScene();
        db.close();
        Data data = new Data();
        Scene scene = new Scene();
        for(Scene s : scenes)
        {
        	if(s.getName().equals(name))
        	scene = s;
        }
        for(Light l: lights)
        {
        	for(Control c: controls)
        	{
        		if(l.getControlName().equals(c.getName()))
        		{
        			byte[] datas = data.delTerScene(c.getAddr(), l.getAddr(), Byte.parseByte(scene.getid()));
        			IoBuffer buffer = IoBuffer.allocate(datas.length);
					buffer.put(datas);
					TreeActivity.addInfo("删锟斤拷锟秸讹拷锟借备锟斤拷锟斤拷~");
					TreeActivity.addInfo("锟斤拷锟?:["+com.justin.common.Common.bytes2HexString(datas)+"]");
					ServerHandler.iosession.write(buffer.flip());
					
        		}
        	}
        }
        /**
		 * 通知树更新
		 *
		 */
		MainActivity.tree.update();
    }
    
    /**
     * string锟矫碉拷锟斤拷
     * @param str
     * @return
     */
    public Light[] strToLight(String str)
    {
    	
    	String[] a = str.split(";");
    	Light[] l = new Light[a.length];
    	for(int i = 0 ; i < a.length ;i++)
    	{
    		String[] b = a[i].split(",");
    		if(b.length == 3)
    		{
    			l[i] = new Light();
    			l[i].setName(b[0]);
    			l[i].setAddr(b[1]);
    			l[i].setLiangdu(b[2]);
    		}
    		else
    		{
    			l[i] = new Light();
    			l[i].setName("");  
    			l[i].setAddr("");
    			l[i].setLiangdu("");
    		}
    	}
    	return l;
    }
    
    /**
     * 更新场景
     */
    public void updateOne(final String name2)
    {
    	LayoutInflater myinflater = LayoutInflater.from(SceneActivity.this);
    	final View view = myinflater.inflate(R.layout.update_scene, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Update Scene");
    	
    	builder.setView(view);
    	
        ListView list = (ListView) view.findViewById(R.id.listView_scene_light_update);
        final EditText name_text = (EditText)view.findViewById(R.id.scene_name_update);
        final EditText id_text   = (EditText)view.findViewById(R.id.scene_id_update);
    
        
        
        final SceneItemUpdateAdapter adapter = new SceneItemUpdateAdapter(this);
        list.setAdapter(adapter);
        List<Light> mData = new ArrayList<Light>();
      //锟斤拷取锟斤拷菘锟?
        DBAdapter db = new DBAdapter(SceneActivity.this);
        db.open();
        Light[] l = db.getAllLight();
        Scene[] scene = db.getAllScene();
        db.close();
        Scene this_s = new Scene();
        for(int i = 0; i < scene.length ; i++)
        {
        	if(scene[i].getName().equals(name2))
        	{
        		this_s = scene[i];
        	}
        }
        
        if(this_s!=null)
        {
        	name_text.setText(this_s.getName());
        	id_text.setText(this_s.getid());
        }
        
       
       Light[] ll =  strToLight(this_s.getValue());
       mData.clear();
        for(int i = 0; i <ll.length ;i++)
        {
        	
        	mData.add(ll[i]);
        }
        
        adapter.setData(mData);
        
    	builder.setPositiveButton("OK", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//锟斤拷锟?
				List<Light> light_list = adapter.getLight();
				String name = name_text.getText().toString();
				String id   = id_text  .getText().toString();
				String value = "";
				DBAdapter db = new DBAdapter(SceneActivity.this);
				Light[] lights = db.getAllLight();
				Control[] controls = db.getAllControl();

				db.open();
			
				for(int i = 0 ;i < light_list.size();i++)
				{
					Control control = new Control();
					Light l = light_list.get(i);
					value += l.getName()+",";
					value += l.getAddr()  + ",";
					value += l.getLiangdu()+";";
					for(Light ll :lights)
					{
						if(ll.getName().equals(l.getName()))
							l = ll;
					}
					for(Control c:controls)
					{
						if(c.getName().equals(l.getControlName()))
						{
							control = c;
						}
					}
					
					/**
					 * 通锟脚ｏ拷锟斤拷锟斤拷锟秸讹拷锟借备锟斤拷锟斤拷
					 */
					SceneLight sl = new SceneLight();
					sl.setNub(Byte.parseByte(id));
					sl.setBrightness(Byte.parseByte(l.getLiangdu()));
					Data data = new Data();
					byte[] datas = data.setTerScene(control.getAddr(), l.getAddr(),sl);
					IoBuffer buffer = IoBuffer.allocate(datas.length);
					buffer.put(datas);
					TreeActivity.addInfo("锟斤拷锟斤拷锟秸讹拷锟借备锟斤拷锟斤拷~");
					TreeActivity.addInfo("锟斤拷锟?:["+com.justin.common.Common.bytes2HexString(datas)+"]");
					ServerHandler.iosession.write(buffer.flip());
					
				}
				Scene scene = new Scene();
				scene.setName(name);
				scene.setId(id);
				scene.setValue(value);
				
				db.updateOneScene(name2, scene);
				db.close();
				update();
				/**
				 * 通知树更新
				 */
				MainActivity.tree.update();
			}
    		
    	}); 
    	builder.setNegativeButton("Cancel",null); 
    	builder.create();
    	builder.show();
    	
    }
 
    /**
     * 锟斤拷锟?
     * @param v
     */
    public void onClick(View v)
    {
    	LayoutInflater myinflater = LayoutInflater.from(SceneActivity.this);
    	final View view = myinflater.inflate(R.layout.add_scene, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Add Scene");
    	builder.setView(view);
    	
        ListView list = (ListView) view.findViewById(R.id.listView_scene_light);
        final EditText name_text = (EditText)view.findViewById(R.id.scene_name_add);
        final EditText id_text   = (EditText)view.findViewById(R.id.scene_id_add);
        
        final SceneItemAdapter adapter = new SceneItemAdapter(this);
        list.setAdapter(adapter);
        List<Light> mData = new ArrayList<Light>();
        
        //获取所有的灯
        DBAdapter db = new DBAdapter(SceneActivity.this);
        db.open();
        Light[] l = db.getAllLight();
        db.close();
        for(int i = 0; i <l.length ;i++)
        {
        	mData.add(l[i]);
        }
		adapter.setData(mData);
        
    	builder.setPositiveButton("OK", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//锟斤拷锟?
				List<Light> light_list = adapter.getLight();
				String name = name_text.getText().toString();
				String id   = id_text  .getText().toString();
				String value = "";
				
				DBAdapter db = new DBAdapter(SceneActivity.this);
				db.open();
				Light[] lights = db.getAllLight();
				Control[] controls = db.getAllControl();
				
				/**
				 * 锟叫讹拷id锟角凤拷锟截革拷
				 */
				if(com.justin.common.Common.isSceneIdExidt(SceneActivity.this, id))
				{
					Toast toast = Toast.makeText(getApplicationContext(),      "Failed! Id exist!", Toast.LENGTH_LONG); 
					   toast.setGravity(Gravity.CENTER, 0, 0);    toast.show(); 
					return;
				}
				
				for(int i = 0 ;i < light_list.size();i++)
				{
						Control control = new Control();
						Light l = light_list.get(i);
						value += l.getName()+",";
						value += l.getAddr()  + ",";
						value += l.getLiangdu()+";";
						for(Light ll :lights)
						{
							if(ll.getName().equals(l.getName()))
							{
								l.setConAddr(ll.getConAddr());
								l.setControlName(ll.getControlName());
								l.setGroupName(ll.getGroupName());
								
								
							}
						}
						for(Control c:controls)
						{
							if(c.getName().equals(l.getControlName()))
							{
								control = c;
							}
						}
						
						/**
						 * 通锟脚ｏ拷锟斤拷锟斤拷锟秸讹拷锟借备锟斤拷锟斤拷
						 */
						SceneLight sl = new SceneLight();
						sl.setNub(Byte.parseByte(id));
						sl.setBrightness(Byte.parseByte(l.getLiangdu()));
						Data data = new Data();
						byte[] datas = data.setTerScene(control.getAddr(), l.getAddr(),sl);
						IoBuffer buffer = IoBuffer.allocate(datas.length);
						buffer.put(datas);
						TreeActivity.addInfo("锟斤拷锟斤拷锟秸讹拷锟借备锟斤拷锟斤拷~");
						TreeActivity.addInfo("锟斤拷锟?:["+com.justin.common.Common.bytes2HexString(datas)+"]");
						ServerHandler.iosession.write(buffer.flip());
					//
				
					if(l.getName() != "")
					value += l.getName()+",";
					if(l.getAddr() != "")
					value += l.getAddr()  + ",";
					if(l.getLiangdu() != "")
					value += l.getLiangdu();
					else
					{
						value += "0";
					}
					value += ";";
					
				}
				Scene scene = new Scene();
				scene.setName(name);
				scene.setId(id);
				scene.setValue(value);
				
				db.addOneScene(scene);
				db.close();
				update();
				
				/**
				 * 通知树更新
				 */
				MainActivity.tree.update();
			}
    		
    	}); 
    	builder.setNegativeButton("Cancel",null); 
    	builder.create();
    	builder.show();
    	
    	
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scene);
		
		
		
		
		//锟斤拷始锟斤拷锟斤拷锟斤拷取锟斤拷菘锟?
		readDB();
		textview = (TextView)findViewById(R.id.text_delete_scene);
		imageview = (ImageView)findViewById(R.id.image_delete);
		 //锟斤拷锟斤拷锟矫碉拷锟斤拷锟皆讹拷锟斤拷GridView
        dragGridViewScene = (DragGridViewScene) findViewById(R.id.drag_grid_scene);
        //list 为null,锟斤拷锟皆憋拷锟斤拷锟剿ｏ拷锟斤拷锟斤拷
        if(list == null)
        {
        	adapter = null;
        }
        else
        {
        	adapter = new DragGridAdapterScene(this, list);
        }
        dragGridViewScene.setAdapter(adapter);
       
        
	}
	
	private void update()
	{
		readDB();
		adapter = new DragGridAdapterScene(this, list);
		dragGridViewScene.setAdapter(adapter);
	}
    
	/**
	 * 锟斤拷锟斤拷锟斤拷莸锟轿拷锟绞癸拷玫锟斤拷啵╝dapter锟斤拷
	 * @author justin
	 *
	 */
	public  class DragGridAdapterScene extends ArrayAdapter<String>{
		
	    public DragGridAdapterScene(Context context, List<String> objects) {
            super(context, 0, objects);
        }
        @Override
		public void add(String object) {
			// TODO Auto-generated method stub
			super.add(object);
		}
        public void update(String str)
        {
        	SceneActivity.this.updateOne(str.toString().trim());
        }
		@Override
		public void remove(String object) {
			// TODO Auto-generated method stub
			super.remove(object.toString().trim());
			
			//删除场景
			SceneActivity.this.deleteOne(object.toString().trim());
		}
		public List<String> getList(){
            return list;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view==null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.picture_item_scene, null);
            	}
          
            	Log.v("item", "------"+getItem(position));
               
                ImageView imageview= (ImageView)view.findViewById(R.id.drag_grid_item_image_scene);
                TextView textview  = (TextView) view.findViewById(R.id.drag_grid_item_text_scene);
                textview.setText(this.getList().get(position));
                
                imageview.setImageResource(R.drawable.changjing);//图锟斤拷
         
            return view;
        }
    }
	
	
	class ViewHolder 
	{ 
	    public TextView title; 
	    public ImageView image; 
	} 

}