package com.justin.communicate.codec_lcp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;



/**
 * 十六进制协议解码
 *
 * */

public class JarDecoder implements MessageDecoder {
	public JarDecoder(){
		
	}
	public MessageDecoderResult decodable(IoSession session ,IoBuffer in){
		
		
		//byte[]data =  in.array();
	//	byte[] data = in.array();
	//	System.out.println("数据:"+com.syq.zhuanfa.common.byteToMinaString.byteToMinaString(data));
	//	Main.d_len += in.remaining();
		if (in.remaining()<4) {
			return MessageDecoderResult.NEED_DATA;
		}
		in.mark();
	
		int end =0;
		int i=0;
		boolean isok=true;
		int len=in.remaining();
		while(i<len&&isok)
			{
				end=in.get();
				if(end==0x55)
				{
					isok=false;
				}
				i++;
				
			}
		in.reset();
		if(isok)
			
		return MessageDecoderResult.NEED_DATA;
		else
			
		return MessageDecoderResult.OK;	
		
	}
	public static int byte2int(byte[] array, int len) {
		if (len == 1) {
			return getNonSign(array[0]);
		} else if (len == 2) {
			return (getNonSign(array[1]) + getNonSign(array[0]) * 256);
		} else if (len == 3) {
			return (getNonSign(array[2]) + getNonSign(array[1]) * 256 + getNonSign(array[0]) * 256 * 256);
		} else if (len == 4) {
			return getNonSign(array[3])
					+ (getNonSign(array[2]) * 256 + getNonSign(array[1]) * 256 * 256 + getNonSign(array[0]) * 256 * 256 * 256);
		} else {
			return 0;
		}
	}
	public static int getNonSign(byte Sign) {
		if (Sign < 0) {
			return (Sign + 256);
		} else {
			return Sign;
		}
	}
	public MessageDecoderResult decode(IoSession session,IoBuffer in,ProtocolDecoderOutput out)throws Exception{
		in.mark();
		int len=0;
		while (true) {
			if(in.remaining() ==0)
			{break;}
			else{
				byte ptr = in.get();
				len++;
				if(ptr == 0x55)
				{
					break;
				}
				
			}

			}
		in.reset();
		byte[] cur = new byte[len];
		in.get(cur);
		IoBuffer bf = IoBuffer.wrap(cur);
		out.write(bf);
			return MessageDecoderResult.OK;
		
	}
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {

	}

}
