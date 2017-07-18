package com.justin.type;

//组
public class Group {
	
	
	private String id;
	private String name;
	
	
	public void setID(String id)
	{
		this.id = id;
	}
	
	public String getID()
	{
		return this.id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	
	/*
	private String name = "";
	private java.util.Map<String, Light> light_list = new ConcurrentHashMap<String, Light>();
	
	private String value = "";
	
	public void SetName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}
	public int getCount()
	{
		return this.light_list.size();
	}
	//添加终端
	public void addLight(Light l)
	{
		this.light_list.put(l.getID(), l);
	}
	//获得终端
	public Light getLightByName(String name)
	{
		return this.light_list.get(name);
	}
	//
	public String getValue()
	{
		String result = "";
		Iterator<Light> it = light_list.values().iterator();
		while(it.hasNext())
		{
			Light l = it.next();
			result += "*"+l.getID();
		}
		return result;
	}
	public void  setValue(String value)
	{
		this.value = value;
	}
	*/
}
