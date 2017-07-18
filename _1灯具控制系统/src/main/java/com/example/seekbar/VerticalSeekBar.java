package com.example.seekbar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.AbsSeekBar;

import com.example.app3.TreeActivity;
import com.justin.type.Node;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class VerticalSeekBar extends AbsSeekBar{
	
	private Drawable mThumb;
    private int height;
    private int width;
    private byte liangdu;
   
    public interface OnSeekBarChangeListener {
        void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser);
        void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar);
        void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar);
    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    
    public VerticalSeekBar(Context context) {
        this(context, null);
        
    }
    
    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }
    
    void onStartTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }
    
    void onStopTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }
    
    
    void onProgressRefresh(float scale, boolean fromUser) {
        Drawable thumb = mThumb;
        if (thumb != null) {
            setThumbPos(getHeight(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
        if (mOnSeekBarChangeListener != null) {
        mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), fromUser);
        }
    }

        private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
            int available = w+getPaddingLeft()-getPaddingRight();
            int thumbWidth = thumb.getIntrinsicWidth();
            int thumbHeight = thumb.getIntrinsicHeight();
            available -= thumbWidth;
            // The extra space for the thumb to move on the track
            available += getThumbOffset() * 2;
            int thumbPos = (int) (scale * available);
            int topBound, bottomBound;
            if (gap == Integer.MIN_VALUE) {
                Rect oldBounds = thumb.getBounds();
                topBound = oldBounds.top;
                bottomBound = oldBounds.bottom;
            } else {
                topBound = gap;
                bottomBound = gap + thumbHeight;
            }
            thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
        }
        protected void onDraw(Canvas c)
        {
                c.rotate(-90);
                c.translate(-height,0);
                super.onDraw(c);
        }
        protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
        width = 72;
        height = 1082;
 //               height = View.MeasureSpec.getSize(heightMeasureSpec);
//                width = View.MeasureSpec.getSize(widthMeasureSpec);
                this.setMeasuredDimension(width, height);

        }
        
        
        //判断是否广播
        public boolean isBroadCast(List<Node> nodes)
        {
        	Iterator  it = nodes.iterator();
        	while(it.hasNext())
        	{
        		Node node = (Node) it.next();
        		if(node.getText().equals("所有")){
        			return true;
        		}
        		
        	}
        	return false;
        }
        
        //返回组序号
        public Set<Integer> getGroup(List<Node> nodes)
        {
        	Iterator it = nodes.iterator();
        	Set<Integer> set = new HashSet<Integer>();
        	while(it.hasNext())
        	{
        		Node node = (Node)it.next();
        		String text = node.getText();
        		if(text.contains("组"))
        		{
        			String re = text.replace("组", "");
        			set.add(Integer.parseInt(re)) ;
        		}
        	}
        	return set;
        }
        
        //返回灯序号
        public Set<Integer> getLight(List<Node> nodes)
        {
        	Iterator it = nodes.iterator();
        	Set<Integer> set = new HashSet<Integer>();
        	while(it.hasNext())
        	{
        		Node node = (Node)it.next();
        		String text = node.getText();
        		if(text.contains("灯"))
        		{
        			String re = text.replace("灯", "");
        			set.add(Integer.parseInt(re));
        		}
        	}
        	return set;
        }
        
