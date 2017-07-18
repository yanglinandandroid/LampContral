package com.justin.communicate.server;

import com.example.app3.TreeActivity;
import com.justin.type.Control;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.net.SocketAddress;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//import org.omg.CORBA.DATA_CONVERSION;

public class ServerHandler extends IoHandlerAdapter {
	
	private int heart_beat_time = 30;
	public static int reconnect_time = 0;
	ServerHandler handler = null;
	private ScheduledExecutorService timedexec = Executors
			.newSingleThreadScheduledExecutor();
	public static IoSession iosession;
	public static IoConnector connector;

	/* ������ʱ�� */
	private static int idle_time = 5;// �����ļ�
	/* ����Ƿ�������������? �� */
	private int detection_time = 5;
	private SocketAddress target;
	
	public  static long rec_time = 0;
	
	public ServerHandler() {
		this.handler = this;

		
		
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		TreeActivity.addInfo("�յ����~~");
		rec_time = System.currentTimeMillis(); 
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
	
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		rec_time = System.currentTimeMillis() /1000; 
		//���һ��������?
		
		this.iosession = session ;
		
		// ����������ݣ�?10�뷢��һ��
		new Thread(
				new Runnable() {
					public void run() {
						
						while(true)
						{
							//���һ��������?
							if(TreeActivity.control_map.size() >0) 
							{
								Control c = null ;
								Iterator it = TreeActivity.control_map.keySet().iterator();
								while(it.hasNext())
								{
									c = (Control) TreeActivity.control_map.get(it.next());
								}
								
								//�������?
								Data_LCP_SH_D data = new Data_LCP_SH_D();
								byte[]data2 = data.sendHeart(c.getAddr());
								IoBuffer buffer = IoBuffer.allocate(data2.length);
								buffer.put(data2);
								if(ServerHandler.iosession.isConnected() && ServerHandler.iosession != null)
								ServerHandler.iosession.write(buffer.flip());
								//TreeActivity.addInfo("��������~");
							}
							
							
							//�ж϶���,����10��
							long time = System.currentTimeMillis()/1000 - ServerHandler.rec_time;
							if(time > 11) 
							{
								
								ServerHandler.reconnect_time ++;                  
								TreeActivity.isonline = false;
								
								//��������
								/*
								try {
								ServerHandler.iosession.close();
								ServerHandler.iosession = null;
								connector = null;
								System.gc();
								connector = new NioSocketConnector();
								connector.setConnectTimeoutMillis(30000);
								if (TreeActivity.protocol == 1)
									connector.getFilterChain().addLast(
											"codec",
											new ProtocolCodecFilter(
													new JarProtocolCodecFactory_lcp()));
								else if (TreeActivity.protocol == 0)
									connector.getFilterChain().addLast("codec",
											new ProtocolCodecFilter(new JarProtocolCodecFactory()));
								ServerHandler handler = new ServerHandler();
								SocketAddress address = new InetSocketAddress(TreeActivity.ip, TreeActivity.port);
								
								connector.setHandler(handler);
								connector.connect(address);
								}
								catch(Exception e)
								{
									
								}
								*/
								
							}
							
							try {
								Thread.sleep(6*1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
				).start()
			;

		
	}
	
	//���͹㲥

	public void sendBroadcast(byte liangdu)
	{
		Data data = new Data();
		//byte[] datas = data.setAll(liangdu);
		
	}
	
}
