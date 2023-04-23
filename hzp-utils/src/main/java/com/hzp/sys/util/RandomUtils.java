package com.hzp.sys.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具，单例模式
 * @author XuJijun
 *
 */
public class RandomUtils {
	//private static Random random;

	private static ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();

		//双重校验锁获取一个Random单例
		/*if(random==null){
			synchronized (RandomUtils.class) {
				if(random==null){
					random =new Random();
				}
			}
		}
		
		return random;*/
	}

	/**
	 * 获得一个[0,max)之间的随机整数。
	 * @param max 最大值（不包括）
	 * @return 一个[0,max)之间的随机整数
	 */
	public static int getRandomInt(int max) {
		return getRandom().nextInt(max);
	}
	
	/**
	 * 获得一个[min, max]之间的随机整数
	 * @param min 最小值（包括）
	 * @param max 最大值（包括）
	 * @return 一个[min, max]之间的随机整数
	 */
	public static int getRandomInt(int min, int max) {
		return getRandom().nextInt(max-min+1) + min;
	}

	/**
	 * 获得一个[0,max)之间的长整数。
	 * @param max 最大值（不包括）
	 * @return 一个[0,max)之间的长整数
	 */
	public static long getRandomLong(long max) {
		return getRandom().nextLong(max);
	}
	
	/**
	 * 从数组中随机获取一个元素
	 * @param array 待选数组
	 * @return 随机获取的一个元素
	 */
	public static <E> E getRandomElement(E[] array){
		return array[getRandomInt(array.length)];		
	}
	
	/**
	 * 从list中随机取得一个元素
	 * @param list 待选列表
	 * @return 随机取得的一个元素
	 */
	public static <E> E getRandomElement(List<E> list){
		return list.get(getRandomInt(list.size()));
	}
	
	/**
	 * 从set中随机取得一个元素
	 * @param set 待选集合
	 * @return 随机取得的一个元素
	 */
	public static <E> E getRandomElement(Set<E> set){
		int rn = getRandomInt(set.size());
		int i = 0;
		for (E e : set) {
			if(i==rn){
				return e;
			}
			i++;
		}
		return null;
	}
	
	/**
	 * 从map中随机取得一个key
	 * @param map 待选map
	 * @return 从map中随机取得的一个key
	 */
	public static <K, V> K getRandomKeyFromMap(Map<K, V> map) {
		int rn = getRandomInt(map.size());
		int i = 0;
		for (K key : map.keySet()) {
			if(i==rn){
				return key;
			}
			i++;
		}
		return null;
	}
	
	/**
	 * 从map中随机取得一个value
	 * @param map 待选map
	 * @return 从map中随机取得的一个value
	 */
	public static <K, V> V getRandomValueFromMap(Map<K, V> map) {
		int rn = getRandomInt(map.size());
		int i = 0;
		for (V value : map.values()) {
			if(i==rn){
				return value;
			}
			i++;
		}
		return null;
	}
	
	/**
	 * 生成一个含有n位随机数字的字符串，用于验证码等
	 * @param n 位数
	 * @return 含有n位数字的一个String
	 */
	public static String getRandNumberStr(int n) {
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			sb.append(getRandomInt(10)); //随机获取一个0~9之间的数
		}
		return sb.toString();

		/*String rn = "";
		if (n > 0 && n < 10) {
			//Random r = new Random();

			//构造一个上界
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < n; i++) {
				str.append('9');
			}
			int num = Integer.parseInt(str.toString()) + 1;

			while (rn.length() < n) {
				rn = String.valueOf(ThreadLocalRandom.current().nextInt(num));
			}
		} else {
			rn = "0";
		}
		return rn;*/
	}

	/**
	 * 返回一个n个数的数组，范围在[min, max]之间
	 * 可用于生成固定值的随机数组：只要四个参数相同，就可以获取相同的随机数组
	 * @param min 最小值（包括）
	 * @param max 最大值（包括）
	 * @param n 结果数量
	 * @param seed 种子，相同的种子可以确定一个相同的返回结果
	 * @return 一个n个数的数组，范围在[min, max]之间
	 */
	public static int[] getRandInts(int min, int max, int n, long seed){
		if(n > max-min+1){ //越界，返回一个空数组
			return null;
		}

		int[] result = new int[n]; //存放结果的数组
		List<Integer> candidates = new ArrayList<>(max-min+1); //候选数列表
		for(int i=min; i<=max; i++){ //初始化候选数列表
			candidates.add(i);
		}

		Random random = new Random(seed); //使用种子

		for(int i=0; i<n; i++){ //从候选数列表中随机选取n个数，放进结果数组中，并从候选列表中删掉该数，以避免重复获取到它
			int index = random.nextInt(max-min-i+1); //随机获得一个下标
			result[i] = candidates.remove(index); //根据下标获取一个数，同时从候选列表中删掉它
		}

		//return random.ints(n, min, max).toArray(); //这种方法会获取到重复的值
		return result;
	}

	public static void main(String[] args) throws Exception {
		/*Set<String> set = new HashSet<>();
		for (int i = 0; i < 12; i++) {
			set.add("I am: " + i);	
		}
		
		for (int i = 0; i < 10; i++) {
			System.out.println(getRandomElement(set));
		}*/
		
		//System.out.println(getRandom().nextInt(-100, 10));
		/*ObjectMapper om = new ObjectMapper();

		long seed = System.currentTimeMillis();
		System.out.println(om.writeValueAsString(getRandInts(1, 10, 10, seed)));
		System.out.println(om.writeValueAsString(getRandInts(1, 10, 10, seed)));
		System.out.println(om.writeValueAsString(getRandInts(1, 10000, 30, seed)));
		System.out.println(om.writeValueAsString(getRandInts(1, 10000, 30, seed)));
		System.out.println(om.writeValueAsString(getRandInts(1, 10, 11, seed)));*/

		System.out.println(getRandNumberStr(6));
		System.out.println(getRandNumberStr(5));
		System.out.println(getRandNumberStr(4));
		System.out.println(getRandNumberStr(10));
	}
}
