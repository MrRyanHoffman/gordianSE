package edu.gordian.internal;

import language.instruction.Method;
import edu.gordian.scopes.GordianRuntime;
import java.util.ArrayList;
import java.util.List;
import language.internal.Methods;

public final class GordianMethods implements Methods {

    private final List nodes = new ArrayList();

    public GordianMethods() {
    }

    public GordianMethods(Methods s) {
        nodes.addAll(s.nodes());
    }

    @Override
    public void put(String key, Method method) {
        GordianRuntime.testName(key);
        nodes.add(new Node(key, method));
    }

    @Override
    public Method get(String key) {
        for (int x = nodes.size() - 1; x >= 0; x--) {
            Node n = (Node) nodes.get(x);
            if (n.key.equals(key)) {
                return n.val;
            }
        }
        return null;
    }

    @Override
    public List nodes() {
        return nodes;
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
