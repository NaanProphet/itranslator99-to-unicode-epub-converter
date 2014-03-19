package org.dontexist.kb.integration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dontexist.kb.SpringDriver;
import org.dontexist.kb.service.EpubReaderService;
import org.springframework.batch.test.AssertFile;
import org.springframework.beans.factory.annotation.Autowired;

abstract class AbstractEpubConverterIT {

	@Autowired
	protected SpringDriver springDriver;

	@Autowired
	protected EpubReaderService epubReaderService;
	
	// --------------- HELPER METHODS -----------------

	protected void verify(File inputfile, File actualOutputFile, File expectedOutputFile) throws IOException, Exception {
		actualOutputFile.deleteOnExit();
//		StringBuilder convertedFileAsString = (StringBuilder) ReflectionTestUtils.invokeMethod(springDriver, "convertFileToUnicode",
//				inputfile);
		StringBuilder convertedFileAsString = epubReaderService.convertFileAsOneStringToUnicode(FileUtils.readFileToString(inputfile));
		FileUtils.writeStringToFile(actualOutputFile, convertedFileAsString.toString());
		AssertFile.assertFileEquals(expectedOutputFile, actualOutputFile);
	}

}
