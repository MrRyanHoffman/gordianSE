package edu.gordian.scopes;

import edu.gordian.elements.GordianAnalyser;
import edu.gordian.elements.GordianInterpreter;
import language.element.Analyser;
import edu.gordian.internal.Methods;
import edu.gordian.internal.Storage;
import language.scope.Scope;
import language.value.Interpreter;

public class GordianScope implements Scope {

    private final Scope scope;
    private final Methods methods;
    private final Storage storage;
    private final Analyser analyser = new GordianAnalyser(this);
    private final Interpreter interpreter = new GordianInterpreter(this);

    public GordianScope(Scope scope) {
        this.scope = scope;
        this.methods = new Methods(scope.methods());
        this.storage = new Storage(scope.storage());
    }

    @Override
    public Scope parent() {
        return scope;
    }

    @Override
    public Methods methods() {
        return methods;
    }

    @Override
    public Storage storage() {
        return storage;
    }

    @Override
    public void run(String i) {
        GordianRuntime.run(this, i);
    }

    @Override
    public Analyser getAnalyser() {
        return analyser;
    }

    @Override
    public Interpreter getInterpreter() {
        return interpreter;
    }
}
