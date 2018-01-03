package com.wxw.tree;

import java.util.List;
import java.util.Stack;

import com.wxw.headwords.AbsractGenerateHeadWords;
import com.wxw.headword.ConcreteGenerateHeadWords;
import com.wxw.headwords.HeadWordsRuleSet;

/**
 * 将语义角色标注树转成带头结点的语义角色标注树
 * @author 王馨苇
 *
 */
public class SRLTreeToSRLHeadTree {

	private AbsractGenerateHeadWords<SRLHeadTreeNode> aghw = new ConcreteGenerateHeadWords(); 
	
	/**
	 * 给语义角色标注树加上头结点
	 * @param tree 没有头结点的语义角色树
	 * @return
	 */
	public SRLHeadTreeNode srlTreeToSRLHeadTree(SRLTreeNode treenode){
		String strtree = "("+treenode.toRoleString()+")";
		PhraseGenerateTree pgt = new PhraseGenerateTree();
		String format = pgt.format(strtree);
		List<String> parts = pgt.stringToList(format);
		int wordindex = 0;
		Stack<SRLHeadTreeNode> tree = new Stack<SRLHeadTreeNode>();
		for (int i = 0; i < parts.size(); i++) {
			if(!parts.get(i).equals(")") && !parts.get(i).equals(" ")){
				SRLHeadTreeNode tn = new SRLHeadTreeNode(parts.get(i));
				tree.push(tn);				
			}else if(parts.get(i).equals(" ")){
				
			}else if(parts.get(i).equals(")")){
				Stack<SRLHeadTreeNode> temp = new Stack<SRLHeadTreeNode>();
				while(!tree.peek().getNodeName().equals("(")){
					if(tree.peek().getNodeName().contains("{")){
						SRLHeadTreeNode rolenode = tree.peek();
						String[] str = tree.peek().getNodeName().split("\\{");
						rolenode.setNewName(str[0]);
						rolenode.setSemanticRole(str[1].substring(0, str[1].length()-1));
						temp.push(rolenode);
					}else{
						temp.push(tree.peek());
					}
					tree.pop();
				}
				tree.pop();
				SRLHeadTreeNode node = temp.pop();
				while(!temp.isEmpty()){		
					temp.peek().setParent(node);					
					if(temp.peek().getChildren().size() == 0){
						SRLTreeNode wordindexnode = temp.pop();
						wordindexnode.setWordIndex(wordindex++);
						node.addChild(wordindexnode);
					}else{
						node.addChild(temp.pop());
					}
				}
				//设置头节点和头结点对应的词性
				//为每一个非终结符，且不是词性标记的设置头节点
				//对于词性标记的头节点就是词性标记对应的词本身				
				//(1)为词性标记的时候，头节点为词性标记下的词语
				if(node.getChildren().size() == 1 && node.getChildren().get(0).getChildren().size() == 0){
					node.setHeadWords(node.getChildren().get(0).getNodeName());
					node.setHeadWordsPos(node.getNodeName());
				//(2)为非终结符，且不是词性标记的时候，由规则推出
				}else if(!node.isLeaf()){
					node.setHeadWords(aghw.extractHeadWords(node, HeadWordsRuleSet.getNormalRuleSet(), HeadWordsRuleSet.getSpecialRuleSet()).split("_")[0]);
					node.setHeadWordsPos(aghw.extractHeadWords(node, HeadWordsRuleSet.getNormalRuleSet(), HeadWordsRuleSet.getSpecialRuleSet()).split("_")[1]);
				}
				tree.push(node);
			}
		}
		SRLHeadTreeNode treeStruct = tree.pop();
        return treeStruct;
	}
}
