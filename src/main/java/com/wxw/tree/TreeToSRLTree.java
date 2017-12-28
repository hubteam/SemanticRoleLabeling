package com.wxw.tree;

/**
 * 将一棵树转换成语义角色标注树
 * @author 王馨苇
 *
 */
public class TreeToSRLTree {

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
	
	/**
	 * 将树转换成语义角色树
	 * @param tree 要转换的树
	 * @param semanticRole 语义角色信息
	 * @return
	 */
	public SemanticRoleTree treeToSRLTree(TreeNode tree, String semanticRole){		
		return treeAddSemanticRole(treeAddWordIndex(tree),semanticRole);
	}
}
