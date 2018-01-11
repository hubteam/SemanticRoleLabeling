package com.wxw.tool;

import java.util.List;

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
	public static String[] postTreat(HeadTreeNode[] tree,Sequence result){
		String[] lastres = result.getOutcomes().toArray(new String[result.getOutcomes().size()]);
		for (int i = 0; i < result.getOutcomes().size(); i++) {
			if(!result.getOutcomes().get(i).equals("NULL")){
				double max = result.getProbs()[i];
				int record = -1;
				for (int j = i+1; j < i+getSonTreeCount(tree[i+1]); j++) {
					if(result.getProbs()[j] > max){
						max = result.getProbs()[j];
						record = j;
					}
				}
				for (int j = i; j < i+getSonTreeCount(tree[i+1]); j++) {
					if(j == record){
						
					}else{
						lastres[j] = "NULL";
					}
				}			
				i = i+getSonTreeCount(tree[i+1])-1;
			}
			
		}
		return null;
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
}
