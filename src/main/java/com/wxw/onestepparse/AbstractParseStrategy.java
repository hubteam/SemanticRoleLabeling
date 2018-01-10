package com.wxw.onestepparse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wxw.onestep.SRLSample;
import com.wxw.tool.IsFunctionLabelTool;
import com.wxw.tool.RoleTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.TreeNode;

/**
 * 一步完成识别和分类的解析策略类
 * @author 王馨苇
 *
 */
public abstract class AbstractParseStrategy<T extends TreeNode> {

	/**
	 * 是否有剪枝操作
	 * @return
	 */
	public abstract boolean hasPrePruning();
	
	/**
	 * 根据规则进行剪枝操作
	 * @param tree
	 * @return
	 */
	public TreeNode prePruning(TreeNode tree,int verbindex){
		if(tree.getChildren().size() != 0){
			if(getAllLeafIndex(tree).contains(verbindex)){
				tree.setFlag(false);
			}
		}else{
			for (TreeNode treenode : tree.getChildren()) {			
				prePruning(treenode,verbindex);
			}
		}
		return tree;
	}
	
	private List<Integer> getAllLeafIndex(TreeNode tree){
		List<Integer> list = new ArrayList<>();
		if(tree.getChildren().size() == 0){
			list.add(tree.getWordIndex());
		}else{
			for (TreeNode treenode : tree.getChildren()) {			
				list.addAll(getAllLeafIndex(treenode));
			}
		}
		return list;
	}
	
	/**
	 * 将树转成训练要用的样本样式
	 * @param tree 树
	 * @param semanticRole 语义信息
	 * @return
	 */
	public abstract SRLSample<T> toSample(TreeNode tree, String semanticRole);
	
	/**
	 * 根据是否要进行剪枝，解析样本
	 * @param tree 树
	 * @param semanticRole 语义信息
	 * @return
	 */
	public SRLSample<T> parse(TreeNode tree, String semanticRole){
		if(hasPrePruning()){
			tree = prePruning(tree,Integer.parseInt(semanticRole.split(" ")[2]));
		}
		return toSample(tree, semanticRole);
	}
	
	/**
	 * 得到语义信息的hash表，键是终结点的标记，值为上溯的步数以及标记
	 * @param semanticRole 语义角色标注信息
	 * @return
	 */
	public HashMap<Integer,RoleTool> getRoleMap(String semanticRole){
		HashMap<Integer,RoleTool> map = new HashMap<>();
		String[] roles = semanticRole.split(" ");
		//对谓词的处理
		for (int i = 6; i < roles.length; i++) {
			//拆开为argument下标和语义标记部分
			String[] digitandrole = roles[i].split("-");
			//处理语义角色部分
			String role = getRole(digitandrole);
			//加入以论元作为根节点的树
			if(role.equals("rel")){
				String[] digits = digitandrole[0].split("\\*");
				//处理,隔开的部分
				String[] comma = digits[0].split(",");
				for (int j = 0; j < comma.length; j++) {
					String[] digit = comma[j].split(":");
					int begin = Integer.parseInt(digit[0]);
					int up = Integer.parseInt(digit[1]);
					if(begin == Integer.parseInt(roles[2])){
						map.put(begin, new RoleTool(-1,up,role));
					}					
				}
				for (int j = 1; j < digits.length; j++) {
					String[] digit = digits[j].split(":");
					int begin = Integer.parseInt(digit[0]);
					int up = Integer.parseInt(digit[1]);
					if(begin == Integer.parseInt(roles[2])){
						map.put(begin, new RoleTool(-1,up,role));
					}
				}
			}
		}
		//处理论元的部分
		for (int i = 6; i < roles.length; i++) {
			//拆开为argument下标和语义标记部分
			String[] digitandrole = roles[i].split("-");
			//处理语义角色部分
			String role = getRole(digitandrole);
			//加入以论元作为根节点的树
			if(!role.equals("rel")){
				String[] digits = digitandrole[0].split("\\*");
				//处理,隔开的部分
				String[] comma = digits[0].split(",");
				for (int j = 0; j < comma.length; j++) {
					String[] digit = comma[j].split(":");
					int begin = Integer.parseInt(digit[0]);
					int up = Integer.parseInt(digit[1]);
					map.put(begin, new RoleTool(-1,up,role));
				}
				for (int j = 1; j < digits.length; j++) {
					String[] digit = digits[j].split(":");
					int begin = Integer.parseInt(digit[0]);
					int up = Integer.parseInt(digit[1]);
					map.put(begin, new RoleTool(-1,up,role));
				}
			}
		}
		return map;
	}
	
	
	/**
	 * 得到角色标注
	 * @param digitandrole 位置和标记数组
	 * @return
	 */
	public String getRole(String[] digitandrole){
		String role = digitandrole[1];		
		for (int j = 2; j < digitandrole.length; j++) {
			if(IsFunctionLabelTool.isFunction(digitandrole[j])){
				role += "-"+digitandrole[j];
			}				
		}
		return role;
	}
}
