package org.dontexist.kb;

import java.io.File;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.AssertFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-context.xml")
public class SpringDriverTest {

	@Autowired
	private SpringDriver springDriver;

	@Test
	public void testConvertFileToUnicode_Unchanged1() throws Exception {
		// this file contains no eligible characters (Sanskrit99 or Palladio IT)
		// to convert. it should be unchanged by the conversion process, except
		// for &ndash;
		File file = new File("src/test/resources/org/dontexist/kb/part0005.html");
		File oFile = new File("src/test/resources/org/dontexist/kb/part0005.html.out");
		oFile.deleteOnExit();
		StringBuffer convertedFileAsString = (StringBuffer) ReflectionTestUtils.invokeMethod(springDriver, "convertFileToUnicode", file);
		FileUtils.writeStringToFile(oFile, convertedFileAsString.toString());

		AssertFile.assertFileEquals(file, oFile);
	}

}
