package cn.sj1.sample;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.sj1.tinyasm.AdvMagic;
import cn.sj1.tinyasm.util.TinyAsmTestUtils;

public class HelloBuilderTest {
	@Test
	public void testHelloBuilder() {

		Class<?> expectedClazz = Hello.class;
		String codeExpected = TinyAsmTestUtils.toString(expectedClazz);

		String codeActual = TinyAsmTestUtils.toString(expectedClazz.getName(), AdvMagic.dump(expectedClazz.getName(), HelloBuilder.class));

		assertEquals("Code", codeExpected, codeActual);
	}
}
