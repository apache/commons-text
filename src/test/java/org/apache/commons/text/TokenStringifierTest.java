package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TokenStringifierTest {

    @Test
    public void testTokenStringifier() {
        TokenStringifier stringifier = new TokenStringifier(TokenFormatterFactory.constantFormatter(',', true), TokenFormatterFactory.noOpFormatter());
        List<String> tokens = Arrays.asList(new String[]{"my", "csv", "tokens"});
        stringifier.reset(tokens);
        String csv = stringifier.getString();
        assertEquals("my,csv,tokens", csv);
        //double check that csv tokenizer can read the csv string
        StringTokenizer csvTokenizer = StringTokenizer.getCSVInstance(csv);
        csvTokenizer.reset(csv);
        List<String> tokenizerTokens = csvTokenizer.getTokenList();
        assertEquals(tokens, tokenizerTokens);
    }
}
