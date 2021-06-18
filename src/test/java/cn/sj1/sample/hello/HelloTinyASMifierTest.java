package cn.sj1.sample.hello;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import  cn.sj1.tinyasm.tools.TinyAsmTestUtils;

public class HelloTinyASMifierTest {
	static {
		TinyAsmTestUtils.setTarget("src/main/java");
	}

	@Test
	public void test_Hello_dumpTinyAsm() throws Exception {
		String codeExpected = TinyAsmTestUtils.toString(Hello.class);

		String codeActual = TinyAsmTestUtils.toString(Hello.class.getName(), TinyAsmTestUtils.dumpTinyAsm(Hello.class));

		assertEquals(codeExpected, codeActual);
	}

	@Test
	public void test_Hello_build() throws Exception {

		String codeExpected = TinyAsmTestUtils.toString(Hello.class);

		HelloTinyAsmBuilder helloTinyAsmBuilder = new HelloTinyAsmBuilder();

		String codeActual = TinyAsmTestUtils.toString(Hello.class.getName(), helloTinyAsmBuilder.build(Hello.class.getName()));

		assertNotEquals(codeExpected, codeActual);
		assertEquals(codeExpected.replaceAll("hello world!", "HELLO WORLD!"), codeActual);
	}

	@Test
	public void test_Hello_compareWithExpected() throws Exception {
		Class<?> expectedClazz = HelloExpected.class;
		String codeExpected = TinyAsmTestUtils.toString(expectedClazz);

		HelloTinyAsmBuilder helloTinyAsmBuilder = new HelloTinyAsmBuilder();
		String codeActual = TinyAsmTestUtils.toString(expectedClazz.getName(), helloTinyAsmBuilder.build(expectedClazz.getName()));

		assertEquals(codeExpected, codeActual);
	}

	@Test
	public void test_Hello_run() throws Exception {

		TinyAsmClassLoader load = new TinyAsmClassLoader();
		HelloTinyAsmBuilder helloTinyAsmBuilder = new HelloTinyAsmBuilder();

		byte[] bytesClass = helloTinyAsmBuilder.build(HelloExpected.class.getName());

		Class<?> classHello = load.defineClassByName(HelloExpected.class.getName(), bytesClass);

		SayHello sayHello = (SayHello) classHello.getConstructor().newInstance();

		assertEquals("HELLO WORLD!", sayHello.sayHello());

	}
}
