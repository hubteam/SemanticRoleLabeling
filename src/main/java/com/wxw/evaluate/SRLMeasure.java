package com.wxw.evaluate;

/**
 * 指标计算类
 * @author 王馨苇
 *
 */
public class SRLMeasure {

	private long truePositive;
	private long selected;
	private long target;
	private long truePositiveIden;
	private long selectedIden;
	private long targetIden;
	private long truePositiveClas;
	private long selectedClas;
	private long targetClas;
	
	/**
	 * 最终结果指标评价
	 * @param references 参考的
	 * @param predictions 预测的
	 */
	public void update(String[] references,String[] predictions){
		for (int i = 0; i < predictions.length; i++) {
			if(references[i].equals(predictions[i])){
				if(!references[i].equals("NULL") && !predictions[i].equals("NULL")){
					truePositive++;
				}
			}
		}
		for (int i = 0; i < predictions.length; i++) {
			if(!predictions[i].equals("NULL")){
				selected++;
			}
		}
		
		for (int i = 0; i < references.length; i++) {
			if(!references[i].equals("NULL")){
				target++;
			}
		}
	}
	
	/**
	 * 识别阶段的结果指标评价
	 * @param references 参考的
	 * @param predictions 预测的
	 */
	public void updateForIden(String[] references,String[] predictions){
		for (int i = 0; i < predictions.length; i++) {
			if(references[i].equals(predictions[i])){
				truePositiveIden++;
			}
		}
		selectedIden += predictions.length;
		
		targetIden += references.length;
	}
	
	/**
	 * 在识别阶段的
	 * @param references 参考的
	 * @param predictions 预测的
	 */
	public void updateForClas(String[] references,String[] predictions){
		for (int i = 0; i < predictions.length; i++) {
			if(references[i].equals(predictions[i])){
				truePositiveClas++;
			}
		}
		selectedClas += predictions.length;
		targetClas += references.length;

	}
	
	public double getPrecisionScore() {
        return selected > 0 ? (double) truePositive / (double) selected : 0;
    }

    public double getRecallScore() {
        return target > 0 ? (double) truePositive / (double) target : 0;
    }
    
    public double getMeasure() {

        if (getPrecisionScore() + getRecallScore() > 0) {
            return 2 * (getPrecisionScore() * getRecallScore())
                    / (getPrecisionScore() + getRecallScore());
        } else {
            return -1;
        }
    }
    
    public double getPrecisionScoreIden() {
        return selectedIden > 0 ? (double) truePositiveIden / (double) selectedIden : 0;
    }

    public double getRecallScoreIden() {
        return targetIden > 0 ? (double) truePositiveIden / (double) targetIden : 0;
    }
    
    public double getMeasureIden() {

        if (getPrecisionScoreIden() + getRecallScoreIden() > 0) {
            return 2 * (getPrecisionScoreIden() * getRecallScoreIden())
                    / (getPrecisionScoreIden() + getRecallScoreIden());
        } else {
            return -1;
        }
    }
    
    public double getPrecisionScoreClas() {
        return selectedClas > 0 ? (double) truePositiveClas / (double) selectedClas : 0;
    }

    public double getRecallScoreClas() {
        return targetClas > 0 ? (double) truePositiveClas / (double) targetClas : 0;
    }
    
    public double getMeasureClas() {

        if (getPrecisionScoreClas() + getRecallScoreClas() > 0) {
            return 2 * (getPrecisionScoreClas() * getRecallScoreClas())
                    / (getPrecisionScoreClas() + getRecallScoreClas());
        } else {
            return -1;
        }
    }
    
    @Override
    public String toString() {
        return ""+ "------------最终的结果------------" + "\n"
        		+ "Precision: " + Double.toString(getPrecisionScore()) + "\n"
                + "Recall: " + Double.toString(getRecallScore()) + "\n" 
        		+ "F-Measure: "
                + Double.toString(getMeasure()) +"\n"
                + "------------识别的结果------------" + "\n"
        		+ "Precision: " + Double.toString(getPrecisionScoreIden()) + "\n"
                + "Recall: " + Double.toString(getRecallScoreIden()) + "\n" 
        		+ "F-Measure: "
                + Double.toString(getMeasureIden()) +"\n"
                + "--------在识别阶段的结果上进行分类的结果--------" + "\n"
        		+ "Precision: " + Double.toString(getPrecisionScoreClas()) + "\n"
                + "Recall: " + Double.toString(getRecallScoreClas()) + "\n" 
        		+ "F-Measure: "
                + Double.toString(getMeasureClas());
    }
}
