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
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public SRLTree srltree(TreeNode tree, String[] predicateinfo);
	
	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public SRLTree srltree(String treeStr, String[] predicateinfo);
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param k
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public SRLTree[] kSrltree(int k, TreeNode tree, String[] predicateinfo);
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param k
	 * @param tree 句法分析得到的树的括号表示
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public SRLTree[] kSrltree(int k, String treeStr, String[] predicateinfo);
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public String srlstr(TreeNode tree, String[] predicateinfo);
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public String srlstr(String treeStr, String[] predicateinfo);
	
	/**
	 * 得到一棵树最好的K个角色标注的中括号表达式形式
	 * @param k
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public String[] kSrlstr(int k, TreeNode tree, String[] predicateinfo);
	
	/**
	 * 得到一棵树最好的K个角色标注的中括号表达式形式
	 * @param k
	 * @param tree 句法分析得到的树的括号表示
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public String[] kSrlstr(int k, String treeStr, String[] predicateinfo);
	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词下标信息
	 * @return
	 */
	public SRLTree srltree(TreeNode tree, int[] predicateindexinfo);
	
	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @param predicateinfo 谓词下标信息
	 * @return
	 */
	public SRLTree srltree(String treeStr, int[] predicateindexinfo);
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param k
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词下标信息
	 * @return
	 */
	public SRLTree[] kSrltree(int k, TreeNode tree, int[] predicateindexinfo);
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param k
	 * @param tree 句法分析得到的树的括号表示
	 * @param predicateinfo 谓词下标信息
	 * @return
	 */
	public SRLTree[] kSrltree(int k, String treeStr, int[] predicateindexinfo);
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	public String srlstr(TreeNode tree, int[] predicateindexinfo);
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @param predicateinfo 谓词下标信息
	 * @return
	 */
	public String srlstr(String treeStr, int[] predicateindexinfo);
	
	/**
	 * 得到一棵树最好的K个角色标注的中括号表达式形式
	 * @param k
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词下标信息
	 * @return
	 */
	public String[] kSrlstr(int k, TreeNode tree, int[] predicateindexinfo);
	
	/**
	 * 得到一棵树最好的K个角色标注的中括号表达式形式
	 * @param k
	 * @param tree 句法分析得到的树的括号表示
	 * @param predicateinfo 谓词下标信息
	 * @return
	 */
	public String[] kSrlstr(int k, String treeStr, int[] predicateindexinfo);
}
