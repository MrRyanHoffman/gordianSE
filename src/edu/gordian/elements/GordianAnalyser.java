package edu.gordian.elements;

import edu.gordian.scopes.GordianIf;
import edu.gordian.scopes.GordianWhile;
import edu.gordian.scopes.GordianFor;
import edu.gordian.element.Analyser;
import edu.gordian.instruction.Method;
import edu.gordian.scope.Scope;
import edu.gordian.scopes.GordianDefinedMethod;
import edu.gordian.scopes.GordianThread;

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
        } else if (s.startsWith("thread")) {
            new GordianThread(scope).runThread(s.substring(s.indexOf(";") + 1));
        } else if (s.startsWith("def")) {
            new GordianDefinedMethod(scope).define(s.substring(3, s.indexOf("(")), 
                    s.substring(s.indexOf("(") + 1,s.substring(0, s.indexOf(";")).lastIndexOf(")")).split(","),
                    s.substring(s.indexOf(";") + 1));
        } else {
            throw new NullPointerException("The value \"" + s + "\" could not be interpreted as a block.");
        }
    }

    @Override
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