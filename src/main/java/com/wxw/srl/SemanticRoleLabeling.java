package com.wxw.srl;

import com.wxw.tree.TreeNode;

/**
 * 语义角色标注器
 * @author 王馨苇
 *
 */
public interface SemanticRoleLabeling {

	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树
	 * @return
	 */
	public SRLTree srl(TreeNode tree);
	
	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @return
	 */
	public SRLTree srl(String treeStr);
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param tree 句法分析得到的树
	 * @return
	 */
	public SRLTree[] kSrl(TreeNode tree);
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param tree 句法分析得到的树的括号表示
	 * @return
	 */
	public SRLTree[] kSrl(String treeStr);
}
