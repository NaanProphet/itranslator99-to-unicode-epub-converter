package org.dontexist.kb;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class UnicodeCharacterCreatorTest {

	@Test
	public void test() throws UnsupportedEncodingException {
		char[] _0950 = Character.toChars(0x950);
		System.out.println(_0950[0]);
		String s = Character.toString((char) 2384);
		s = new String(Character.toString((char) 2384).getBytes(), "UTF8");
		System.out.println(s);
		System.out.println("ॐ");
	}
	
	@Test
	public void test2() throws Exception {
		char c = 0xCF;
		System.out.println(c);
		
		String s = new String(Character.toString((char) 0xCF).getBytes(), "UTF8");
		System.out.println(s);
	}

}
