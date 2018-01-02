package com.wxw.tree;

import com.wxw.stream.SemanticRoleLabelingSample;

/**
 * 将树转换成样本类样式
 * @author 王馨苇
 *
 * @param <T>
 */
public abstract class SRLTreeToSample {

	/**
	 * 将树转换成语义角色标注所需的样本类格式
	 * @param tree
	 * @param semanticRole
	 * @return
	 */
	public SemanticRoleLabelingSample<SemanticRoleTree> getSample(TreeNode tree, String semanticRole){
		TreeToSRLTree ttst = new TreeToSRLTree();
		SemanticRoleTree srt = ttst.treeToSRLTree(tree, semanticRole); 
		if(hasHeadWords()){
			SRLTreeToSRLHeadTree toheadtree = new SRLTreeToSRLHeadTree();
			SemanticRoleHeadTree headtree = toheadtree.srlTreeToSRLHeadTree(srt);
			return null;
		}else{
			return null;
		}
	}
	
	/**
	 * 是否有头结点
	 * @return
	 */
	public abstract boolean hasHeadWords();
}
