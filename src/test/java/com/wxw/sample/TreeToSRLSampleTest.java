package com.wxw.sample;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.pretreatRun.TreePreTreatment;
import com.wxw.stream.SemanticRoleLabelingSample;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.SRLHeadTreeNode;
import com.wxw.tree.TreeNode;
import com.wxw.tree.TreeToHeadTree;
import com.wxw.tree.TreeToSample;

/**
 * 树转成样本类的测试
 * @author 王馨苇
 *
 */
public class TreeToSRLSampleTest {

	private String roles1;
	private String result1;
	private String result2;
	private String result3;
	private String result4;
	private List<String> label;
	private List<String> srlinfo;
	private PhraseGenerateTree pgt ;
	private TreeNode tree1;
	private TreeToSRLSample shtts;
	private SRLSample<HeadTreeNode> sample;
	
	@Before
	public void setUp(){
		pgt = new PhraseGenerateTree();
		
		roles1 = "wsj/00/wsj0012.mrg 9 12 gold shore.01 i---a 4:1*10:0-ARG0 12:0,13:1-rel 14:2-ARG1";
		tree1 = pgt.generateTree(""
				+ "((S(S(NP-SBJ(NNP Mr.)(NNP Spoon))(VP(VBD said)(SBAR (-NONE- 0)(S(NP-SBJ(DT the)(NN plan))"
				+ "(VP(VBZ is)(RB not)(NP-PRD(DT an)(NN attempt)(S(NP-SBJ(-NONE- *))(VP(TO to)(VP(VB shore)"
				+ "(PRT(RP up))(NP(NP(DT a)(NN decline))(PP-LOC(IN in)(NP(NN ad)(NNS pages)))(PP-TMP(IN in)"
				+ "(NP(NP(DT the)(JJ first)(CD nine)(NNS months))(PP(IN of)(NP(CD 1989)))))))))))))))"
				+ "(: ;)(S(NP-SBJ(NP(NNP Newsweek)(POS 's))(NN ad)(NNS pages))(VP(VBD totaled)(NP"
				+ "(NP(CD 1,620))(, ,)(NP(NP(DT a)(NN drop))(PP(IN of)(NP (CD 3.2)(NN %)))"
				+ "(PP-DIR(IN from)(NP(JJ last)(NN year)))))(, ,)(PP(VBG according)(PP(TO to)"
				+ "(NP(NNP Publishers)(NNP Information)(NNP Bureau))))))(. .)))");		
		TreePreTreatment.travelTree(tree1);
		shtts = new TreeToSRLSample();
		sample = shtts.getSample(tree1, roles1);

		srlinfo = new ArrayList<>();		
		srlinfo.add("12");
		srlinfo.add("shore_up");
		srlinfo.add("i---a");
		srlinfo.add("4");
		srlinfo.add("14");
		
		label = new ArrayList<>();
		label.add("ARG0");
		label.add("ARG1");

		result1 = "(S(S(NP(NNP Mr.[0])(NNP Spoon[1]))(VP(VBD said[2])(S(NP(DT the[4])(NN plan[5]))"
				+ "(VP(VBZ is[6])(RB not[7])(NP(DT an[8])(NN attempt[9])(VP(TO to[11])(VP(VB shore[12])"
				+ "(PRT(RP up[13]))(NP(NP(DT a[14])(NN decline[15]))(PP(IN in[16])(NP(NN ad[17])(NNS pages[18])))(PP(IN in[19])"
				+ "(NP(NP(DT the[20])(JJ first[21])(CD nine[22])(NNS months[23]))(PP(IN of[24])(NP(CD 1989[25])))))))))))))"
				+ "(: ;[26])(S(NP(NP(NNP Newsweek[27])(POS 's[28]))(NN ad[29])(NNS pages[30]))(VP(VBD totaled[31])(NP"
				+ "(NP(CD 1,620[32]))(, ,[33])(NP(NP(DT a[34])(NN drop[35]))(PP(IN of[36])(NP(CD 3.2[37])(NN %[38])))"
				+ "(PP(IN from[39])(NP(JJ last[40])(NN year[41])))))(, ,[42])(PP(VBG according[43])(PP(TO to[44])"
				+ "(NP(NNP Publishers[45])(NNP Information[46])(NNP Bureau[47]))))))(. .[48]))";
		
		result2 = "(VB{shore[VB]} shore[12])";
		
		result3 = "(NP{plan[NN]}(DT{the[DT]} the[4])(NN{plan[NN]} plan[5]))";
		
		result4 = "(NP{decline[NN]}(NP{decline[NN]}(DT{a[DT]} a[14])(NN{decline[NN]} decline[15]))(PP{in[IN]}(IN{in[IN]} in[16])"
				+ "(NP{ad[NN]}(NN{ad[NN]} ad[17])(NNS{pages[NNS]} pages[18])))(PP{in[IN]}(IN{in[IN]} in[19])"+ 
                  "(NP{months[NNS]}(NP{months[NNS]}(DT{the[DT]} the[20])(JJ{first[JJ]} first[21])(CD{nine[CD]} nine[22])"
                  + "(NNS{months[NNS]} months[23]))(PP{of[IN]}(IN{of[IN]} of[24])(NP{1989[CD]}(CD{1989[CD]} 1989[25]))))))";
	}
	
	@Test
	public void test(){
		assertEquals(Arrays.asList(sample.getLabelInfo()),label);
		assertEquals(Arrays.asList(sample.getSemanticInfo()),srlinfo);
		assertEquals(sample.getTree().toNoNoneSample(),result1);
		assertEquals(sample.getHeadTree().get(0).toString(),result2);
		assertEquals(sample.getHeadTree().get(1).toString(),result3);
		assertEquals(sample.getHeadTree().get(2).toString(),result4);
	}
}
