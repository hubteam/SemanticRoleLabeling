package com.wxw.pretreatment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 将所有的训练语料和语义信息语料合并成一个完整的训练语料
 * @author 王馨苇
 *
 */
public class TreeFileToTrainFile {

	/**
	 * 将所有的训练语料和语义信息语料合并成一个完整的训练语料
	 * @param fromtreepath 树文件来源的地址
	 * @param fromsemanticpath 语义文件来源地址
	 * @param topath 新文件输出地址
	 * @throws IOException 
	 */
	public static void treeFileToTrainFile(String fromtreepath, String fromsemanticpath,String topath) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(topath)));
		BufferedReader br1 = null;
		BufferedReader br2 = new BufferedReader(new FileReader(new File(fromsemanticpath)));
		String filename = "";
		String semanticline = "";
		for (int i = 1; i < 200; i++) {
			if(i < 10){
				filename = "000" + i;
			}else if(i < 100){
				filename = "00" + i;
			}else{
				filename = "0" + i;
			}
			String treefilename = "wsj_"+filename+".mrg";
			int treecount = -1;
			br1 = new BufferedReader(new FileReader(new File(fromtreepath+"/wsj_"+filename+".mrg")));	
			String treeline = "";
			String treecontent = "";
			int left = 0;
			int right = 0;
			while((treeline = br1.readLine()) != null){			
				if(treeline != "" && !treeline.equals("")){
					treecontent += treeline+"\n";
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
							if(temp.split(" ")[0].split("/")[2].equals(treefilename) &&
									Integer.parseInt(temp.split(" ")[1]) == treecount){
								bw.write(treecontent);
								bw.write(temp);
								bw.newLine();
								bw.newLine();
							}
						}
						while((semanticline = br2.readLine()) != null){
							String temp = semanticline.trim().replaceAll("\n","");
							if(temp.split(" ")[0].split("/")[2].equals(treefilename) &&
									Integer.parseInt(temp.split(" ")[1]) == treecount){
								bw.write(treecontent);
								bw.write(temp);
								bw.newLine();
								bw.newLine();
							}else{
								break;
							}
						}
						treecontent = "";
					}
				}
			}
		}
		System.out.println("success");
		bw.close();
		br1.close();
		br2.close();
	}
	
	public static void main(String[] args) throws IOException {
		String cmd = args[0];
		if(cmd.equals("-totrain")){
			String fromtreepath = args[1];
			String fromsemanticpath = args[2];
			String topath = args[3];
			TreeFileToTrainFile.treeFileToTrainFile(fromtreepath, fromsemanticpath, topath);
		}
	}
}
