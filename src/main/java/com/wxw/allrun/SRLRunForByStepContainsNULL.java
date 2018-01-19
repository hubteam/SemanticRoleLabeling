package com.wxw.allrun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.wxw.bystep.SRLCrossValidationForByStepContainsNullLabel;
import com.wxw.bystep.SRLEvaluatorForByStepContainsNullLabel;
import com.wxw.bystep.SRLMEForClassificationContainsNullLabel;
import com.wxw.bystep.SRLMEForIdentification;
import com.wxw.bystep.SRLModelForClassification;
import com.wxw.bystep.SRLModelForIdentification;
import com.wxw.evaluate.SRLErrorPrinter;
import com.wxw.evaluate.SRLMeasure;
import com.wxw.feature.SRLContextGenerator;
import com.wxw.feature.SRLContextGeneratorConfForClassification;
import com.wxw.feature.SRLContextGeneratorConfForIdentification;
import com.wxw.stream.FileInputStreamFactory;
import com.wxw.stream.PlainTextByTreeStream;
import com.wxw.stream.SRLSample;
import com.wxw.stream.SRLSampleStream;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;

/**
 * 分步训练模型（分类阶段包含NULL标记）
 * @author 王馨苇
 *
 */
public class SRLRunForByStepContainsNULL {
	private static String flag = "train";
	//静态内部类
	public static class Corpus{
		//文件名和编码
		public String name;
		public String encoding;
		public String trainFile;
		public String testFile;
		public String classificationmodelFile;
		public String identificationmodelFile;
		public String errorFile;
	}
	
	private static String[] corpusName = {"tree"};
	
	public static void main(String[] args) throws IOException {
		String cmd = args[0];
		if(cmd.equals("-train")){
			flag = "train";
			runFeature();
		}else if(cmd.equals("-model")){
			flag = "model";
			runFeature();
		}else if(cmd.equals("-evaluate")){
			flag = "evaluate";
			runFeature();
		}else if(cmd.equals("-cross")){
			String corpus = args[1];
			crossValidation(corpus);
		}
	}

