/*
 * MIT License
 *
 * Copyright (c) 2022-2024 Andre_601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ch.andre601.advancedserverlist.core;

import ch.andre601.advancedserverlist.core.profiles.conditions.ProfileConditionParser;
import ch.andre601.expressionparser.ParseWarnCollector;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionParserTests{
    
    private final ProfileConditionParser parser = ProfileConditionParser.create();
    private final DummyPluginLogger logger = new DummyPluginLogger();
    private final Map<String, Boolean> testValuesMap = getTestMap();
    
    @Test
    public void testBasicExpressionResults(){
        for(Map.Entry<String, Boolean> values : testValuesMap.entrySet()){
            logger.info("TEST[expression=\"%s\", expected=%b]", values.getKey(), values.getValue());
            
            ParseWarnCollector collector = new ParseWarnCollector(values.getKey());
            boolean result = parser.evaluate(values.getKey(), null, null, collector);
            
            logger.info("Result (Output, Expected?): %b, %b", result, (result == values.getValue()));
            assertEquals(result, values.getValue());
        }
    }
    
    private Map<String, Boolean> getTestMap(){
        return Map.of(
            "20 > 10", true,
            "10 > 20", false,
            "10 = 10", true,
            "10 = 20", false,
            "(10 - 5) > (1 + 1)", true,
            "(10 - 10) > (1 + 1)", false,
            "100 > 50 and 50 < 100", true,
            "100 < 50 and 50 > 100", false,
            "true or 100 > 10", true,
            "false or 100 < 10", false
        );
    }
}
