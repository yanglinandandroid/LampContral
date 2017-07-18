package com.example.app3;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.justin.type.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ݹ�������ui�����ݿ�֮�������������
 * @author justin
 *
 */
public class TreeAdapter extends BaseAdapter{
	private Context con;
	private LayoutInflater lif;
	private List<Node> allsCache = new ArrayList<Node>();
	private List<Node> alls = new ArrayList<Node>();
	private TreeAdapter oThis = this;
	private boolean hasCheckBox = true;//�Ƿ�ӵ�и�ѡ��
	private int expandedIcon = -1;
	private int collapsedIcon = -1;
	private int expandedIcon_root = -1;
	private int collapsedIcon_root = -1;
	
	/**
	 * TreeAdapter���캯��
	 * @param context 
	 * @param rootNode ���ڵ�
	 */
	public TreeAdapter(Context context,Node rootNode){
		this.con = context;
		this.lif = (LayoutInflater) con.
    	getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addNode(rootNode);
	}
	
	private void addNode(Node node){
		alls.add(node);
		allsCache.add(node);
		if(node.isLeaf())return;
		for(int i=0;i<node.getChildren().size();i++){
			addNode(node.getChildren().get(i));
		}
	}
	

	// ��ѡ������
	private void checkNode(Node node,boolean isChecked){
		node.setChecked(isChecked);
		for(int i=0;i<node.getChildren().size();i++){
			checkNode(node.getChildren().get(i),isChecked);
		}
	}
	
	/**
	 * ���ѡ�нڵ�
	 * @return
	 */
	public List<Node> getSeletedNodes(){
		List<Node> nodes = new ArrayList<Node>();
		for(int i=0;i<allsCache.size();i++){
			Node n = allsCache.get(i);
			if(n.isChecked()){
				nodes.add(n);
			}
		}
		return nodes;
	}
	
	// ���ƽڵ��չ�����۵�
	private void filterNode(){
		alls.clear();
		for(int i=0;i<allsCache.size();i++){
			Node n = allsCache.get(i);
			if(!n.isParentCollapsed() || n.isRoot()){
				alls.add(n);
			}
		}
	}
	
	/**
     * �����Ƿ�ӵ�и�ѡ��
     * @param hasCheckBox
     */
    public void setCheckBox(boolean hasCheckBox){
    	this.hasCheckBox = hasCheckBox;
    }
	
    /**
     * ����չ�����۵�״̬ͼ��
     * @param expandedIcon չ��ʱͼ��
     * @param collapsedIcon �۵�ʱͼ��
     */
    public void setExpandedCollapsedIcon(int expandedIcon,int collapsedIcon,int expandedIcon_root,int collapsedIcon_root){
    	this.expandedIcon = expandedIcon;
    	this.collapsedIcon = collapsedIcon;
    	this.expandedIcon_root = expandedIcon_root;
    	this.collapsedIcon_root = collapsedIcon_root;
    }
    
	/**
	 * ����չ������
	 * @param level
	 */
	public void setExpandLevel(int level){
		alls.clear();
		for(int i=0;i<allsCache.size();i++){
			Node n = allsCache.get(i);
			if(n.getLevel()<=level){
				if(n.getLevel()<level){// �ϲ㶼����չ��״̬
					n.setExpanded(true);
				}else{// ���һ�㶼�����۵�״̬
					n.setExpanded(false);
				}
				alls.add(n);
			}
		}
		this.notifyDataSetChanged();
	}
	
	/**
	 * ���ƽڵ��չ��������
	 * @param position
	 */
	public void ExpandOrCollapse(int position){
		Node n = alls.get(position);
		if(n != null){
			if(!n.isLeaf()){
				n.setExpanded(!n.isExpanded());
				filterNode();
				this.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return alls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		
		ViewHolder holder = null;
		if (view == null) {
			view = this.lif.inflate(R.layout.listview_item_tree, null);
			holder = new ViewHolder();
			holder.chbSelect = (CheckBox)view.findViewById(R.id.chbSelect);
			
			// ��ѡ�򵥻��¼�
			holder.chbSelect.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Node n = (Node)v.getTag();
					checkNode(n,((CheckBox)v).isChecked());
					//�ı����ӽڵ�
					oThis.notifyDataSetChanged();
					
					
					Node p1 = n.getParent();
					if(p1!=null && !n.isChecked())
					{
						p1.setChecked(false);
						Node p11 = p1.getParent();
						if(p11 != null && !p1.isChecked())
						{
							p11.setChecked(false);
						}
					}
				
				}
				
			});
			holder.ivIcon = (ImageView)view.findViewById(R.id.ivIcon);
			holder.tvText = (TextView)view.findViewById(R.id.tvText);
			holder.ivExEc = (ImageView)view.findViewById(R.id.ivExEc);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		
		// �õ���ǰ�ڵ�
		Node n = alls.get(position);
		
		if(n != null){
			//��һ��
			if(position == 0)
			{
				view.setBackgroundColor(Color.rgb(125, 125, 125));
				holder.tvText.setTextColor(Color.rgb( 255,255,255));
				holder.tvText.setTextSize(20);
				
				TextPaint tp = holder.tvText.getPaint();
				
                tp.setFakeBoldText(true);
                
				//Typeface face = Typeface.createFromAsset (TreeActivity..getAssets(),"fonts/timesi.ttf");
			}
			

			
			holder.chbSelect.setTag(n);
			holder.chbSelect.setChecked(n.isChecked());
			
			// �Ƿ���ʾ��ѡ��
			if(n.hasCheckBox() && hasCheckBox){
				holder.chbSelect.setVisibility(View.VISIBLE);
			}else{
				holder.chbSelect.setVisibility(View.GONE);
			}
			
			// �Ƿ���ʾͼ��
			if(n.getIcon() == -1){
			    holder.ivIcon.setVisibility(View.GONE);
			}else{
				holder.ivIcon.setVisibility(View.VISIBLE);
				holder.ivIcon.setImageResource(n.getIcon());
			}
			
			// ��ʾ�ı�
			holder.tvText.setText(n.getText());
			

			
			if(n.isLeaf()){
				// ��Ҷ�ڵ� ����ʾչ�����۵�״̬ͼ��
				holder.ivExEc.setVisibility(View.GONE);
			}else{ 
				// ����ʱ�����ӽڵ�չ�����۵�,״̬ͼ��ı�
				holder.ivExEc.setVisibility(View.VISIBLE);
				if(n.isExpanded()){
					if(expandedIcon != -1)
					{
						if(position == 0)
							holder.ivExEc.setImageResource(expandedIcon_root);
						else
							holder.ivExEc.setImageResource(expandedIcon);
					}
				}
				else {
					if(collapsedIcon != -1)
					{
						if(position == 0)
							holder.ivExEc.setImageResource(collapsedIcon_root);
						else
						{
							holder.ivExEc.setImageResource(collapsedIcon);
						}
					}
				}
				
			}
			
			// ��������
			if(n.getLevel() == 0 || n.getLevel() == 1)
			view.setPadding(70*0, 3,3, 3);
			else
				view.setPadding(35*n.getLevel(), 3, 3, 3);
			
			
		}
		
		return view;
	}

	/**
	 * 
	 * �б���ؼ�����
	 *
	 */
	public class ViewHolder{
		CheckBox chbSelect;//ѡ�����
		ImageView ivIcon;//ͼ��
		TextView tvText;//�ı�������
		ImageView ivExEc;//չ�����۵����">"��"v"
	}





}
