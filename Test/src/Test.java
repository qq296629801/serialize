import java.io.IOException;
import java.util.Scanner;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test {

	public static void main(String[] args)
			throws IOException, ClassNotFoundException, ScriptException, NoSuchMethodException {

	}

	public void script() throws ScriptException, NoSuchMethodException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
		Bindings bind = engine.createBindings();
		bind.put("factor", 1);
		engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
		Scanner input = new Scanner(System.in);
		while (input.hasNext()) {
			int firt = input.nextInt();
			int sec = input.nextInt();
			System.out.println("�����1������:" + firt + "," + sec);
			engine.eval("function aaa(var1,var2){return var1+var2-factor}");
			if (engine instanceof Invocable) {
				Invocable in = (Invocable) engine;
				double resul = (double) in.invokeFunction("aaa", firt, sec);
				System.out.println("���=" + resul);
			}
		}
	}

	public void test() throws ClassNotFoundException, IOException {
		// Studetv1 v1=new Studetv1();
		// v1.setA("a");
		// v1.setAge(1);
		// v1.setName("test");
		// v1.setSex(0);
		// String sql=ConverterUtils.convertObjectToBase64String(v1);
		// System.out.println(sql);
		String sql = "rO0ABXNyAAhTdHVkZXR2MS4PerhRfxEEAgAFSQADYWdlSQADc2V4TAABYXQAEkxqYXZhL2xhbmcvU3RyaW5nO0wABG5hbWVxAH4AAUwABW5hbWVhcQB+AAF4cAAAAAEAAAAAdAABYXQABHRlc3Rw";
		Studetv1 v1 = ConverterUtils.convertBase64StringToObject(sql, new Studetv1());
		Studetv2 v2 = ConverterUtils.convertBase64StringToObject(sql, new Studetv2());

		System.out.println(v1.toString());
		System.out.println(v2.toString());

	}
}
