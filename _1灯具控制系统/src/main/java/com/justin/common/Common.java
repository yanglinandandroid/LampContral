package com.justin.common;

import android.content.Context;

import com.justin.db.DBAdapter;
import com.justin.type.Group;
import com.justin.type.Light;
import com.justin.type.Node;
import com.justin.type.Scene;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//import sun.security.pkcs11.Secmod.DbMode;

@SuppressWarnings("LossyEncoding")
public class Common {
	//inputstream2 string GBK
	public   static String   inputStream2String   (InputStream   in)   throws   IOException   { 
        StringBuffer   out   =   new   StringBuffer(); 
        byte[]   b   =   new   byte[4096]; 
        for   (int   n;   (n   =   in.read(b))   !=   -1;)   { 
                out.append(new   String(b,   0,   n,"GBK")); 
        } 
        return   out.toString(); 
	}
	
	
	/**
	 * 閿熸枻鎷峰彇閿熸枻鎷烽敓琛楁枻鎷?
	 * @param group
	 * @return
	 */
	public static byte[] getGroup(byte group)
	{
		byte[] b = new byte[8];
		for(int i = 0 ; i < 7;i++)
			b[i] = (byte)0xff;
		b[7] =  group;
		return b;
	}
	
	/**
	 * 閿熸枻鎷峰彇閿熸枻鎷烽敓琛楁枻鎷?
	 * @param group
	 * @return
	 */
	public static String getGroup_lcpshd(byte group)
	{
		String re = "00000000000000";
		String s = Byte.toString(group);
		if(s.length() == 1)
			s = '0'+s;
		return re+s;
	}
	
	

	/**
	 * 閿熸枻鎷峰潃閿熻鍑ゆ嫹杞敓鏂ゆ嫹涓洪敓鏂ゆ嫹鍧?閿熻鏂ゆ嫹
	 * @param str
	 * @return
	 */
	public static byte[] stringToAddr(String str)
	{
		str = str.replaceAll("0x", "");
		str = str.replaceAll("0X", "");
		String re = str.trim();
		if(re.length() > 16)
			return null;
		if(re.length() < 16)
		{
			for(int i = 0 ; i <16-re.length() ; i ++ )
			{
				re = '0' +re; 
			}
		}
		byte[] result = new byte[8];
		for(int j = 0 ; j < 16 ; j+=2)
		{
			char up,down;
			up = re.charAt(j);
			down = re.charAt(j+1);
			result[j/2] = (byte)char2Int(up, down);
			
		}
		return result;
	}
	
	public static byte charToByte(char c)
	{
		if( c>= '0' && c<= '9')
		{
			return (byte) (c-'0');
		}
		else if(c == 'a'|| c=='A')
			return 10;
		else if(c == 'b'|| c=='B')
			return 11;
		else if(c == 'c'|| c=='C')
			return 12;
		else if(c == 'd'|| c=='D')
			return 13;
		else if(c == 'e'|| c=='E')
			return 14;
		else if(c == 'f'|| c=='F')
			return 15;
		else return -1;
	}
	
	public static int char2Int(char up,char down)
	{
		byte up_b = charToByte(up);
		byte down_b = charToByte(down);
		return ((up_b * 16) + down_b);
	}
	
	/**
	 *
	 * @param i
	 * @return
	 */
	public static String intToIp(int i) {     

	       

        return (i & 0xFF ) + "." +     

      ((i >> 8 ) & 0xFF) + "." +     

      ((i >> 16 ) & 0xFF) + "." +     

      ( i >> 24 & 0xFF) ;

   } 
	
