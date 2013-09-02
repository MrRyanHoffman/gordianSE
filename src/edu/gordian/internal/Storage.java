package edu.gordian.internal;

import edu.gordian.scopes.GordianRuntime;
import edu.gordian.value.Value;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Storage {

    private final List nodes = new ArrayList();

    public Storage() {
    }

    public Storage(Storage s) {
        nodes.addAll(s.nodes);
    }

    public Value put(String key, Value value) {
        GordianRuntime.testName(key);
        Value old = get(key);
        nodes.add(new Node(key, value));
        return old;
    }

    public Value set(String key, Value value) {
        GordianRuntime.testName(key);
        Value old = get(key);
        if (old == null) {
            nodes.add(new Node(key, value));
        } else {
            Iterator i = nodes();
            while (i.hasNext()) {
                Node n = (Node) i.next();
                if (n.key.equals(key)) {
                    n.val = value;
                }
            }
        }
        return old;
    }

    public Value get(String key) {
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
        private Value val;

        public Node(String key, Value val) {
            this.key = key;
            this.val = val;
        }
    }
}
