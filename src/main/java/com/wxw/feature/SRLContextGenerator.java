package com.wxw.feature;

import com.wxw.tree.HeadTreeNode;
import opennlp.tools.util.BeamSearchContextGenerator;

/**
 * 生成上下文特征接口
 * @author 王馨苇
 *
 */
public interface SRLContextGenerator extends BeamSearchContextGenerator<HeadTreeNode>{
	
	/**
	 * 为测试语料生成上下文特征
	 * @param i 当前位置
	 * @param roleTree 以谓词和论元为根的树数组
	 * @param semanticinfo 语义角色信息
	 * @param labelinfo 标记信息
	 * @return
	 */
	String[] getContext(int i, HeadTreeNode[] roleTree, String[] semanticinfo, Object[] labelinfo);
}
