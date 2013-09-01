package edu.gordian.element;

import edu.gordian.value.Interpreter;
import edu.gordian.value.Value;

public interface Element {

    public void analyse(Analyser a);

    public Value interpret(Interpreter i);
}
