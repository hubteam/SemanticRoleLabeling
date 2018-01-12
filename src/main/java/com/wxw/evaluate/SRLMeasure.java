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
    
    @Override
    public String toString() {
        return "Precision: " + Double.toString(getPrecisionScore()) + "\n"
                + "Recall: " + Double.toString(getRecallScore()) + "\n" 
        		+ "F-Measure: "
                + Double.toString(getMeasure());
    }
}
