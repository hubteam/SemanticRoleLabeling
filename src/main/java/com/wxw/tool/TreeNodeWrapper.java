package com.wxw.tool;

import com.wxw.tree.TreeNode;

/**
 * 记录一棵树和一棵树最左边的叶子节点下标
 * @author 王馨苇
 *
 */
public class TreeNodeWrapper<T extends TreeNode> {

	private T tree;
	private int leftLeafIndex;
	
	public TreeNodeWrapper(T tree, int leftLeafIndex){
		this.tree = tree;
		this.leftLeafIndex = leftLeafIndex;
	}
	
	public int getLeftLeafIndex(){
		return this.leftLeafIndex;
	}
	
	public T getTree(){
		return this.tree;
	}
}
