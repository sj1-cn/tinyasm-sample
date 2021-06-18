# tinyasm-sample

使用tinyasm的简单例子。

使用tinyasm最快的方法是先建立一个模版Class。为了后续的方便，我们先建立一个接口 SayHello

```
public interface SayHello {
	String sayHello();
}
```

然后建立一个实现这个接口的class，Hello

```
public class Hello implements SayHello {
	public String sayHello() {
		return "hello world!";
	}
}
```

然后我们使用代码：

```
TinyAsmTestUtils.dumpTinyAsm(Hello.class);
```
这样会生成一个如下的class。也就是生成HelloClass的全部代码。直接在这个上边改就行了。

```
package cn.sj1.sample.hello;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;

import cn.sj1.tinyasm.core.Annotation;
import cn.sj1.tinyasm.core.ClassBody;
import cn.sj1.tinyasm.core.ClassBuilder;
import cn.sj1.tinyasm.core.Clazz;
import cn.sj1.tinyasm.core.MethodCode;

import cn.sj1.sample.hello.SayHello;

@SuppressWarnings("unused")
public class HelloTinyAsmDump {

	public static byte[] dump() throws Exception {
		return new HelloTinyAsmDump().build("cn.sj1.sample.hello.Hello");
	}

	public byte[] build(String className) throws Exception {
		ClassBody classBody = ClassBuilder.class_(className, Object.class, SayHello.class)
			.access(ACC_PUBLIC | ACC_SUPER).body();

		__init_(classBody);
		_sayHello(classBody);

		return classBody.end().toByteArray();
	}

	protected void __init_(ClassBody classBody) {
		MethodCode code = classBody.public_().method("<init>").begin();

		code.LINE();
		code.LOAD("this");
		code.SPECIAL(Object.class, "<init>").INVOKE();
		code.RETURN();

		code.END();
	}

	protected void _sayHello(ClassBody classBody) {
		MethodCode code = classBody.public_().method("sayHello")
			.return_(String.class ).begin();

		code.LINE();
		code.LOADConst("hello world!");
		code.RETURNTop();

		code.END();
	}

}
```

还可以使用更简单更实用的方法。因为模版Class可以不断追加功能，每次追加功能以后，都需要重新修改一遍太不方便了，所以我们可以生成一个Builderclass,只替换我们修改的部分。
比如把"hello world!"修改成"HELLO WORLD!"。

```
public class HelloTinyAsmBuilder extends HelloTinyAsmDump {

	@Override
	protected void _sayHello(ClassBody classBody) {
		MethodCode code = classBody.public_().method("sayHello")
			.return_(String.class ).begin();

		code.LINE();
		code.LOADConst("HELLO WORLD!");
		code.RETURNTop();

		code.END();
	}

}
```

然后我们就可以直接调用了：

```

		HelloTinyAsmBuilder helloTinyAsmBuilder = new HelloTinyAsmBuilder();
		helloTinyAsmBuilder.build(Hello.class.getName());
		
```

为了检查生成的结果对不对，我们可以利用ASM的功能把生成的字节码转换成ASM的文件，直接比对。
注:为了比对方便，行号等对最终结果无关的内容忽略了。

```
		HelloTinyAsmBuilder helloTinyAsmBuilder = new HelloTinyAsmBuilder();
		
		String codeExpected = TinyAsmTestUtils.toString(Hello.class);
		String codeActual = TinyAsmTestUtils.toString(Hello.class.getName(), helloTinyAsmBuilder.build(Hello.class.getName()));

		assertNotEquals(codeExpected, codeActual);
		assertEquals(codeExpected.replaceAll("hello world!", "HELLO WORLD!"), codeActual);
```


也可以直接生成实际Class，新建instance并执行。

```

		TinyAsmClassLoader load = new TinyAsmClassLoader();
		HelloTinyAsmBuilder helloTinyAsmBuilder = new HelloTinyAsmBuilder();

		byte[] bytesClass = helloTinyAsmBuilder.build(HelloExpected.class.getName());

		Class<?> classHello = load.defineClassByName(HelloExpected.class.getName(), bytesClass);

		SayHello sayHello = (SayHello) classHello.getConstructor().newInstance();

		assertEquals("HELLO WORLD!", sayHello.sayHello());

```

