package com.justin.communicate.server;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Set;


import org.apache.mina.core.buffer.IoBuffer;

import com.justin.type.SceneLight;
/**
 * 
 * 
 * 协议为:  第一份协议，lcp300(可能是)
 * 构造要发送的数据，2014/2/17
 * @author justin
 *
 */
public class Data {
	
	/**
	 * 心跳  1003
	 * @param addr
	 * @return
	 */
	public byte[] sendHeart(String con_addr)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(11);
		buffer.put(con_addr_byte);
		buffer.putShort((short)0x1003);
		buffer.put((byte)0xff);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 终端设备信息初始化  1008
	 * @param con_addr
	 * @param deng_addr
	 * @return
	 */
	public byte[] initTerminalInfo(String con_addr,String[] deng_addr)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(8+2+1+deng_addr.length*8);
		buffer.put(con_addr_byte);
		buffer.putShort((short)0x1008);
		buffer.put((byte)0xff);
		for(int i  = 0 ; i < deng_addr.length ; i++)
		{
			buffer.put(com.justin.common.Common.stringToAddr(deng_addr[i]));
		}
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	public byte[] initTerminalInfo(String con_addr,Set<String> deng_addr)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(8+2+1+deng_addr.size()*8);
		buffer.put(con_addr_byte);
		buffer.putShort((short)0x1008);
		buffer.put((byte)0xff);
		Iterator it = deng_addr.iterator();
		while(it.hasNext())
		{
			String str = (String)it.next();
			buffer.put(com.justin.common.Common.stringToAddr(str));
		}
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 终端设备信息删除  1009
	 * @param con_addr
	 * @param deng_addr
	 * @return
	 */
	public byte[] deleteTerminalInfo(String con_addr,String[] deng_addr)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(8+2+1+deng_addr.length*8);
		buffer.put(con_addr_byte);
		buffer.putShort((short)0x1009);
		buffer.put((byte)0xff);
		for(int i  = 0 ; i < deng_addr.length ; i++)
		{
			buffer.put(com.justin.common.Common.stringToAddr(deng_addr[i]));
		}
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	public byte[] deleteTerminalInfo(String con_addr,String deng_addr)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(8+2+1+8);
		buffer.put(con_addr_byte);
		buffer.putShort((short)0x1009);
		buffer.put((byte)0xff);
		
