package com.wxw.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wxw.tree.TreeNode;

/**
 * 样本类
 * @author 王馨苇
 *
 * @param <T>
 */
public class SRLSample <T extends TreeNode>{
	private TreeNode tree;//句法分析得到的树
	private List<T> headTree;//子树序列
	private List<String> semanticinfo = new ArrayList<String>();//角色标注信息
	private List<String> labelinfo = new ArrayList<String>();//角色标注论元标记信息
	private String[][] addtionalContext;
	
	public SRLSample(TreeNode tree, List<T> headTree,  List<String> semanticinfo, List<String> labelinfo) {
		this(tree, headTree,semanticinfo,labelinfo,null);
	}
	
	public SRLSample(TreeNode tree, List<T> headTree,  List<String> semanticinfo, List<String> labelinfo,String[][] addtionalContext) {
		this.tree = tree;
		this.headTree = Collections.unmodifiableList(headTree);
		this.labelinfo = Collections.unmodifiableList(labelinfo);
        this.semanticinfo = Collections.unmodifiableList(semanticinfo);
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
	 * 获取以谓词和论元为树根的树
	 * @return
	 */
	public List<T> getHeadTree(){
		return this.headTree;
	}
	
	/**
	 * 获取语义角色标注信息
	 * @return
	 */
	public String[] getSemanticInfo(){
		return this.semanticinfo.toArray(new String[this.semanticinfo.size()]);
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

