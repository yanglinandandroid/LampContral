package com.justin.communicate.server;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;


/**
 * LCP_SH_D   协议    
 *
 * 只需要添加    灯     控制器     组相关协议即可
 * 主要功能：    调光
 *
 * @author justin
 *
 */

public class Data_LCP_SH_D {

	/**
	 * 心跳
	 * @return
	 */
	public byte[] heartData()
	{
		byte[] data = new byte[7];
		data[0] = 0x00;
		data[1] = 0x0a;
		data[2] = (byte)0xff;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x00;
		data[6] = 0x00;
		return data;
	}

	/**
	 * 重启集中控制器
	 * @return
	 */
	public byte[] reBoortController()
	{
		byte[] data = new byte[7];
		data[0] = 0x00;
		data[1] = 0x0b;
		data[2] = (byte)0xff;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x00;
		data[6] = 0x00;
		return data;
	}

	/**
	 * 集中控制器时间同步
	 * @return
	 */
	public byte[] setControllerTime()
	{
		ByteBuffer buffer = ByteBuffer.allocate(15);
		buffer.put((byte) 0x00).put((byte) 0x0c).put((byte) 0xff).put((byte) 0x00).put((byte) 0x00).put((byte) 0x00).put((byte) 8);
		//获取当前时间
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		buffer.putShort((short)date.getYear()).put((byte)date.getMonth()).put((byte)date.getDate());
		buffer.put((byte)date.getDay()).put((byte)date.getHours()).put((byte)date.getMinutes()).put((byte)date.getSeconds());
		return (byte[]) buffer.flip().array();
	}

	/**
	 * 终端设备调光
	 * @param con_addr
	 * @param light_addr ,地址可以是广播地址，可以是组地址，或者单灯地址
	 * @param brightness
	 * @return
	 */
	public byte[] setBrightness(String con_addr,String[] light_addr,byte brightness)
	{
		int len = light_addr.length;
		ByteBuffer buffer = ByteBuffer.allocate(7 + 9*len);
		buffer.put((byte)0x10);
		buffer.put((byte)0x02);
		buffer.put((byte)0xff);
		int datalen = 9*len;
		buffer.putInt(datalen);
		for(int i = 0;i<len;i++)
		{
			buffer.put(com.justin.common.Common.stringToAddr(light_addr[i]));
			buffer.put(brightness);
		}

		byte[]re  =  (byte[]) buffer.flip().array();
		re = packageData(re, con_addr);
		return re;
	}

	/**
	 * 心跳
	 * @param con_addr
	 * @return
	 */
	public byte[] sendHeart(String con_addr)
	{

		ByteBuffer buffer = ByteBuffer.allocate(7);
		buffer.put((byte)0x00);
		buffer.put((byte)0x0A);
		buffer.put((byte)0xff);
		int datalen = 0;
		buffer.putInt(datalen);


		byte[]re  =  (byte[]) buffer.flip().array();
		re = packageData(re, con_addr);
		return re;
	}



	/**
	 * 打包数据
	 * @param old_data
	 * @param addr
	 * @return
	 */
	private byte[] packageData(byte[] old_data,String addr)
	{
		ByteBuffer buffer = ByteBuffer.allocate(old_data.length + 9);
		buffer.put((byte)0xaa);
		byte[] addr_byte = com.justin.common.Common.stringToAddr(addr);
		buffer.put(addr_byte);
		buffer.put(old_data);
		byte[] cur_data = (byte[]) buffer.flip().array();
		short crc = getcrc16(cur_data, cur_data.length);
		ByteBuffer buffer_r = ByteBuffer.allocate(cur_data.length + 3);
		buffer_r.put(cur_data).putShort(crc).put((byte)0x55);
		return (byte[]) buffer_r.flip().array();
	}

	/**
	 * 获得crc校验码
	 * @param buffer
	 * @param buffer_length
	 * @return
	 */
	private static short getcrc16(byte[] buffer, int buffer_length)
	{
		short crc = 0;
		int i;

		for (i = 0; i < buffer_length; i++)
			crc = (short)(crc_table[((crc >> 8) ^ buffer[i]) & 0xFF] ^ (crc << 8));

		return crc;

	}

	/**
	 * crc数据
	 */
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
