package edu.gordian.instructions;

import edu.gordian.instruction.Declaration;
import edu.gordian.value.Value;
import edu.gordian.scope.Scope;

public class GordianDeclaration implements Declaration {

    private final Scope scope;

    public GordianDeclaration(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Value set(String key, Value val) {
        scope.storage().put(key, val);
        return val;
    }
}
