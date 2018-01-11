package com.wxw.onestepparse;

import com.wxw.onestep.SRLSample;
import com.wxw.tree.HeadTreeNode;

public class SRLParserNormalHasPruning extends AbstractParseStrategy<HeadTreeNode>{

	@Override
	public boolean hasPrePruning() {

		return true;
	}

	@Override
	public boolean hasHeadWord() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HeadTreeNode prePruning(HeadTreeNode tree, int verbindex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SRLSample<HeadTreeNode> toSample(HeadTreeNode tree, String semanticRole) {
		// TODO Auto-generated method stub
		return null;
	}

}
