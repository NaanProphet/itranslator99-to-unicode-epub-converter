package org.dontexist.kb.misc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.script.*;
import java.io.File;
import java.io.FileReader;

public class JavascriptTest {

    private ScriptEngine engine;

    @Before
    public void setup() {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("JavaScript");
    }

    @Test
    public void testHelloWorldInline() throws Exception {
        // http://metoojava.wordpress.com/2010/06/20/execute-javascript-from-java/
        engine.eval("print('Welocme to java world')");
    }

    @Test
    public void testHelloWorldFile() throws Exception {
        File file = new ClassPathResource("hello.js").getFile();
        Object oReturn = engine.eval(new FileReader(file));
        Assert.assertNull(oReturn);
    }

    @Test
    public void testFunctionInline() throws Exception {
        // http://technologymashup.blogspot.com/2013/06/execute-javascript-from-java.html
        // JavaScript code in a String
        String script = "function hello(name) { print('Hello, ' + name); }";
        // evaluate script
        engine.eval(script);
        // javax.script.Invocable is an optional interface.
        // Check whether your script engine implements or not!
        // Note that the JavaScript engine implements Invocable interface.
        Invocable inv = (Invocable) engine;

        // invoke the global function named "hello"
        inv.invokeFunction("hello", "Scripting!!");
    }

    @Test
    public void testFunctionFile_Print() throws Exception {
        File file = new ClassPathResource("hello_function.js").getFile();
        engine.eval(new FileReader(file));
        Invocable inv = (Invocable) engine;
        inv.invokeFunction("hello", "Scripting yo!!");
    }

    @Test
    public void testFunctionFile_Return() throws Exception {
        File file = new ClassPathResource("hello_function.js").getFile();
        engine.eval(new FileReader(file));
        Invocable inv = (Invocable) engine;
        String retVal = (String) inv.invokeFunction("hello_two", "Scripting yo yo!!");
        Assert.assertEquals("Hello, Scripting yo yo!!", retVal);
    }

    @Test
    public void testUnicode() throws Exception {
        File file = new ClassPathResource("sanskrit99_to_unicode.js").getFile();
        engine.eval(new FileReader(file));
        Invocable inv = (Invocable) engine;
        String actual = (String) inv.invokeFunction("convert_to_unicode", "ivvahidnimd< Évtu h;Rdm!,");
        String expected = "विवाहदिनमिदं भवतु हर्षदम्।";
        Assert.assertEquals(expected, actual);
    }

}
