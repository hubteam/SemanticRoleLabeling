package com.wxw.tool;

import java.util.List;
import java.util.Stack;

import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.SRLTreeNode;
import com.wxw.tree.TreeNode;

/**
 * 将一棵树转换成语义角色标注树
 * @author 王馨苇
 *
 */
public class TreeToSRLTreeTool {
	
	/**
	 * 将一颗树转成语义角色树
	 * @param tree
	 * @return
	 */
	public static SRLTreeNode transferToSRLTreeNodeStructure(TreeNode treenode){
		String strtree = "("+treenode.toNoNoneSample()+")";
		PhraseGenerateTree pgt = new PhraseGenerateTree();
		String format = pgt.format(strtree);
		List<String> parts = pgt.stringToList(format);
		Stack<SRLTreeNode> tree = new Stack<SRLTreeNode>();
        for (int i = 0; i < parts.size(); i++) {
			if(!parts.get(i).equals(")") && !parts.get(i).equals(" ")){
				SRLTreeNode tn = new SRLTreeNode(parts.get(i));
				tree.push(tn);				
			}else if(parts.get(i).equals(" ")){
				
			}else if(parts.get(i).equals(")")){
				Stack<SRLTreeNode> temp = new Stack<SRLTreeNode>();
				while(!tree.peek().getNodeName().equals("(")){
					if(!tree.peek().getNodeName().equals(" ")){
						temp.push(tree.pop());						
					}
				}
				tree.pop();
				SRLTreeNode node = temp.pop();
				while(!temp.isEmpty()){		
					temp.peek().setParent(node);					
					if(temp.peek().getChildren().size() == 0){
						SRLTreeNode wordindexnode = temp.peek();
						String[] str = temp.peek().getNodeName().split("\\[");
						wordindexnode.setNewName(str[0]);
						wordindexnode.setWordIndex(Integer.parseInt(str[1].substring(0, str[1].length()-1)));
						node.addChild(wordindexnode);
					}else{
						node.addChild(temp.peek());
					}
					temp.pop();
				}
				tree.push(node);
			}
		}
        SRLTreeNode treeStruct = tree.pop();
        return treeStruct;
	}
	
	/**
	 * 为带词下标的语义角色标注树加上语义信息
	 * @param tree 不含语义角色信息的树
	 * @param argumenttree 论元树
	 * @param labelinfo 论元标记
	 * @return
	 */
	public static SRLTreeNode treeAddSemanticRole(SRLTreeNode tree, TreeNodeWrapper<HeadTreeNode>[] argumenttree,String[] labelinfo){
		for (int i = 0; i < labelinfo.length; i++) {
			if(labelinfo[i].contains("NULL")){
				
			}else{
				treeAddSingleSemanticRole(tree,argumenttree[i].getTree(),labelinfo[i]);
			}
		}
		return tree;
	}
	
	private static void treeAddSingleSemanticRole(SRLTreeNode tree,HeadTreeNode headtree,String role){
		if(tree.toString().equals(headtree.toBracket())){
			tree.setSemanticRole(role);
		}else{
			for (TreeNode treenode : tree.getChildren()) {
				treeAddSingleSemanticRole((SRLTreeNode)treenode,headtree,role);
			}
		}
	}

	
	/**
	 * 将树转换成语义角色树
	 * @param tree 要转换的树
	 * @param semanticRole 语义角色信息
	 * @return
	 */
	public static SRLTreeNode treeToSRLTree(TreeNode tree, TreeNodeWrapper<HeadTreeNode>[] argumenttree,String[] labelinfo){		
		return treeAddSemanticRole(transferToSRLTreeNodeStructure(tree),argumenttree,labelinfo);
	}
}
