package com.wxw.tree;

/**
 * ��ͷ���������ɫ��ע��
 * @author ��ܰέ
 *
 */
public class SemanticRoleHeadTree extends HeadTreeNode{

	public SemanticRoleHeadTree(String nodename) {
		super(nodename);
	}

	//������±�
	private int wordIndex;
	
	//�����ɫ
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
