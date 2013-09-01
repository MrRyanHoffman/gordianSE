package edu.gordian.elements;

import edu.gordian.instructions.GordianDeclaration;
import edu.gordian.scopes.GordianIf;
import edu.gordian.scopes.GordianWhile;
import edu.gordian.scopes.GordianFor;
import edu.gordian.element.Analyser;
import edu.gordian.instruction.Method;
import edu.gordian.scope.Scope;
import edu.gordian.values.GordianNumber;

public class GordianAnalyser implements Analyser {

    private final Scope scope;

    public GordianAnalyser(Scope scope) {
        this.scope = scope;
    }

    @Override
    public void analyseBlock(String s) {
        if (s.startsWith("if")) {
            new GordianIf(scope).run(s.substring(3, s.substring(0, s.indexOf(":")).lastIndexOf(")")),
                    s.substring(s.indexOf(";") + 1));
        } else if (s.startsWith("while")) {
            new GordianWhile(scope).run(s.substring(6, s.substring(0, s.indexOf(":")).lastIndexOf(")")),
                    s.substring(s.indexOf(";") + 1));
        } else if (s.startsWith("for")) {
            new GordianFor(scope).run(s.substring(4, s.substring(0, s.indexOf(":")).lastIndexOf(")")),
                    s.substring(s.indexOf(";") + 1));
        } else {
            throw new NullPointerException("The value \"" + s + "\" could not be interpreted as a block.");
        }
    }

    @Override
    public void analyseInstruction(String s) {
        if (s.contains("(") && s.charAt(s.length() - 1) == ')') {
            Method m = scope.methods().get(s.substring(0, s.indexOf("(")));
            if (m != null) {
                m.run(scope.getInterpreter().interpretValues(s.substring(s.indexOf("(") + 1, s.lastIndexOf(")")).split(",")));
            }
        } else if (s.contains("=")) {
            new GordianDeclaration(scope).set(s.substring(0, s.indexOf("=")),
                    scope.getInterpreter().interpretValue(s.substring(s.indexOf("=") + 1)));
        } else if (s.endsWith("++")) {
            GordianNumber n = (GordianNumber) scope.storage().get(s.substring(0, s.indexOf("++")));
            if(n == null) {
                n = new GordianNumber(0);
            }
            new GordianDeclaration(scope).set(s.substring(0, s.indexOf("++")),
                    new GordianNumber(n.getDouble() + 1));
        }  else if (s.endsWith("--")) {
            GordianNumber n = (GordianNumber) scope.storage().get(s.substring(0, s.indexOf("--")));
            if(n == null) {
                n = new GordianNumber(0);
            }
            new GordianDeclaration(scope).set(s.substring(0, s.indexOf("--")),
                    new GordianNumber(n.getDouble() - 1));
        } else {
            throw new NullPointerException("The value \"" + s + "\" could not be interpreted as an instruction.");
        }
    }
}
