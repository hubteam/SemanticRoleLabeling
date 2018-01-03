package com.wxw.headword;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.wxw.pretreatRun.TreePreTreatment;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.SRLTreeToSRLHeadTree;
import com.wxw.tree.SRLHeadTreeNode;
import com.wxw.tree.SRLTreeNode;
import com.wxw.tree.TreeNode;
import com.wxw.tree.TreeToSRLTree;

/**
 * 生成头结点的单元测试
 * @author 王馨苇
 *
 */
public class HeadWordTest {

	private String roles1;
	private String result1;
	private SRLTreeToSRLHeadTree toheadtree;
	private PhraseGenerateTree pgt ;
	private TreeToSRLTree ttst ;
	private TreeNode tree1;
	private SRLTreeNode roletree1;
	private SRLHeadTreeNode headtree1;
	
	@Before
	public void setUp(){
		pgt = new PhraseGenerateTree();
		ttst = new TreeToSRLTree();	
		toheadtree = new SRLTreeToSRLHeadTree();
		
		roles1 = "wsj/00/wsj_0001.mrg 0 8 gold join.01 vf--a 0:2-ARG0 7:0-ARGM-MOD 8:0-rel 9:1-ARG1 11:1-ARGM-PRD 15:1-ARGM-TMP";
		tree1 = pgt.generateTree("((S(NP-SBJ(NP(NNP Pierre)(NNP Vinken))(, ,)(ADJP(NP(CD 61)(NNS years))(JJ old))(, ,))(VP(MD will)(VP(VB join)(NP(DT the)(NN board))(PP-CLR(IN as)(NP (DT a)(JJ nonexecutive)(NN director)))(NP-TMP(NNP Nov.)(CD 29))))(. .)))");
		roletree1 = ttst.treeToSRLTree(tree1, roles1);
		headtree1 = toheadtree.srlTreeToSRLHeadTree(roletree1);		
		TreePreTreatment.travelTree(headtree1);
		result1 = "(S{will[MD]}(NP{Pierre[NNP]}_{ARG0}(NP{Pierre[NNP]}(NNP{Pierre[NNP]} Pierre_[0])(NNP{Vinken[NNP]} Vinken_[1]))(,{,[,]} ,_[2])"
				+ "(ADJP{old[JJ]}(NP{years[NNS]}(CD{61[CD]} 61_[3])(NNS{years[NNS]} years_[4]))"
				+ "(JJ{old[JJ]} old_[5]))(,{,[,]} ,_[6]))(VP{will[MD]}(MD{will[MD]}_{ARGM-MOD} will_[7])(VP{join[VB]}(VB{join[VB]}_{rel} join_[8])"
				+ "(NP{board[NN]}_{ARG1}(DT{the[DT]} the_[9])"
				+ "(NN{board[NN]} board_[10]))(PP{as[IN]}_{ARGM-PRD}(IN{as[IN]} as_[11])(NP{director[NN]}(DT{a[DT]} a_[12])"
				+ "(JJ{nonexecutive[JJ]} nonexecutive_[13])"
				+ "(NN{director[NN]} director_[14])))(NP{Nov.[NNP]}_{ARGM-TMP}(NNP{Nov.[NNP]} Nov._[15])(CD{29[CD]} 29_[16]))))(.{.[.]} ._[17]))";	
	}
	
	@Test
	public void test(){
		assertEquals(headtree1.toString(),result1);
	}
}
