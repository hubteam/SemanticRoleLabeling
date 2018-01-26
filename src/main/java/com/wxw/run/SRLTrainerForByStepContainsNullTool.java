package com.wxw.run;

import java.io.File;
import java.io.IOException;

import com.wxw.bystep.SRLMEForClassificationContainsNullLabel;
import com.wxw.bystep.SRLMEForIdentification;
import com.wxw.bystep.SRLModelForIdentification;
import com.wxw.feature.SRLContextGenerator;
import com.wxw.feature.SRLContextGeneratorConfForClassification;
import com.wxw.feature.SRLContextGeneratorConfForIdentification;
import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseAddNULL_101;
import com.wxw.parse.SRLParseAddNULL_101HasPruning;
import com.wxw.parse.SRLParseNormal;
import com.wxw.parse.SRLParseNormalHasPruning;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.util.TrainingParameters;

/**
 * 分步骤完成模型的训练【识别阶段包含NULL标签】
 * @author 王馨苇
 *
 */
public class SRLTrainerForByStepContainsNullTool {
	    //parse指示文本解析的方式，共四种
		//normal  normalprune addnull addnullprune
		private static void usage(){
			System.out.println(SRLTrainerForByStepContainsNullTool.class.getName()+"-data <corpusFile> -idenmodel <idenmodelFile> -clasmodel <clasmodelFile> -parse <parsetype> -type <algorithom>"
					+ "-encoding"+"[-cutoff <num>] [-iters <num>]");
		}
		
		public static void main(String[] args) throws IOException {
			if(args.length < 1){
				usage();
				return;
			}
			AbstractParseStrategy<HeadTreeNode> parse = null;
			int cutoff = 3;
			int iters = 100;
	        File corpusFile = null;
	        File idenmodelFile = null;
	        File clasmodelFile = null;
	        String parsestr = "";
	        String encoding = "UTF-8";
	        String type = "MAXENT";
	        for (int i = 0; i < args.length; i++)
	        {
	            if (args[i].equals("-data"))
	            {
	                corpusFile = new File(args[i + 1]);
	                i++;
	            }
	            else if (args[i].equals("-idenmodel"))
	            {
	                idenmodelFile = new File(args[i + 1]);
	                i++;
	            }
	            else if (args[i].equals("-clasmodel"))
	            {
	                clasmodelFile = new File(args[i + 1]);
	                i++;
	            }
	            else if (args[i].equals("-parse"))
	            {
	            	parsestr = args[i + 1];
	                i++;
	            }
	            else if (args[i].equals("-type"))
	            {
	                type = args[i + 1];
	                i++;
	            }
	            else if (args[i].equals("-encoding"))
	            {
	                encoding = args[i + 1];
	                i++;
	            }
	            else if (args[i].equals("-cutoff"))
	            {
	                cutoff = Integer.parseInt(args[i + 1]);
	                i++;
	            }
	            else if (args[i].equals("-iters"))
	            {
	                iters = Integer.parseInt(args[i + 1]);
	                i++;
	            }
	        }
	        
	        SRLContextGenerator contextGenIden = new SRLContextGeneratorConfForIdentification();
	        SRLContextGenerator contextGenClas = new SRLContextGeneratorConfForClassification();
	        TrainingParameters params = TrainingParameters.defaultParams();
	        params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(cutoff));
	        params.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(iters));
	        params.put(TrainingParameters.ALGORITHM_PARAM, type.toUpperCase());

	        if(parsestr.equals("normal")){
	    		parse = new SRLParseNormal();   						
	    	}else if(parsestr.equals("normalprune")){
	    		parse = new SRLParseNormalHasPruning();
	    	}else if(parsestr.equals("addnull")){
	    		parse = new SRLParseAddNULL_101();
	    	}else{
	    		parse = new SRLParseAddNULL_101HasPruning();
	    	}
	        SRLMEForIdentification meIden = new SRLMEForIdentification(parse);
	        SRLModelForIdentification modelIden = meIden.train(corpusFile,idenmodelFile,params, contextGenIden, encoding);

	        SRLMEForIdentification tagger = new SRLMEForIdentification(modelIden, contextGenIden,parse);
	        SRLMEForClassificationContainsNullLabel meClas = new SRLMEForClassificationContainsNullLabel(parse);
	        meClas.train(corpusFile, clasmodelFile, params, contextGenClas, encoding,tagger);
		}	
}
