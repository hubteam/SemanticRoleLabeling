package com.wxw.headwords;

import java.util.HashMap;
import java.util.List;

import com.wxw.headwords.Rule;
import com.wxw.tree.SemanticRoleHeadTree;

/**
 * 生成头结点的模板类【模板设计模式】
 * @author 王馨苇
 *
 */
public abstract class AbsractGenerateHeadWords {
	
	/**
	 * 为并列结构生成头结点
	 * @param node
	 * @return
	 */
	public abstract String generateHeadWordsForCordinator(SemanticRoleHeadTree node);

	/**
	 * 为特殊规则生成头结点
	 * @param node
	 * @param specialRules
	 * @return
	 */
	public abstract String generateHeadWordsForSpecialRules(SemanticRoleHeadTree node,HashMap<String,List<Rule>> specialRules);
	
	/**
	 * 为一般规则生成头结点
	 * @param node
	 * @param normalRules
	 * @return
	 */
	public String generateHeadWordsForNormalRules(SemanticRoleHeadTree node,HashMap<String,Rule> normalRules){
		String currentNodeName = node.getNodeName();
		if(normalRules.containsKey(currentNodeName)){
			if(normalRules.get(currentNodeName).getDirection().equals("left")){
				//用所有的子节点从左向右匹配规则中每一个
				for (int i = 0; i < normalRules.get(currentNodeName).getRightRules().size(); i++) {
					for (int j = 0; j < node.getChildren().size(); j++) {
						if(node.getChildren().get(j).getNodeName().equals(normalRules.get(currentNodeName).getRightRules().get(i))){
							return node.getChildren().get(j).getHeadWords();
						}
					}
				}
			}else if(normalRules.get(currentNodeName).getDirection().equals("right")){
				for (int i = normalRules.get(currentNodeName).getRightRules().size() -1 ; i >= 0; i--) {
					for (int j = 0; j < node.getChildren().size(); j++) {
						if(node.getChildren().get(j).getNodeName().equals(normalRules.get(currentNodeName).getRightRules().get(i))){
							return node.getChildren().get(j).getHeadWords();
						}
					}
				}
			}
			//如果所有的规则都没有匹配，返回最左边的第一个
			return node.getChildren().get(0).getHeadWords();
		}else{
			return null;
		}
	}

	/**
	 * 提取头结点【自底向上生成头结点】
	 * @param node 带头结点的树【树的子树都是有头结点的，给根节点生成头结点】
	 * @param rules
	 * @return
	 */
	public String extractHeadWords(SemanticRoleHeadTree node, HashMap<String,Rule> normalRules,HashMap<String,List<Rule>> specialRules){
		String headWords = null;
		headWords = generateHeadWordsForCordinator(node);
		
		if(headWords == null && normalRules != null){			
			headWords = generateHeadWordsForNormalRules(node,normalRules);
		}
		
		if(headWords == null && specialRules != null){
			headWords = generateHeadWordsForSpecialRules(node,specialRules);
		}
		return headWords;
	}
}
