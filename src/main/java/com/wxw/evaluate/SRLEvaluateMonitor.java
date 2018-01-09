package com.wxw.evaluate;

import com.wxw.sample.SRLSample;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.util.eval.EvaluationMonitor;

/**
 * 评估检测器
 * @author 王馨苇
 *
 */
public class SRLEvaluateMonitor implements EvaluationMonitor<SRLSample<HeadTreeNode>>{

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	@Override
	public void correctlyClassified(SRLSample<HeadTreeNode> arg0, SRLSample<HeadTreeNode> arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	@Override
	public void missclassified(SRLSample<HeadTreeNode> arg0, SRLSample<HeadTreeNode> arg1) {
		// TODO Auto-generated method stub
		
	}

}

