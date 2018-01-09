package com.wxw.headword;

import java.util.HashMap;
import java.util.List;

import com.wxw.headwords.AbsractGenerateHeadWords;
import com.wxw.headwords.Rule;
import com.wxw.tree.SRLHeadTreeNode;

/**
 * 根据规则生成头结点
 * @author 王馨苇
 *
 */
public class ConcreteGenerateHeadWords extends AbsractGenerateHeadWords<SRLHeadTreeNode>{

	/**
	 * 为并列结构生成头结点：头结点_头结点词性
	 * @param node 子节点带头结点，父节点不带头结点的树
	 * @return
	 */
	@Override
	public String generateHeadWordsForCordinator(SRLHeadTreeNode node) {
		//有些非终端节点需要进行处理，因为它可能是NP-SBJ的格式，我只需要拿NP的部分进行匹配操作
		String parentNonTerminal = node.getNodeName().split("-")[0];
		//处理X-X CC X的情况
		boolean flag = false;
		int record = -1;
		//先判断是不是这种结构
		for (int i = 0; i < node.getChildren().size() - 2; i++) {
			if(node.getChildren().get(i).getNodeName().split("-")[0].equals(parentNonTerminal) &&
					node.getChildren().get(i+1).getNodeName().split("-")[0].equals("CC") &&
					node.getChildren().get(i+2).getNodeName().split("-")[0].equals(parentNonTerminal)){
				flag = true;
				record = i;
				break;
			}
		}
		if(flag == true && record != -1){
			return node.getChildren().get(record).getHeadWords()+"_"+node.getChildren().get(record).getHeadWordsPos();
		}
		return null;
	}

	/**
	 * 为特殊规则生成头结点
	 * @param node 子节点带头结点，父节点不带头结点的树
	 * @param specialRules 生成头结点的特殊规则
	 * @return
	 */
	@Override
	public String generateHeadWordsForSpecialRules(SRLHeadTreeNode node, HashMap<String, List<Rule>> specialRules) {
		String currNodeName = node.getNodeName().split("-")[0];
		//如果最后一个是POS，返回最后一个
		if(node.getChildren().get(node.getChildren().size() - 1).getNodeName().split("-")[0].equals("POS")){
			return node.getChildren().get(node.getChildren().size() - 1).getHeadWords()+"_"+node.getChildren().get(node.getChildren().size() - 1).getHeadWordsPos();
		}
		if(specialRules.containsKey(currNodeName)){
			for (int k = 0; k < specialRules.get(currNodeName).size(); k++) {
				if(specialRules.get(currNodeName).get(k).getDirection().equals("left")){
					//用所有的子节点从左向右匹配规则中每一个
					for (int i = 0; i < specialRules.get(currNodeName).get(k).getRightRules().size(); i++) {
						for (int j = 0; j < node.getChildren().size(); j++) {
							if(node.getChildren().get(j).getNodeName().split("-")[0].equals(specialRules.get(currNodeName).get(k).getRightRules().get(i))){
								return node.getChildren().get(j).getHeadWords()+"_"+node.getChildren().get(j).getHeadWordsPos();
							}
						}
					}
				}else if(specialRules.get(currNodeName).get(k).getDirection().equals("right")){
					for (int i = specialRules.get(currNodeName).get(k).getRightRules().size() -1 ; i >= 0; i--) {
						for (int j = 0; j < node.getChildren().size(); j++) {
							if(node.getChildren().get(j).getNodeName().split("-")[0].equals(specialRules.get(currNodeName).get(k).getRightRules().get(i))){
								return node.getChildren().get(j).getHeadWords()+"_"+node.getChildren().get(j).getHeadWordsPos();
							}
						}
					}
				}
			}
			//否则返回最后一个		
			return node.getChildren().get(node.getChildren().size() - 1).getHeadWords()+"_"+node.getChildren().get(node.getChildren().size() - 1).getHeadWordsPos();
		}else{
			return null;
		}
	}

	/**
	 * 为一般规则生成头结点
	 * @param node 子节点带头结点，父节点不带头结点的树
	 * @param normalRules 生成头结点的一般规则
	 * @return
	 */
	@Override
	public String generateHeadWordsForNormalRules(SRLHeadTreeNode node, HashMap<String, Rule> normalRules) {
		String currentNodeName = node.getNodeName().split("-")[0];
		if(normalRules.containsKey(currentNodeName)){
			if(normalRules.get(currentNodeName).getDirection().equals("left")){
				//用所有的子节点从左向右匹配规则中每一个
				for (int i = 0; i < normalRules.get(currentNodeName).getRightRules().size(); i++) {
					for (int j = 0; j < node.getChildren().size(); j++) {
						if(node.getChildren().get(j).getNodeName().split("-")[0].equals(normalRules.get(currentNodeName).getRightRules().get(i))){
							return node.getChildren().get(j).getHeadWords()+"_"+node.getChildren().get(j).getHeadWordsPos();
						}
					}
				}
			}else if(normalRules.get(currentNodeName).getDirection().equals("right")){
				for (int i = normalRules.get(currentNodeName).getRightRules().size() -1 ; i >= 0; i--) {
					for (int j = 0; j < node.getChildren().size(); j++) {
						if(node.getChildren().get(j).getNodeName().split("-")[0].equals(normalRules.get(currentNodeName).getRightRules().get(i))){
							return node.getChildren().get(j).getHeadWords()+"_"+node.getChildren().get(j).getHeadWordsPos();
						}
					}
				}
			}
			//如果所有的规则都没有匹配，返回最左边的第一个
			return node.getChildren().get(0).getHeadWords()+"_"+node.getChildren().get(0).getHeadWordsPos();
		}else{
			return node.getChildren().get(0).getHeadWords()+"_"+node.getChildren().get(0).getHeadWordsPos();
		}
	}
}
