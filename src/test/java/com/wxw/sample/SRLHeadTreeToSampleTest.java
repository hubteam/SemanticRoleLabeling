package com.wxw.sample;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.pretreatRun.TreePreTreatment;
import com.wxw.stream.SemanticRoleLabelingSample;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.SRLHeadTreeNode;
import com.wxw.tree.SRLHeadTreeToSample;
import com.wxw.tree.SRLTreeNode;
import com.wxw.tree.SRLTreeToSRLHeadTree;
import com.wxw.tree.TreeNode;
import com.wxw.tree.TreeToSRLTree;

/**
 * 带头结点的语义角色标注树转成
 * @author 王馨苇
 *
 */
public class SRLHeadTreeToSampleTest {

	private String roles1;
	private String result2;
	private String result3;
	private List<String> result1;
	private SRLTreeToSRLHeadTree toheadtree;
	private PhraseGenerateTree pgt ;
	private TreeToSRLTree ttst ;
	private TreeNode tree1;
	private SRLTreeNode roletree1;
	private SRLHeadTreeNode headtree1;
	private SRLHeadTreeToSample shtts;
	private SemanticRoleLabelingSample<SRLHeadTreeNode> sample;
	
	@Before
	public void setUp(){
		pgt = new PhraseGenerateTree();
		ttst = new TreeToSRLTree();	
		toheadtree = new SRLTreeToSRLHeadTree();
		
		roles1 = "wsj/00/wsj_0012.mrg 9 12 gold shore.01 i---a 4:1*10:0-ARG0 12:0,13:1-rel 14:2-ARG1";
		tree1 = pgt.generateTree(""
				+ "((S(S(NP-SBJ(NNP Mr.)(NNP Spoon))(VP(VBD said)(SBAR (-NONE- 0)(S(NP-SBJ(DT the)(NN plan))"
				+ "(VP(VBZ is)(RB not)(NP-PRD(DT an)(NN attempt)(S(NP-SBJ(-NONE- *))(VP(TO to)(VP(VB shore)"
				+ "(PRT(RP up))(NP(NP(DT a)(NN decline))(PP-LOC(IN in)(NP(NN ad)(NNS pages)))(PP-TMP(IN in)"
				+ "(NP(NP(DT the)(JJ first)(CD nine)(NNS months))(PP(IN of)(NP(CD 1989)))))))))))))))"
				+ "(: ;)(S(NP-SBJ(NP(NNP Newsweek)(POS 's))(NN ad)(NNS pages))(VP(VBD totaled)(NP"
				+ "(NP(CD 1,620))(, ,)(NP(NP(DT a)(NN drop))(PP(IN of)(NP (CD 3.2)(NN %)))"
				+ "(PP-DIR(IN from)(NP(JJ last)(NN year)))))(, ,)(PP(VBG according)(PP(TO to)"
				+ "(NP(NNP Publishers)(NNP Information)(NNP Bureau))))))(. .)))");
		roletree1 = ttst.treeToSRLTree(tree1, roles1);
		TreePreTreatment.travelTree(roletree1);
		headtree1 = toheadtree.srlTreeToSRLHeadTree(roletree1);				
//		System.out.println(headtree1.toString());
		shtts = new SRLHeadTreeToSample();
		sample = shtts.getSample(headtree1, roles1);
		
		result1 = new ArrayList<>();		
		result1.add("12");
		result1.add("shore_up");
		result1.add("i---a");
		result1.add("4_ARG0");
		result1.add("14_ARG1");

		result2 = "(NP{plan[NN]}_{ARG0}(DT{the[DT]} the_[4])(NN{plan[NN]} plan_[5]))";
		result3 = "(NP{decline[NN]}_{ARG1}(NP{decline[NN]}(DT{a[DT]} a_[14])(NN{decline[NN]} decline_[15]))(PP{in[IN]}(IN{in[IN]} in_[16])"
				+ "(NP{ad[NN]}(NN{ad[NN]} ad_[17])(NNS{pages[NNS]} pages_[18])))(PP{in[IN]}(IN{in[IN]} in_[19])"+ 
                  "(NP{months[NNS]}(NP{months[NNS]}(DT{the[DT]} the_[20])(JJ{first[JJ]} first_[21])(CD{nine[CD]} nine_[22])"
                  + "(NNS{months[NNS]} months_[23]))(PP{of[IN]}(IN{of[IN]} of_[24])(NP{1989[CD]}(CD{1989[CD]} 1989_[25]))))))";
	}
	
	@Test
	public void test(){
		assertEquals(sample.getSemanticInfo(),result1);
		assertEquals(sample.getRoleTree().get(0).toString(),result2);
		assertEquals(sample.getRoleTree().get(1).toString(),result3);
	}
}
