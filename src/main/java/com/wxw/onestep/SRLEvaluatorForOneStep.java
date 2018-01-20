package com.wxw.onestep;

import java.util.Arrays;

import com.wxw.evaluate.SRLEvaluateMonitor;
import com.wxw.evaluate.SRLMeasure;
import com.wxw.stream.SRLSample;
import com.wxw.tool.PostTreatTool;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.util.Sequence;
import opennlp.tools.util.eval.Evaluator;

/**
 * 基于一步训练模型的评估类
 * @author 王馨苇
 *
 */
public class SRLEvaluatorForOneStep extends Evaluator<SRLSample<HeadTreeNode>>{

	private SRLMEForOneStep tagger;
	private SRLMeasure measure;
	
	public SRLEvaluatorForOneStep(SRLMEForOneStep tagger) {
		this.tagger = tagger;
	}
	
	public SRLEvaluatorForOneStep(SRLMEForOneStep tagger,SRLEvaluateMonitor... evaluateMonitors) {
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
		HeadTreeNode node = sample.getTree();
		TreeNodeWrapper<HeadTreeNode>[] argumenttree = sample.getArgumentTree();
		TreeNodeWrapper<HeadTreeNode>[] predicatetree = sample.getPredicateTree();
		String[] labelinfo = sample.getLabelInfo();
		String[] labelinforef = PostTreatTool.NULL_1012NULL(labelinfo);
//		for (int i = 0; i < labelinforef.length; i++) {
//			if(!labelinfo[i].equals(labelinforef[i])){
//				System.out.print(labelinforef[i]);
//			}
//		}
//		System.out.println();
		Sequence result = tagger.topSequences(argumenttree, predicatetree);
		String[] newlabelinfo = null;
		newlabelinfo = result.getOutcomes().toArray(new String[result.getOutcomes().size()]);
		if(sample.getIsPruning() == true){
			newlabelinfo = PostTreatTool.postTreat(argumenttree,result,PostTreatTool.getSonTreeCount(predicatetree[0].getTree().getParent()));
		}else{
			newlabelinfo = PostTreatTool.postTreat(argumenttree,result,argumenttree.length);
		}
//
//		for (int i = 0; i < newlabelinfo.length; i++) {
//			System.out.print(newlabelinfo[i]);
//		}
//		System.out.println();
		measure.update(labelinforef, newlabelinfo);
		SRLSample<HeadTreeNode> newsample = new SRLSample<HeadTreeNode>(node,Arrays.asList(argumenttree),Arrays.asList(predicatetree),Arrays.asList(newlabelinfo));
		return newsample;
	}
}
