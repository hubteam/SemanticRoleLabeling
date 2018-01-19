package com.wxw.event;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.feature.SRLContextGeneratorConf;
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
 * 对生成特征进行单元测试(对包含NULL_101类别，有剪枝的样本生成特征)
 * @author 王馨苇
 *
 */
public class SRLSampleNULL_101HasPruningEventStreamForOneStepTest {
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
	private HashSet<String> hs1;
	private HashSet<String> hs2;
	
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
		labelinfo = sample.getLabelInfo();
		generator = new SRLContextGeneratorConf();	
		for (int i = 0; i < argumenttree.length; i++) {
			String[] context = generator.getContext(i, argumenttree, labelinfo, predicatetree);
			events.add(new Event(labelinfo[i],context));
		}

		result1 = new ArrayList<>();
		result1.add("predicate=shore");
		result1.add("predicatepos=VB");
		result1.add("path=NP↑VP↓VB");
		result1.add("pathlength=3");
		result1.add("partialpath=NP↑VP");
		result1.add("phrasetype=NP");
		result1.add("position=after");
		result1.add("voice=a");	
		result1.add("headword=decline");
		result1.add("headwordpos=NN");
		result1.add("subcategorization=VP→VB PRT NP");
		result1.add("firstargument=a");
		result1.add("firstargumentpos=DT");
		result1.add("lastargument=1989");
		result1.add("lastargumentpos=CD");
		result1.add("positionAndvoice=after|a");
		result1.add("predicateAndpath=shore|NP↑VP↓VB");
		result1.add("pathAndpositionAndvoice=NP↑VP↓VB|after|a");
		result1.add("pathAndpositionAndvoiceAndpredicate=NP↑VP↓VB|after|a|shore");
		result1.add("headwordAndpredicateAndpath=decline|shore|NP↑VP↓VB");
		result1.add("headwordAndPhrasetype=decline|NP");
		result1.add("predicateAndHeadword=shore|decline");
		result1.add("predicateAndPhrasetype=shore|NP");
		
		result2 = new ArrayList<>();
		result2.add("predicate=shore");
		result2.add("predicatepos=VB");
		result2.add("path=NP↑NP↑VP↓VB");
		result2.add("pathlength=4");
		result2.add("partialpath=NP↑NP↑VP");
		result2.add("phrasetype=NP");
		result2.add("position=after");
		result2.add("voice=a");	
		result2.add("headword=decline");
		result2.add("headwordpos=NN");
		result2.add("subcategorization=VP→VB PRT NP");
		result2.add("firstargument=a");
		result2.add("firstargumentpos=DT");
		result2.add("lastargument=decline");
		result2.add("lastargumentpos=NN");
		result2.add("positionAndvoice=after|a");
		result2.add("predicateAndpath=shore|NP↑NP↑VP↓VB");
		result2.add("pathAndpositionAndvoice=NP↑NP↑VP↓VB|after|a");
		result2.add("pathAndpositionAndvoiceAndpredicate=NP↑NP↑VP↓VB|after|a|shore");
		result2.add("headwordAndpredicateAndpath=decline|shore|NP↑NP↑VP↓VB");
		result2.add("headwordAndPhrasetype=decline|NP");
		result2.add("predicateAndHeadword=shore|decline");
		result2.add("predicateAndPhrasetype=shore|NP");
		
		result27 = new ArrayList<>();
		result27.add("predicate=shore");
		result27.add("predicatepos=VB");
		result27.add("path=NP↑S↓VP↓NP↓S↓VP↓VP↓VB");
		result27.add("pathlength=8");
		result27.add("partialpath=NP↑S");
		result27.add("phrasetype=NP");
		result27.add("position=before");
		result27.add("voice=a");	
		result27.add("headword=plan");
		result27.add("headwordpos=NN");
		result27.add("subcategorization=VP→VB PRT NP");
		result27.add("firstargument=the");
		result27.add("firstargumentpos=DT");
		result27.add("lastargument=plan");
		result27.add("lastargumentpos=NN");
		result27.add("positionAndvoice=before|a");
		result27.add("predicateAndpath=shore|NP↑S↓VP↓NP↓S↓VP↓VP↓VB");
		result27.add("pathAndpositionAndvoice=NP↑S↓VP↓NP↓S↓VP↓VP↓VB|before|a");
		result27.add("pathAndpositionAndvoiceAndpredicate=NP↑S↓VP↓NP↓S↓VP↓VP↓VB|before|a|shore");
		result27.add("headwordAndpredicateAndpath=plan|shore|NP↑S↓VP↓NP↓S↓VP↓VP↓VB");
		result27.add("headwordAndPhrasetype=plan|NP");
		result27.add("predicateAndHeadword=shore|plan");
		result27.add("predicateAndPhrasetype=shore|NP");
		
		result29 = new ArrayList<>();
		result29.add("predicate=shore");
		result29.add("predicatepos=VB");
		result29.add("path=NP↑S↓VP↓SBAR↓S↓VP↓NP↓S↓VP↓VP↓VB");
		result29.add("pathlength=11");
		result29.add("partialpath=NP↑S");
		result29.add("phrasetype=NP");
		result29.add("position=before");
		result29.add("voice=a");	
		result29.add("headword=Mr.");
		result29.add("headwordpos=NNP");
		result29.add("subcategorization=VP→VB PRT NP");
		result29.add("firstargument=Mr.");
		result29.add("firstargumentpos=NNP");
		result29.add("lastargument=Spoon");
		result29.add("lastargumentpos=NNP");
		result29.add("positionAndvoice=before|a");
		result29.add("predicateAndpath=shore|NP↑S↓VP↓SBAR↓S↓VP↓NP↓S↓VP↓VP↓VB");
		result29.add("pathAndpositionAndvoice=NP↑S↓VP↓SBAR↓S↓VP↓NP↓S↓VP↓VP↓VB|before|a");
		result29.add("pathAndpositionAndvoiceAndpredicate=NP↑S↓VP↓SBAR↓S↓VP↓NP↓S↓VP↓VP↓VB|before|a|shore");
		result29.add("headwordAndpredicateAndpath=Mr.|shore|NP↑S↓VP↓SBAR↓S↓VP↓NP↓S↓VP↓VP↓VB");
		result29.add("headwordAndPhrasetype=Mr.|NP");
		result29.add("predicateAndHeadword=shore|Mr.");
		result29.add("predicateAndPhrasetype=shore|NP");
		event1.add(new Event("ARG1",result1.toArray(new String[result1.size()])));
		event2.add(new Event("NULL1",result2.toArray(new String[result2.size()])));
		event27.add(new Event("ARG0",result27.toArray(new String[result27.size()])));
		event29.add(new Event("NULL_1",result29.toArray(new String[result29.size()])));
		
		hs1 = new HashSet<>();
		for (int i = 0; i < labelinfo.length; i++) {
			hs1.add(labelinfo[i]);
		}
		hs2 = new HashSet<>();
		hs2.add("ARG0");
		hs2.add("ARG1");
		hs2.add("NULL1");
		hs2.add("NULL_1");
	}
	
	@Test
	public void test(){
		assertEquals(hs1.toString(),hs2.toString());
		assertEquals(argumenttree.length,29);
		assertEquals(events.size(),29);
		assertEquals(events.get(0).toString(),event1.get(0).toString());
		assertEquals(events.get(1).toString(),event2.get(0).toString());
		assertEquals(events.get(26).toString(),event27.get(0).toString());
		assertEquals(events.get(28).toString(),event29.get(0).toString());
	}
}
