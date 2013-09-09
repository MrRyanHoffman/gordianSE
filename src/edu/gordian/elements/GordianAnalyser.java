package edu.gordian.elements;

import edu.gordian.internal.ScopeBreak;
import edu.gordian.scopes.GordianIf;
import edu.gordian.scopes.GordianWhile;
import edu.gordian.scopes.GordianFor;
import language.element.Analyser;
import language.scope.Scope;
import edu.gordian.scopes.GordianCount;
import edu.gordian.scopes.GordianDefinedMethod;
import edu.gordian.scopes.GordianScope;
import edu.gordian.scopes.GordianThread;
import edu.gordian.scopes.GordianTry;
import edu.gordian.values.GordianClass;

public final class GordianAnalyser implements Analyser {

    private final Scope scope;

    public GordianAnalyser(Scope scope) {
        this.scope = scope;
    }

    public void analyseBlock(String s) {
        try {
            if (s.startsWith("if")) {
                GordianIf.run(scope, s);
            } else if (s.startsWith("while")) {
                GordianWhile.run(scope, s);
            } else if (s.startsWith("for")) {
                GordianFor.run(scope, s);
            } else if (s.startsWith("count")) {
                GordianCount.run(scope, s);
            } else if (s.startsWith("thread")) {
                GordianThread.run(scope, s);
            } else if (s.startsWith("try")) {
                GordianTry.run(scope, s);
            } else if (s.startsWith("def")) {
                GordianDefinedMethod.run(scope, s);
            } else if (s.startsWith("scope")) {
                GordianScope.run(scope, s);
            } else if (s.startsWith("class")) {
                GordianClass.run(scope, s);
            } else {
                throw new NullPointerException("The value \"" + s + "\" could not be interpreted as a block.");
            }
        } catch (ScopeBreak ex) {
            // Block was broken
        }
    }

    public void analyseInstruction(String s) {
        try {
            // Ask for value - all instructions have a value.
            scope.getInterpreter().interpretValue(s);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            throw new NullPointerException("The instruction \"" + s + "\" could not be completed.");
        }
    }
}