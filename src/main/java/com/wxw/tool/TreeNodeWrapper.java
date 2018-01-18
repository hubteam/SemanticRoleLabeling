package com.wxw.tool;

import com.wxw.tree.TreeNode;

/**
 * 记录一棵树和一棵树最左边的叶子节点下标
 * @author 王馨苇
 *
 */
public class TreeNodeWrapper<T extends TreeNode> {

	private T tree;//树
	private int leftLeafIndex;//当前树最左边的叶子节点
	//如果树是以谓词为根节点的树，那么stem为动词的词根
	//如果树是以成分为根节点的树，stem为头结点的词根
	private String stem;
	
	public TreeNodeWrapper(T tree, int leftLeafIndex){
		this.tree = tree;
		this.leftLeafIndex = leftLeafIndex;
	}
	
	public TreeNodeWrapper(T tree, int leftLeafIndex,String stem){
		this.tree = tree;
		this.leftLeafIndex = leftLeafIndex;
		this.stem = stem;
	}
	
	public int getLeftLeafIndex(){
		return this.leftLeafIndex;
	}
	
	public T getTree(){
		return this.tree;
	}
	
	public String getStem(){
		return this.stem;
	}
}
