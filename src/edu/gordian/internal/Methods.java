package edu.gordian.internal;

import edu.gordian.instruction.Method;
import edu.gordian.scopes.GordianRuntime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Methods {

    private final List nodes = new ArrayList();

    public Methods() {
    }

    public Methods(Methods s) {
        nodes.addAll(s.nodes);
    }

    public void put(String key, Method method) {
        GordianRuntime.testName(key);
        nodes.add(new Node(key, method));
    }

    public Method get(String key) {
        Iterator i = nodes();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            if (n.key.equals(key)) {
                return n.val;
            }
        }
        return null;
    }

    private Iterator nodes() {
        return new Iterator() {
            private final Node[] vals;
            private int index = 0;

            {
                Iterator i = nodes.iterator();
                vals = new Node[nodes.size()];
                int index = vals.length - 1;
                while (i.hasNext()) {
                    vals[index--] = (Node) i.next();
                }
            }

            @Override
            public boolean hasNext() {
                return index < vals.length;
            }

            @Override
            public Object next() {
                return vals[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    private static final class Node {

        private final String key;
        private final Method val;

        public Node(String key, Method val) {
            this.key = key;
            this.val = val;
        }
    }
}
