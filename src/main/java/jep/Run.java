/**
 * Copyright (c) 2004-2022 JEP AUTHORS.
 *
 * This file is licensed under the the zlib/libpng License.
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any
 * damages arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any
 * purpose, including commercial applications, and to alter it and
 * redistribute it freely, subject to the following restrictions:
 * 
 *     1. The origin of this software must not be misrepresented; you
 *     must not claim that you wrote the original software. If you use
 *     this software in a product, an acknowledgment in the product
 *     documentation would be appreciated but is not required.
 * 
 *     2. Altered source versions must be plainly marked as such, and
 *     must not be misrepresented as being the original software.
 * 
 *     3. This notice may not be removed or altered from any source
 *     distribution.
 */
package jep;

import java.io.File;

/**
 * Executes a Python script in a Jep Interpreter.
 * 
 * @author Mike Johnson
 */
public class Run {
    private static boolean interactive = false;

    private static boolean swingApp = false;

    private static String file = null;

    private static String scriptArgv = null;

    private static final String USAGE = "  Usage: jep.Run [OPTIONS]...  [FILE].. [SCRIPT ARGS]\n"
            + "Options:\n"
            + "  -i                         Run script interactively.\n"
            + "  -s                         Run script in event dispatching thread (for use with Swing)\n";

    public static int run(boolean eventDispatch) {
        try (Jep jep = new SharedInterpreter()) {
            jep.exec("import sys");
            jep.exec("sys.path.append('.')");

            // Windows file system compatibility
            if (scriptArgv.contains("\\")) {
                scriptArgv = scriptArgv.replace("\\", "\\\\");
            }
            if (scriptArgv.contains(":")) {
                scriptArgv = scriptArgv.replace(":", "\\:");
            }

            // "set" by eval'ing it
            jep.exec("sys.argv = argv = " + scriptArgv);
            if (!file.endsWith("jep" + File.separator + "console.py")) {
                jep.runScript(file);
            } else {
                interactive = true;
            }
            if (interactive) {
                jep.set("jepInstance", jep);
                jep.exec("from jep import console");
                jep.exec("console.prompt(jepInstance)");
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            return 1;
        }

        // if we're the event dispatch thread, we should quit now.
        // don't close jep.
        if (eventDispatch) {
            return 0;
        }

        return 0;
    }

    /**
     * Describe <code>main</code> method here.
     * 
     * @param args
     *            a <code>String</code> value
     * @throws Throwable
     *             if an error occurs
     * @throws Exception
     *             if an error occurs
     */
    public static void main(String[] args) throws Throwable {
        String[] scriptArgs = new String[args.length];
        int argsi = 0;
        
        for (String arg : args) {
            if (file != null)
                scriptArgs[argsi++] = arg;
            else if (arg.equals("-i"))
                interactive = true;
            else if (arg.equals("-s"))
                swingApp = true;
            else if (arg.equals("-h")) {
                System.out.println(USAGE);
                System.exit(1);
            } else if (arg.startsWith("-")) {
                System.out.println("Run: Unknown option: " + arg);
                System.out.println(USAGE);
                System.exit(1);
            } else {
                file = arg;
            }
        }

        if (file == null) {
            System.out.println("Run: Invalid file, null");
            System.out.println(USAGE);
            System.exit(1);
        }

        // setup argv
        StringBuilder b = new StringBuilder("[");
        // always the first arg
        b.append("'").append(file).append("',");
        // trailing comma is okay
        for (int i = 0; i < argsi; i++)
            b.append("'").append(scriptArgs[i]).append("',");
        b.append("]");
        scriptArgv = b.toString();

        int ret;
        if (swingApp) {
            // run in the event-dispatching thread
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    Run.run(swingApp);
                }
            });
            ret = 0;
        } else {
            ret = run(swingApp);
        }

        // in case we're run with -Xrs
        if (!swingApp)
            System.exit(ret);
    }

    private Run() {
    }

} // Run
