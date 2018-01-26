package com.wxw.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseNormal;
import com.wxw.parse.SRLParseNormalHasPruning;
import com.wxw.parse.SRLParseAddNULL_101;
import com.wxw.parse.SRLParseAddNULL_101HasPruning;
import com.wxw.tool.PreTreatTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.TreeNode;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;

/**
 * 解析样本流
 * @author 王馨苇
 *
 */
public class SRLSampleStream extends FilterObjectStream<String[],SRLSample<HeadTreeNode>>{

	private Logger logger = Logger.getLogger(SRLSampleStream.class.getName());
	private AbstractParseStrategy<HeadTreeNode> parse;
	public SRLSampleStream(ObjectStream<String[]> samples,AbstractParseStrategy<HeadTreeNode> parse) {
		super(samples);
		this.parse = parse;
	}

	public SRLSample<HeadTreeNode> read() throws IOException {				
		String[] sentence = samples.read();	
		SRLSample<HeadTreeNode> sample = null;
		PhraseGenerateTree pgt = new PhraseGenerateTree();
		if(sentence[0]!= null && sentence[1] != null){
			try{
				TreeNode tree = pgt.generateTree(sentence[0]);
				PreTreatTool.preTreat(tree);
                sample = parse.parse(tree, sentence[1]);
			}catch(Exception e){
				if (logger.isLoggable(Level.WARNING)) {						
	                logger.warning("Error during parsing, ignoring sentence: " + sentence[1]);
	            }	
				sample = new SRLSample<HeadTreeNode>(new HeadTreeNode(""), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
				}
			return sample;
		}else {
			return null;
		}
	}
}
