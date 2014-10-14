/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import javax.script.ScriptException;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-26
 */
public class JsEngineTest {
    @Test
    public void run() throws ScriptException {
//        String script = "var startTime = Date.now();\n" +
//                "for (var i = 0; i < 10000000; i++) {\n" +
//                "  obj.incr();\n" +
//                "}\n" +
//                "java.lang.System.out.println(\"time spend: \" + (Date.now() - startTime));" +
//                "result = obj.getI();";
        String script = "with(obj) {\n" +
                "  incr();\n" +
                "}\n" +
                "result = obj.getI();";
//        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
//        ScriptEngine engine = scriptEngineManager.getEngineByName("rhino");
//        engine.eval(script);
        Context mozillaJsContext = Context.enter();
        Scriptable scope = mozillaJsContext.initStandardObjects();
        mozillaJsContext.setOptimizationLevel(9);
        long start = System.currentTimeMillis();
        Script s = mozillaJsContext.compileString(script, "script", 1, null);
        ScriptableObject.putProperty(scope, "obj", Context.javaToJS(new Obj(), scope));
        s.exec(mozillaJsContext, scope);
        System.out.println(scope.get("result", scope));
        System.out.println("Total time spend: " + (System.currentTimeMillis() - start));
    }

    public static class Obj {
        private int i = 0;

        public void incr() {
            i++;
        }

        public int getI() {
            return i;
        }
    }
}
