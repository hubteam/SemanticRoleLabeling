package com.wxw.tree;

/**
 * 带头结点的语义角色标注树
 * @author 王馨苇
 *
 */
public class SemanticRoleHeadTree extends HeadTreeNode{

	public SemanticRoleHeadTree(String nodename) {
		super(nodename);
	}

	//词语的下标
	private int wordIndex;
	
	//语义角色
	private String semanticRole;
	
	public void setWordIndex(int wordIndex){
		this.wordIndex = wordIndex;
	}
	
	public void setSemanticRole(String semanticRole){
		this.semanticRole = semanticRole;
	}
	
	public String getSemanticRole(){
		return this.semanticRole;
	}
	
	public int getWordIndex(){
		return this.wordIndex;
	}
}
