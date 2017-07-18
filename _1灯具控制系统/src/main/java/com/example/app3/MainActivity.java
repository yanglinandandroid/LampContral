package com.example.app3;

import android.app.TabActivity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

/**
 * �������,
 * tabhost
 * @author justin
 *
 */
public class MainActivity extends TabActivity {
	
	public static final String TAB_HOME = "tabHome";
	public static final String TAB_MES = "tabMes";
	public static final String TAB_TOUCH = "tab_touch";
	public static final String TAB_GROUP = "tab_group";
	public static final String TAB_LIGHT = "tab_light";
	public static final String TAB_SCENE = "tab_scene";


	public static TreeActivity tree;
	private TabHost tabHost = null;
	private RadioGroup group;

	//����һ��SoundPool
	private SoundPool sp;

	//����һ��������load������������suondID
	private int music;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//ǿ���˳�  System.exit(1);��ʾ�쳣��ֹ�˳�
		System.exit(0);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ȥ��Activity�����״̬��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_ctrl);
		tabHost = getTabHost();
		group = (RadioGroup) findViewById(R.id.main_radio);
		
		//����
		tabHost.addTab(tabHost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME)
				.setContent(new Intent(this, TreeActivity.class)));
		//����������
		tabHost.addTab(tabHost.newTabSpec(TAB_MES).setIndicator(TAB_MES)
				.setContent(new Intent(this, ControllerActivity.class)));
		
		//�����
		tabHost.addTab(tabHost.newTabSpec(TAB_GROUP).setIndicator(TAB_GROUP)
				.setContent(new Intent(this, GroupActivity.class)));
		//�ƹ���
		tabHost.addTab(tabHost.newTabSpec(TAB_LIGHT).setIndicator(TAB_LIGHT)
				.setContent(new Intent(this, LightActivity.class)));
		
		
		//��������
		tabHost.addTab(tabHost.newTabSpec(TAB_SCENE).setIndicator(TAB_SCENE)
				.setContent(new Intent(this,SceneActivity.class)));

		//��ǰ����
		tabHost.setCurrentTabByTag(TAB_HOME);

		//��һ������Ϊͬʱ���������������������ڶ����������ͣ�����Ϊ��������
		sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

		//����������زķŵ�res/raw���2��������Ϊ��Դ�ļ�����3��Ϊ���ֵ����ȼ�
		music = sp.load(this, R.raw.click_03, 1);
		
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//������Ч
				sp.play(music, (float)0.3, (float)0.3, 0, 0, 1);
				RadioButton b0 = (RadioButton) findViewById(R.id.radio_button0);
				RadioButton b1 = (RadioButton) findViewById(R.id.radio_button1);
				RadioButton b2 = (RadioButton) findViewById(R.id.radio_button2);
				RadioButton b3 = (RadioButton) findViewById(R.id.radio_button3);
				RadioButton b4 = (RadioButton) findViewById(R.id.radio_button4);
				switch (checkedId) {
				// ��ʾ
				case R.id.radio_button0:
					tabHost.setCurrentTabByTag(TAB_HOME);

					break;
				// ������
				case R.id.radio_button1:
					tabHost.setCurrentTabByTag(TAB_MES);
					break;

				// ��
				case R.id.radio_button2:
					tabHost.setCurrentTabByTag(TAB_GROUP);

					break;
				// ��
				case R.id.radio_button3:
					tabHost.setCurrentTabByTag(TAB_LIGHT);
					break;
				// ����
				case R.id.radio_button4:
					tabHost.setCurrentTabByTag(TAB_SCENE);
					break;

				default:
					break;
				}
			}

		});
	}

}
