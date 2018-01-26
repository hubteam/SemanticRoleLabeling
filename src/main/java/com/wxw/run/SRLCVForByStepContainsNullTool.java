package com.wxw.run;

import java.io.File;
import java.io.IOException;

import com.wxw.bystep.SRLEvaluatorForByStepNotNullLabel;
import com.wxw.bystep.SRLMEForClassificationContainsNullLabel;
import com.wxw.bystep.SRLMEForClassificationNotNullLabel;
import com.wxw.bystep.SRLMEForIdentification;
import com.wxw.bystep.SRLModelForClassification;
import com.wxw.bystep.SRLModelForIdentification;
import com.wxw.depRun.SyntacticAnalysisCrossValidationRun;
import com.wxw.evaluate.SRLEvaluateMonitor;
import com.wxw.evaluate.SRLMeasure;
import com.wxw.feature.SRLContextGenerator;
import com.wxw.feature.SRLContextGeneratorConfForClassification;
import com.wxw.feature.SRLContextGeneratorConfForIdentification;
import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseAddNULL_101;
import com.wxw.parse.SRLParseAddNULL_101HasPruning;
import com.wxw.parse.SRLParseNormal;
import com.wxw.parse.SRLParseNormalHasPruning;
import com.wxw.stream.FileInputStreamFactory;
import com.wxw.stream.PlainTextByTreeStream;
import com.wxw.stream.SRLSample;
import com.wxw.stream.SRLSampleStream;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.CrossValidationPartitioner;

/**
 * 分步训练模型的交叉验证类
 * 分类阶段有NULL标记
 * @author 王馨苇
 *
 */
public class SRLCVForByStepContainsNullTool {

	private final String languageCode;

    private final TrainingParameters params;

    private SRLEvaluateMonitor[] listeners;
    
    public SRLCVForByStepContainsNullTool(String languageCode,TrainingParameters trainParam,SRLEvaluateMonitor... listeners){
    	this.languageCode = languageCode;
        this.params = trainParam;
        this.listeners = listeners;
    }
    
    public void evaluate(ObjectStream<SRLSample<HeadTreeNode>> samples, AbstractParseStrategy<HeadTreeNode> parse, int nFolds,SRLContextGenerator contextIden, SRLContextGenerator contextClas) throws IOException{
    	CrossValidationPartitioner<SRLSample<HeadTreeNode>> partitioner = new CrossValidationPartitioner<SRLSample<HeadTreeNode>>(samples, nFolds);
		int run = 1;
		//小于折数的时候
		while(partitioner.hasNext()){
			System.out.println("Run"+run+"...");
			CrossValidationPartitioner.TrainingSampleStream<SRLSample<HeadTreeNode>> trainingSampleStream = partitioner.next();
			SRLMEForIdentification meIden = new SRLMEForIdentification(parse);
			SRLModelForIdentification modelIden = meIden.train(languageCode, trainingSampleStream, params, contextIden);
			trainingSampleStream.reset();
			SRLMEForIdentification tagger = new SRLMEForIdentification(modelIden,contextIden,parse);
			SRLMEForClassificationContainsNullLabel meClas = new SRLMEForClassificationContainsNullLabel(parse);
			SRLModelForClassification modelClas = meClas.train(languageCode, trainingSampleStream, params, contextClas, tagger);
			
			SRLEvaluatorForByStepNotNullLabel evaluator = new SRLEvaluatorForByStepNotNullLabel(new SRLMEForIdentification(modelIden,contextIden,parse),
					new SRLMEForClassificationNotNullLabel(modelIden,modelClas,contextIden,contextClas,parse),listeners);
			SRLMeasure measure = new SRLMeasure();
			evaluator.setMeasure(measure);
	        //设置测试集（在测试集上进行评价）
	        evaluator.evaluate(trainingSampleStream.getTestSampleStream());
	        
	        System.out.println(measure);
	        run++;
		}
    }
    
    private static void usage(){
    	System.out.println(SyntacticAnalysisCrossValidationRun.class.getName() + " -data <corpusFile> -parse <parsetype> -encoding <encoding> -type<algorithm>" + 
    "[-cutoff <num>] [-iters <num>] [-folds <nFolds>] ");
    }
    
    public static void main(String[] args) throws IOException {
    	if (args.length < 1)
        {
            usage();
            return;
        }

    	AbstractParseStrategy<HeadTreeNode> parse = null;
        int cutoff = 3;
        int iters = 100;
        int folds = 10;
        File corpusFile = null;
        String parsestr = null;
        String encoding = "UTF-8";
        String type = "MAXENT";
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-data"))
            {
                corpusFile = new File(args[i + 1]);
                i++;
            }
            else if (args[i].equals("-parse"))
            {
            	parsestr = args[i + 1];
                i++;
            }
            else if (args[i].equals("-encoding"))
            {
                encoding = args[i + 1];
                i++;
            }
            else if (args[i].equals("-type"))
            {
                type = args[i + 1];
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
            else if (args[i].equals("-folds"))
            {
                folds = Integer.parseInt(args[i + 1]);
                i++;
            }
        }

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
        SRLContextGenerator contextGenIden = new SRLContextGeneratorConfForIdentification();
        SRLContextGenerator contextGenClas = new SRLContextGeneratorConfForClassification();
        System.out.println(contextGenIden);
        System.out.println(contextGenClas);
        ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(corpusFile), encoding);       
        ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream,parse);
        SRLCVForByStepContainsNullTool run = new SRLCVForByStepContainsNullTool("zh",params);
        run.evaluate(sampleStream,parse,folds,contextGenIden,contextGenClas);
	}
}
