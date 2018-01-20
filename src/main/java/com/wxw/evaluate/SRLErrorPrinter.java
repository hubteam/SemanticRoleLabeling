package com.wxw.evaluate;

import java.io.OutputStream;
import java.io.PrintStream;

import com.wxw.stream.SRLSample;
import com.wxw.tree.HeadTreeNode;

public class SRLErrorPrinter extends SRLEvaluateMonitor{
    private PrintStream errOut;
	
	public SRLErrorPrinter(OutputStream out){
		errOut = new PrintStream(out);
	}
	
	/**
	 * 样本和预测的不一样的时候进行输出
	 * @param reference 参考的样本
	 * @param predict 预测的结果
	 */
	@Override
	public void missclassified(SRLSample<HeadTreeNode> reference, SRLSample<HeadTreeNode> predict) {
		 errOut.println("样本的结果：");
//		 errOut.println(SyntacticAnalysisSample.toSample(reference.getWords(), reference.getActions()).toBracket());
		 errOut.println();
		 errOut.println("预测的结果：");
//		 errOut.println(SyntacticAnalysisSample.toSample(predict.getWords(), predict.getActions()).toBracket());
		 errOut.println();
	}
}
