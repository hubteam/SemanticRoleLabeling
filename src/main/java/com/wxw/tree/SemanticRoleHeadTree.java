package com.wxw.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * ��ͷ���������ɫ��ע��
 * @author ��ܰέ
 *
 */
public class SemanticRoleHeadTree extends SemanticRoleTree{

	private String headWords;
	
	public SemanticRoleHeadTree(String nodename) {
		super(nodename);
	}

	public void setHeadWords(String headWords){
		this.headWords = headWords;
	}
	
	public String getHeadWords(){
		return this.headWords;
	}

	//���ظ��ڵ�
	public SemanticRoleHeadTree getParent(){
		return (SemanticRoleHeadTree) parent;
	}

	//�����ӽڵ��б�
	public List<SemanticRoleHeadTree> getChildren(){
		List<SemanticRoleHeadTree> hnode = new ArrayList<SemanticRoleHeadTree>();
		for (TreeNode treeNode : children) {
			SemanticRoleHeadTree node = (SemanticRoleHeadTree) treeNode;
			hnode.add(node);
		}
		return hnode;
	}
}
