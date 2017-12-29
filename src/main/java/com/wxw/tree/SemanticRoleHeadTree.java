package com.wxw.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 带头结点的语义角色标注树
 * @author 王馨苇
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

	public SemanticRoleHeadTree getParent(){
		return (SemanticRoleHeadTree) parent;
	}

	public List<SemanticRoleHeadTree> getChildren(){
		List<SemanticRoleHeadTree> hnode = new ArrayList<SemanticRoleHeadTree>();
		for (TreeNode treeNode : children) {
			SemanticRoleHeadTree node = (SemanticRoleHeadTree) treeNode;
			hnode.add(node);
		}
		return hnode;
	}
}
