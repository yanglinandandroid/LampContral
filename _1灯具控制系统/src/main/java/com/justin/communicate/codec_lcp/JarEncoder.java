package com.justin.communicate.codec_lcp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 十六进制协议编码
 *
 * */

public class JarEncoder implements MessageEncoder{
private static final Set<Class<?>> TYPES;
    
    static
    {
        Set<Class<?>> types = new HashSet<Class<?>>();
        types.add( IoBuffer.class );
        TYPES = Collections.unmodifiableSet( types );
    }
	public Set<Class<?>> getMessageTypes() {
		return JarEncoder.TYPES;
	}

    public JarEncoder()
    {
    }

    public void encode( IoSession session, Object message, ProtocolEncoderOutput out ) throws Exception
    {
    	
        IoBuffer m = (IoBuffer) message ;
        if (m==null) {
        	return;
        }
       
        out.write( m);
    }

}
