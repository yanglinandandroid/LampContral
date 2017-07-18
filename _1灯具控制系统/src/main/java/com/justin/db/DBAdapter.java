package com.justin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.justin.type.Control;
import com.justin.type.Group;
import com.justin.type.Light;
import com.justin.type.Scene;

/**
 * 数据库操作类，包含数据库的打开创建、插入数据、增加数据、删除、更新数据
 * 
 * @author justin
 * 
 */
public class DBAdapter {
	private SQLiteDatabase db;
	private final Context context;
	public DBOpenHelper dbOpenHelper;
	private static final int DB_VERSION = 1;
	private static final String DB_NAME_LIGHT = "light.db";
	
	
	//地址
	private static final String DB_TABLE_ADDR = "addr";
	private static final String KEY_IP_ADDR   = "ip";
	private static final String KEY_PORT_ADDR ="port";
	
	// 灯信息
	private static final String DB_TABLE_LIGHT = "light";
	private static final String KEY_ID_LIGHT = "id";
	private static final String KEY_NAME_LIGHT = "name";
	private static final String KEY_CONTROL_LIGHT = "control_name";
	private static final String KEY_GROUP_LIGHT     = "group_name";

	// 组信息
	private static final String DB_TABLE_GROUP = "grp";
	private static final String KEY_ID_GROUP = "name";
	//private static final String KEY_NAME_GROUP = "name";
	private static final String KEY_VALUE_GROUP = "value";


	// 控制器信息
	private static final String DB_TABLE_CON = "controller_t";
	private static final String KEY_NAME_CON = "name";
	private static final String KEY_IP_CON = "ip";
	
	
	//场景
	private static final String DB_TABLE_SCENE = "scene_t";
	private static final String KEY_NAME_SCENE = "name";
	private static final String KEY_ID_SCENE   = "id";
	private static final String KEY_VALUE_SCENE  = "value";
	
	// 构造函数
	public DBAdapter(Context _context) {
		this.context = _context;
	}

	/**
	 * close the database 关闭数据库
	 * 
	 * @author justin
	 * 
	 */
	public void close() {
		if (this.db != null) {
			db.close();
			db = null;
		}
	}

	/**
	 * 重新创建表
	 */
	public void reCreateTable()
	{
		dbOpenHelper.onCreate(this.db);
		
	}
	
	/**
	 * open the database 打开数据库
	 * 
	 * @author justin
	 * 
	 */
	public void open() {
		dbOpenHelper = new DBOpenHelper(context, DB_NAME_LIGHT, null,
				DB_VERSION);
		//dbOpenHelper.onCreate(db);
		try {
			
			
			db = dbOpenHelper.getWritableDatabase();
		//	reCreateTable();
		
		} catch (SQLiteException ex) {
			db = dbOpenHelper.getReadableDatabase();
		}
		System.out.println(db.getPath());
		
		//addAddr("127.1.1.1","9900");
	}

	// help类，用于打开数据库、更新数据库
	private static class DBOpenHelper extends SQLiteOpenHelper {
		private static final String DB_CREATE_LIGHT = "create table "
				+ DB_TABLE_LIGHT + " (" + KEY_ID_LIGHT
				+ " varchar(32) primary key, " 
				+KEY_NAME_LIGHT+" varcahr(32),"
				+KEY_GROUP_LIGHT+" varchar(32),"
				+ KEY_CONTROL_LIGHT
				+ " varchar(32))";
		private static final String DB_CREATE_GROUP = "create table "
				+ DB_TABLE_GROUP + " (" + KEY_ID_GROUP
				+ " varchar(32) primary key, " + KEY_VALUE_GROUP
				+ " varchar(32))";
		private static final String DB_CREATE_CON = "create table "
				+ DB_TABLE_CON + " (" + KEY_NAME_CON
				+ " varchar(64) primary key, " + KEY_IP_CON
				+ " varchar(64))";
		private static final String DB_CREATE_SCENE = "create table "
				+ DB_TABLE_SCENE +" (" +KEY_NAME_SCENE 
				+"  varchar(64) primary key, " + KEY_ID_SCENE
				+"  varchar(64), "+KEY_VALUE_SCENE+" varchar(64))";
		
