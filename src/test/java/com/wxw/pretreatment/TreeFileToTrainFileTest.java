package com.wxw.pretreatment;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 合并宾州树库语料和语义角色标注语料的测试
 * @author 王馨苇
 *
 */
public class TreeFileToTrainFileTest {

	private URL url1;
	private URL url2 ;
	private URL url3 ;
	private BufferedReader br1 ;
	private BufferedReader br2 ;
	private BufferedReader br3 ;
	private String treecontent ;
	private String newtreecontent ;
	
	@Before
	public void setUp() throws FileNotFoundException{
		url1 = TreeFileToTrainFileTest.class.getClassLoader().getResource("wsj_0008.mrg");
		url2 = TreeFileToTrainFileTest.class.getClassLoader().getResource("partprop.txt");
		url3 = TreeFileToTrainFileTest.class.getClassLoader().getResource("wsj_0008new.mrg");
		br1 = new BufferedReader(new FileReader(url1.getFile()));
		br2 = new BufferedReader(new FileReader(url2.getFile()));
		br3 = new BufferedReader(new FileReader(url3.getFile()));
		treecontent = "";
		newtreecontent = "";
	}
	
	@Test
	public void test() throws NumberFormatException, IOException{
		String semanticline = "";
		String treeline = "";
		String newcontent = "";
		String tree = "";
		int left = 0;
		int right = 0;
		int treecount = -1;
		while((treeline = br1.readLine()) != null){			
			if(treeline != "" && !treeline.equals("")){
				tree += treeline+"\n";
				treeline = treeline.replaceAll("\n","");
				char[] c = treeline.trim().toCharArray();
				for (int j = 0; j < c.length; j++) {
					if(c[j] == '('){
						left++;
					}else if(c[j] == ')'){
						right++;
					}
				}
				if(left == right){
					treecount++;
					if(!semanticline.equals("")){
						String temp = semanticline.trim().replaceAll("\n","");
						if(Integer.parseInt(temp.split(" ")[1]) == treecount){
							treecontent += tree+temp+"\n"+"\n";
						}
					}
					while((semanticline = br2.readLine()) != null){
						String temp = semanticline.trim().replaceAll("\n","");
						if(Integer.parseInt(temp.split(" ")[1]) == treecount){
							treecontent += tree+temp+"\n"+"\n";
						}else{
							break;
						}
					}
					tree = "";
				}
			}
		}
		while((newcontent = br3.readLine()) != null){
			newtreecontent += newcontent+"\n";
		}
		assertEquals(treecontent,newtreecontent+"\n");
	}
	
	@After
	public void tearDown() throws IOException{
		br1.close();
		br2.close();
		br3.close();
	}
}
