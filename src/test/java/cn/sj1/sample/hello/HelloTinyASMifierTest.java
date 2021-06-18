package cn.sj1.sample.hello;

import static cn.sj1.tinyasm.tools.TinyAsmTestUtils.dumpTinyAsm;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import cn.sj1.tinyasm.tools.TinyAsmTestUtils;

public class HelloTinyASMifierTest {
	static {
		TinyAsmTestUtils.setTarget("src/main/java");
	}

	@Test
	public void test_Hello_dumpTinyAsm() throws Exception {
		Class<?> expectedClazz = Hello.class;
		String codeExpected = TinyAsmTestUtils.toString(expectedClazz);

		String codeActual = TinyAsmTestUtils.toString(expectedClazz.getName(), dumpTinyAsm(expectedClazz));

		assertEquals("Code", codeExpected, codeActual);
	}

	@Test
	public void test_Hello_build() throws Exception {
		Class<?> expectedClazz = Hello.class;
		String codeExpected = TinyAsmTestUtils.toString(expectedClazz);
		
		HelloTinyAsmBuilder helloTinyAsmBuilder = new HelloTinyAsmBuilder();

		String codeActual = TinyAsmTestUtils.toString(expectedClazz.getName(), helloTinyAsmBuilder.build(expectedClazz.getName()));

		assertNotEquals("Code", codeExpected, codeActual);
		assertEquals("Code", codeExpected.replaceAll("hello world!", "HELLO WORLD!"), codeActual);
		

		expectedClazz = HelloExpected.class;
		codeExpected = TinyAsmTestUtils.toString(expectedClazz);
		codeActual = TinyAsmTestUtils.toString(expectedClazz.getName(), helloTinyAsmBuilder.build(expectedClazz.getName()));
		
		assertEquals("Code", codeExpected, codeActual);
	}
}
