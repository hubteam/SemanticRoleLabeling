package com.wxw.evaluate;

import java.util.Arrays;
import java.util.List;

import com.wxw.modelBystep.POSTaggerMEExtend;
import com.wxw.modelBystep.SyntacticAnalysisMEForBuildAndCheck;
import com.wxw.modelBystep.SyntacticAnalysisMEForChunk;
import com.wxw.onestep.SRLME;
import com.wxw.onestep.SRLSample;
import com.wxw.tool.PostTreatTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.TreeNode;

import opennlp.tools.util.Sequence;
import opennlp.tools.util.eval.Evaluator;

public class SRLEvaluator extends Evaluator<SRLSample<HeadTreeNode>>{

	private SRLME tagger;
	private SRLMeasure measure;
	
	public SRLEvaluator(SRLME tagger) {
		this.tagger = tagger;
	}
	
	public SRLEvaluator(SRLME tagger,SRLEvaluateMonitor... evaluateMonitors) {
		super(evaluateMonitors);
		this.tagger = tagger;
	}
	
	/**
	 * 设置评估指标的对象
	 * @param measure 评估指标计算的对象
	 */
	public void setMeasure(SRLMeasure measure){
		this.measure = measure;
	}
	
	/**
	 * 得到评估的指标
	 * @return
	 */
	public SRLMeasure getMeasure(){
		return this.measure;
	}

	@Override
	protected SRLSample<HeadTreeNode> processSample(SRLSample<HeadTreeNode> sample) {
		TreeNode node = sample.getTree();
		HeadTreeNode[] headtree = sample.getHeadTree().toArray(new HeadTreeNode[sample.getHeadTree().size()]);
		String[] semanticinfo = sample.getSemanticInfo();
		String[] labelinfo = sample.getLabelInfo();
		Sequence result = tagger.topSequences(headtree, semanticinfo);
		String[] newlabelinfo = PostTreatTool.postTreat(headtree,result);
		measure.update(labelinfo, newlabelinfo);
		SRLSample<HeadTreeNode> newsample = new SRLSample<HeadTreeNode>(node,Arrays.asList(headtree),Arrays.asList(semanticinfo),Arrays.asList(newlabelinfo));
		return newsample;
	}
}
