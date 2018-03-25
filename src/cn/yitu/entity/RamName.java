package cn.yitu.entity;

import java.util.Random;

public class RamName {
	
	private static String ramName;
	private final static String head="solace_";
	
	/**
	 * 为新用户注册随机名字
	 * @return
	 */
	public static String getRamName() {
		int tail;
		Random rand=new Random();
		tail=rand.nextInt(9999)+1;
		ramName=head+tail;
		return ramName;
	}

}
