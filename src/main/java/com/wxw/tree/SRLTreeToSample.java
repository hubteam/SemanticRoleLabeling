package com.wxw.tree;

import com.wxw.stream.SemanticRoleLabelingSample;

/**
 * 将语义角色标注树或带头结点的语义角色标注树转成样本类形式
 * @author 王馨苇
 *
 */
public abstract class SRLTreeToSample<T extends SemanticRoleTree> {

	//记录词下标的量
	private int wordindex = 0;
	
	/**
	 * 为树加上词下标
	 * @param tree
	 * @return
	 */
	public SemanticRoleTree treeAddWordIndex(TreeNode tree){
		SemanticRoleTree srltree = new SemanticRoleTree(tree.getNodeName());
		for (int i = 0; i < tree.getChildren().size(); i++) {
			srltree.addChild(treeAddWordIndex(tree.getChildren().get(i)));
		}
		SemanticRoleTree leaf = null;
		if(tree.getChildren().size() == 0){	
			leaf = new SemanticRoleTree(tree.getNodeName());
			leaf.setWordIndex(wordindex);
			wordindex++;			
		}else{
			leaf = (SemanticRoleTree) tree.getChildren();
		}
		srltree.addChild(leaf);
		return srltree;
	}
	
	/**
	 * 为带词下标的语义角色标注树加上语义信息
	 * @param tree 不含语义角色信息的树
	 * @param semanticRole 语义角色标注信息
	 * @return
	 */
	public SemanticRoleTree treeAddSemanticRole(SemanticRoleTree tree, String semanticRole){
		return null;
	}
	
	public SemanticRoleLabelingSample getSample(TreeNode tree, String semanticRole){
		if(hasHeadWords()){
			
		}else{
			
		}
		return null;
	}
	
	public abstract boolean hasHeadWords();
}
