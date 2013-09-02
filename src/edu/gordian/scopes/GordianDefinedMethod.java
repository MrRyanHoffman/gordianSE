package edu.gordian.scopes;

import edu.gordian.instruction.Method;
import edu.gordian.internal.ValueReturned;
import edu.gordian.scope.Scope;
import edu.gordian.value.Value;

public class GordianDefinedMethod extends GordianScope {

    public GordianDefinedMethod(Scope scope) {
        super(scope);
    }

    public void define(final String name, final String[] args, final String run) {
        parent().methods().put(name, new Method() {
            @Override
            public Value run(Value[] a) {
                if (a.length < args.length) {
                    throw new IllegalArgumentException(name + " does not have enough arguments (" + a.length + "/" + args.length);
                }
                for (int x = 0; x < args.length; x++) {
                    storage().put(args[x], a[x]);
                }
                try {
                    GordianDefinedMethod.this.run(run);
                } catch (ValueReturned r) {
                    return r.value;
                } catch (Exception e) {
                    throw new RuntimeException("Defined method (" + name + ") failed - " + e.getMessage());
                }
                return null;
            }
        });
    }
}