	/**
	 * 交叉验证
	 * @param corpus 语料的名称
	 * @throws IOException 
	 */
	private static void crossValidation(String corpusName) throws IOException {
		Properties config = new Properties();
		InputStream configStream = SRLRunForByStepContainsNULL.class.getClassLoader().getResourceAsStream("com/wxw/run/corpus.properties");
		config.load(configStream);
		Corpus[] corpora = getCorporaFromConf(config);
        //定位到某一语料
        Corpus corpus = getCorpus(corpora, corpusName);
        SRLContextGenerator contextIden = new SRLContextGeneratorConfForIdentification(config);
        SRLContextGenerator contextClas = new SRLContextGeneratorConfForClassification(config);
        ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(new File(corpus.trainFile)), corpus.encoding);
        
        ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream);

        //默认参数
        TrainingParameters params = TrainingParameters.defaultParams();
        params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(1));

        //把刚才属性信息封装
        SRLCrossValidationForByStepContainsNullLabel crossValidator = new SRLCrossValidationForByStepContainsNullLabel("zh", params);

        System.out.println(contextIden);
        System.out.println(contextClas);
        crossValidator.evaluate(sampleStream, 10, contextIden, contextClas);
	}
	
	/**
	 * 根据语料名称获取某个语料
	 * @param corpora 语料内部类数组，包含了所有语料的信息
	 * @param corpusName 语料的名称
	 * @return
	 */
	private static Corpus getCorpus(Corpus[] corpora, String corpusName) {
		for (Corpus c : corpora) {
            if (c.name.equalsIgnoreCase(corpusName)) {
                return c;
            }
        }
        return null;
	}

	/**
	 * 根据配置文件配置的信息获取特征
	 * @throws IOException IO异常
	 */
	private static void runFeature() throws IOException {
		//配置参数
		TrainingParameters params = TrainingParameters.defaultParams();
		params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(3));
	
		//加载语料文件
        Properties config = new Properties();
        InputStream configStream = SRLRunForByStepContainsNULL.class.getClassLoader().getResourceAsStream("com/wxw/run/corpus.properties");
        config.load(configStream);
        Corpus[] corpora = getCorporaFromConf(config);//获取语料

        runFeatureOnCorporaByFlag(corpora, params,config);
	}

	/**
	 * 根据命令行参数执行相应的操作
	 * @param corpora 语料信息内部类对象数组
	 * @param params 训练模型的参数
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private static void runFeatureOnCorporaByFlag(Corpus[] corpora,
			TrainingParameters params,Properties config) throws FileNotFoundException, IOException {
		if(flag == "train" || flag.equals("train")){
			for (int i = 0; i < corpora.length; i++) {
				trainOnCorpus(corpora[i],params,config);
			}
		}else if(flag == "model" || flag.equals("model")){
			for (int i = 0; i < corpora.length; i++) {
				modelOutOnCorpus(corpora[i],params,config);
			}
		}else if(flag == "evaluate" || flag.equals("evaluate")){
			for (int i = 0; i < corpora.length; i++) {
				evaluateOnCorpus(corpora[i],params,config);
			}
		}	
	}
	
	/**
	 * 读取模型，评估模型
	 * @param contextGen 上下文特征生成器
	 * @param corpus 语料对象
	 * @param params 训练模型的参数
	 * @throws UnsupportedOperationException 
	 * @throws IOException 
	 */	
	private static void evaluateOnCorpus(Corpus corpus,
			TrainingParameters params,Properties config) throws IOException {
		SRLContextGenerator contextIden = new SRLContextGeneratorConfForIdentification(config);
		SRLContextGenerator contextClas = new SRLContextGeneratorConfForClassification(config);
		System.out.println("ContextGenerator: " + contextIden);
		System.out.println("ContextGenerator: " + contextClas);
		SRLModelForIdentification modelIden = new SRLModelForIdentification(new File(corpus.identificationmodelFile));
		SRLMEForIdentification taggerIden = new SRLMEForIdentification(modelIden,contextIden);
		SRLModelForClassification modelClas = new SRLModelForClassification(new File(corpus.classificationmodelFile));
		SRLMEForClassificationContainsNullLabel taggerClas = new SRLMEForClassificationContainsNullLabel(modelClas,contextClas);
       
		SRLMeasure measure = new SRLMeasure();
		SRLEvaluatorForByStepContainsNullLabel evaluator = null;
		SRLErrorPrinter printer = null;
        if(corpus.errorFile != null){
        	System.out.println("Print error to file " + corpus.errorFile);
        	printer = new SRLErrorPrinter(new FileOutputStream(corpus.errorFile));    	
        	evaluator = new SRLEvaluatorForByStepContainsNullLabel(taggerIden,taggerClas,printer);
        }else{
        	evaluator = new SRLEvaluatorForByStepContainsNullLabel(taggerIden,taggerClas);
        }
        evaluator.setMeasure(measure);
        ObjectStream<String[]> linesStream = new PlainTextByTreeStream(new FileInputStreamFactory(new File(corpus.testFile)), corpus.encoding);
        ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(linesStream);
        evaluator.evaluate(sampleStream);
        SRLMeasure measureRes = evaluator.getMeasure();
        System.out.println("--------结果--------");
        System.out.println(measureRes);
	}

	/**
	 * 训练模型，输出模型文件
	 * @param contextGen 上下文特征生成器
	 * @param corpus 语料对象
	 * @param params 训练模型的参数
	 * @throws UnsupportedOperationException 
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */	
	private static void modelOutOnCorpus(Corpus corpus,
			TrainingParameters params,Properties config) throws IOException {
		SRLContextGenerator contextIden = new SRLContextGeneratorConfForIdentification(config);
		SRLContextGenerator contextClas = new SRLContextGeneratorConfForClassification(config);
		System.out.println("ContextGenerator: " + contextIden);
		System.out.println("ContextGenerator: " + contextClas);
        System.out.println("Training on " + corpus.name + "...");
        //训练模型
        SRLModelForIdentification modelIden = SRLMEForIdentification.train(new File(corpus.trainFile), new File(corpus.identificationmodelFile), params, contextIden, corpus.encoding);
		SRLMEForIdentification taggerIden = new SRLMEForIdentification(modelIden,contextIden);
        SRLMEForClassificationContainsNullLabel.train(new File(corpus.trainFile), new File(corpus.classificationmodelFile), params, contextIden, corpus.encoding,taggerIden);
		
	}

	/**
	 * 训练模型
	 * @param contextGen 上下文特征生成器
	 * @param corpus 语料对象
	 * @param params 训练模型的参数
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */	
	private static void trainOnCorpus(Corpus corpus, TrainingParameters params,Properties config) throws FileNotFoundException, IOException {
		SRLContextGenerator contextIden = new SRLContextGeneratorConfForIdentification(config);
		SRLContextGenerator contextClas = new SRLContextGeneratorConfForClassification(config);
		System.out.println("ContextGenerator: " + contextIden);
		System.out.println("ContextGenerator: " + contextClas);
        System.out.println("Training on " + corpus.name + "...");
        //训练模型
        SRLModelForIdentification modelIden = SRLMEForIdentification.train(new File(corpus.trainFile), params, contextIden, corpus.encoding);
		SRLMEForIdentification taggerIden = new SRLMEForIdentification(modelIden,contextIden);
        SRLMEForClassificationContainsNullLabel.train(new File(corpus.trainFile), params, contextClas, corpus.encoding,taggerIden);
	}

	private static Corpus[] getCorporaFromConf(Properties config) {
		Corpus[] corpuses = new Corpus[corpusName.length];
		for (int i = 0; i < corpuses.length; i++) {
			String name = corpusName[i];
			String encoding = config.getProperty(name + "." + "corpus.encoding");
			String trainFile = config.getProperty(name + "." + "corpus.train.file");
			String testFile = config.getProperty(name+"."+"corpus.test.file");
			String identificationmodelFile = config.getProperty(name + "." + "corpus.identificationmodel.file");
			String classificationmodelFile = config.getProperty(name + "." + "corpus.classificationmodel.file");
			String errorFile = config.getProperty(name + "." + "corpus.error.file");
			Corpus corpus = new Corpus();
			corpus.name = name;
			corpus.encoding = encoding;
			corpus.trainFile = trainFile;
			corpus.testFile = testFile;
			corpus.identificationmodelFile = identificationmodelFile;
			corpus.classificationmodelFile = classificationmodelFile;
			corpus.errorFile = errorFile;
			corpuses[i] = corpus;			
		}
		return corpuses;
	}
}
