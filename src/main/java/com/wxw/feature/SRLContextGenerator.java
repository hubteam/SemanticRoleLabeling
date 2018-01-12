package com.wxw.feature;

import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;
import opennlp.tools.util.BeamSearchContextGenerator;

/**
 * 生成上下文特征接口
 * @author 王馨苇
 *
 */
public interface SRLContextGenerator extends BeamSearchContextGenerator<TreeNodeWrapper<HeadTreeNode>>{
	
	/**
	 * 为测试语料生成上下文特征
	 * @param i 当前位置
	 * @param argumenttree 以论元为根的树数组
	 * @param predicatetree 以谓词为根的树
	 * @param labelinfo 标记信息
	 * @return
	 */
	String[] getContext(int i, TreeNodeWrapper<HeadTreeNode>[] argumenttree, String[] labelinfo, Object[] predicatetree);
}
