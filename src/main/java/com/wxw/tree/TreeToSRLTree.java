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
					if(temp.peek().getChildren().size() == 0){
						SemanticRoleTree wordindexnode = temp.pop();
						wordindexnode.setWordIndex(wordindex++);
						node.addChild(wordindexnode);
					}else{
						node.addChild(temp.pop());
					}
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
		String[] roles = semanticRole.split(" ");
		for (int i = 6; i < roles.length; i++) {
			//拆开为argument下标和语义标记部分
			String[] digitandrole = roles[i].split("-");
			//处理语义角色部分
			String role = "";
			for (int j = 1; j < digitandrole.length; j++) {
				if(j == digitandrole.length-1){
					role += digitandrole[j];
				}else{
					role += digitandrole[j] + "-";
				}
			}
			//处理argument部分
			//7:3*28:0-ARG1
			//0:1,1:1*5:1*6:0-ARG1
			String[] digits = digitandrole[0].split("\\*");
			//处理,隔开的部分
			String[] comma = digits[0].split(",");
			for (int j = 0; j < comma.length; j++) {
				String[] digit = comma[j].split(":");
				int begin = Integer.parseInt(digit[0]);
				int up = Integer.parseInt(digit[1]);
				treeAddSingleSemanticRole(tree,begin,up,role);
			}
			for (int j = 1; j < digits.length; j++) {
				String[] digit = digits[j].split(":");
				int begin = Integer.parseInt(digit[0]);
				int up = Integer.parseInt(digit[1]);
				treeAddSingleSemanticRole(tree,begin,up,role);
			}
		}
		return tree;
	}
	
	private void treeAddSingleSemanticRole(SemanticRoleTree tree,int begin,int up,String role){
		if(tree.getChildren().size() == 0 && tree.getWordIndex() == begin){
			SemanticRoleTree temp = tree;
			tree = (SemanticRoleTree) tree.getParent();;
			for (int i = 0; i < up; i++) {
				tree = (SemanticRoleTree) tree.getParent();
			}
			tree.setSemanticRole(role);
			tree = temp;
		}else{
			for (TreeNode treenode : tree.getChildren()) {
				treeAddSingleSemanticRole((SemanticRoleTree)treenode,begin,up,role);
			}
		}
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
