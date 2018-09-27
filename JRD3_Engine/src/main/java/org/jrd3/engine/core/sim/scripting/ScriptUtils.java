package org.jrd3.engine.core.sim.scripting;

import groovy.lang.GroovyShell;
import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.sim.AbstractState;
import org.jrd3.engine.core.sim.MouseInput;
import org.jrd3.engine.core.sim.Window;
import org.jrd3.engine.core.utils.CommonUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Some utility for scripts management.
 *
 * @author Ray1184
 * @version 1.0
 */
public class ScriptUtils {

    private static GroovyShell groovyShell;

    /**
     * Groovy shell singleton.
     *
     * @return the groovy shell.
     */

    private static GroovyShell groovyInstance() {
        if (ScriptUtils.groovyShell == null) {
            ScriptUtils.groovyShell = new GroovyShell();
        }
        return ScriptUtils.groovyShell;
    }

    /**
     * Creation of script callback.
     *
     * @param inputStream the script input stream.
     * @return the callback object.
     */
    public static CustomScript loadScriptCallback(InputStream inputStream) {
        Object script = ScriptUtils.groovyInstance().evaluate(
                new InputStreamReader(inputStream));
        return ScriptUtils.loadScriptCallback(script);
    }

    /**
     * Creation of script callback.
     *
     * @param script the script object.
     * @return the callback object.
     */
    public static CustomScript loadScriptCallback(Object script) {
        Object result = Proxy.newProxyInstance(
                script.getClass().getClassLoader(),
                new Class[]
                        {CustomScript.class},
                (proxy, method, args) ->
                {
                    if ((args != null) && (args.length == 1)) {
                        Method m = script.getClass().getMethod(
                                method.getName(), AbstractState.class);
                        return m.invoke(script, args);
                    }

                    if ((args != null) && (args.length == 2)) {
                        Method m = script.getClass().getMethod(
                                method.getName(), AbstractState.class, Float.class);
                        return m.invoke(script, args);
                    }

                    if ((args != null) && (args.length == 3)) {
                        Method m = script.getClass().getMethod(
                                method.getName(), AbstractState.class, Window.class, MouseInput.class);
                        return m.invoke(script, args);
                    }

                    return null;


                });
        if (result instanceof CustomScript) {
            return (CustomScript) result;
        }
        return null;
    }

    /**
     * Creation of script callback.
     *
     * @param scriptPath the script file.
     * @return the callback object.
     */
    public static CustomScript loadScriptCallback(String scriptPath) {
        Object script;
        try {
            String scriptText = CommonUtils.loadResource(scriptPath);
            script = ScriptUtils.groovyInstance().evaluate(scriptText);
            return ScriptUtils.loadScriptCallback(script);
        } catch (JRD3Exception e) {
            return null;
        }

    }
}
