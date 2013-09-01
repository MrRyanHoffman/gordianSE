package edu.gordian.elements;

import edu.gordian.instruction.Method;
import edu.gordian.instructions.GordianDeclaration;
import edu.gordian.operator.Operator;
import edu.gordian.scope.Scope;
import edu.gordian.scopes.GordianRuntime;
import edu.gordian.value.Interpreter;
import edu.gordian.value.Value;
import edu.gordian.values.GordianBoolean;
import edu.gordian.values.GordianNumber;
import java.util.Iterator;

public class GordianInterpreter implements Interpreter {

    private final Scope scope;

    public GordianInterpreter(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Value interpretValue(String s) {
        if (s.isEmpty()) {
            return null;
        }

        GordianNumber n = GordianNumber.toNumber(s);
        if (n != null) {
            return n;
        }

        if (s.equalsIgnoreCase("true")) {
            return new GordianBoolean(true);
        } else if (s.equalsIgnoreCase("false")) {
            return new GordianBoolean(false);
        }

        if (s.contains("(") && s.charAt(s.length() - 1) == ')') {
            Method m = scope.methods().get(s.substring(0, s.indexOf("(")));
            if (m != null) {
                return m.run(scope.getInterpreter().interpretValues(s.substring(s.indexOf("(") + 1, s.lastIndexOf(")")).split(",")));
            }
        }
        Value v = scope.storage().get(s);
        if (v != null) {
            return v;
        }

        Iterator i = GordianRuntime.operations.iterator();
        while (i.hasNext()) {
            Operator o = (Operator) i.next();
            String x = o.getChar() + "=";
            if (s.contains(x)) {
                s = s.substring(0, s.indexOf(x)) + "="
                        + s.substring(0, s.indexOf(x)) + o.getChar() + s.substring(s.indexOf(x) + 2);
            }
        }

        if (s.contains("=")) {
            return new GordianDeclaration(scope).set(s.substring(0, s.indexOf("=")),
                    scope.getInterpreter().interpretValue(s.substring(s.indexOf("=") + 1)));
        } else if (s.endsWith("++")) {
            GordianNumber h = (GordianNumber) scope.storage().get(s.substring(0, s.indexOf("++")));
            if (h == null) {
                h = new GordianNumber(0);
            }
            return new GordianDeclaration(scope).set(s.substring(0, s.indexOf("++")),
                    new GordianNumber(h.getDouble() + 1));
        } else if (s.endsWith("--")) {
            GordianNumber h = (GordianNumber) scope.storage().get(s.substring(0, s.indexOf("--")));
            if (h == null) {
                h = new GordianNumber(0);
            }
            return new GordianDeclaration(scope).set(s.substring(0, s.indexOf("--")),
                    new GordianNumber(h.getDouble() - 1));
        }

        throw new NullPointerException("The value \"" + s + "\" could not be interpreted as a value.");
    }

    @Override
    public Value[] interpretValues(String[] s) {
        Value[] v = new Value[s.length];
        for (int x = 0; x < v.length; x++) {
            v[x] = interpretValue(s[x]);
        }
        return v;
    }
}
