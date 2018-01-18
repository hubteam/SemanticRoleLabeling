package com.wxw.evaluate;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * 指标计算类的测试
 * @author 王馨苇
 *
 */
public class SRLMeasureTest {

	private SRLMeasure measure;
	private String[] ref;
	private String[] pre;
	
	@Before
	public void setUp(){
		measure = new SRLMeasure();
		ref = new String[]{"ARG0","NULL","ARGM-DIS","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL"};
		pre = new String[]{"NULL","NULL","ARGM-DIS","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL","NULL"};
	    measure.update(ref, pre);
	}
	
	@Test
	public void test(){
		assertEquals(measure.getPrecisionScore(),1.0,0.001);
		assertEquals(measure.getRecallScore(),0.5,0.001);
		assertEquals(measure.getMeasure(),0.666,0.001);
	}
}
