package com.wxw.tool;

import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.TreeNode;

import opennlp.tools.util.Sequence;

/**
 * 对得到的结果进行后处理
 * @author 王馨苇
 *
 */
public class PostTreatTool {

	/**
	 * 当遇到嵌套结构时候，保留概率大的
	 * @param result 结果序列
	 * @return
	 */
	public static String[] postTreat(TreeNodeWrapper<HeadTreeNode>[] tree,Sequence result,int length){
		String[] null_101 = result.getOutcomes().toArray(new String[result.getOutcomes().size()]);
		String[] lastres = NULL_1012NULL(null_101);
		for (int i = 0; i < length; i++) {
			if(!lastres[i].equals("NULL")){
				double max = result.getProbs()[i];
				int record = -1;
				for (int j = i+1; j < i+getSonTreeCount(tree[i].getTree()) && j < length; j++) {
					if((result.getProbs()[j] > max) && (!lastres[j].equals("NULL"))){
						max = result.getProbs()[j];
						record = j;
					}
				}
				if(record != -1){
					for (int j = i; j < i+getSonTreeCount(tree[i].getTree()) && j < length; j++) {
						if(j == record){
							
						}else{
							lastres[j] = "NULL";
						}
					}
				}			
				i = i+getSonTreeCount(tree[i].getTree())-1;
			}
		}
		return lastres;
	}
	
	public static int getSonTreeCount(HeadTreeNode headtree){
		int count = 0;
		if(headtree.getChildren().size() == 0){
			
		}else{
			if(headtree.getFlag() == true){
				count++;
			}
			for (TreeNode treenode: headtree.getChildren()) {
				count += getSonTreeCount((HeadTreeNode)treenode);
			}	
		}
		return count;
	}
	
	public static String[] NULL_1012NULL(String[] str){
		String[] res = new String[str.length];
		for (int i = 0; i < str.length; i++) {
			if(str[i].contains("NULL")){
				res[i] = "NULL";
			}else{
				res[i] = str[i];
			}
		}
		return res;
	}
}
