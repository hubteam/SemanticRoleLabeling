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
 * 没有NULL标记，表示用于分类训练的都是论元
 * @author 王馨苇
 *
 */
public class SRLEventStreamForClassificationNotNullLabel extends AbstractEventStream<SRLSample<HeadTreeNode>>{
	private HashSet<String> hs = new HashSet<>();

	private SRLContextGenerator generator;
	
	/**
	 * 构造
	 * @param samples 样本流
	 * @param generator 上下文产生器
	 */
	public SRLEventStreamForClassificationNotNullLabel(ObjectStream<SRLSample<HeadTreeNode>> samples,SRLContextGenerator generator) {
		super(samples);
		this.generator = generator;
	}

	/**
	 * 生成事件
	 */
	@Override
	protected Iterator<Event> createEvents(SRLSample<HeadTreeNode> sample) {
		//根据训练语料获取识别阶段用的类别
		String[] labelinfoRef = sample.getLabelInfo();
		//获取实际上是论元的下标
		List<Integer> index = SRLSample.filterNotNULLLabelIndex(labelinfoRef);
		TreeNodeWrapper<HeadTreeNode>[] argumenttree = sample.getArgumentTree();
		TreeNodeWrapper<HeadTreeNode>[] predicatetree = sample.getPredicateTree();		
		//得到用于分类训练的类别标记（根据tagger是否为空，决定是否使用NULL标记）
		String[] argumentlabel = SRLSample.getLabelFromIndex(labelinfoRef, index);
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
