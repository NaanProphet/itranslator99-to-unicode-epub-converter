package org.dontexist.kb;


import junit.framework.Assert;

import org.junit.Test;

public class Sanskrit99ToUnicodeConverterTest {

	private Sanskrit99ToUnicodeConverter converter = new Sanskrit99ToUnicodeConverter();
	
	@Test
	public void testConvert1() throws Exception {
		String input = "nih&lt; AnIit nih&lt; kDu àÉuta$, sunhu krhu jae tuMhih saeha$.";
		String actual = converter.convert(input);
		String expected = "नहिं अनीति नहिं कछु प्रभुताई। सुनहु करहु जो तुम्हहि सोहाई॥";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testConvert2() throws Exception {
		String input = "jgit àaßuyaTSwangaErv&lt;";
		String actual = converter.convert(input);
		String expected = "जगति प्राप्नुयात्स्थानगौरवं";
		Assert.assertEquals(expected, actual);
	}
}
