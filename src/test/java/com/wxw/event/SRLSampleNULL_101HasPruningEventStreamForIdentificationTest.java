package com.wxw.event;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.feature.SRLContextGeneratorConfForIdentification;
import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseAddNULL_101HasPruning;
import com.wxw.stream.SRLSample;
import com.wxw.tool.PreTreatTool;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.TreeNode;

import opennlp.tools.ml.model.Event;

/**
 * 对识别阶段生成特征进行单元测试(对包含NULL_101类别，有剪枝的样本生成特征)
 * @author 王馨苇
 *
 */
public class SRLSampleNULL_101HasPruningEventStreamForIdentificationTest {
	private SRLContextGenerator generator ;
	private PhraseGenerateTree pgt ;
	private TreeNode tree1;
	private AbstractParseStrategy<HeadTreeNode> ttss;
	private SRLSample<HeadTreeNode> sample;
	private String roles1;
	private List<String> result1;
	private List<String> result2;
	private List<String> result27;
	private List<String> result29;
	
	private List<Event> events = new ArrayList<Event>();
	private List<Event> event1 = new ArrayList<Event>();
	private List<Event> event2 = new ArrayList<Event>();
	private List<Event> event27 = new ArrayList<Event>();
	private List<Event> event29 = new ArrayList<Event>();
	private TreeNodeWrapper<HeadTreeNode>[] argumenttree;
	private TreeNodeWrapper<HeadTreeNode>[] predicatetree;
	private String[] labelinfo;
	
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
		
		ttss = new SRLParseAddNULL_101HasPruning();
		roles1 = "wsj/00/wsj0012.mrg 9 12 gold shore.01 i---a 4:1*10:0-ARG0 12:0,13:1-rel 14:2-ARG1";
		sample = ttss.parse(tree1, roles1);

		argumenttree = sample.getArgumentTree();
		predicatetree = sample.getPredicateTree();
		labelinfo = sample.getIdentificationLabelInfo();
		generator = new SRLContextGeneratorConfForIdentification();	
		for (int i = 0; i < argumenttree.length; i++) {
			String[] context = generator.getContext(i, argumenttree, labelinfo, predicatetree);
			events.add(new Event(labelinfo[i],context));
		}

		result1 = new ArrayList<>();
		result1.add("path=NP↑VP↓VB");
		result1.add("pathlength=3");
		result1.add("headword=decline");
		result1.add("headwordpos=NN");
		result1.add("predicateAndHeadword=shore|decline");
		result1.add("predicateAndPhrasetype=shore|NP");
		
		result2 = new ArrayList<>();
		result2.add("path=NP↑NP↑VP↓VB");
		result2.add("pathlength=4");
		result2.add("headword=decline");
		result2.add("headwordpos=NN");
		result2.add("predicateAndHeadword=shore|decline");
		result2.add("predicateAndPhrasetype=shore|NP");
		
		result27 = new ArrayList<>();
		result27.add("path=NP↑S↓VP↓NP↓S↓VP↓VP↓VB");
		result27.add("pathlength=8");	
		result27.add("headword=plan");
		result27.add("headwordpos=NN");
		result27.add("predicateAndHeadword=shore|plan");
		result27.add("predicateAndPhrasetype=shore|NP");
		
		result29 = new ArrayList<>();
		result29.add("path=NP↑S↓VP↓SBAR↓S↓VP↓NP↓S↓VP↓VP↓VB");
		result29.add("pathlength=11");	
		result29.add("headword=Mr.");
		result29.add("headwordpos=NNP");
		result29.add("predicateAndHeadword=shore|Mr.");
		result29.add("predicateAndPhrasetype=shore|NP");
		event1.add(new Event("YES",result1.toArray(new String[result1.size()])));
		event2.add(new Event("NULL1",result2.toArray(new String[result2.size()])));
		event27.add(new Event("YES",result27.toArray(new String[result27.size()])));
		event29.add(new Event("NULL_1",result29.toArray(new String[result29.size()])));	
	}
	
	@Test
	public void test(){
		assertEquals(argumenttree.length,29);
		assertEquals(events.size(),29);
		assertEquals(events.get(0).toString(),event1.get(0).toString());
		assertEquals(events.get(1).toString(),event2.get(0).toString());
		assertEquals(events.get(26).toString(),event27.get(0).toString());
		assertEquals(events.get(28).toString(),event29.get(0).toString());
	}
}
