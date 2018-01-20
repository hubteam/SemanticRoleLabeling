package com.wxw.validate;

import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.util.SequenceValidator;

/**
 * 校验
 * @author 王馨苇
 *
 */
public class DefaultSRLSequenceValidator implements SequenceValidator<TreeNodeWrapper<HeadTreeNode>>{

	@Override
	public boolean validSequence(int arg0, TreeNodeWrapper<HeadTreeNode>[] arg1, String[] arg2, String arg3) {
		return true;
	}

}
