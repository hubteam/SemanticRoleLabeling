package com.wxw.stream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.onestep.SRLSample;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.ml.model.Event;
import opennlp.tools.util.AbstractEventStream;
import opennlp.tools.util.ObjectStream;

/**
 * 生成事件
 * @author 王馨苇
 *
 */
public class SRLSampleEventStream extends AbstractEventStream<SRLSample<HeadTreeNode>>{
	private HashSet<String> hs = new HashSet<>();

	private SRLContextGenerator generator;
	
	/**
	 * 构造
	 * @param samples 样本流
	 * @param generator 上下文产生器
	 */
	public SRLSampleEventStream(ObjectStream<SRLSample<HeadTreeNode>> samples,SRLContextGenerator generator) {
		super(samples);
		this.generator = generator;
	}

	/**
	 * 生成事件
	 */
	@Override
	protected Iterator<Event> createEvents(SRLSample<HeadTreeNode> sample) {
		String[] semanticinfo = sample.getSemanticInfo();
		String[] labelinfo = sample.getLabelInfo();
		List<HeadTreeNode> headtree = sample.getHeadTree();
		String[][] ac = sample.getAdditionalContext();
		List<Event> events = generateEvents(headtree, semanticinfo,labelinfo,ac);
        return events.iterator();
	}

	/**
	 * 事件生成
	 * @param words 词语序列
	 * @param posTree pos得到的子树
	 * @param chunkTree chunk得到的子树
	 * @param buildAndCheck buildAndCheck得到的子树
	 * @param actions 动作序列
	 * @param ac
	 * @return
	 */
	private List<Event> generateEvents(List<HeadTreeNode> headTree, String[] semanticinfo, String[] labelinfo, String[][] ac) {
		List<Event> events = new ArrayList<Event>(labelinfo.length);
		HeadTreeNode[] tree = headTree.toArray(new HeadTreeNode[headTree.size()]);
		for (int i = 0; i < labelinfo.length; i++) {
			String[] context = generator.getContext(i+1, tree, labelinfo, semanticinfo);
			events.add(new Event(labelinfo[i],context));
			hs.add(labelinfo[i]);
		}
//		for (String str : hs) {
//			System.out.print(str + " ");
//		}
//		System.out.println();
		return events;
	}
}

