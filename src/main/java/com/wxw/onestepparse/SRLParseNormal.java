package com.wxw.onestepparse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wxw.onestep.SRLSample;
import com.wxw.tool.RoleTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.TreeNode;

public class SRLParseNormal extends AbstractParseStrategy<HeadTreeNode>{
	private List<String> semanticinfo = new ArrayList<>();
	private List<String> labelinfo = new ArrayList<>();
	private List<HeadTreeNode> listtree = new ArrayList<>();

	/**
	 * 将树转换成语义角色标注所需的样本类格式
	 * @param tree 带头结点的语义角色树
	 * @param semanticRole 语义角色标注信息
	 * @return
	 */
	public SRLSample<HeadTreeNode> toSample(HeadTreeNode headtree, String semanticRole){

		String[] roles = semanticRole.split(" ");
		//加入动词的下标和动词
		semanticinfo.add(roles[2]);
		//加入动词的描述，比如主动被动等五个信息
		semanticinfo.add(roles[5]);
		//加入以当前论元或者谓词作为根节点的树，和语义标记信息
		addInfo(headtree,getRoleMap(semanticRole));		
		return new SRLSample<HeadTreeNode>(headtree, listtree,semanticinfo,labelinfo);
	}
	
	/**
	 * 往列表中加入标记信息
	 * @param tree 带头结点的语义角色树
	 * @param semanticRole 语义角色标注信息
	 */
	private void addInfo(HeadTreeNode tree,HashMap<Integer,RoleTool> map){
		boolean flag = true;
		if(tree.getChildren().size() != 0 && tree.getParent() != null){
			if(tree.getFlag() == true){
				if(map.containsKey(getLeftIndexAndDownSteps(tree)[0])){
					if(getLeftIndexAndDownSteps(tree)[1] == map.get(getLeftIndexAndDownSteps(tree)[0]).getUp()){
						flag = false;
						if(map.get(getLeftIndexAndDownSteps(tree)[0]).getRole().equals("rel")){
							listtree.add(0,tree);
							semanticinfo.add(1,tree.getChildren().get(0).getNodeName());
						}else{
							listtree.add(tree);
							semanticinfo.add(getLeftIndexAndDownSteps(tree)[0]+"");
							labelinfo.add(map.get(getLeftIndexAndDownSteps(tree)[0]).getRole());
						}
					}
				}
				if(flag == true){
					listtree.add(tree);
					semanticinfo.add(getLeftIndexAndDownSteps(tree)[0]+"");
					labelinfo.add("NULL");
				}
			}
		}
		for (TreeNode treenode : tree.getChildren()) {			
			addInfo((HeadTreeNode)treenode,map);
		}
	}

	/**
	 * 获取当前树最左端终结点的下标和记录得到下标走过的步数
	 * @param tree 树
	 * @return
	 */
	private int[] getLeftIndexAndDownSteps(HeadTreeNode tree){
		int step = -1;
		int[] leftanddown = new int[2];
		while(tree.getChildren().size() != 0){
			tree = tree.getChildren().get(0);
			step++;
		}
		leftanddown[0] = tree.getWordIndex();
		leftanddown[1] = step;
		return leftanddown;
	}

	/**
	 * 决定是否使用剪枝的处理
	 */
	@Override
	public boolean hasPrePruning() {
		return false;
	}


	@Override
	public boolean hasHeadWord() {
		return true;
	}

	@Override
	public HeadTreeNode prePruning(HeadTreeNode tree, int verbindex) {
		return null;
	}
}

