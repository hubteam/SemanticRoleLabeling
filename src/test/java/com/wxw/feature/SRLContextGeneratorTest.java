package com.wxw.feature;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.onestep.SRLSample;
import com.wxw.onestepparse.AbstractParseStrategy;
import com.wxw.onestepparse.SRLParseNormal;
import com.wxw.onestepparse.SRLParserAddNULL_101HasPruning;
import com.wxw.tool.PreTreatTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.TreeNode;

/**
 * 对生成特征进行单元测试
 * @author 王馨苇
 *
 */
public class SRLContextGeneratorTest {

	private SRLContextGenerator generator ;
	private PhraseGenerateTree pgt ;
	private TreeNode tree1;
	private AbstractParseStrategy<HeadTreeNode> ttss;
	private SRLSample<HeadTreeNode> sample;
	private String roles1;
	private String[] context1;
	private String[] context2;
	private List<String> result1;
	private List<String> result2;
	
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

		generator = new SRLContextGeneratorConf();	
		context1 = generator.getContext(8, sample.getArgumentTree(), sample.getLabelInfo(), sample.getPredicateTree());
		context2 = generator.getContext(23, sample.getArgumentTree(), sample.getLabelInfo(), sample.getPredicateTree());
		
		result1 = new ArrayList<>();
		result1.add("predicate=shore");
		result1.add("predicatepos=VB");
		result1.add("predicatesuffix=ore");
		result1.add("path=NP↑S↓VP↓NP↓S↓VP↓VP↓VB");
		result1.add("pathlength=8");
		result1.add("partialpath=NP↑S");
		result1.add("phrasetype=NP");
		result1.add("position=before");
		result1.add("voice=a");	
		result1.add("headword=plan");
		result1.add("headwordpos=NN");
		result1.add("subcategorization=VP→VB PRT NP");
		result1.add("firstargument=the");
		result1.add("firstargumentpos=DT");
		result1.add("lastargument=plan");
		result1.add("lastargumentpos=NN");
		result1.add("positionAndvoice=before|a");
		result1.add("predicateAndpath=shore|NP↑S↓VP↓NP↓S↓VP↓VP↓VB");
		result1.add("pathAndpositionAndvoice=NP↑S↓VP↓NP↓S↓VP↓VP↓VB|before|a");
		result1.add("pathAndpositionAndvoiceAndpredicate=NP↑S↓VP↓NP↓S↓VP↓VP↓VB|before|a|shore");
		result1.add("headwordAndpredicateAndpath=plan|shore|NP↑S↓VP↓NP↓S↓VP↓VP↓VB");
		result1.add("headwordAndPhrase=plan|NP");
		result1.add("predicateAndHeadword=shore|plan");
		result1.add("predicateAndPhrasetype=shore|NP");
		
		result2 = new ArrayList<>();
		result2.add("predicate=shore");
		result2.add("predicatepos=VB");
		result2.add("predicatesuffix=ore");	
		result2.add("path=NP↑VP↓VB");
		result2.add("pathlength=3");
		result2.add("partialpath=NP↑VP");
		result2.add("phrasetype=NP");
		result2.add("position=after");
		result2.add("voice=a");	
		result2.add("headword=decline");
		result2.add("headwordpos=NN");
		result2.add("subcategorization=VP→VB PRT NP");
		result2.add("firstargument=a");
		result2.add("firstargumentpos=DT");
		result2.add("lastargument=1989");
		result2.add("lastargumentpos=CD");
		result2.add("positionAndvoice=after|a");
		result2.add("predicateAndpath=shore|NP↑VP↓VB");
		result2.add("pathAndpositionAndvoice=NP↑VP↓VB|after|a");
		result2.add("pathAndpositionAndvoiceAndpredicate=NP↑VP↓VB|after|a|shore");
		result2.add("headwordAndpredicateAndpath=decline|shore|NP↑VP↓VB");
		result2.add("headwordAndPhrase=decline|NP");
		result2.add("predicateAndHeadword=shore|decline");
		result2.add("predicateAndPhrasetype=shore|NP");
	}
	
	@Test
	public void test(){
		assertEquals(Arrays.asList(context1),result1);
		assertEquals(Arrays.asList(context2),result2);
	}
}
