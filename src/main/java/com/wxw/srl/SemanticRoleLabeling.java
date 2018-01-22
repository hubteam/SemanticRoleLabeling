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
	public SRLTree srltree(TreeNode tree);
	
	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @return
	 */
	public SRLTree srltree(String treeStr);
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param tree 句法分析得到的树
	 * @return
	 */
	public SRLTree[] kSrltree(TreeNode tree);
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param tree 句法分析得到的树的括号表示
	 * @return
	 */
	public SRLTree[] kSrltree(String treeStr);
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树
	 * @return
	 */
	public String srlstr(TreeNode tree);
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @return
	 */
	public String srlstr(String treeStr);
	
	/**
	 * 得到一棵树最好的K个角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树
	 * @return
	 */
	public String[] kSrlstr(TreeNode tree);
	
	/**
	 * 得到一棵树最好的K个角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树的括号表示
	 * @return
	 */
	public String[] kSrlstr(String treeStr);
}
