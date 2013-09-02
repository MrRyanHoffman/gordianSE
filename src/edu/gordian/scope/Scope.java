package edu.gordian.scope;

import edu.gordian.element.Analyser;
import edu.gordian.internal.Methods;
import edu.gordian.internal.Storage;
import edu.gordian.value.Interpreter;

public interface Scope {

    public Scope parent();

    public Methods methods();

    public Storage storage();

    public void run(String i);

    public Analyser getAnalyser();

    public Interpreter getInterpreter();
}
