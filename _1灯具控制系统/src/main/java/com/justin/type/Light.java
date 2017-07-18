package com.justin.type;

//÷’∂À£®µ∆£©
public class Light {
	private String addr ;
	private String name ; 
	private String group_name = "";
	private String control_name = "";
	private String control_addr = "";
	private String liangdu = "";
	private String state = "";
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
	public String getAddr()
	{
		return this.addr;
	}
	public void setGroupName(String group_name)
	{
		this.group_name = group_name;
	}
	public String getGroupName()
	{
		return this.group_name;
	}
	public void setControlName(String control_name)
	{
		this.control_name = control_name;
	}
	public String getControlName()
	{
		return this.control_name;
	}
	
	public String getLiangdu()
	{
		return this.liangdu;
	}
	public void setLiangdu(String liangdu)
	{
		this.liangdu = liangdu;
	}
	
	public void setConAddr(String control_addr)
	{
		this.control_addr = control_addr;
	}
	
	public String getConAddr()
	{
		return this.control_addr;
	}
}
