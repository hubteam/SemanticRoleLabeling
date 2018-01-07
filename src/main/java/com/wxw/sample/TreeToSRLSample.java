package com.wxw.sample;

import java.util.ArrayList;
import java.util.List;

import com.wxw.tool.IsFunctionLabelTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.TreeNode;
import com.wxw.tree.TreeToHeadTree;

public class TreeToSRLSample {
	private List<String> semanticinfo = new ArrayList<>();
	private List<String> labelinfo = new ArrayList<>();
	private List<HeadTreeNode> listtree = new ArrayList<>();
	private TreeToHeadTree toheadtree = new TreeToHeadTree();

	/**
	 * 将树转换成语义角色标注所需的样本类格式
	 * @param tree 带头结点的语义角色树
	 * @param semanticRole 语义角色标注信息
	 * @return
	 */
	public SRLSample<HeadTreeNode> getSample(TreeNode tree, String semanticRole){

		HeadTreeNode headtree = toheadtree.treeToHeadTree(tree);
		String[] roles = semanticRole.split(" ");
		//加入动词的下标和动词
		semanticinfo.add(roles[2]);
		semanticinfo.add(getPredicate(headtree, semanticRole));
		//加入动词的描述，比如主动被动等五个信息
		semanticinfo.add(roles[5]);
		//加入以当前论元或者谓词作为根节点的树，和语义标记信息
		addLabelInfo(headtree,semanticRole);
		return new SRLSample<HeadTreeNode>(tree, listtree,semanticinfo,labelinfo);
	}

	
	/**
	 * 加入谓词本身，由argument作为根节点的树，argument标记信息
	 * @param tree 带头结点的语义角色树
	 * @param semanticRole 语义角色标注信息
	 */
	private String getPredicate(HeadTreeNode tree,String semanticRole){
		String predicate = "";
		String[] roles = semanticRole.split(" ");
		for (int i = 6; i < roles.length; i++) {
			//拆开为argument下标和语义标记部分
			String[] digitandrole = roles[i].split("-");
			//处理语义角色部分
			String role = getRole(digitandrole);
			//处理argument部分   7:3*28:0-ARG1   0:1,1:1*5:1*6:0-ARG1
			if(role.equals("rel")){
				String[] digits = digitandrole[0].split("\\*");
				//处理,隔开的部分
				String[] comma = digits[0].split(",");
				for (int j = 0; j < comma.length; j++) {
					String[] digit = comma[j].split(":");
					int begin = Integer.parseInt(digit[0]);
					if(j == comma.length - 1){
						predicate += getVerbs(tree,begin);
					}else{
						predicate += getVerbs(tree,begin)+"_";
					}					
				}
				for (int j = 1; j < digits.length; j++) {
					String[] digit = digits[j].split(":");
					int begin = Integer.parseInt(digit[0]);
					if(j == digits.length - 1){
						predicate += getVerbs(tree,begin);
					}else{
						predicate += getVerbs(tree,begin)+"_";
					}
				}
			}
		}
		return predicate;
	}
	
	/**
	 * 得到角色标注
	 * @param digitandrole 位置和标记数组
	 * @return
	 */
	private String getRole(String[] digitandrole){
		String role = digitandrole[1];		
		for (int j = 2; j < digitandrole.length; j++) {
			if(IsFunctionLabelTool.isFunction(digitandrole[j])){
				role += "-"+digitandrole[j];
			}				
		}
		return role;
	}
	
	/**
	 * 往列表中加入标记信息
	 * @param tree 带头结点的语义角色树
	 * @param semanticRole 语义角色标注信息
	 */
	private void addLabelInfo(HeadTreeNode tree,String semanticRole){
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
						addTreeAndLabels(tree,begin,up,role);
					}					
				}
				for (int j = 1; j < digits.length; j++) {
					String[] digit = digits[j].split(":");
					int begin = Integer.parseInt(digit[0]);
					int up = Integer.parseInt(digit[1]);
					if(begin == Integer.parseInt(roles[2])){
						addTreeAndLabels(tree,begin,up,role);
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
					addTreeAndLabels(tree,begin,up,role);
				}
				for (int j = 1; j < digits.length; j++) {
					String[] digit = digits[j].split(":");
					int begin = Integer.parseInt(digit[0]);
					int up = Integer.parseInt(digit[1]);
					addTreeAndLabels(tree,begin,up,role);
				}
			}
		}
	}
	
	/**
	 * 获得谓词
	 * @param tree 带头结点的语义角色树
	 * @param begin 谓词的下标索引
	 * @return
	 */
	private String getVerbs(HeadTreeNode tree,int begin){
		if(tree.getChildren().size() == 0 && tree.getWordIndex() == begin){
			return tree.getNodeName();
		}else{
			String str = "";
			for (TreeNode treenode : tree.getChildren()) {
				str += getVerbs((HeadTreeNode)treenode,begin);
			}
			return str;
		}
	}
	
	/**
	 * 向列表中加入树和标记信息标记role
	 * @param tree 带头结点的语义角色树
	 * @param begin 终结点的标记
	 * @param up 向上回溯的步数
	 * @param role 语义角色标记
	 */
	private void addTreeAndLabels(HeadTreeNode tree, int begin, int up, String role){
		if(tree.getChildren().size() == 0 && tree.getWordIndex() == begin){
			tree = (HeadTreeNode) tree.getParent();
			for (int i = 0; i < up; i++) {
				tree = (HeadTreeNode) tree.getParent();
			}
			if(role.equals("rel")){
				listtree.add(tree);
			}else{
				listtree.add(tree);
				semanticinfo.add(begin+"");
				labelinfo.add(role);
			}
		}else{
			for (TreeNode treenode : tree.getChildren()) {			
				addTreeAndLabels((HeadTreeNode)treenode,begin,up,role);
			}
		}
	}
}