	/**
	 *
	 * @param addr
	 * @return
	 */
	public static boolean isAddr(String addr)
	{
		addr = addr.replaceAll("0x", "");
		addr = addr.replaceAll("0X", "");
		if (addr.length() != 16)
			return false;
		for(int i = 0 ; i< 16 ;i++)
		{
			char c = addr.charAt(i);
			if( c>= '0' && c<='9' ){}
			else if(c >= 'a' && c<= 'f'){}
			else if(c>='A' && c<='F'){}
			else{
				return false;
			}
			
		}
		return true;
	}
	
	
	public static boolean isIP(String ip)
	{
		int num = 0 ;
		for(int i = 0 ;i < ip.length() ; i++)
		{
			char c = ip.charAt(i);
			if(c == '.')
			{
				num++;
				
			}
			if(!(c>='0'&&c<='9'))
			{
				if(c != '.')
				return false;
			}
		}
		if(num != 3)
			return false;
		
		String[] s = ip.split("\\.");
		if(s.length != 4)
		{return false;}
		
		if(Integer.parseInt(s[0])>255)
			return false;
		
		if(Integer.parseInt(s[1])>255)
			return false;
		
		if(Integer.parseInt(s[2])>255)
			return false;
		
		if(Integer.parseInt(s[3])>255)
			return false;
		return true;
	}
	
	public static boolean iSGroupIdExist(Context _context,String id) 
	{
		DBAdapter db = new DBAdapter(_context);
		db.open();
		Group[] g = db.getAllGroup();
		db.close();
		if(g == null)
			return false;
		for(int i = 0 ; i< g.length; i++)
		{
			if(g[i].getID().equals(id))
				return true;
		}
		return false;
	}
	
	public static boolean isLightIdExidt(Context _context,String id)
	{
		DBAdapter db = new DBAdapter(_context);
		db.open();
		Light[] l  = db.getAllLight();
		db.close();
		if(l == null)
			return false;
		for(int i  =  0 ;i  < l.length ; i++)
		{
			if(l[i].getAddr() .equals(id))
				return true;
		}
		return false;
	}
	
	public static boolean isSceneIdExidt(Context _context,String id)
	{
		DBAdapter db = new DBAdapter(_context);
		db.open();
		Scene[] s  = db.getAllScene();
		db.close();
		if(s == null )
			return false;
		for(int i  =  0 ;i  < s.length ; i++)
		{
			if(s[i].getid().equals(id))
				return true;
		}
		return false;
	}
	
	public static void main()
	{
		Common c = new Common();
		String str = "0012558712";
		byte[] s = c.stringToAddr(str);
		for(int i  = 0 ; i< s.length ; i++ )
		System.out.println(s[i]+"  ");
		
	}

    
    //判断是否为广播
    public static boolean isBroadCast(List<Node> nodes)
    {
    	Iterator  it = nodes.iterator();
    	while(it.hasNext())
    	{
    		Node node = (Node) it.next();
    		if(node.getText().equals("All")){
    			return true;
    		}
    		
    	}
    	return false;
    }
    
    //获取组
    public static Set<String> getGroup(List<Node> nodes)
    {
    	Iterator it = nodes.iterator();
    	Set<String> set = new HashSet<String>();
    	while(it.hasNext())
    	{
    		
    		Node node = (Node)it.next();
    		String text = node.getText();
    		Node father = node.getParent();
    		if(father != null && father.getText().equals("All"))
    		{
    			set.add(text) ;
    		}
    	}
    	return set;
    }
    
    //获取灯
    public static Set<String> getLight(List<Node> nodes)
    {
    	Iterator it = nodes.iterator();
    	Set<String> set = new HashSet<String>();
    	while(it.hasNext())
    	{
    		Node node = (Node)it.next();
    		String text = node.getText();
    		Node f = node.getParent();
    		Node ff = f.getParent();
    		if(ff!=null && ff.getText().equals("All"))
    		{
    		
    			set.add(text);
    		}
    	}
    	return set;
    }
    /**
	 * 字节数组转化成16进制
	 *
	 */
	public static String bytes2HexString(byte[]src){
		StringBuilder stringBuilder = new StringBuilder("");
		if(src == null || src.length<=0){
			return null;
		}
		
		
		for(int i= 0;i<src.length;i++){
			
			int v = src[i] & 0XFF;
			String hv = Integer.toHexString(v);
			if(hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv).append(' ');
		}
		return stringBuilder.toString();
		
	}
	
	
	
}
