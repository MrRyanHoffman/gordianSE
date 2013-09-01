package edu.gordian.scopes;

import edu.gordian.elements.GordianAnalyser;
import edu.gordian.elements.GordianInterpreter;
import edu.gordian.element.Analyser;
import edu.gordian.value.Interpreter;
import edu.gordian.value.Value;
import edu.gordian.instruction.Method;
import edu.gordian.internal.Methods;
import edu.gordian.internal.Storage;
import edu.gordian.operator.Operator;
import edu.gordian.values.GordianNull;
import java.util.Arrays;
import edu.gordian.scope.Scope;
import java.util.List;
import java.util.StringTokenizer;

public final class GordianRuntime implements Scope {

    private final Analyser analyser = new GordianAnalyser(this);
    private final Interpreter interpreter = new GordianInterpreter(this);
    private final Methods methods = new Methods();
    private final Storage storage = new Storage();
    public static final List operations = Arrays.asList(new Operator[]{
        new Addition(), new Subtraction(), new Multiplication(), new Division(), new Modulus()
    });

    {
        methods.put("print", new Method() {
            @Override
            public Value run(Value[] args) {
                if (args.length > 1) {
                    System.out.println(Arrays.asList(args));
                } else {
                    System.out.println(args[0]);
                }
                return null;
            }
        });
        storage.put("null", GordianNull.get());
    }

    @Override
    public void run(String i) {
        i = pre(i);
        if ((i.indexOf(";") == i.lastIndexOf(";")) && (i.indexOf(";") == i.length() - 1)) {
            getAnalyser().analyseInstruction(i.substring(0, i.length() - 1));
        } else {
            StringTokenizer tokenizer = new StringTokenizer(i, ";");
            while (tokenizer.hasMoreElements()) {
                String t = tokenizer.nextToken();
                if (t.endsWith(":")) {
                    StringBuilder buffer = new StringBuilder(t).append(";");
                    int scopes = 1;
                    while (tokenizer.hasMoreElements() && !(scopes == 0)) {
                        t = tokenizer.nextToken();
                        buffer.append(t).append(";");
                        if (t.endsWith(":")) {
                            scopes++;
                        } else if (t.equals("fi")) {
                            scopes--;
                        }
                    }
                    String f = buffer.toString();
                    if (f.endsWith("fi;")) {
                        f = f.substring(0, f.length() - 4);
                    }
                    getAnalyser().analyseBlock(f);
                } else {
                    run(t + ";");
                }
            }
        }
    }

    @Override
    public Analyser getAnalyser() {
        return analyser;
    }

    @Override
    public Interpreter getInterpreter() {
        return interpreter;
    }

    @Override
    public Methods methods() {
        return methods;
    }

    @Override
    public Storage storage() {
        return storage;
    }

    private static String pre(String s) {
        if (!s.endsWith(";")) {
            s = s + ";";
        }
        s = s.replaceAll("\n", ";");
        s = s.replaceAll(":", ":;");
        if (s.contains(" ")) {
            s = removeSpaces(s, 0);
        }
        while (s.contains("#")) {
            String toRemove = s.substring(s.indexOf('#'), (s.substring(s.indexOf('#'))).indexOf(';') + s.indexOf('#'));
            s = s.replace(toRemove, "");
        }
        return s;
    }

    private static String removeSpaces(final String s, int x) {
        String a = s.replaceAll("\t", " ");

        boolean inQuotes = false;
        x += a.substring(x).indexOf(' ');
        for (int i = 0; i < x; i++) {
            if (a.charAt(i) == '"') {
                inQuotes = !inQuotes;
            } else if (a.charAt(i) == ';') {
                inQuotes = false;
            }
        }
        if (!inQuotes) {
            a = a.substring(0, x) + s.substring(x + 1);
        } else {
            if (a.substring(x + 1).indexOf(' ') == -1) {
                x = a.length() - 1;
            } else {
                x = a.substring(x + 1).indexOf(' ') + x + 1;
            }
        }

        if (a.substring(x).contains(" ")) {
            return removeSpaces(a, x);
        }

        return a;
    }

    private static class Addition implements Operator {

        @Override
        public char getChar() {
            return '+';
        }

        @Override
        public double result(double o, double o1) {
            return o + o1;
        }
    }

    private static class Subtraction implements Operator {

        @Override
        public char getChar() {
            return '-';
        }

        @Override
        public double result(double o, double o1) {
            return o - o1;
        }
    }

    private static class Multiplication implements Operator {

        @Override
        public char getChar() {
            return '*';
        }

        @Override
        public double result(double o, double o1) {
            return o * o1;
        }
    }

    private static class Division implements Operator {

        @Override
        public char getChar() {
            return '/';
        }

        @Override
        public double result(double o, double o1) {
            return o / o1;
        }
    }

    private static class Modulus implements Operator {

        @Override
        public char getChar() {
            return '%';
        }

        @Override
        public double result(double o, double o1) {
            return o % o1;
        }
    }
}
