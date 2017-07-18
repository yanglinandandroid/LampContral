package com.justin.communicate.codec;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;



public class JarEncoder_lcp implements MessageEncoder{
private static final Set<Class<?>> TYPES;
    
    static
    {
        Set<Class<?>> types = new HashSet<Class<?>>();
        types.add( IoBuffer.class );
        TYPES = Collections.unmodifiableSet( types );
    }
	public Set<Class<?>> getMessageTypes() {
		return JarEncoder_lcp.TYPES;
	}

    public JarEncoder_lcp()
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
