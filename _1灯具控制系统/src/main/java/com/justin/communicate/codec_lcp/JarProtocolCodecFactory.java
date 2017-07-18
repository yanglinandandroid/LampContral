package com.justin.communicate.codec_lcp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;



public class JarProtocolCodecFactory extends DemuxingProtocolCodecFactory{
	public JarProtocolCodecFactory(){
		addMessageDecoder(JarDecoder.class);
		addMessageEncoder(IoBuffer.class, JarEncoder.class);
	}
}
