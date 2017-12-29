package com.wxw.tree;

import java.util.List;
import java.util.Stack;

/**
 * 将一棵树转换成语义角色标注树
 * @author 王馨苇
 *
 */
public class TreeToSRLTree {
	
	/**
	 * 为树加上词下标
	 * @param tree
	 * @return
	 */
	public SemanticRoleTree treeAddWordIndex(TreeNode treenode){
		String strtree = "("+treenode.toString()+")";
		PhraseGenerateTree pgt = new PhraseGenerateTree();
		String format = pgt.format(strtree);
		List<String> parts = pgt.stringToList(format);
		Stack<SemanticRoleTree> tree = new Stack<SemanticRoleTree>();
		int wordindex = 0;
        for (int i = 0; i < parts.size(); i++) {
			if(!parts.get(i).equals(")") && !parts.get(i).equals(" ")){
				SemanticRoleTree tn = new SemanticRoleTree(parts.get(i));
				tree.push(tn);				
			}else if(parts.get(i).equals(" ")){
				
			}else if(parts.get(i).equals(")")){
				Stack<SemanticRoleTree> temp = new Stack<SemanticRoleTree>();
				while(!tree.peek().getNodeName().equals("(")){
					if(!tree.peek().getNodeName().equals(" ")){
						temp.push(tree.pop());
					}
				}
				tree.pop();
				SemanticRoleTree node = temp.pop();
				while(!temp.isEmpty()){		
					temp.peek().setParent(node);
					node.addChild(temp.pop());
				}
				tree.push(node);
			}
		}
        SemanticRoleTree treeStruct = tree.pop();
        return treeStruct;
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
