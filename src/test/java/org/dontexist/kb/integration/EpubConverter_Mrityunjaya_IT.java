package org.dontexist.kb.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-context.xml")
public class EpubConverter_Mrityunjaya_IT extends AbstractEpubConverterIT {

    private final boolean deleteOutputFile = true;

    @Test
    public void testConvertFileAsOneStringToUnicode_Unchanged1() throws Exception {
        // this file contains no eligible characters (Sanskrit99 or Palladio IT)
        // to convert. it should be unchanged by the conversion process, except
        // for &ndash;
        File inputfile = new File("src/test/resources/org/dontexist/kb/mrit.part0003.trim.xml");
        File actualOutputFile = new File("src/test/resources/org/dontexist/kb/mrit.part0003.trim.xml.out");
        File expectedOutputFile = inputfile;
        verify(inputfile, actualOutputFile, expectedOutputFile, deleteOutputFile);
    }

    @Test
    public void testConvertFileAsOneStringToUnicode_Convert1() throws Exception {
        // sample from "Maha Mrityunjaya Mantra" containing Sanskrit99 and PalladioIT, copyright CCMT
        File inputfile = new File("src/test/resources/org/dontexist/kb/mrit.part0003.xml");
        File actualOutputFile = new File("src/test/resources/org/dontexist/kb/mrit.part0003.xml.out");
        File expectedOutputFile = new File("src/test/resources/org/dontexist/kb/mrit.part0003.xml.expected");
        verify(inputfile, actualOutputFile, expectedOutputFile, deleteOutputFile);
    }

}
