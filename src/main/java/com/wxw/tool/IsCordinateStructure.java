package com.wxw.tool;

import com.wxw.tree.TreeNode;

/**
 * 判断当前子树是不是并列结构
 * @author 王馨苇
 *
 */
public class IsCordinateStructure {

	/**
	 * 判断是否是并列结构
	 * @param tree 要判断的树
	 * @return
	 */
	public static boolean isCordinate(TreeNode tree){
		// X CC X
		// X , CC X [.,but.]
		// X , X
		// X : X [. ; .]
		if(tree.getChildren().get(tree.getChildren().size()-1).equals(".")){
			if(tree.getChildren().size() == 5){
				if(tree.getChildren().get(0).equals(tree.getChildren().get(3)) &&
						tree.getChildren().get(1).equals(",") && tree.getChildren().get(2).equals("CC")){
					return true;
				}
			}else if(tree.getChildren().size() == 4){
				if(tree.getChildren().get(0).equals(tree.getChildren().get(2))){
					if(tree.getChildren().get(1).equals("CC") || tree.getChildren().get(1).equals(",") || tree.getChildren().get(1).equals(":")){
						return true;
					}
				}
			}
		}else{
			if(tree.getChildren().size() == 4){
				if(tree.getChildren().get(0).equals(tree.getChildren().get(3)) &&
						tree.getChildren().get(1).equals(",") && tree.getChildren().get(2).equals("CC")){
					return true;
				}
			}else if(tree.getChildren().size() == 3){
				if(tree.getChildren().get(0).equals(tree.getChildren().get(2))){
					if(tree.getChildren().get(1).equals("CC") || tree.getChildren().get(1).equals(",") || tree.getChildren().get(1).equals(":")){
						return true;
					}
				}
			}
		}
		return false;
	}
}
