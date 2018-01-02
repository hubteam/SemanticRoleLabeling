package com.wxw.tree;

import java.util.List;
import java.util.Stack;

import com.wxw.headwords.AbsractGenerateHeadWords;
import com.wxw.headwords.ConcreteGenerateHeadWords;
import com.wxw.headwords.HeadWordsRuleSet;

/**
 * 将语义角色标注树转成带头结点的语义角色标注树
 * @author 王馨苇
 *
 */
public class SRLTreeToSRLHeadTree {

	private AbsractGenerateHeadWords aghw = new ConcreteGenerateHeadWords(); 
	
	/**
	 * 给语义角色标注树加上头结点
	 * @param tree 没有头结点的语义角色树
	 * @return
	 */
	public SemanticRoleHeadTree srlTreeToSRLHeadTree(SemanticRoleTree treenode){
		String strtree = "("+treenode.toRoleString()+")";
		PhraseGenerateTree pgt = new PhraseGenerateTree();
		String format = pgt.format(strtree);
		List<String> parts = pgt.stringToList(format);
		int wordindex = 0;
		Stack<SemanticRoleHeadTree> tree = new Stack<SemanticRoleHeadTree>();
		for (int i = 0; i < parts.size(); i++) {
			if(!parts.get(i).equals(")") && !parts.get(i).equals(" ")){
				SemanticRoleHeadTree tn = new SemanticRoleHeadTree(parts.get(i));
				tree.push(tn);				
			}else if(parts.get(i).equals(" ")){
				
			}else if(parts.get(i).equals(")")){
				Stack<SemanticRoleHeadTree> temp = new Stack<SemanticRoleHeadTree>();
				while(!tree.peek().getNodeName().equals("(")){
					if(!tree.peek().getNodeName().equals(" ")){
						temp.push(tree.pop());						
					}
				}
				tree.pop();
				SemanticRoleHeadTree node = temp.pop();
				if(node.getNodeName().contains("{")){
					node.setSemanticRole(node.getNodeName().substring(node.getNodeName().indexOf("{")+1, node.getNodeName().length()-1));
				}
				while(!temp.isEmpty()){		
					temp.peek().setParent(node);
					//加入终结点的标记
					if(temp.peek().getChildren().size() == 0){
						SemanticRoleHeadTree wordindexnode = temp.peek();
						wordindexnode.setWordIndex(wordindex++);
						node.addChild(wordindexnode);
					}else{
						node.addChild(temp.peek());
					}
					//加入语义标记
					if(temp.peek().getNodeName().contains("{")){
						SemanticRoleHeadTree rolenode = temp.peek();
						rolenode.setSemanticRole(temp.peek().getNodeName().substring(temp.peek().getNodeName().indexOf("{")+1, temp.peek().getNodeName().length()-1));
						node.addChild(rolenode);
					}else{
						node.addChild(temp.peek());
					}
					temp.pop();
				}
				//设置头节点的部分
				//为每一个非终结符，且不是词性标记的设置头节点
				//对于词性标记的头节点就是词性标记对应的词本身				
				//(1)为词性标记的时候，头节点为词性标记下的词语
				if(node.getChildren().size() == 1 && node.getChildren().get(0).getChildren().size() == 0){
					node.setHeadWords(node.getChildren().get(0).getNodeName());
				//(2)为非终结符，且不是词性标记的时候，由规则推出
				}else if(!node.isLeaf()){
					node.setHeadWords(aghw.extractHeadWords(node, HeadWordsRuleSet.getNormalRuleSet(), HeadWordsRuleSet.getSpecialRuleSet()));
				}
				tree.push(node);
			}
		}
		SemanticRoleHeadTree treeStruct = tree.pop();
        return treeStruct;
	}
}