		private static final String DB_CREATE_ADDR  = "create table "
				+ DB_TABLE_ADDR +" (" +KEY_IP_ADDR 
				+"  varchar(64) primary key, " + KEY_PORT_ADDR
				+" varchar(64))";
		

		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			// TODO Auto-gene rated method stub
			
			arg0.execSQL(DB_CREATE_ADDR);
			arg0.execSQL(DB_CREATE_LIGHT);
			arg0.execSQL(DB_CREATE_GROUP);
			arg0.execSQL(DB_CREATE_CON);
			arg0.execSQL(DB_CREATE_SCENE);
			
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			arg0.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_LIGHT);
			arg0.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_GROUP);
			arg0.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_CON);
			arg0.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_SCENE);
			onCreate(arg0);
		}
	}

	// 灯表插入灯
	public long addOneLight(Light light) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_ID_LIGHT, light.getAddr());
		newValues.put(KEY_NAME_LIGHT,light.getName());
		newValues.put(KEY_GROUP_LIGHT, light.getGroupName());
		newValues.put(KEY_CONTROL_LIGHT, light.getControlName());
		return db.insert(DB_TABLE_LIGHT, null, newValues);
	}

	
	// 删除一个终端
	public long deleteOneLight(String name) {
		
		db.execSQL("delete from "+DB_TABLE_LIGHT+" where "+KEY_NAME_LIGHT+"= '"+name+"'");
		return 0;
	}

	// 更新一个终端
	public long updateLight(String name, Light light) {
		//this.addOneLight(light);
		//this.deleteOneLight(name);
		
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_ID_LIGHT, light.getAddr());
		newValues.put(KEY_NAME_LIGHT,light.getName());
		newValues.put(KEY_GROUP_LIGHT, light.getGroupName());
		newValues.put(KEY_CONTROL_LIGHT, light.getControlName());
		/*
		db.execSQL("update light set name = "+light.getName()+",id="+light.getID()+",control_name = "+light.getControlName()
				
				+",group_name = "+light.getGroupName()+" where name="+name);
		
		TreeActivity.addInfo("update light set name = "+light.getName()+",id="+light.getID()+",control_name = "+light.getControlName()
				
				+",group_name = "+light.getGroupName()+" where name="+name);

				*/
		db.update(DB_TABLE_LIGHT,newValues , "name=?",new String[]{name});
		return 1;
	}
	
	//删除light表
	public void deleteTableLight()
	{
		 db.execSQL("drop table "+DB_TABLE_LIGHT);
		 
	}
	
	public void deleteAll()
	{
		db.execSQL("drop table "+DB_TABLE_GROUP);
		 db.execSQL("drop table "+DB_TABLE_LIGHT);
		 db.execSQL("drop table "+DB_TABLE_CON);
	}
	// 获得数据库中所有的灯
	public Light[] getAllLight() {
		Cursor cursor = db.query(DB_TABLE_LIGHT, new String[] { KEY_ID_LIGHT,KEY_NAME_LIGHT,KEY_GROUP_LIGHT,
				KEY_CONTROL_LIGHT }, null, null, null, null, null);
		return convertToLight(cursor);
	}
	//获取所有的控制器

	public Control[] getAllControl(){
		Cursor cursor = db.query(DB_TABLE_CON, new String[]{KEY_NAME_CON,KEY_IP_CON},null,null,null,null,null);
		if(cursor!=null)
		return  convertToControl(cursor);
		else
			return null;
	}
	

	// 装换成灯类
	private Light[] convertToLight(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Light[] lights = new Light[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			lights[i] = new Light();
			lights[i].setAddr(cursor.getString(0));
			lights[i].setName(cursor.getString(1));
			lights[i].setGroupName(cursor.getString(2));
			lights[i].setControlName(cursor.getString(3));
			cursor.moveToNext();
		}
		return lights;
	}
	//转换成控制器
	private Control[] convertToControl(Cursor cursor)
	{
		int resultCounts = cursor.getCount();
		if(resultCounts == 0 || !cursor.moveToFirst())
		{
			return null;
		}
		Control[] controls = new Control[resultCounts];
		for(int i = 0 ; i <resultCounts;i++)
		{
			controls[i] = new Control();
			
			controls[i].setName(cursor.getString(0));
			controls[i].setAddr(cursor.getString(1));
			cursor.moveToNext();
		}
		return controls;
	}
	//转换成组
	private Group[] convertToGroup(Cursor cursor)
	{
		int resultCounts = cursor.getCount();
		if(resultCounts == 0 || !cursor.moveToFirst())
		{
			return null;
		}
		Group[] groups = new Group[resultCounts];
		for(int i = 0 ; i <resultCounts;i++)
		{
			groups[i] = new Group();
			groups[i].setID(cursor.getString(0));
			groups[i].setName(cursor.getString(1));
			cursor.moveToNext();
		}
		return groups;
	}

	// 添加组数据
	public long addOneGroup(Group g) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_ID_GROUP, g.getID());
		newValues.put(KEY_VALUE_GROUP, g.getName());
		return db.insert(DB_TABLE_GROUP, null, newValues);
	}

	// 删除一个组
	public long deleteOneGroup(String name) {
		 db.execSQL("delete from "+DB_TABLE_GROUP+" where "+KEY_VALUE_GROUP+"= '"+name+"'");
		// db.delete(DB_TABLE_GROUP, "?=?",new String[]{KEY_VALUE_GROUP,name});
		 return 1;
	}
	//删除所有组
	public long deleteAllGroup()
	{
		return db.delete(DB_TABLE_GROUP, null, null);
	}
	
	//获得所有组
	public Group[] getAllGroup()
	{
		Cursor cursor = db.query(DB_TABLE_GROUP, new String[]{KEY_ID_GROUP,KEY_VALUE_GROUP},null,null,null,null,null);
		if(cursor!=null)
		return  convertToGroup(cursor);
		else
			return null;
	}

	// 更新一个组
	public long updateGroup(String name, Group g) {
	
		this.deleteOneGroup(name);
		this.addOneGroup(g);
		return 1;
		/*
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_ID_GROUP, g.getID());
		updateValues.put(KEY_VALUE_GROUP, g.getName());
		return db.update(DB_TABLE_GROUP, updateValues, "name=?",
				new String[]{name});
		*/
	}

	// 添加控制器
	public long addController(Control c) {
		/*
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_NAME_CON, c.getName());
		newValues.put(KEY_IP_CON, c.getIP());
		return db.insert(DB_TABLE_CON, null, newValues);
		*/
		db.execSQL("insert into controller_t(name,ip) values ('"+c.getName()+"','"+c.getAddr()+"')");
System.out.println("insert into controller_t(name,ip) values ('"+c.getName()+"','"+c.getAddr()+"')");
		return 1;
	}
	
	public void addAddr(String ip,String port)
	{
		db.execSQL("insert into addr(ip,port) values ('" + ip+"','"+port+"')");
		
	}
	
	public void update(String ip,String port)
	{
		db.execSQL("delete  from addr");
		addAddr(ip,port);
	}
	
	public String getIP()
	{
		Cursor cursor = db.query(DB_TABLE_ADDR, new String[]{KEY_IP_ADDR,KEY_PORT_ADDR},null,null,null,null,null);
		int resultCounts = cursor.getCount();
		if(resultCounts == 0 || !cursor.moveToFirst())
		{
			return null;
		}
		String ip = "";
		for(int i = 0 ; i <resultCounts;i++)
		{
			ip = cursor.getString(0);
			cursor.moveToNext();
		}
		
		
		return ip;
	}
	
	
	public String getPort()
	{
		Cursor cursor = db.query(DB_TABLE_ADDR, new String[]{KEY_IP_ADDR,KEY_PORT_ADDR},null,null,null,null,null);
		int resultCounts = cursor.getCount();
		if(resultCounts == 0 || !cursor.moveToFirst())
		{
			return null;
		}
		String port = "";
		for(int i = 0 ; i <resultCounts;i++)
		{
			port = cursor.getString(1);
			cursor.moveToNext();
		}
		
		
		return port;
	}
	
	
	

	// 删除一个控制器
	public long deleteOneControl(String name) {
		long l = 99;
		try{
		 l = db.delete(DB_TABLE_CON , KEY_NAME_CON + "= '" + name+"'", null);
		// System.out.println("l = "+l);
			
		// db.delete(DB_TABLE_CON, "?=?", new String[]{KEY_VALUE_CON,name});
		 return l;
		}
		catch(Exception e)
		{
			System.out.println("控制器删除失败!"+e);
			return l;
		}
		
		
	}
	
	//删除所有控制器
	public long deleteAllControl()
	{
		return db.delete(DB_TABLE_CON, null, null);
	}

	// 更新一个控制器
	public long updateControl(String name, Control c) {
		
	//	this.deleteOneControl(name);
	//	this.addController(c);
	//	return 1;
		
		
		ContentValues updateValues = new ContentValues();
		updateValues.put("name", c.getName());
		updateValues.put("ip", c.getAddr());
		
		return db.update("controller_t", updateValues, "name = ?",
				new String[]{name});
		//return (long)1;
		//db.execSQL("update controller_t set name = '"+c.getName()+"',ip = '"+c.getIP()+"' where name='"+name+"'");
		//System.out.println("update controller_t set name = '"+c.getName()+"',ip = '"+c.getIP()+"' where name='"+name+"'");
		//return 1;
	}  
	
	
	//添加一个场景
	public void addOneScene(Scene s)
	{
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_NAME_SCENE, s.getName());
		newValues.put(KEY_ID_SCENE, s.getid());
		newValues.put(KEY_VALUE_SCENE, s.getValue());
		db.insert(DB_TABLE_SCENE, null, newValues);
	}
	
	//删除一个场景
	
	public void deleteOneScene(String s)
	{
			 db.execSQL("delete from "+DB_TABLE_SCENE+" where "+KEY_NAME_SCENE+"= '"+s+"'");
	}
	
	
	//修改一个场景
	public void updateOneScene(String name,Scene s)
	{
		
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_NAME_SCENE, s.getName());
		updateValues.put(KEY_ID_SCENE, s.getid());
		updateValues.put(KEY_VALUE_SCENE, s.getValue());
		db.update(DB_TABLE_SCENE, updateValues, "name = ?",
				new String[]{name});
	}
	
	//获取所有场景

		public Scene[] getAllScene(){
			Cursor cursor = db.query(DB_TABLE_SCENE, new String[]{KEY_NAME_SCENE,KEY_ID_SCENE,KEY_VALUE_SCENE},null,null,null,null,null);
			if(cursor!=null)
			return  convertToScene(cursor);
			else
				return null;
		}
	
	
	//装换成场景
	private Scene[] convertToScene(Cursor cursor)
	{
		int resultCounts = cursor.getCount();
		if(resultCounts == 0 || !cursor.moveToFirst())
		{
			return null;
		}
		Scene[] scenes = new Scene[resultCounts];
		for(int i = 0 ; i <resultCounts;i++)
		{
			scenes[i] = new Scene();
			scenes[i].setName(cursor.getString(0));
			scenes[i].setId(cursor.getString(1));
			scenes[i].setValue(cursor.getString(2));
			cursor.moveToNext();
		}
		return scenes;
	}
	
	

}
