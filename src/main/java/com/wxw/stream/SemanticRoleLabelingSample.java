package com.wxw.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wxw.tree.SRLHeadTreeNode;
import com.wxw.tree.SRLTreeNode;

/**
 * 语义角色标注的样本类格式
 * @author 王馨苇
 *
 * @param <T> 语义角色标注树及其子类
 */
public class SemanticRoleLabelingSample<T extends SRLTreeNode> {
	
	private List<T> roleTree;//带语义角色的子树序列
	private List<String> semanticinfo = new ArrayList<String>();//角色标注信息
	private String[][] addtionalContext;
	
	public SemanticRoleLabelingSample(List<T> roleTree, List<String> semanticinfo) {
		this(roleTree,semanticinfo,null);
	}
	
	public SemanticRoleLabelingSample(List<T> roleTree, List<String> semanticinfo,String[][] addtionalContext) {
		this.roleTree = Collections.unmodifiableList(roleTree);
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
	
	public List<T> getRoleTree(){
		return this.roleTree;
	}
	
	public List<String> getSemanticInfo(){
		return this.semanticinfo;
	}
	
	public String[][] getAdditionalContext(){
		return this.addtionalContext;
	}
}
