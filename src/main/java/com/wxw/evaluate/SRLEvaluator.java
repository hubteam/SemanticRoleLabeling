package com.wxw.evaluate;

import java.util.Arrays;
import java.util.List;

import com.wxw.modelBystep.POSTaggerMEExtend;
import com.wxw.modelBystep.SyntacticAnalysisMEForBuildAndCheck;
import com.wxw.modelBystep.SyntacticAnalysisMEForChunk;
import com.wxw.onestep.SRLME;
import com.wxw.onestep.SRLSample;
import com.wxw.tool.PostTreatTool;
import com.wxw.tool.TreeNodeWrapper;
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
		TreeNodeWrapper<HeadTreeNode>[] argumenttree = sample.getArgumentTree();
		TreeNodeWrapper<HeadTreeNode>[] predicatetree = sample.getPredicateTree();
		String[] labelinfo = sample.getLabelInfo();
		for (int i = 0; i < labelinfo.length; i++) {
			System.out.print(labelinfo[i]);
		}
		System.out.println();
		Sequence result = tagger.topSequences(argumenttree, predicatetree);
		String[] newlabelinfo = PostTreatTool.postTreat(argumenttree,result);
		for (int i = 0; i < newlabelinfo.length; i++) {
			System.out.print(newlabelinfo[i]);
		}
		System.out.println();
		measure.update(labelinfo, newlabelinfo);
		SRLSample<HeadTreeNode> newsample = new SRLSample<HeadTreeNode>(node,Arrays.asList(argumenttree),Arrays.asList(predicatetree),Arrays.asList(newlabelinfo));
		return newsample;
	}
}
