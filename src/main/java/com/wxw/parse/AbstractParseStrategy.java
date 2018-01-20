package com.wxw.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wxw.stream.SRLSample;
import com.wxw.tool.IsCordinateStructure;
import com.wxw.tool.IsFunctionLabelTool;
import com.wxw.tool.IsPunctuationTool;
import com.wxw.tool.RoleTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.TreeNode;
import com.wxw.tree.TreeToHeadTree;

/**
 * 一步完成识别和分类的解析策略类
 * @author 王馨苇
 *
 */
public abstract class AbstractParseStrategy<T extends TreeNode> {

	private TreeToHeadTree toheadtree = new TreeToHeadTree();
	protected boolean containPredicateFlag;
	
	/**
	 * 是否有剪枝操作
	 * @return
	 */
	public abstract boolean hasPrePruning();
	
	/**
	 * 是否有头结点
	 * @return
	 */
	public abstract boolean hasHeadWord();
	
	/**
	 * 根据规则进行剪枝操作[剪掉标点符号和动词及动词父节点是并列结构的节点]
	 * @param tree 要剪枝的树[由谓词为根表示的树]
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T prePruning(T tree){
		int count = 0;
		List<Integer> list = new ArrayList<>();
		while(tree.getParent() != null){
			count++;
			list.add(tree.getIndex());
			tree = (T) tree.getParent();
			tree.setFlag(false);
			if(IsCordinateStructure.isCordinate(tree)){
				for (int i = 0; i < tree.getChildren().size(); i++) {
					tree.getChildren().get(i).setFlag(false);
				}
			}
		}
		
		for (int i = 0; i < count; i++) {
			tree = (T) tree.getChildren().get(list.get(list.size()-1-i));
		}
		return tree;
	}
	
	/**
	 * 得到以谓词为根节点的树
	 * @param tree 正常的一棵树
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<T> getPredicateTree(T tree,HashMap<Integer,RoleTool> map,int verbindex){
		List<T> list = new ArrayList<>();
		if(tree.getChildren().size() == 0){
			if(map.containsKey(tree.getWordIndex())){
				if(map.get(tree.getWordIndex()).getRole().equals("rel")){
					int begin = tree.getWordIndex();
					int up = map.get(begin).getUp();
					for (int i = 0; i <= up; i++) {
						tree = (T) tree.getParent();
					}
					if(verbindex == begin){
						list.add(tree);
					}
					for (int i = 0; i <= up; i++) {
						tree = (T) tree.getChildren().get(0);
					}
				}			
			}
		}else{
			for (TreeNode treenode : tree.getChildren()) {
				list.addAll(getPredicateTree((T)treenode,map,verbindex));
			}
		}
		return list;
	}
	
	/**
	 * 去除标点符号，将其flag设置为false
	 * @param tree 要处理的树
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T removePunctuation(T tree){
		if(tree.getChildren().size() == 0){
			if(IsPunctuationTool.isPunctuation(tree.getParent().getNodeName())){
				tree.getParent().setFlag(false);
				tree.setFlag(false);
			}
		}else{
			for (TreeNode treenode : tree.getChildren()) {			
				removePunctuation((T) treenode);
			}
		}
		return tree;
	}
	
	/**
	 * 将谓词的子节点设置为false
	 * @param tree 树
	 * @param map 语义角色的map
	 * @param verbindex 动词的标记
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T removePredicateSon(T tree,HashMap<Integer,RoleTool> map,int verbindex){
		if(tree.getChildren().size() == 0){
			if(map.containsKey(tree.getWordIndex())){
				if(map.get(tree.getWordIndex()).getRole().equals("rel")){
					int begin = tree.getWordIndex();
					int up = map.get(tree.getWordIndex()).getUp();
					for (int i = 0; i <= up; i++) {
						tree = (T) tree.getParent();
						tree.setFlag(false);
					}
					if(verbindex == begin){
						tree.setFlag(true);
					}
					for (int i = 0; i <= up; i++) {
						tree = (T) tree.getChildren().get(0);
					}
				}			
			}
		}else{
			for (TreeNode treenode : tree.getChildren()) {
				removePredicateSon((T)treenode,map,verbindex);
			}
		}
		return tree;
	}
	
	/**
	 * 将树转成训练要用的样本样式
	 * @param tree 树
	 * @param semanticRole 语义信息
	 * @return
	 */
	public abstract SRLSample<T> toSample(T tree, String semanticRole);
	
	/**
	 * 根据是否要进行剪枝，解析样本
	 * @param tree 树
	 * @param semanticRole 语义信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SRLSample<T> parse(TreeNode tree, String semanticRole){
		SRLSample<T> sample;
		String[] roles = semanticRole.split(" ");
		int verbindex = Integer.parseInt(roles[2]);
		if(hasHeadWord()){
			HeadTreeNode headtree = toheadtree.treeToHeadTree(tree);
			if(hasPrePruning()){
				sample = toSample(prePruning(getPredicateTree(removePredicateSon(removePunctuation((T) headtree),getRoleMap(semanticRole),verbindex),getRoleMap(semanticRole),verbindex).get(0)), semanticRole);
				sample.setPruning(true);
				return sample;
			}else{
				sample = toSample(removePredicateSon(removePunctuation((T) headtree),getRoleMap(semanticRole),verbindex), semanticRole);
				sample.setPruning(false);
				return sample;
			}
		}else{
			if(hasPrePruning()){
				sample = toSample(prePruning(getPredicateTree(removePredicateSon(removePunctuation((T) tree),getRoleMap(semanticRole),verbindex),getRoleMap(semanticRole),verbindex).get(0)), semanticRole);
				sample.setPruning(true);
				return sample;
			}else{
				sample = toSample(removePredicateSon(removePunctuation((T) tree),getRoleMap(semanticRole),verbindex), semanticRole);
				sample.setPruning(false);
				return sample;
			}
		}				
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
	

	/**
	 * 获取当前树最左端终结点的下标和记录得到下标走过的步数
	 * @param tree 树
	 * @return
	 */
	public int[] getLeftIndexAndDownSteps(HeadTreeNode tree){
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
	 * 判断一棵树的子节点中是否包含谓词
	 * @param tree 要判断的树
	 * @param verbindex 动词的下标
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean containsPredicate(T tree,int verbindex){
		if(tree.getWordIndex() == verbindex){			
			containPredicateFlag = true;
		}else{
			for (TreeNode treenode : tree.getChildren()) {			
				containsPredicate((T)treenode,verbindex);
			}
		}	
		return containPredicateFlag;
	}
}
