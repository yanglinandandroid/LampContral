package com.example.app3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.justin.communicate.codec.JarProtocolCodecFactory_lcp;
import com.justin.communicate.codec_lcp.JarProtocolCodecFactory;
import com.justin.communicate.server.Data;
import com.justin.communicate.server.Data_LCP_SH_D;
import com.justin.communicate.server.ServerHandler;
import com.justin.db.DBAdapter;
import com.justin.type.Control;
import com.justin.type.Group;
import com.justin.type.Light;
import com.justin.type.Node;
import com.justin.type.Scene;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ShowActivity��������ʾ������ʾ��������ƣ����ṩ��������İ�ť��
 * @author justin
 * 
 */
public class TreeActivity extends Activity implements OnItemClickListener {

	private static int liangdu;
	// ��������Ϣ�����Ϣ
	public static Map<String, Set<Light>> group_map = new ConcurrentHashMap<String, Set<Light>>();

	public static Map<String, Light> light_map = new ConcurrentHashMap<String, Light>();
	public static Map<String, Control> control_map = new ConcurrentHashMap<String, Control>();
	// ����
	public static List<Scene> scene_list = new ArrayList<Scene>();

	public static TreeAdapter ta;
	   public int index = 1;
	ListView code_list;

	public static TextView view; // ���ȵĴ�С
	public static ImageView image;
	private static TextView view_info;// ��Ϣ��ʾ��
	private static Spinner spinner_scene;
	private static TextView text_scene;
	private SoundPool sp;// ����һ��SoundPool
	private int music;// ����һ��������load������������suondID
	private RadioGroup radioGroup = null;
	public static int protocol;
	public static String ip = "202.11.4.70";
	public static int port = 3434;
	
	public static boolean isonline = true;
	public static ImageView image_online;
	private Thread mThread;
	private boolean show = true;
	