@Override
public void setThumb(Drawable thumb)
{
        mThumb = thumb;
super.setThumb(thumb);
}
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
            super.onSizeChanged(h, w, oldw, oldh);
    }   
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                onStartTrackingTouch();
                trackTouchEvent(event);
                break;
                
            case MotionEvent.ACTION_MOVE:
                trackTouchEvent(event);
                attemptClaimDrag();
                
               
                break;
                
            case MotionEvent.ACTION_UP:
                TreeActivity.addInfo("亮度设置,亮度:"+this.liangdu+"!");
            	trackTouchEvent(event);
                onStopTrackingTouch();
                setPressed(false);
                
                /**
                 * 多个控制器 ，则每个控制器发送数据，广播的时候，也是对所有的控制器发送广播的命令。
                 */
                //通信
                List<Node> nodes = TreeActivity.ta.getSeletedNodes();
                //判断出是组，还是单个灯，还是广播
                //优先判断是否为广播
                
                if(isBroadCast(nodes))
                {
                	/*
                	TreeActivity.addInfo("广播!亮度="+this.liangdu);
                	Data data = new Data();
                	byte[] datas = data.setAll(this.liangdu);
                	IoBuffer buffer = IoBuffer.allocate(datas.length);
                	buffer.put(datas);
                	ServerHandler.iosession.write(buffer.flip());
                	return true;
                	*/
                }
                //是否为组
                else if(getGroup(nodes).size() != 0)
                {
                	Set<Integer> set = getGroup(nodes);
                	Iterator it = set.iterator();
                	while(it.hasNext())
                	{/*
                		int group = (Integer)it.next();
                		TreeActivity.addInfo("组="+group+",亮度="+this.liangdu);
                    	Data data = new Data();
                    	byte[] datas = data.setGroup((byte)group,this.liangdu);
                    	IoBuffer buffer = IoBuffer.allocate(datas.length);
                    	buffer.put(datas);
                    	ServerHandler.iosession.write(buffer.flip());
                		*/
                	}
                	
                	return true;
                }
                //为灯
                else if(getLight(nodes).size() != 0)
                {
                	
                	Set<Integer> set = getLight(nodes);
                	Iterator it = set.iterator();
                	while(it.hasNext())
                	{
                		/*
                		int light = (Integer)it.next();
                		TreeActivity.addInfo("灯:"+light+",亮度="+this.liangdu);
                    	Data data = new Data();
                    	byte[] datas = data.setLight((byte)light,this.liangdu);
                    	IoBuffer buffer = IoBuffer.allocate(datas.length);
                    	buffer.put(datas);
                    	ServerHandler.iosession.write(buffer.flip());
                	*/
                	}
                	return true;
                }
                	
                break;
                
            case MotionEvent.ACTION_CANCEL:
                onStopTrackingTouch();
                setPressed(false);
                break;
        }
        return true;
    }
    
    
    //改变亮度
    private void trackTouchEvent(MotionEvent event) {
        final int Height = getHeight();
        final int available = Height - getPaddingBottom() - getPaddingTop();
        int Y = (int)event.getY();
        float scale;
        float progress = 0;
        if (Y > Height - getPaddingBottom()) {
            scale = 0.0f;
        } else if (Y  < getPaddingTop()) {
            scale = 1.0f;
        } else {
            scale = (float)(Height - getPaddingBottom()-Y) / (float)available;
        }
        
        final int max = getMax();
        progress = scale * max;
        
        
        /**
         * 改变进度的大小的时候触发
         */
        setProgress((int) progress);
    //    TextView view = (TextView)findViewById(R.id.textView_2);
        TreeActivity.view.setText(Integer.toString((int)progress));
        this.liangdu  = (byte)progress;
        
      
    }
    
    private void attemptClaimDrag() {
        if (getParent() != null) {
        getParent().requestDisallowInterceptTouchEvent(true);
        }
    }
    public boolean dispatchKeyEvent(KeyEvent event) {
    if(event.getAction()==KeyEvent.ACTION_DOWN)
    {
    KeyEvent newEvent = null;
    switch(event.getKeyCode())
    {
    case KeyEvent.KEYCODE_DPAD_UP:
    newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_RIGHT);
    break;
    case KeyEvent.KEYCODE_DPAD_DOWN:
    newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_LEFT);
    break;
    case KeyEvent.KEYCODE_DPAD_LEFT:
    newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN);
    break;
    case KeyEvent.KEYCODE_DPAD_RIGHT:
    newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_UP);
    break;
    default:
    newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,event.getKeyCode());
break;
    }
    return newEvent.dispatch(this);
    }
    return false;
     }
}
