package org.dontexist.kb;

import java.io.File;
import java.io.IOException;

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
	
	// --------------- HELPER METHODS -----------------
	
	private void verify(File inputfile, File actualOutputFile, File expectedOutputFile) throws IOException, Exception {
		actualOutputFile.deleteOnExit();
		StringBuilder convertedFileAsString = (StringBuilder) ReflectionTestUtils.invokeMethod(springDriver, "convertFileToUnicode", inputfile);
		FileUtils.writeStringToFile(actualOutputFile, convertedFileAsString.toString());
		AssertFile.assertFileEquals(expectedOutputFile, actualOutputFile);
	}
	
	// ---------------- FILE INTEGRATION TESTS ---------

	@Test
	public void testConvertFileToUnicode_Unchanged1() throws Exception {
		// this file contains no eligible characters (Sanskrit99 or Palladio IT)
		// to convert. it should be unchanged by the conversion process, except
		// for &ndash;
		File inputfile = new File("src/test/resources/org/dontexist/kb/part0005.html");
		File actualOutputFile = new File("src/test/resources/org/dontexist/kb/part0005.html.out");
		File expectedOutputFile = inputfile;
		verify(inputfile, actualOutputFile, expectedOutputFile);
	}

	@Test
	public void testConvertFileToUnicode_Convert1() throws Exception {
		// sample from "Marriage A Melody" containing Sanskrit99 and PalladioIT
		File inputfile = new File("src/test/resources/org/dontexist/kb/part0006.html");
		File actualOutputFile = new File("src/test/resources/org/dontexist/kb/part0006.html.out");
		File expectedOutputFile = new File("src/test/resources/org/dontexist/kb/part0006.html.expected");
		verify(inputfile, actualOutputFile, expectedOutputFile);
	}

}