	//private Handler mThread = null;
	
	
	final private Handler mHandler = new Handler(){
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	        case 0 :
	           // timeLable.setText("timeCount=" + timeCount + " ��");
	        	if(show == true)
	        	{
	        		//image_online.setVisibility(View.INVISIBLE);
	        		image_online.setImageDrawable(getResources().getDrawable(R.drawable.online3));
	        	}
	        	//image_online.setBackgroundResource(R.drawable.online3);
	        	else 
	        	{
	        		//image_online.setBackgroundResource(R.drawable.online3);
	        		//image_online.setVisibility(View.VISIBLE);
	        		if(isonline == true)
	        		image_online.setImageDrawable(getResources().getDrawable(R.drawable.online));
	        		else
	        			image_online.setImageDrawable(getResources().getDrawable(R.drawable.online2));
	        	}
	        	show = show==true?false:true;
	            break;
	        default :
	            break;
	        }
	    }
	};
	

	/**
	 * �����Ϣ
	 * 
	 * @param info
	 */
	public static void addInfo(String info) {

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		String hehe = dateFormat.format(now);
		String str = "\t[" + hehe + "]  ";
		info = str + info;
		info = "\t" + info;
		String old_text = TreeActivity.view_info.getText().toString();
		String new_text = old_text + "\n" + info;

		String arg[] = new_text.split("\n");
		if (arg.length > 11) {
			TreeActivity.view_info.setText(info);
		} else {
			TreeActivity.view_info.setText(new_text);
		}
	}

	public int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * ��������
	 */
	public void update() {

		if (group_map != null)
			group_map.clear();

		if (scene_list != null) {
			scene_list.clear();
		}

		readDB();

		setNode();

		//setScene();
	}

	/**
	 * ��ӳ�����spinner
	 */
	public void setScene() {
		if (this.scene_list.size() == 0) {
			spinner_scene.setVisibility(View.INVISIBLE);
			text_scene.setText("Please add scene first!");
		} else {
			spinner_scene.setVisibility(View.VISIBLE);
			text_scene.setText("Scene");
		}
		String[] g = new String[scene_list.size()];
		for (int i = 0; i < this.scene_list.size(); i++) {
			g[i] = scene_list.get(i).getName();
		}
		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, g);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_scene.setAdapter(adapter);
		spinner_scene.setVisibility(View.VISIBLE);
	}

	/*
	 * 8��ʼ�����ݿ��е�����
	 */
	private void readDB() {

		// �����
		DBAdapter db = new DBAdapter(TreeActivity.this);
		db.open();
		
		
		Group[] groups = db.getAllGroup();
		if (groups != null) {
			String[] g = new String[groups.length];
			if (this.group_map == null) {
				this.group_map = new ConcurrentHashMap<String, Set<Light>>();
			}
			for (int i = 0; i < groups.length; i++) {
				g[i] = groups[i].getName();
				Set<Light> set = new HashSet<Light>();
				this.group_map.put(g[i], set);
			}
		} else {
			this.group_map = null;
		}

		// ��ӵ�
		Light[] lights = db.getAllLight();
		if (lights != null && this.group_map != null) {
			String[] ids = new String[lights.length];
			for (int i = 0; i < lights.length; i++) {
				this.light_map.put(lights[i].getName(), lights[i]);
				ids[i] = lights[i].getName();
				if (this.group_map.containsKey(lights[i].getGroupName())) {
					this.group_map.get(lights[i].getGroupName()).add(lights[i]);
				}
			}
		} else {
			// list = null;
		}

		this.scene_list.clear();
		Scene[] scenes = db.getAllScene();
		if (scenes != null) {
			for (int i = 0; i < scenes.length; i++) {
				this.scene_list.add(scenes[i]);
			}
		}

		// ��ȡ������
		Control[] controls = db.getAllControl();
		if (controls != null) {
			for (Control c : controls) {
				this.control_map.put(c.getName(), c);
			}
		}
		db.close();
		this.addInfo("���ݿ����ݼ������!");

	}

	/**
	 * ����wifi������ protocol : 0 :lcpdsh 1 : lcp300
	 */
	public void connectWifi(int protocol) {
		TreeActivity.addInfo("����wifi������~");
		try {
			IoConnector connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(30000);
			if (protocol == 1)
				connector.getFilterChain().addLast(
						"codec",
						new ProtocolCodecFilter(
								new JarProtocolCodecFactory_lcp()));
			else if (protocol == 0)
				connector.getFilterChain().addLast("codec",
						new ProtocolCodecFilter(new JarProtocolCodecFactory()));
			ServerHandler handler = new ServerHandler();
			SocketAddress address = new InetSocketAddress(this.ip, this.port);
			
			connector.setHandler(handler);
			connector.connect(address);
			return;
		} catch (Exception e) {
			this.addInfo("����" + e);
			return;
		}
	}

	/**
	 * �������ӣ�Э��仯������
	 */
	public void reconnectWifi(int protocol) {

		if (protocol == this.protocol)
			return;
		try {
			ServerHandler.iosession.close();
			ServerHandler.iosession = null;

			TreeActivity.addInfo("����wifi������~");
			IoConnector connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(30000);
			if (protocol == 1)
				connector.getFilterChain().addLast(
						"codec",
						new ProtocolCodecFilter(
								new JarProtocolCodecFactory_lcp()));
			else if (protocol == 0)
				connector.getFilterChain().addLast("codec",
						new ProtocolCodecFilter(new JarProtocolCodecFactory()));
			ServerHandler handler = new ServerHandler();
			SocketAddress address = new InetSocketAddress(this.ip, this.port);
			connector.setHandler(handler);
			connector.connect(address);
			return;
		} catch (Exception e) {
			this.addInfo("����" + e);
			return;
		}
	}

	/**
	 * ��ȡip��ַ
	 * 
	 * @throws SocketException
	 */
	public String getLocalIpAddress() {
		// ��ȡwifi����
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getActiveNetworkInfo();
		if (mWifi != null) {
			TreeActivity.addInfo("wifi+�ɹ�");
		} else
			TreeActivity.addInfo("wifi+ʧ��");

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						TreeActivity
								.addInfo("��ȡIP��ַ�ɹ�,IP��ַΪ:["
										+ inetAddress.getHostAddress()
												.toString() + "]");
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			TreeActivity.addInfo("��ȡIP��ַ�쳣" + ex.toString());

		}
		return null;
	}

	private int getProtocol() {
		// Э�� , 0 : lcpshd 1: lcp300
		if (radioGroup.getCheckedRadioButtonId() == R.id.radio_lcpshd) {
			return 0;
		} else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_lcp300) {
			return 1;
		} else
			return 0;
	}

	/*
	 * ��ȡip �˿�
	 */
	public void inputIp() {
		LayoutInflater myinflater = LayoutInflater.from(TreeActivity.this);
		final View view = myinflater.inflate(R.layout.activity_ip, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("IP");
		builder.setView(view);
		
		//Db
		DBAdapter db = new DBAdapter(TreeActivity.this);
		db.open();
		String ip_str = db.getIP();
		String port_str = db.getPort();
		EditText str_ip = (EditText) view.findViewById(R.id.ip_ip);
		EditText str_port = (EditText) view.findViewById(R.id.ip_port);
		str_ip.setText(ip_str);
		str_port.setText(port_str);
		
		db.close();
		
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				// ���
				EditText str_ip = (EditText) view.findViewById(R.id.ip_ip);
				EditText str_port = (EditText) view.findViewById(R.id.ip_port);
				String ip_1 = str_ip.getText().toString();
				String port_1 = str_port.getText().toString();
				ip = ip_1;
				port = Integer.parseInt(port_1);
				//db
				DBAdapter db = new DBAdapter(TreeActivity.this);
				db.open();
				db.update(ip_1, port_1);
				db.close();
				
				
			}
		});
		builder.setNegativeButton("Cancel", null);
		builder.create();
		builder.show();

	}
	
	/**
	 * �߳� 
	 */
	public void startThread()
	{
		//���һ��������
		
		mThread = new Thread(new Runnable() {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                    Thread.sleep(2000);
	                    //timeCount++;
	                    mHandler.sendEmptyMessage(0);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    });
	    mThread.start();
	}
	

	/**
	 * �ؼ�����
	 */
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		MainActivity.tree = this;

		setContentView(R.layout.activity_light_tree);

		this.view_info = (TextView) findViewById(R.id.textView_info);

		code_list = (ListView) findViewById(R.id.code_list);
		code_list.setOnItemClickListener(this);

		this.spinner_scene = (Spinner) findViewById(R.id.spinner_scene);

		text_scene = (TextView) findViewById(R.id.TextView_scene);
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);// ��һ������Ϊͬʱ���������������������ڶ����������ͣ�����Ϊ��������
		music = sp.load(this, R.raw.click_01, 1); // ����������زķŵ�res/raw���2��������Ϊ��Դ�ļ�����3��Ϊ���ֵ����ȼ�

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup1);
		
		TreeActivity.isonline = true;
		image_online = (ImageView)findViewById(R.id.isonline);

		
		// ip ����
		inputIp();

		// ��ȡ���ݿ�
		readDB();

		// ��ӽڵ�
		setNode();
		
		startThread();
		

		// ��ӳ���,��һ���汾���
		// setScene();

		this.protocol = getProtocol();
		// ���ӷ����� ,�˴����ӷ�����
		connectWifi(this.protocol);

		getLocalIpAddress();

		// view = (TextView) findViewById(R.id.textView_2);

		// image = (ImageView)findViewById(R.id.imageViewLight);
		// ��Ӧ��ȡ״̬
		Button button_state = (Button) findViewById(R.id.button_state);
		button_state.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				// �����Լ��Ĵ���......
				TreeActivity.addInfo("��ȡ״̬!");
				List<Node> nodes = TreeActivity.ta.getSeletedNodes();

				// �жϳ����飬���ǵ����ƣ����ǹ㲥
				// �����ж��Ƿ�Ϊ�㲥

				if (com.justin.common.Common.getGroup(nodes).size() != 0) {
					Set<String> set = com.justin.common.Common.getGroup(nodes);// com.justin.common.Common.getGroup(nodes);
					Iterator it = set.iterator();
					while (it.hasNext()) {
						int group = (Integer) it.next();
						Data data = new Data();
						for (Control c : control_map.values()) {

							TreeActivity.addInfo("������:[" + c.getAddr()
									+ "],�ն��豸��Ϣ��ѯ");
							byte[] datas = data.getTerminalInfo(c.getAddr(),
									(short) 0);// .setLightBrightness(c.getAddr(),
												// com.justin.common.Common.getGroup((byte)group),
												// (byte)liangdu);
							IoBuffer buffer = IoBuffer.allocate(datas.length);
							buffer.put(datas);
							TreeActivity.addInfo("����:["
									+ com.justin.common.Common
											.bytes2HexString(datas) + "]");
							ServerHandler.iosession.write(buffer.flip());
						}

					}

					return;
				}
				// Ϊ��
				else if (com.justin.common.Common.getLight(nodes).size() != 0) {

					Set<String> set = com.justin.common.Common.getLight(nodes);
					Iterator it = set.iterator();
					while (it.hasNext()) {
						String name = (String) it.next();
						Light light = new Light();
						for (Light l : light_map.values()) {
							if (l.getName().equals(name))
								light = l;
						}
						TreeActivity.addInfo("��:[" + light.getName()
								+ "],��ѯ״̬!");
						Control control = new Control();
						for (Control c : control_map.values()) {
							if (c.getName().equals(light.getControlName())) {
								control = c;
							}
						}
						Data data = new Data();
						byte[] datas = data.getTerminalInfo(control.getAddr(),
								(short) 0);// .setLightBrightness(control.getAddr(),light.getAddr()
											// ,(byte)liangdu);
						IoBuffer buffer = IoBuffer.allocate(datas.length);
						buffer.put(datas);
						TreeActivity.addInfo("����:["
								+ com.justin.common.Common
										.bytes2HexString(datas) + "]");
						ServerHandler.iosession.write(buffer.flip());
					}

					return;
				}

			}
		});

		final TextView text = (TextView) findViewById(R.id.light_num);
		SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			/**
			 * ��ָ�뿪
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				// protocol,�������ƣ�Э��ı�������
				reconnectWifi(getProtocol());
				TreeActivity.protocol = getProtocol();

				// ������Ч
				sp.play(music, (float) 0.3, (float) 0.3, 0, 0, 1);
				// TreeActivity.addInfo("��������,����:"+liangdu+"!");

				/**
				 * ��������� ����ÿ���������������ݣ��㲥��ʱ��Ҳ�Ƕ����еĿ��������͹㲥�����
				 */
				// ͨ��
				List<Node> nodes = TreeActivity.ta.getSeletedNodes();
				// �жϳ����飬���ǵ����ƣ����ǹ㲥
				// �����ж��Ƿ�Ϊ�㲥

				if (com.justin.common.Common.isBroadCast(nodes)) {
					TreeActivity.addInfo("�㲥!����=" + liangdu);
					Data_LCP_SH_D data_2 = new Data_LCP_SH_D();
					Data data = new Data();
					for (Control c : control_map.values()) {
						byte[] datas = {};
						if (getProtocol() == 1)
							datas = data.setLightBrightness(c.getAddr(),
									"FFFFFFFFFFFFFFFF", (byte) liangdu);
						else if (getProtocol() == 0)
							datas = data_2.setBrightness(c.getAddr(),
									new String[] { "FFFFFFFFFFFFFFFF" },
									(byte) liangdu);
						IoBuffer buffer = IoBuffer.allocate(datas.length);
						buffer.put(datas);
						TreeActivity.addInfo("����:["
								+ com.justin.common.Common
										.bytes2HexString(datas) + "]");
						ServerHandler.iosession.write(buffer.flip());
					}
					return;
				}
				// �Ƿ�Ϊ��

				else if (com.justin.common.Common.getGroup(nodes).size() != 0) {
					Set<String> set = com.justin.common.Common.getGroup(nodes);
					Iterator it = set.iterator();

					String[] g_lcp = new String[set.size()];
					int index = 0;
					while (it.hasNext()) {
						if (getProtocol() == 0) {

							String group_name = (String) it.next();
							int group = 0;
							DBAdapter db = new DBAdapter(TreeActivity.this);
							db.open();
							Group[] gs = db.getAllGroup();
							for (int i = 0; i < gs.length; i++) {
								if (gs[i].getName().equals(group_name)) {
									group = Integer.parseInt(gs[i].getID());
								}
							}
							// add
							g_lcp[index++] = com.justin.common.Common
									.getGroup_lcpshd((byte) group);

							continue;
						}
						if (getProtocol() == 1) {
							// lc300
							String group_name = (String) it.next();
							int group = 0;
							DBAdapter db = new DBAdapter(TreeActivity.this);
							db.open();
							Group[] gs = db.getAllGroup();
							for (int i = 0; i < gs.length; i++) {
								if (gs[i].getName().equals(group_name)) {
									group = Integer.parseInt(gs[i].getID());
								}
							}
							TreeActivity.addInfo("��=" + group + ",����="
									+ liangdu);
							Data data = new Data();
							for (Control c : control_map.values()) {
								byte[] datas = data.setLightBrightness(c
										.getAddr(), com.justin.common.Common
										.getGroup((byte) group), (byte) liangdu);
								IoBuffer buffer = IoBuffer
										.allocate(datas.length);
								buffer.put(datas);
								TreeActivity.addInfo("����:["
										+ com.justin.common.Common
												.bytes2HexString(datas) + "]");
								ServerHandler.iosession.write(buffer.flip());
							}
						}

					}

					// lcpshd ͨ��
					if (getProtocol() == 0) {
						Data_LCP_SH_D data_2 = new Data_LCP_SH_D();
						for (Control c : control_map.values()) {
							byte[] datas = data_2.setBrightness(c.getAddr(),
									g_lcp, (byte) liangdu);
							IoBuffer buffer = IoBuffer.allocate(datas.length);
							buffer.put(datas);
							TreeActivity.addInfo("����:["
									+ com.justin.common.Common
											.bytes2HexString(datas) + "]");
							ServerHandler.iosession.write(buffer.flip());
						}
					}

					return;
				}
				// Ϊ��
				else if (com.justin.common.Common.getLight(nodes).size() != 0) {

					Set<String> set = com.justin.common.Common.getLight(nodes);
					Iterator it = set.iterator();
					while (it.hasNext()) {
						String name = (String) it.next();
						Light light = new Light();
						for (Light l : light_map.values()) {
							if (l.getName().equals(name))
								light = l;
						}
						TreeActivity.addInfo("��:" + light.getName() + ",����="
								+ liangdu);
						Control control = new Control();
						for (Control c : control_map.values()) {
							if (c.getName().equals(light.getControlName())) {
								control = c;
							}
						}
						if (getProtocol() == 0) {
							Data_LCP_SH_D data2 = new Data_LCP_SH_D();
							byte[] datas = data2.setBrightness(
									control.getAddr(),
									new String[] { light.getAddr() },
									(byte) liangdu);
							IoBuffer buffer = IoBuffer.allocate(datas.length);
							buffer.put(datas);
							TreeActivity.addInfo("����:["
									+ com.justin.common.Common
											.bytes2HexString(datas) + "]");
							ServerHandler.iosession.write(buffer.flip());
							continue;
						}
						Data data = new Data();
						byte[] datas = data.setLightBrightness(
								control.getAddr(), light.getAddr(),
								(byte) liangdu);
						IoBuffer buffer = IoBuffer.allocate(datas.length);
						buffer.put(datas);
						TreeActivity.addInfo("����:["
								+ com.justin.common.Common
										.bytes2HexString(datas) + "]");
						ServerHandler.iosession.write(buffer.flip());
					}

					return;
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			/**
			 * ����ֵ�����仯��ͨ��
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (text != null)
					text.setText("" + progress);
				liangdu = progress;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void OnBack(View v) {
		/*
		 * Intent intent = new Intent(); intent.setClass(this, Second.class);
		 * this.startActivity(intent);
		 */
		// �ر�
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		// д�������
		((TreeAdapter) parent.getAdapter()).ExpandOrCollapse(position);
	}

	// ���ýڵ㣬ѭ�����ߵݹ�
	private void setNode() {

		// �������ڵ�
		Node root = new Node("All", "1");
		root.setLevel(0);

		if (this.group_map != null) {

			Iterator it = this.group_map.keySet().iterator();
			while (it.hasNext()) {
				String group = it.next().toString();
				Node n1 = new Node(group, "");
				n1.setParent(root);// ���ø��ڵ�
				n1.setLevel(1);

				// ������������ն�
				Set<Light> set = this.group_map.get(group);
				Iterator it2 = set.iterator();
				while (it2.hasNext()) {
					Light light = (Light) it2.next();
					Node n11 = new Node(light.getName(), "");
					n11.setLevel(2);
					n11.setParent(n1);
					n1.add(n11);

				}
				root.add(n1);
			}
		}
		//
		ta = new TreeAdapter(this, root);
		// �����������Ƿ���ʾ��ѡ��
		ta.setCheckBox(true);
		// ����չ�����۵�ʱͼ��
		ta.setExpandedCollapsedIcon(R.drawable.tree_ex_1, R.drawable.tree_ec_1,
				R.drawable.tree_ex_root, R.drawable.tree_ec_root);
		// ����Ĭ��չ������
		ta.setExpandLevel(2);
		code_list.setAdapter(ta);

	}

}
