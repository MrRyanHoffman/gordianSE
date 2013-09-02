package language.scope;

import language.element.Analyser;
import edu.gordian.internal.Methods;
import edu.gordian.internal.Storage;
import language.value.Interpreter;

public interface Scope {

    public Scope parent();

    public Methods methods();

    public Storage storage();

    public void run(String i);

    public Analyser getAnalyser();

    public Interpreter getInterpreter();
}
