package com.wxw.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 带头结点的语义角色标注树
 * @author 王馨苇
 *
 */
public class SRLHeadTreeNode extends SRLTreeNode{

	
	private String headWords;
	private String headWordsPos;
	
	public SRLHeadTreeNode(String nodename) {
		super(nodename);
	}

	public void setHeadWords(String headWords){
		this.headWords = headWords;
	}
	
	public String getHeadWords(){
		return this.headWords;
	}

	public void setHeadWordsPos(String headWordsPos){
		this.headWordsPos = headWordsPos;
	}
	
	public String getHeadWordsPos(){
		return this.headWordsPos;
	}
//	public SemanticRoleHeadTree getParent(){
//		return (SemanticRoleHeadTree) parent;
//	}
//
	public List<SRLHeadTreeNode> getChildren(){
		List<SRLHeadTreeNode> hnode = new ArrayList<SRLHeadTreeNode>();
		for (TreeNode treeNode : children) {
			SRLHeadTreeNode node = (SRLHeadTreeNode) treeNode;
			hnode.add(node);
		}
		return hnode;
	}
	
	@Override
	public String toString() {
		if(this.children.size() == 0){
			return " "+this.nodename+"_["+getWordIndex()+"]";
		}else{
			String treestr = "";
			if(getSemanticRole() != null){
				treestr = "("+this.nodename+"{"+this.headWords+"["+this.headWordsPos+"]"+"}_"+"{"+this.getSemanticRole()+"}";
			}else{
				treestr = "("+this.nodename+"{"+this.headWords+"["+this.headWordsPos+"]"+"}";
			}			
			for (TreeNode node:this.children) {
				treestr += node.toString();
			}
			treestr += ")";
			return treestr;
		}
	}
}
