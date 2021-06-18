package cn.sj1.sample.hello;

import cn.sj1.tinyasm.core.ClassBody;
import cn.sj1.tinyasm.core.MethodCode;

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
