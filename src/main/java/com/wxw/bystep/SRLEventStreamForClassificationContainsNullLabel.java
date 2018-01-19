package com.wxw.bystep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.stream.SRLSample;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.ml.model.Event;
import opennlp.tools.util.AbstractEventStream;
import opennlp.tools.util.ObjectStream;

/**
 * 论元分类阶段生成事件
 * 说明：根据上一步分类阶段的模型tagger来决定是否增加一个NULL标记
 * 其中NULL标记表示：将本来不是论元，但是却识别为论元的，标记记为NULL
 * @author 王馨苇
 *
 */
public class SRLEventStreamForClassificationContainsNullLabel extends AbstractEventStream<SRLSample<HeadTreeNode>>{
	private HashSet<String> hs = new HashSet<>();

	private SRLContextGenerator generator;
	private SRLMEForIdentification tagger;
	
	/**
	 * 构造
	 * @param samples 样本流
	 * @param generator 上下文产生器
	 */
	public SRLEventStreamForClassificationContainsNullLabel(ObjectStream<SRLSample<HeadTreeNode>> samples,SRLContextGenerator generator,SRLMEForIdentification tagger) {
		super(samples);
		this.generator = generator;
		this.tagger = tagger;
	}

	/**
	 * 生成事件
	 */
	@Override
	protected Iterator<Event> createEvents(SRLSample<HeadTreeNode> sample) {
		//根据训练语料获取识别阶段用的类别
		String[] labelinfoIdenRef = sample.getIdentificationLabelInfo();
		//获取实际上是论元的下标
		List<Integer> index = SRLSample.filterNotNULLLabelIndex(labelinfoIdenRef);
		TreeNodeWrapper<HeadTreeNode>[] argumenttree = sample.getArgumentTree();
		TreeNodeWrapper<HeadTreeNode>[] predicatetree = sample.getPredicateTree();

		//从识别阶段的模型中得到识别阶段预测的结果
		String[] labelinfoIdenPre = tagger.tag(argumenttree, predicatetree);
		//识别阶段的参考结果和预测结果对比，找到原本不是论元，但是却识别为论元的下标
		List<Integer> index1 = SRLSample.filter(labelinfoIdenRef, labelinfoIdenPre);
		if(index1.size() == 0){
		}else{
			for (int i = 0; i < labelinfoIdenRef.length; i++) {
				System.out.print(labelinfoIdenRef[i] + "\n");
			}
			System.out.println();
			for (int i = 0; i < labelinfoIdenPre.length; i++) {
				System.out.print(labelinfoIdenPre[i] + "\n");
			}
			System.out.println();
			System.out.println(index1.toString());
			System.out.println();
		}
		//将本来就是论元的下标，和错误识别为论元的下标进行合并
		index.addAll(index1);	
		//得到用于分类训练的类别标记(包含NULL标记)
		String[] argumentlabel = SRLSample.getLabelFromIndex(labelinfoIdenRef, index);
		if(index1.size() != 0){
			for (int i = 0; i < argumentlabel.length; i++) {
				System.out.print(argumentlabel[i]+"\t");
			}
			System.out.println();
		}
		//得到用于分类训练的论元树
		TreeNodeWrapper<HeadTreeNode>[] trueargumenttree = SRLSample.getArgumentTreeFromIndex(argumenttree, index);
		String[][] ac = sample.getAdditionalContext();
		List<Event> events = generateEvents(trueargumenttree, predicatetree,argumentlabel,ac);
        return events.iterator();
	}

	/**
	 * 事件生成
	 * @param argumenttree 以论元为根节点的树
	 * @param predicatetree 以谓词为根节点的树
	 * @param labelinfo 标记序列
	 * @param ac
	 * @return
	 */
	private List<Event> generateEvents(TreeNodeWrapper<HeadTreeNode>[] argumenttree, TreeNodeWrapper<HeadTreeNode>[] predicatetree, String[] labelinfoIden, String[][] ac) {

		
		List<Event> events = new ArrayList<Event>(labelinfoIden.length);
		for (int i = 0; i < labelinfoIden.length; i++) {
			String[] context = generator.getContext(i, argumenttree, labelinfoIden, predicatetree);
			events.add(new Event(labelinfoIden[i],context));
			hs.add(labelinfoIden[i]);
		}
		return events;
	}
}

