package com.example.view;



import com.example.app3.ControllerActivity;
import com.example.app3.R;
import com.example.app3.SceneActivity;
import com.example.app3.SceneActivity.DragGridAdapterScene;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;


public class DragGridViewScene extends GridView{

	//��������ĳ�Ա����
    private ImageView dragImageView;
    private int dragSrcPosition;
    private int dragPosition;
    //x,y����ļ���
    private int dragPointX;
    private int dragPointY;
    private int dragOffsetX;
    private int dragOffsetY;
     
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;
     
    private int scaledTouchSlop;
    private int upScrollBounce;
    private int downScrollBounce;
    
	
	
	public DragGridViewScene(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	    if(ev.getAction()==MotionEvent.ACTION_DOWN){
	        int x = (int)ev.getX();
	        int y = (int)ev.getY();
	        
	        dragSrcPosition = dragPosition = pointToPosition(x, y);
	        if(dragPosition==AdapterView.INVALID_POSITION){
	            return super.onInterceptTouchEvent(ev);
	        }
	 
	        ViewGroup itemView = (ViewGroup) getChildAt(dragPosition-getFirstVisiblePosition());
	        dragPointX = x - itemView.getLeft();
	        dragPointY = y - itemView.getTop();
	        dragOffsetX = (int) (ev.getRawX() - x);
	        dragOffsetY = (int) (ev.getRawY() - y);
	       /*  
	        View dragger = itemView.findViewById(R.id.drag_grid_item_drag);
	        //���ѡ���϶�ͼ��
	        if(dragger!=null&&dragPointX>dragger.getLeft()&&dragPointX<dragger.getRight()&&dragPointY>dragger.getTop()&&dragPointY<dragger.getBottom()+20){
	 
	            upScrollBounce = Math.min(y-scaledTouchSlop, getHeight()/4);
	            downScrollBounce = Math.max(y+scaledTouchSlop, getHeight()*3/4);
	             
	            itemView.setDrawingCacheEnabled(true);
	            Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
	            startDrag(bm, x, y);
	        }
	        */
	        //ѡ��ͼ��֮���ƶ�
	        View image = itemView.findViewById(R.id.drag_grid_item_image_scene);
	        if(image != null )
	        {
	        	 	upScrollBounce = Math.min(y-scaledTouchSlop, getHeight()/4);
		            downScrollBounce = Math.max(y+scaledTouchSlop, getHeight()*3/4);
		             
		            itemView.setDrawingCacheEnabled(true);
		            Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
		            startDrag(bm, x, y);
		            //����ɾ��
		            SceneActivity.textview.setVisibility(View.VISIBLE);
		            SceneActivity.imageview.setVisibility(View.VISIBLE);
	        }
	        
	       
	        return false;
	     }
	     return super.onInterceptTouchEvent(ev);
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    if(dragImageView!=null&&dragPosition!=INVALID_POSITION){
	        int action = ev.getAction();
	        switch(action){
	            case MotionEvent.ACTION_UP:
	                int upX = (int)ev.getX();
	                int upY = (int)ev.getY();
	                stopDrag();
	                onDrop(upX,upY);
	               
	                /**
	                 * ɾ��
	                 */
	                if(upY > 1000)
	                {
	                	DragGridAdapterScene adapter = (DragGridAdapterScene)getAdapter();
	                	String dragItem = adapter.getItem(dragSrcPosition);
	                	
	                	adapter.remove(dragItem);
	                	adapter.getList().remove(dragItem);
	                	  
	                	
	                }
	                
	                /**
	                 * �޸�
	                 */
	                else if(upY < 150)
	                {
	                	DragGridAdapterScene adapter = (DragGridAdapterScene)getAdapter();
	                	String dragItem = adapter.getItem(dragSrcPosition);
	                	adapter.update(dragItem);
	                }
	                SceneActivity.textview.setVisibility(View.INVISIBLE);
	                SceneActivity.imageview.setVisibility(View.INVISIBLE);
	                break;
	            case MotionEvent.ACTION_MOVE:
	                int moveX = (int)ev.getX();
	                int moveY = (int)ev.getY();
	                System.out.println("y="+moveY);
	                if(moveY  > 1000)
	                {
	                	SceneActivity.textview.setBackgroundColor(Color.argb(70, 255, 0, 0));
	                }
	                	
	                else
	                {	
	                	SceneActivity.textview.setBackgroundColor(Color.argb( 20,255, 0, 0));	
	                }

	                onDrag(moveX,moveY);
	                break;
	            
	            default:break;
	        }
	        return true;
	    }
	    return super.onTouchEvent(ev);
	}
	
	
	
	public void startDrag(Bitmap bm, int x, int y){
	    stopDrag();
	     
	    windowParams = new WindowManager.LayoutParams();
	    windowParams.gravity = Gravity.TOP|Gravity.LEFT;
	    windowParams.x = x - dragPointX + dragOffsetX;
	    windowParams.y = y - dragPointY + dragOffsetY;
	    windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
	    windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
	    windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
	                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
	    windowParams.format = PixelFormat.TRANSLUCENT;
	    windowParams.windowAnimations = 0;
	 
	    ImageView imageView = new ImageView(getContext());
	    imageView.setImageBitmap(bm);
	    windowManager = (WindowManager)getContext().getSystemService("window");
	    windowManager.addView(imageView, windowParams);
	    dragImageView = imageView;
	    
	 
	   
	}
	 
	public void onDrag(int x, int y){
	    if(dragImageView!=null){
	        windowParams.alpha = 0.8f;
	        windowParams.x = x - dragPointX + dragOffsetX;
	        windowParams.y = y - dragPointY + dragOffsetY;
	        windowManager.updateViewLayout(dragImageView, windowParams);
	    }
	 
	    int tempPosition = pointToPosition(x, y);
	    if(tempPosition!=INVALID_POSITION){
	        dragPosition = tempPosition;
	    }
	     
	    //����
	    if(y<upScrollBounce||y>downScrollBounce){
	        //ʹ��setSelection��ʵ�ֹ���
	        setSelection(dragPosition);
	    }        
	}
	
	public void onDrop(int x, int y){
        
		
//		TextView textview = (TextView) findViewById(R.id.text_delete);
//	    textview.setVisibility(View.VISIBLE);
        //Ϊ�˱��⻬�����ָ��ߵ�ʱ�򣬷���-1������
        int tempPosition = pointToPosition(x, y);
        if(tempPosition!=INVALID_POSITION){
            dragPosition = tempPosition;
        }
         
        //�����߽紦��
        if(y<getChildAt(0).getTop()){
            //�����ϱ߽�
            dragPosition = 0;
        }else if(y>getChildAt(getChildCount()-1).getBottom()||(y>getChildAt(getChildCount()-1).getTop()&&x>getChildAt(getChildCount()-1).getRight())){
            //�����±߽�
            dragPosition = getAdapter().getCount()-1;
        }
//        System.out.println(y);
      
        /*
        //���ݽ���
        if(dragPosition!=dragSrcPosition&&dragPosition>-1&&dragPosition<getAdapter().getCount()){
            DragGridAdapter adapter = (DragGridAdapter)getAdapter();
            String dragItem = adapter.getItem(dragSrcPosition);
            adapter.remove(dragItem);
            adapter.insert(dragItem, dragPosition);
            Toast.makeText(getContext(), adapter.getList().toString(), Toast.LENGTH_SHORT).show();
        }
        */
         
    }
	
	
	/***
	 * ֹͣ�϶���ȥ���϶�ʱ���Ӱ��
	 */
	public void stopDrag(){
		if(dragImageView != null){
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}
	
	
	

}
