package com.wxw.onestep;

import com.wxw.tree.HeadTreeNode;

import opennlp.tools.util.SequenceValidator;

public class DefaultSRLSequenceValidator implements SequenceValidator<HeadTreeNode>{

	@Override
	public boolean validSequence(int arg0, HeadTreeNode[] arg1, String[] arg2, String arg3) {

		return true;
	}

}
