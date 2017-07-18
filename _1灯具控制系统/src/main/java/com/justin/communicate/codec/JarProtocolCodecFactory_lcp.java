package com.justin.communicate.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;



public class JarProtocolCodecFactory_lcp extends DemuxingProtocolCodecFactory{
	public JarProtocolCodecFactory_lcp(){
		addMessageDecoder(JarDecoder_lcp.class);
		addMessageEncoder(IoBuffer.class, JarEncoder_lcp.class);
	}
}
