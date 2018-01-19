package com.wxw.event;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.feature.SRLContextGeneratorConfForClassification;
import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseNormal;
import com.wxw.stream.SRLSample;
import com.wxw.tool.PreTreatTool;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.TreeNode;

import opennlp.tools.ml.model.Event;

/**
 * 对分类阶段生成特征进行单元测试
 * (样本包含NULL类别或者包含NULL_101类别，没有剪枝或者有剪枝的样本都适用)
 * 将样本类转成分类阶段需要的样式，分类阶段没有类别NULL
 * @author 王馨苇
 *
 */
public class SRLSampleEventStreamForClassNoNullLabelTest {
	private SRLContextGenerator generator ;
	private PhraseGenerateTree pgt ;
	private TreeNode tree1;
	private AbstractParseStrategy<HeadTreeNode> ttss;
	private SRLSample<HeadTreeNode> sample;
	private String roles1;
	private List<String> result1;
	private List<String> result0;
	
	private List<Event> events = new ArrayList<Event>();
	private List<Event> event1 = new ArrayList<Event>();
	private List<Event> event0 = new ArrayList<Event>();
	private TreeNodeWrapper<HeadTreeNode>[] argumenttree;
	private TreeNodeWrapper<HeadTreeNode>[] predicatetree;
	private String[] labelinfo;
	private String[] labelinfoIden;
	private String[] labelfortrain;
	private TreeNodeWrapper<HeadTreeNode>[] argumenttreefortrain;
	private List<Integer> index;
	
	@Before
	public void setUp() throws IOException{
        pgt = new PhraseGenerateTree(); 
        
		tree1 = pgt.generateTree(""
				+ "((S(S(NP-SBJ(NNP Mr.)(NNP Spoon))(VP(VBD said)(SBAR (-NONE- 0)(S(NP-SBJ(DT the)(NN plan))"
				+ "(VP(VBZ is)(RB not)(NP-PRD(DT an)(NN attempt)(S(NP-SBJ(-NONE- *))(VP(TO to)(VP(VB shore)"
				+ "(PRT(RP up))(NP(NP(DT a)(NN decline))(PP-LOC(IN in)(NP(NN ad)(NNS pages)))(PP-TMP(IN in)"
				+ "(NP(NP(DT the)(JJ first)(CD nine)(NNS months))(PP(IN of)(NP(CD 1989)))))))))))))))"
				+ "(: ;)(S(NP-SBJ(NP(NNP Newsweek)(POS 's))(NN ad)(NNS pages))(VP(VBD totaled)(NP"
				+ "(NP(CD 1,620))(, ,)(NP(NP(DT a)(NN drop))(PP(IN of)(NP (CD 3.2)(NN %)))"
				+ "(PP-DIR(IN from)(NP(JJ last)(NN year)))))(, ,)(PP(VBG according)(PP(TO to)"
				+ "(NP(NNP Publishers)(NNP Information)(NNP Bureau))))))(. .)))");	
		
		PreTreatTool.preTreat(tree1);
		
		ttss = new SRLParseNormal();
		roles1 = "wsj/00/wsj0012.mrg 9 12 gold shore.01 i---a 4:1*10:0-ARG0 12:0,13:1-rel 14:2-ARG1";
		sample = ttss.parse(tree1, roles1);

		argumenttree = sample.getArgumentTree();
		predicatetree = sample.getPredicateTree();
		labelinfo = sample.getLabelInfo();
		labelinfoIden = sample.getIdentificationLabelInfo();
		index = SRLSample.filterNotNULLLabelIndex(labelinfoIden);
		labelfortrain = SRLSample.getLabelFromIndex(labelinfo, index);
		argumenttreefortrain = SRLSample.getArgumentTreeFromIndex(argumenttree, index);
		generator = new SRLContextGeneratorConfForClassification();	
		for (int i = 0; i < argumenttreefortrain.length; i++) {
			String[] context = generator.getContext(i, argumenttreefortrain, labelfortrain, predicatetree);
			events.add(new Event(labelfortrain[i],context));
		}

		result1 = new ArrayList<>();
		result1.add("path=NP↑VP↓VB");
		result1.add("phrasetype=NP");	
		result1.add("headword=decline");
		result1.add("headwordpos=NN");
		result1.add("subcategorization=VP→VB PRT NP");
		result1.add("firstargument=a");
		result1.add("firstargumentpos=DT");
		result1.add("lastargument=1989");
		result1.add("lastargumentpos=CD");
		result1.add("positionAndvoice=after|a");
		result1.add("predicateAndHeadword=shore|decline");
		result1.add("predicateAndPhrasetype=shore|NP");
		
		result0 = new ArrayList<>();
		result0.add("path=NP↑S↓VP↓NP↓S↓VP↓VP↓VB");
		result0.add("phrasetype=NP");
		result0.add("headword=plan");
		result0.add("headwordpos=NN");
		result0.add("subcategorization=VP→VB PRT NP");
		result0.add("firstargument=the");
		result0.add("firstargumentpos=DT");
		result0.add("lastargument=plan");
		result0.add("lastargumentpos=NN");
		result0.add("positionAndvoice=before|a");
		result0.add("predicateAndHeadword=shore|plan");
		result0.add("predicateAndPhrasetype=shore|NP");

		event0.add(new Event("ARG0",result0.toArray(new String[result0.size()])));
		event1.add(new Event("ARG1",result1.toArray(new String[result1.size()])));
		
	}
	
	@Test
	public void test(){
		assertEquals(events.size(),2);
		assertEquals(events.get(1).toString(),event1.get(0).toString());
		assertEquals(events.get(0).toString(),event0.get(0).toString());
	}
}
