package com.wxw.onestepparse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wxw.onestep.SRLSample;
import com.wxw.tool.RoleTool;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.TreeNode;

/**
 * 解析成样本类，没有剪枝操作
 * @author 王馨苇
 *
 */
public class SRLParseNormal extends AbstractParseStrategy<HeadTreeNode>{
	private List<String> labelinfo = new ArrayList<>();
	private List<TreeNodeWrapper<HeadTreeNode>> argumenttree = new ArrayList<>();
	private List<TreeNodeWrapper<HeadTreeNode>> predicatetree = new ArrayList<>();

	/**
	 * 将树转换成语义角色标注所需的样本类格式
	 * @param tree 带头结点的语义角色树
	 * @param semanticRole 语义角色标注信息
	 * @return
	 */
	public SRLSample<HeadTreeNode> toSample(HeadTreeNode headtree, String semanticRole){
		String[] roles = semanticRole.split(" ");
		//加入以当前论元或者谓词作为根节点的树，和语义标记信息
		addInfo(headtree,getRoleMap(semanticRole),Integer.parseInt(roles[2]));		
		return new SRLSample<HeadTreeNode>(headtree, argumenttree,predicatetree,labelinfo);
	}
	
	/**
	 * 往列表中加入标记信息
	 * @param tree 带头结点的语义角色树
	 * @param semanticRole 语义角色标注信息
	 * @param verbindex 动词的位置标记
	 */
	private void addInfo(HeadTreeNode tree,HashMap<Integer,RoleTool> map,int verbindex){
		boolean flag = true;
		if(tree.getChildren().size() != 0 && tree.getParent() != null){
			if(tree.getFlag() == true){
				if(map.containsKey(getLeftIndexAndDownSteps(tree)[0])){
					if(getLeftIndexAndDownSteps(tree)[1] == map.get(getLeftIndexAndDownSteps(tree)[0]).getUp()){
						flag = false;
						if(map.get(getLeftIndexAndDownSteps(tree)[0]).getRole().equals("rel")){
							if(getLeftIndexAndDownSteps(tree)[0] == verbindex){
								predicatetree.add(new TreeNodeWrapper<HeadTreeNode>(tree,getLeftIndexAndDownSteps(tree)[0]));
							}else{
								
							}
						}else{
							labelinfo.add(map.get(getLeftIndexAndDownSteps(tree)[0]).getRole());
							argumenttree.add(new TreeNodeWrapper<HeadTreeNode>(tree,getLeftIndexAndDownSteps(tree)[0]));
						}
					}
				}
				if(flag == true){
					labelinfo.add("NULL");
					argumenttree.add(new TreeNodeWrapper<HeadTreeNode>(tree,getLeftIndexAndDownSteps(tree)[0]));
				}
			}
		}
		for (TreeNode treenode : tree.getChildren()) {			
			addInfo((HeadTreeNode)treenode,map,verbindex);
		}
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
}

