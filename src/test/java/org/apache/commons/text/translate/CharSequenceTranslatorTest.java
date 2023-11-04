package org.apache.commons.text.translate;


import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class CharSequenceTranslatorTest {
    @Test
    public void testWithMethod(){
        CharSequenceTranslator translator1 = new AggregateTranslator();
        CharSequenceTranslator translator2 = new AggregateTranslator();
        CharSequenceTranslator translator3 = new AggregateTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);

        assertTrue(mergedTranslator instanceof CharSequenceTranslator);
    }
}
