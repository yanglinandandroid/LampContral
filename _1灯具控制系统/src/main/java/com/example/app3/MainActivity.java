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
 * 程序入口,
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

	//声明一个SoundPool
	private SoundPool sp;

	//定义一个整型用load（）；来设置suondID
	private int music;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//强制退出  System.exit(1);表示异常终止退出
		System.exit(0);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 去掉Activity上面的状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_ctrl);
		tabHost = getTabHost();
		group = (RadioGroup) findViewById(R.id.main_radio);
		
		//控制
		tabHost.addTab(tabHost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME)
				.setContent(new Intent(this, TreeActivity.class)));
		//控制器管理
		tabHost.addTab(tabHost.newTabSpec(TAB_MES).setIndicator(TAB_MES)
				.setContent(new Intent(this, ControllerActivity.class)));
		
		//组管理
		tabHost.addTab(tabHost.newTabSpec(TAB_GROUP).setIndicator(TAB_GROUP)
				.setContent(new Intent(this, GroupActivity.class)));
		//灯管理
		tabHost.addTab(tabHost.newTabSpec(TAB_LIGHT).setIndicator(TAB_LIGHT)
				.setContent(new Intent(this, LightActivity.class)));
		
		
		//场景管理
		tabHost.addTab(tabHost.newTabSpec(TAB_SCENE).setIndicator(TAB_SCENE)
				.setContent(new Intent(this,SceneActivity.class)));

		//当前界面
		tabHost.setCurrentTabByTag(TAB_HOME);

		//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

		//把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
		music = sp.load(this, R.raw.click_03, 1);
		
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//播放音效
				sp.play(music, (float)0.3, (float)0.3, 0, 0, 1);
				RadioButton b0 = (RadioButton) findViewById(R.id.radio_button0);
				RadioButton b1 = (RadioButton) findViewById(R.id.radio_button1);
				RadioButton b2 = (RadioButton) findViewById(R.id.radio_button2);
				RadioButton b3 = (RadioButton) findViewById(R.id.radio_button3);
				RadioButton b4 = (RadioButton) findViewById(R.id.radio_button4);
				switch (checkedId) {
				// 演示
				case R.id.radio_button0:
					tabHost.setCurrentTabByTag(TAB_HOME);

					break;
				// 控制器
				case R.id.radio_button1:
					tabHost.setCurrentTabByTag(TAB_MES);
					break;

				// 组
				case R.id.radio_button2:
					tabHost.setCurrentTabByTag(TAB_GROUP);

					break;
				// 灯
				case R.id.radio_button3:
					tabHost.setCurrentTabByTag(TAB_LIGHT);
					break;
				// 场景
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
