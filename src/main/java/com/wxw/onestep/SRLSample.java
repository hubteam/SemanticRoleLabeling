package com.wxw.onestep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.TreeNode;

/**
 * 样本类
 * @author 王馨苇
 *
 * @param <T>
 */
public class SRLSample <T extends TreeNode>{
	private TreeNode tree;//句法分析得到的树
	private List<TreeNodeWrapper<T>> argumenttree = new ArrayList<>();//论元为根的树
	private List<TreeNodeWrapper<T>> predicatetree = new ArrayList<>();//谓词为根的树
	private List<String> labelinfo = new ArrayList<String>();//角色标注论元标记信息
	private String[][] addtionalContext;
	
	public SRLSample(TreeNode tree, List<TreeNodeWrapper<T>> argumenttree,  List<TreeNodeWrapper<T>> predicatetree, List<String> labelinfo) {
		this(tree, argumenttree,predicatetree,labelinfo,null);
	}
	
	public SRLSample(TreeNode tree, List<TreeNodeWrapper<T>> argumenttree,  List<TreeNodeWrapper<T>> predicatetree, List<String> labelinfo,String[][] addtionalContext) {
		this.tree = tree;
		this.argumenttree = Collections.unmodifiableList(argumenttree);
		this.labelinfo = Collections.unmodifiableList(labelinfo);
        this.predicatetree = Collections.unmodifiableList(predicatetree);
        String[][] ac;
        if (addtionalContext != null) {
            ac = new String[addtionalContext.length][];
            for (int i = 0; i < addtionalContext.length; i++) {
                ac[i] = new String[addtionalContext[i].length];
                System.arraycopy(addtionalContext[i], 0, ac[i], 0,
                		addtionalContext[i].length);
            }
        } else {
            ac = null;
        }
        this.addtionalContext = ac;
	}
	
	/**
	 * 获取句法分析得到的树
	 * @return
	 */
	public TreeNode getTree(){
		return this.tree;
	}
	
	/**
	 * 获取以论元为树根的树
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TreeNodeWrapper<T>[] getArgumentTree(){
		return this.argumenttree.toArray(new TreeNodeWrapper[this.argumenttree.size()]);
	}
	
	/**
	 * 获取以谓词为树根的树
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TreeNodeWrapper<T>[] getPredicateTree(){
		return this.predicatetree.toArray(new TreeNodeWrapper[this.predicatetree.size()]);
	}
	
	/**
	 * 获取论元的标记的信息
	 * @return
	 */
	public String[] getLabelInfo(){
		return this.labelinfo.toArray(new String[this.labelinfo.size()]);
	}
	
	public String[][] getAdditionalContext(){
		return this.addtionalContext;
	}
}

