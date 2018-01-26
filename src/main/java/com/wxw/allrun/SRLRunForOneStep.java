package com.wxw.allrun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.wxw.evaluate.SRLErrorPrinter;
import com.wxw.evaluate.SRLMeasure;
import com.wxw.feature.SRLContextGenerator;
import com.wxw.feature.SRLContextGeneratorConf;
import com.wxw.onestep.SRLCrossValidationForOneStep;
import com.wxw.onestep.SRLEvaluatorForOneStep;
import com.wxw.onestep.SRLMEForOneStep;
import com.wxw.onestep.SRLModelForOneStep;
import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseAddNULL_101HasPruning;
import com.wxw.stream.FileInputStreamFactory;
import com.wxw.stream.PlainTextByTreeStream;
import com.wxw.stream.SRLSample;
import com.wxw.stream.SRLSampleStream;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;

/**
 * 运行类
 * @author 王馨苇
 *
 */
public class SRLRunForOneStep {

	private static String flag = "train";
	private static AbstractParseStrategy<HeadTreeNode> parse = new SRLParseAddNULL_101HasPruning();
	//静态内部类
	public static class Corpus{
		//文件名和编码
		public String name;
		public String encoding;
		public String trainFile;
		public String testFile;
		public String modelFile;
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
		InputStream configStream = SRLRunForOneStep.class.getClassLoader().getResourceAsStream("com/wxw/run/corpus.properties");
		config.load(configStream);
		Corpus[] corpora = getCorporaFromConf(config);
        //定位到某一语料
        Corpus corpus = getCorpus(corpora, corpusName);
        SRLContextGenerator contextGen = getContextGenerator(config);
        ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(new File(corpus.trainFile)), corpus.encoding);
        
        ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream,parse);

        //默认参数
        TrainingParameters params = TrainingParameters.defaultParams();
        params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(1));

        //把刚才属性信息封装
        SRLCrossValidationForOneStep crossValidator = new SRLCrossValidationForOneStep("zh", params);

        System.out.println(contextGen);
        crossValidator.evaluate(sampleStream, 10, contextGen);
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
        InputStream configStream = SRLRunForOneStep.class.getClassLoader().getResourceAsStream("com/wxw/run/corpus.properties");
        config.load(configStream);
        Corpus[] corpora = getCorporaFromConf(config);//获取语料

        SRLContextGenerator contextGen = getContextGenerator(config);

        runFeatureOnCorporaByFlag(contextGen, corpora, params);
	}

	/**
	 * 根据命令行参数执行相应的操作
	 * @param contextGen 上下文特征生成器
	 * @param corpora 语料信息内部类对象数组
	 * @param params 训练模型的参数
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private static void runFeatureOnCorporaByFlag(SRLContextGenerator contextGen, Corpus[] corpora,
			TrainingParameters params) throws FileNotFoundException, IOException {
		if(flag == "train" || flag.equals("train")){
			for (int i = 0; i < corpora.length; i++) {
				trainOnCorpus(contextGen,corpora[i],params);
			}
		}else if(flag == "model" || flag.equals("model")){
			for (int i = 0; i < corpora.length; i++) {
				modelOutOnCorpus(contextGen,corpora[i],params);
			}
		}else if(flag == "evaluate" || flag.equals("evaluate")){
			for (int i = 0; i < corpora.length; i++) {
				evaluateOnCorpus(contextGen,corpora[i],params);
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
	private static void evaluateOnCorpus(SRLContextGenerator contextGen, Corpus corpus,
			TrainingParameters params) throws IOException {
		System.out.println("ContextGenerator: " + contextGen);
		SRLModelForOneStep model = new SRLModelForOneStep(new File(corpus.modelFile));
		SRLMEForOneStep tagger = new SRLMEForOneStep(model,contextGen,parse);
       
		SRLMeasure measure = new SRLMeasure();
		SRLEvaluatorForOneStep evaluator = null;
		SRLErrorPrinter printer = null;
        if(corpus.errorFile != null){
        	System.out.println("Print error to file " + corpus.errorFile);
        	printer = new SRLErrorPrinter(new FileOutputStream(corpus.errorFile));    	
        	evaluator = new SRLEvaluatorForOneStep(tagger,printer);
        }else{
        	evaluator = new SRLEvaluatorForOneStep(tagger);
        }
        evaluator.setMeasure(measure);
        ObjectStream<String[]> linesStream = new PlainTextByTreeStream(new FileInputStreamFactory(new File(corpus.testFile)), corpus.encoding);
        ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(linesStream,parse);
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
	 * @throws IOException 
	 */	
	private static void modelOutOnCorpus(SRLContextGenerator contextGen, Corpus corpus,
			TrainingParameters params) throws IOException {
		System.out.println("ContextGenerator: " + contextGen);
        System.out.println("Training on " + corpus.name + "...");
        //训练模型
        SRLMEForOneStep me = new SRLMEForOneStep(parse);
        me.train(new File(corpus.trainFile), params, contextGen, corpus.encoding);		
	}

	/**
	 * 训练模型
	 * @param contextGen 上下文特征生成器
	 * @param corpus 语料对象
	 * @param params 训练模型的参数
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */	
	private static void trainOnCorpus(SRLContextGenerator contextGen, Corpus corpus, TrainingParameters params) throws FileNotFoundException, IOException {
		System.out.println("ContextGenerator: " + contextGen);
        System.out.println("Training on " + corpus.name + "...");
        //训练模型
        SRLMEForOneStep me = new SRLMEForOneStep(parse);
        me.train(new File(corpus.trainFile), new File(corpus.modelFile),params, contextGen, corpus.encoding);
        //corpus.trainFile, corpus.modelFile,params, contextGen, corpus.encoding
	}

	/**
	 * 得到生成特征的实例对象
	 * @param config 配置文件
	 * @return
	 */
	private static SRLContextGenerator getContextGenerator(Properties config) {
		String featureClass = config.getProperty("feature.class");
		if(featureClass.equals("com.wxw.feature.SRLContextGeneratorConf")){
			//初始化需要哪些特征
        	return  new SRLContextGeneratorConf(config);
		}else{
			return null;
		} 
	}

	private static Corpus[] getCorporaFromConf(Properties config) {
		Corpus[] corpuses = new Corpus[corpusName.length];
		for (int i = 0; i < corpuses.length; i++) {
			String name = corpusName[i];
			String encoding = config.getProperty(name + "." + "corpus.encoding");
			String trainFile = config.getProperty(name + "." + "corpus.train.file");
			String testFile = config.getProperty(name+"."+"corpus.test.file");
			String modelFile = config.getProperty(name + "." + "corpus.model.file");
			String errorFile = config.getProperty(name + "." + "corpus.error.file");
			Corpus corpus = new Corpus();
			corpus.name = name;
			corpus.encoding = encoding;
			corpus.trainFile = trainFile;
			corpus.testFile = testFile;
			corpus.modelFile = modelFile;
			corpus.errorFile = errorFile;
			corpuses[i] = corpus;			
		}
		return corpuses;
	}
}
