package com.wxw.srl;

import com.wxw.tree.SRLTreeNode;

/**
 * 语义角色树
 * @author 王馨苇
 *
 */
public class SRLTree {

	private SRLTreeNode srltreenode;
	
	public void setSRLTree(SRLTreeNode srltreenode){
		this.srltreenode = srltreenode;
	}
	
	/**
	 * 获取句法树的根节点，从根节点可以遍历出整个树
	 * @return
	 */
	public SRLTreeNode getSRLTreeRoot(){
		return this.srltreenode;
	}
	
}