			buffer.put(com.justin.common.Common.stringToAddr(deng_addr));
		
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 查询控制器的终端设备的地址信息  100A
	 * @param con_addr
	 * @param num
	 * @return
	 */
	public byte[] getTerminalInfo(String con_addr,short num)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(8+2+1+2);
		buffer.put(con_addr_byte);
		buffer.putShort((short)0x100A);
		buffer.put((byte)0xff);
		buffer.putShort(num);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 设置集中控制器时间同步  1101
	 * @param con_addr
	 * @param year
	 * @param month
	 * @param date
	 * @param day
	 * @param hour
	 * @param min
	 * @param sec
	 * @return
	 */
	public byte[] setConTime(String con_addr,byte year,byte month,byte date,byte day,byte hour ,byte min,byte sec)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(8+2+1+7);
		buffer.put(con_addr_byte);
		buffer.putShort((short)0x1101);
		buffer.put((byte)0xff);
		buffer.put(year).put(month).put(date).put(day).put(hour).put(min).put(sec);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 设置集中控制器调光模式   1102
	 * @param con_addr
	 * @param mode   0x00:远程调光模式   0x01:自动调光模式
	 * @return
	 */
	public byte[] setConLightMode(String con_addr,byte mode)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(8+2+1+1);
		buffer.put(con_addr_byte).putShort((short)0x1102).put((byte)0xff).put(mode);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}

	/**
	 * 查询集中控制器当前系统时间   1201
	 * @param con_addr
	 * @return
	 */
	public byte[] getConCurTime(String con_addr)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(8+2+1);
		buffer.put(con_addr_byte).putShort((short)0x1201).put((byte)0xff);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 查询集中控制器当前调光工作模式 1202
	 * @param con_addr
	 * @return
	 */
	public  byte[] getConCurMode(String con_addr)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer =  IoBuffer.allocate(8+2+1 );
		buffer.put(con_addr_byte).putShort((short)0x1202).put((byte)0xff);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 查询集中控制器时控计划  1203
	 * @param con_addr
	 * @param light_addr
	 * @return
	 */
	public byte[] getConTimeSchedule(String con_addr,String light_addr)
	{
		byte[] con_addr_byte = com.justin.common.Common.stringToAddr(con_addr);
		IoBuffer buffer = IoBuffer.allocate(11+8);
		buffer.put(con_addr_byte).putShort((short)0x1203).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr));
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 终端设备调光  2001
	 * @param con_addr
	 * @param light_addr    可以是广播地址，组地址，终端设备地址
	 * @param brightness
	 * @return
	 */
	public byte[] setLightBrightness(String con_addr,String light_addr,byte brightness)
	{
		IoBuffer buffer =  IoBuffer.allocate(11+9);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2001).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr)).put(brightness);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	public byte[] setLightBrightness(String con_addr,byte[] light_addr,byte brightness)
	{
		IoBuffer buffer =  IoBuffer.allocate(11+9);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2001).put((byte)0xff).put(light_addr).put(brightness);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	
	
	/**
	 * 终端设备恢复出厂设置  2002
	 * @param con_addr
	 * @param light_addr     可以是广播地址，组地址，终端设备地址
	 * @param act     
	 * @return
	 */
	public byte[] setTerminalBack(String con_addr,String light_addr,byte act)
	{
		IoBuffer buffer = IoBuffer.allocate(11+9);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2002).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr)).put(act);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	
	/**
	 * 设置终端设备默认亮度值  2101
	 * @param con_addr
	 * @param light_addr
	 * @param brightness_init     0- 100   0表示关灯
	 * @param brightness_false
	 * @param brightness_max
	 * @param brightness_min
	 * @return
	 */
	public byte[] setTerToInitBrightness(String con_addr,String light_addr,byte brightness_init,byte brightness_false,byte brightness_max,byte brightness_min)
	{
		IoBuffer buffer = IoBuffer.allocate(11+8+4);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2101).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr))
		.put(brightness_init).put(brightness_false).put(brightness_max).put(brightness_min);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	
	/**
	 * 设置终端设备淡光时间   2102
	 * @param con_addr
	 * @param light_addr
	 * @param time
	 * @return
	 */
	public byte[] setShimmerTime(String con_addr,String light_addr,byte time)
	{
		IoBuffer buffer = IoBuffer.allocate(11+9);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2102).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr))
		.put(time);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 设置终端设备调光系数   2103
	 * 实际亮度值 = 调光亮度值 * 调光系数 /100
	 * @param con_addr
	 * @param light_addr
	 * @param xishu
	 * @return
	 */
	public byte[] setTerLightRate(String con_addr,String light_addr,byte xishu)
	{
		IoBuffer buffer = IoBuffer .allocate(11+9);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2103).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr))
		.put(xishu);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 设置终端设备组 2104
	 * @param con_addr
	 * @param light_addr 
	 * @param group  0-63
	 * @return
	 */
	public byte[] setTerGroup(String con_addr,String light_addr,int group)
	{
		IoBuffer buffer = IoBuffer.allocate(11+16);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2104).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr))
		.put(com.justin.common.Common.getGroup((byte) group));
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 终端设备组删除 2105
	 * @param con_addr
	 * @param light_addr
	 * @param group
	 * @return
	 */
	public byte[] delTerGroup(String con_addr,String light_addr,int group)
	{
		IoBuffer buffer = IoBuffer.allocate(11+16);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2105).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr))
		.put(com.justin.common.Common.getGroup((byte)group));
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 终端设备场景设置  0x2106
	 * @param con_addr
	 * @param light_addr
	 * @param s
	 * @return
	 */
	public byte[] setTerScene(String con_addr,String light_addr,SceneLight[] s)
	{
		IoBuffer buffer = IoBuffer.allocate(11+8+2*s.length);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2106).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr)
				);
		for(int i = 0 ; i < s.length ; i++)
		{
			buffer.put(s[i].getNub()).put(s[i].getBrightness());
		}
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	public byte[] setTerScene(String con_addr,String light_addr,SceneLight s)
	{
		IoBuffer buffer = IoBuffer.allocate(11+8+2);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2106).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr)
				);
		
			buffer.put(s.getNub()).put(s.getBrightness());
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 终端场景删除  0x2107
	 * @param con_addr
	 * @param light_addr
	 * @param scene_nub
	 * @return
	 */
	public byte[] delTerScene(String con_addr,String light_addr,byte scene_nub)
	{
		IoBuffer buffer = IoBuffer.allocate(11+9);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2107).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr))
		.put(scene_nub);
		byte[] data = buffer.flip().array();
		return sendDataPake(data);
	}
	
	/**
	 * 快速查询终端设备状态量信息 2201
	 * @param con_addr
	 * @param light_addr
	 * @param data   0x00  - 0x11
	 * @return
	 */
	public byte[] getState(String con_addr,String light_addr,byte[] data)
	{
		IoBuffer buffer = IoBuffer.allocate(11+8+data.length);
		buffer.put(com.justin.common.Common.stringToAddr(con_addr))
		.putShort((short)0x2201).put((byte)0xff).put(com.justin.common.Common.stringToAddr(light_addr))
		.put(data);
		return buffer.flip().array();
	}
	
	
	
	
	
	
	
	//对发送的数据进行校验和转义，添加头和尾
	public byte[] sendDataPake(byte[] data)
	{
		short crc = getcrc16(data,data.length);
		IoBuffer buffer = IoBuffer.allocate(data.length + 4);
		buffer.put((byte)0x02).put(data).putShort(crc).put((byte)0x03);
		buffer.flip();
		byte[] result = Sturn(buffer.array());
		return result;
	}
	
	/*
	//广播亮度
	public byte[] setAll(byte liangdu)
	{
		byte[] data = new byte[4];
		data[0] = (byte)0xff;
		data[1] = (byte)0xff;
		data[2] = 0x30;
		data[3] = liangdu;
		return sendDataPake(data);
	}
	
	//组亮度
	public byte[] setGroup(byte group,byte liangdu)
	{
		byte[] data = new byte[4];
		data[0] = 0xf;
		data[1] = group;
		data[2] = 0x30;
		data[3] = liangdu;
		return sendDataPake(data);
	}
	
	//单个灯的亮度
	public byte[] setLight(byte num,byte liangdu)
	{
		byte[] data = new byte[4];
		data[0] = 0x0;
		data[1] = num;
		data[2] = 0x30;
		data[3] = liangdu;
		return sendDataPake(data);
	}
	
	//单个灯的状态
	public byte[] getLightState(byte num)
	{
		byte[] data = new byte[4];
		data[0] = 0x0;
		data[1] = num;
		data[2] = 0x31; 
		return sendDataPake(data);
	}
	*/

	//转义算法接收(与DTU通信),头 02   尾 03
	public static byte[] Rturn(byte[] barray)
	{
		ByteBuffer buf = ByteBuffer.allocate(barray.length);
		boolean find_head = false;
		boolean find_tail = false;
		
		for(int i = 0;i < barray.length;i++)
		{
			if(barray[i] == 0x02 && find_head == false)
			{
				find_head = true;
				buf.put((byte)0x02);
			}
			else if(barray[i] == 0x1B )
			{
				buf.put((byte)(barray[i]+barray[i+1]));
				i++;
			}
			
			else if(barray[i] == 0x03 && find_tail == false)
			{
				find_tail = true;
				buf.put((byte)0x03);
				break;
			}
			else 
			{
				buf.put(barray[i]);
			}
			
		}
		int remain = buf.remaining();
		byte[] data = buf.array();
		byte []result_data = new byte[data.length-remain];
		System.arraycopy(data, 0, result_data, 0, result_data.length);
		return result_data;
		
	}
	
	
	//转义算法发送
	public static byte[] Sturn(byte[] barray)
	{	
		int len = barray.length;
		boolean find_head = false;
		boolean find_tail = false;
		if(barray[0] != 0x02 || barray[len-1] != 0x03)
		{
			return null;
		}
		
		else
		{
			ByteBuffer buffer = ByteBuffer.allocate(len*2);
			for(int i = 0;i<len;i++)
			{
				if(find_head == false && barray[i] == 0x02)
				{
					buffer.put((byte)0x02);
					find_head = true;
				}
				else if(find_tail == false && barray[i] == 0x03 && i == len-1)
				{
					buffer.put((byte)0x03);
					find_tail = true;
				}
				else if(barray[i] == 0x02 )
				{
					buffer.putShort((short)0x1BE7);
					//buffer.put((byte)0xE7);
				}
				else if(barray[i] == 0x03)
				{
					buffer.putShort((short)0x1BE8);
					//buffer.put((byte)0xE8);
				}
				else if(barray[i] == 0x1B)
				{
					buffer.putShort((short)0x1B00);
					//buffer.put((byte)0xE8);
				}
				
				else
				{
					buffer.put(barray[i]);
				}
			}
			byte[] data =buffer.array();
			int length = len*2 - buffer.remaining();
			byte[] result  = new byte[length];
			System.arraycopy(data, 0, result, 0, length);
			return result;
			
		}
		
	}
	
	public static short getcrc16(byte[] buffer, int buffer_length)
	{
		short crc = 0;
		int i;

		for (i = 0; i < buffer_length; i++)
			crc = (short)(crc_table[((crc >> 8) ^ buffer[i]) & 0xFF] ^ (crc << 8));

		return crc;
		
	}
	
	public static int getNonShort(short sign)
	{
		if(sign<0)
		{
			return sign+65536;
		}
		else{
			return sign;
		}
	}
	
	static short crc_table[] =
		{
			0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50A5, 0x60C6, 0x70E7,
			(short) 0x8108, (short) 0x9129, (short) 0xA14A, (short) 0xB16B, (short) 0xC18C, (short) 0xD1AD, (short) 0xE1CE, (short) 0xF1EF,
			0x1231, 0x0210, 0x3273, 0x2252, 0x52B5, 0x4294, 0x72F7, 0x62D6,
			(short) 0x9339, (short) 0x8318, (short) 0xB37B, (short) 0xA35A, (short) 0xD3BD, (short) 0xC39C, (short) 0xF3FF, (short) 0xE3DE,
			0x2462, 0x3443, 0x0420, 0x1401, 0x64E6, 0x74C7, 0x44A4, 0x5485,
			(short) 0xA56A, (short) 0xB54B, (short) 0x8528, (short) 0x9509, (short) 0xE5EE, (short) 0xF5CF, (short) 0xC5AC, (short) 0xD58D,
			0x3653, 0x2672, 0x1611, 0x0630, 0x76D7, 0x66F6, 0x5695, 0x46B4,
			(short) 0xB75B, (short) 0xA77A, (short) 0x9719, (short) 0x8738, (short) 0xF7DF, (short) 0xE7FE, (short) 0xD79D, (short) 0xC7BC,
			0x48C4, 0x58E5, 0x6886, 0x78A7, 0x0840, 0x1861, 0x2802, 0x3823,
			(short) 0xC9CC, (short) 0xD9ED, (short) 0xE98E, (short) 0xF9AF, (short) 0x8948, (short) 0x9969, (short) 0xA90A, (short) 0xB92B,
			0x5AF5, 0x4AD4, 0x7AB7, 0x6A96, 0x1A71, 0x0A50, 0x3A33, 0x2A12,
			(short) 0xDBFD, (short) 0xCBDC, (short) 0xFBBF, (short) 0xEB9E, (short) 0x9B79, (short) 0x8B58, (short) 0xBB3B, (short) 0xAB1A,
			0x6CA6, 0x7C87, 0x4CE4, 0x5CC5, 0x2C22, 0x3C03, 0x0C60, 0x1C41,
			(short) 0xEDAE, (short) 0xFD8F, (short) 0xCDEC, (short) 0xDDCD, (short) 0xAD2A, (short) 0xBD0B, (short) 0x8D68, (short) 0x9D49,
			0x7E97, 0x6EB6, 0x5ED5, 0x4EF4, 0x3E13, 0x2E32, 0x1E51, 0x0E70,
			(short) 0xFF9F, (short) 0xEFBE, (short) 0xDFDD, (short) 0xCFFC, (short) 0xBF1B, (short) 0xAF3A, (short) 0x9F59, (short) 0x8F78,
			(short) 0x9188, (short) 0x81A9, (short) 0xB1CA, (short) 0xA1EB, (short) 0xD10C, (short) 0xC12D, (short) 0xF14E, (short) 0xE16F,
			0x1080, 0x00A1, 0x30C2, 0x20E3, 0x5004, 0x4025, 0x7046, 0x6067,
			(short) 0x83B9, (short) 0x9398, (short) 0xA3FB, (short) 0xB3DA, (short) 0xC33D, (short) 0xD31C, (short) 0xE37F, (short) 0xF35E,
			0x02B1, 0x1290, 0x22F3, 0x32D2, 0x4235, 0x5214, 0x6277, 0x7256,
			(short) 0xB5EA, (short) 0xA5CB, (short) 0x95A8, (short) 0x8589, (short) 0xF56E, (short) 0xE54F, (short) 0xD52C, (short) 0xC50D,
			0x34E2, 0x24C3, 0x14A0, 0x0481, 0x7466, 0x6447, 0x5424, 0x4405,
			(short) 0xA7DB, (short) 0xB7FA, (short) 0x8799, (short) 0x97B8, (short) 0xE75F, (short) 0xF77E, (short) 0xC71D, (short) 0xD73C,
			0x26D3, 0x36F2, 0x0691, 0x16B0, 0x6657, 0x7676, 0x4615, 0x5634,
			(short) 0xD94C, (short) 0xC96D, (short) 0xF90E, (short) 0xE92F, (short) 0x99C8, (short) 0x89E9, (short) 0xB98A, (short) 0xA9AB,
			0x5844, 0x4865, 0x7806, 0x6827, 0x18C0, 0x08E1, 0x3882, 0x28A3,
			(short) 0xCB7D, (short) 0xDB5C, (short) 0xEB3F, (short) 0xFB1E, (short) 0x8BF9, (short) 0x9BD8, (short) 0xABBB, (short) 0xBB9A,
			0x4A75, 0x5A54, 0x6A37, 0x7A16, 0x0AF1, 0x1AD0, 0x2AB3, 0x3A92,
			(short) 0xFD2E, (short) 0xED0F, (short) 0xDD6C, (short) 0xCD4D, (short) 0xBDAA, (short) 0xAD8B, (short) 0x9DE8, (short) 0x8DC9,
			0x7C26, 0x6C07, 0x5C64, 0x4C45, 0x3CA2, 0x2C83, 0x1CE0, 0x0CC1,
			(short) 0xEF1F, (short) 0xFF3E, (short) 0xCF5D, (short) 0xDF7C, (short) 0xAF9B, (short) 0xBFBA, (short) 0x8FD9, (short) 0x9FF8,
			0x6E17, 0x7E36, 0x4E55, 0x5E74, 0x2E93, 0x3EB2, 0x0ED1, 0x1EF0
		};
}
