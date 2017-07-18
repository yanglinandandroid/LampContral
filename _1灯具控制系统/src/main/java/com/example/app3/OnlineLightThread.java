package com.example.app3;

import android.view.View;


/**
 * 在线提示灯     闪烁线程类
 * @author justin
 *
 */
public class OnlineLightThread extends Thread{

	public void run()
	{
		while(true)
		{
			TreeActivity.image_online.setImageResource(R.drawable.online);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TreeActivity.image_online.setImageResource(R.drawable.online3);
		}
	}

}
