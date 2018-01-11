package com.wxw.tool;

import com.wxw.tree.TreeNode;

/**
 * 对树进行预处理的工具类
 * @author 王馨苇
 *
 */
public class PreTreatTool {

	/**
	 * 去掉空节点和标记-后面的部分
	 * @param node 要处理的树
	 * @return
	 */
	public static void preTreat(TreeNode node){
		if(node.getChildren().size() != 0){
			for (TreeNode treenode:node.getChildren()) {
				preTreat(treenode);
			}
		}		
		if(!node.isLeaf()){
			if(node.getNodeName().contains("NONE")){
				//该节点的父节点只有空节点一个孩子
				if(node.getParent().getChildren().size() > 1){
					//将NONE和NONE的子节点标记位false
					node.setFlag(false);
					node.getChildren().get(0).setFlag(false);	
					//(SBAR(-NONE- 0)(S(-NONE- *T*-1)))
					if(node.getParent().getChildren().size() == 2){
//						node.getParent().setFlag(false);
						if(node.getParent().getChildren().get(1).getChildren().size() == 1){
							if(node.getParent().getChildren().get(1).getChildren().get(0).getNodeName().contains("NONE")){
								node.getParent().setFlag(false);
								node.getParent().getChildren().get(1).setFlag(false);
								node.getParent().getChildren().get(1).getChildren().get(0).setFlag(false);
								node.getParent().getChildren().get(1).getChildren().get(0).getChildren().get(0).setFlag(false);
							}
						}
					}
				}else if(node.getParent().getChildren().size() == 1){
					//将NONE和NONE的子节点和父节点标记位false
					node.setFlag(false);
					node.getChildren().get(0).setFlag(false);
					node.getParent().setFlag(false);

				}
			}else if(node.getNodeName().contains("-")){
				if(!node.getNodeName().equals("-LRB-") && !(node.getNodeName().equals("-RRB-"))){
					node.setNewName(node.getNodeName().split("-")[0]);
				}
			}else if(IsDigitTool.isDigit(node.getNodeName().charAt(node.getNodeName().length()-1))){
				if(IsDigitTool.isDigit(node.getNodeName().charAt(node.getNodeName().length()-2))){
					node.setNewName(node.getNodeName().substring(0, node.getNodeName().length()-3));
				}else{
					node.setNewName(node.getNodeName().substring(0, node.getNodeName().length()-2));
				}
			}
		}
	}
}
