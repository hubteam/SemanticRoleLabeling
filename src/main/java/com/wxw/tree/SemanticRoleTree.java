package com.wxw.tree;

/**
 * 语义角色标注树
 * @author 王馨苇
 *
 */
public class SemanticRoleTree extends TreeNode{

	public SemanticRoleTree(String nodename){
		super(nodename);
	}
	
	//词语下标索引
	private int wordIndex;
	
	//语义角色标注信息
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
