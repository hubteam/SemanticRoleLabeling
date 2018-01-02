package com.wxw.headwords;

import java.util.HashMap;
import java.util.List;

import com.wxw.headwords.AbsractGenerateHeadWords;
import com.wxw.headwords.Rule;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.SemanticRoleHeadTree;

/**
 * 生成头结点
 * @author 王馨苇
 *
 */
public class ConcreteGenerateHeadWords extends AbsractGenerateHeadWords{

	@Override
	public String generateHeadWordsForCordinator(SemanticRoleHeadTree node) {
		//有些非终端节点需要进行处理，因为它可能是NP-SBJ的格式，我只需要拿NP的部分进行匹配操作
		String parentNonTerminal = node.getNodeName().split("-")[0];
		//处理X-X CC X的情况
		boolean flag = false;
		int record = -1;
		//先判断是不是这种结构
		for (int i = 0; i < node.getChildren().size() - 2; i++) {
			if(node.getChildren().get(i).getNodeName().split("-")[0].equals(parentNonTerminal) &&
					node.getChildren().get(i+1).getNodeName().equals("CC") &&
					node.getChildren().get(i+2).getNodeName().split("-")[0].equals(parentNonTerminal)){
				flag = true;
				record = i;
				break;
			}
		}
		if(flag == true && record != -1){
			return node.getChildren().get(record).getHeadWords();
		}
		return null;
	}

	@Override
	public String generateHeadWordsForSpecialRules(SemanticRoleHeadTree node, HashMap<String, List<Rule>> specialRules) {
		String currNodeName = node.getNodeName();
		//如果最后一个是POS，返回最后一个
		if(node.getChildren().get(node.getChildren().size() - 1).getNodeName().equals("POS")){
			return node.getChildren().get(node.getChildren().size() - 1).getHeadWords();
		}
		if(specialRules.containsKey(currNodeName)){
			for (int k = 0; k < specialRules.get(currNodeName).size(); k++) {
				if(specialRules.get(currNodeName).get(k).getDirection().equals("left")){
					//用所有的子节点从左向右匹配规则中每一个
					for (int i = 0; i < specialRules.get(currNodeName).get(k).getRightRules().size(); i++) {
						for (int j = 0; j < node.getChildren().size(); j++) {
							if(node.getChildren().get(j).getNodeName().equals(specialRules.get(currNodeName).get(k).getRightRules().get(i))){
								return node.getChildren().get(j).getHeadWords();
							}
						}
					}
				}else if(specialRules.get(currNodeName).get(k).getDirection().equals("right")){
					for (int i = specialRules.get(currNodeName).get(k).getRightRules().size() -1 ; i >= 0; i--) {
						for (int j = 0; j < node.getChildren().size(); j++) {
							if(node.getChildren().get(j).getNodeName().equals(specialRules.get(currNodeName).get(k).getRightRules().get(i))){
								return node.getChildren().get(j).getHeadWords();
							}
						}
					}
				}
			}
			//否则返回最后一个		
			return node.getChildren().get(node.getChildren().size() - 1).getHeadWords();
		}else{
			return null;
		}
	}
}
