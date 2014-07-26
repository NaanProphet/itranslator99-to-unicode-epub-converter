package org.dontexist.kb.service.converter;

public interface PalladioIT2UnicodeConverterService extends Text2UnicodeService {

    /**
     * Converts PalladioIT input to Unicode output.
     * 
     * @param input
     * @return
     */
    String convert(String input);

}