package org.gordian.scope;

import api.gordian.Arguments;
import api.gordian.Class;
import api.gordian.Object;
import api.gordian.Scope;
import api.gordian.Signature;
import api.gordian.methods.Method;
import api.gordian.storage.InternalNotFoundException;
import api.gordian.storage.Methods;
import api.gordian.storage.Variables;
import edu.first.util.Strings;
import edu.first.util.list.ArrayList;
import edu.first.util.list.Collections;
import edu.first.util.list.Iterator;
import edu.first.util.list.List;
import org.gordian.GordianClass;
import org.gordian.storage.GordianMethods;
import org.gordian.storage.GordianVariables;
import org.gordian.value.GordianBoolean;
import org.gordian.value.GordianNumber;
import org.gordian.value.GordianString;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class GordianScope implements Scope {

    public static final String[] keywords = {"new"};
    public static final List operations = Collections.asList(new Operator[]{
        new Addition(), new Subtraction(), new Multiplication(), new Division(), new Modulus()
    });

    public static boolean isInstruction(String s) {
        // All instructions will have one semi-colon at the end. No other semi-colons will be there.
        return s.indexOf(';') > 0
                && Strings.allIndexesOf(s, ';').length == 1
                && s.endsWith(";")
                && !Strings.contains(s, '{') && !Strings.contains(s, '}');
    }

    public static boolean isBlock(String s) {
        // Requirement for a block is for there to be matching { and }.
        // Pre-block cannot contain separete instruction
        // It is also only a real block if all is contained within a pair of {}s.
        while (s.endsWith(";")) {
            s = s.substring(0, s.lastIndexOf(";"));
        }
        if (!(Strings.contains(s, '{') && s.endsWith("}"))
                || Strings.contains(s.substring(0, s.indexOf('{')), ';')) {
            return false;
        }
        int p = 0;
        for (int x = s.indexOf('{'); x < s.length(); x++) {
            if (s.charAt(x) == '{') {
                p++;
            } else if (s.charAt(x) == '}') {
                p--;
            }
            if (p == 0) {
                if (x == s.length() - 1
                        || (s.startsWith("if(") && s.substring(x + 1).startsWith("else"))
                        || (s.startsWith("try{") && s.substring(x + 1).startsWith("catch"))) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isName(String s) {
        // Names can only contain letters and numbers.
        if (s.length() == 0) {
            return false;
        }
        for (int x = 0; x < s.length(); x++) {
            if (!( // Letters
                    (Character.isLowerCase(s.charAt(x)) || Character.isUpperCase(s.charAt(x)))
                    // Numbers
                    || (s.charAt(x) > 47 && s.charAt(x) < 58))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMethod(String s) {
        // Methods start with a name, the a ( and ends with a ). ( and )s should match.
        if (s.endsWith(";")) {
            s = s.substring(0, s.length() - 1);
        }
        return Strings.contains(s, '(') && Strings.contains(s, ')')
                && Strings.allIndexesOf(s, '(').length == Strings.allIndexesOf(s, ')').length
                && s.endsWith(")")
                && isName(s.substring(0, s.indexOf('(')));
    }

    public static String next(String s) {
        if (!Strings.contains(s, ';')) {
            return s + ';';
        }
        if (Strings.contains(s.substring(0, s.indexOf(';')), '{')) {
            int i = 0;
            StringBuffer buffer = new StringBuffer();
            for (int x = 0; x < s.length(); x++) {
                char e = s.charAt(x);
                if (e == '{') {
                    i++;
                } else if (e == '}') {
                    i--;
                }
                buffer.append(e);
                if (i == 0 && buffer.indexOf("{") >= 0) {
                    // Add everything until matching bracket shows up.
                    return buffer.toString();
                }
            }
            // Bracket didn't find a match
            throw new RuntimeException("Brackets could not be matched in \n\t" + s);
        } else {
            return s.substring(0, s.indexOf(';') + 1);
        }
    }

    public static boolean isProper(String s) {
        // Tabs should be spaces
        if (Strings.contains(s, "\t") || Strings.contains(s, '\n')) {
            return false;
        }
        if (Strings.allIndexesOf(s, '{').length != Strings.allIndexesOf(s, '}').length) {
            throw new RuntimeException("Curly braces are not matched up.");
        }
        int[] i = Strings.allIndexesOf(s, ' ');
        // Assume there are no spaces
        boolean found = true;
        for (int x = 0; x < i.length; x++) {
            // There are spaces - prove me wrong!
            found = false;
            for (int k = 0; k < keywords.length; k++) {
                if (x - keywords[k].length() >= 0 && s.substring(x - keywords[k].length(), x).equals(keywords[k])) {
                    // Proven wrong - keyword proceeded the space
                    found = true;
                    break;
                }
            }
        }
        // If there were spaces, but no keyword proceeded it.
        if (!found) {
            return false;
        }

        int a[] = Strings.allIndexesOf(s, "\"");
        if (a.length % 2 != 0) {
            // Quotation marks must match - fast way to find out
            return false;
        }
        for (int x = 0; x < a.length; x++) {
            if ((x == 0 || s.charAt(a[x] - 1) != '!') && (x == s.length() - 1 || s.charAt(a[x] + 1) != '!')) {
                return false;
            }
        }
        a = Strings.allIndexesOf(s, "\'");
        if (a.length % 2 != 0) {
            // Quotation marks must match - fast way to find out
            return false;
        }
        for (int x = 0; x < a.length; x++) {
            if ((x == 0 || s.charAt(a[x] - 1) == '\\' || s.charAt(a[x] - 1) != '!')
                    && (x == s.length() - 1 || s.charAt(a[x] + 1) != '!')) {
                return false;
            }
        }

        return true;
    }

    private static String convertStrings(String s) {
        char[] c = s.toCharArray();
        StringBuffer b = new StringBuffer();

        int debug1 = -1;
        boolean i1 = false, i2 = false;
        for (int x = 0; x < c.length; x++) {
            String add = String.valueOf(c[x]);
            if (c[x] == '\'' && (x == c.length - 1 || x == 0 || (c[x - 1] != '\\' && c[x - 1] != '!' && c[x + 1] != '!'))) {
                i1 = !i1;
                if (i1) {
                    debug1 = x;
                    add = "\'!";
                } else {
                    add = "!\'";
                }
            } else if (c[x] == '\"' && (x == c.length - 1 || x == 0 || (c[x - 1] != '\\' && c[x - 1] != '!' && c[x + 1] != '!'))) {
                i2 = !i2;
                if (i2) {
                    debug1 = x;
                    add = "\'!";
                } else {
                    add = "!\'";
                }
            } else if (i1 || i2) {
                add = "@" + (int) c[x];
            }
            b.append(add);
        }

        if (i1 || i2) {
            throw new IllegalStateException("Quotes were not closed! - " + s.substring(debug1));
        }

        return b.toString();
    }

    public static String clean(String s) {
        s = Strings.replaceAll(s, '\t', ' ');
        s = Strings.replaceAll(s, '\n', ';');
        s = Strings.replaceAll(s, '{', "{;");
        s = Strings.replaceAll(s, '}', ";}");

        s = convertStrings(s);

        int a = s.indexOf(" "), y = a;
        boolean found = false;
        while (a >= 0) {
            String stub = s.substring(y);
            for (int k = 0; k < keywords.length; k++) {
                if (a - keywords[k].length() >= 0 && s.substring(a - keywords[k].length(), a).equals(keywords[k])) {
                    // Proven wrong - keyword proceeded the space
                    found = true;
                    break;
                }
            }
            if (!found) {
                s = s.substring(0, y) + s.substring(y + 1);
                stub = stub.substring(1);
                y -= 1;
            }
            a = stub.indexOf(' ');
            y += a + 1;
        }
        
        while(s.contains(";;")) {
            s = Strings.replaceAll(s, ";;", ";");
        }

        return s;
    }

    public static String betweenMatch(String s, char f, char l) {
        int scope = 0;
        for (int x = 0; x < s.length(); x++) {
            if (s.charAt(x) == f) {
                scope++;
            } else if (s.charAt(x) == l) {
                scope--;
            }
            if (scope == 0 && Strings.contains(s, f) && s.indexOf(f) < x) {
                return s.substring(s.indexOf(f) + 1, x);
            }
        }
        return "";
    }

    public static int betweenMatchLast(String s, char f, char l) {
        int scope = 0;
        for (int x = 0; x < s.length(); x++) {
            if (s.charAt(x) == f) {
                scope++;
            } else if (s.charAt(x) == l) {
                scope--;
            }
            if (scope == 0 && Strings.contains(s, f) && s.indexOf(f) < x) {
                return x;
            }
        }
        return -1;
    }

    private static boolean isBetween(String s, char i, char f, char l) {
        int pos = s.lastIndexOf(i);
        int scope = 0;
        for (int x = 0; x < s.length(); x++) {
            if (s.charAt(x) == f) {
                scope++;
            } else if (s.charAt(x) == l) {
                scope--;
            }
            if (scope > 0 && Strings.contains(s, f) && s.indexOf(f) < x) {
                // in between
                if (x == pos) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String[] getPureArgs(String s) {
        if (!Strings.contains(s, '(')) {
            return Strings.split(s, ',');
        } else {
            List l = new ArrayList();
            boolean inQuotes = false;
            int p1 = 0;
            int last = 0;
            char[] d = s.toCharArray();
            for (int x = 0; x < d.length; x++) {
                if (d[x] == '(') {
                    p1++;
                } else if (d[x] == ')') {
                    p1--;
                }
                if (d[x] == ',' && !inQuotes && p1 == 0 && !Strings.isEmpty(s.substring(last, x))) {
                    l.add(s.substring(last, x));
                    last = x + 1;
                }
                if (x == d.length - 1 && !Strings.isEmpty(s.substring(last))) {
                    l.add(s.substring(last));
                }
            }
            String[] args = new String[l.size()];
            for (int x = 0; x < args.length; x++) {
                args[x] = (String) l.get(x);
            }
            return args;
        }
    }

    protected Object[] getArgs(String s) {
        if (!Strings.contains(s, '(')) {
            return toObjects(Strings.split(s, ','));
        } else {
            List l = new ArrayList();
            boolean inQuotes = false;
            int p1 = 0;
            int last = 0;
            char[] d = s.toCharArray();
            for (int x = 0; x < d.length; x++) {
                if (d[x] == '(') {
                    p1++;
                } else if (d[x] == ')') {
                    p1--;
                }
                if (d[x] == ',' && !inQuotes && p1 == 0 && !Strings.isEmpty(s.substring(last, x))) {
                    l.add(s.substring(last, x));
                    last = x + 1;
                }
                if (x == d.length - 1 && !Strings.isEmpty(s.substring(last))) {
                    l.add(s.substring(last));
                }
            }
            Object[] args = new Object[l.size()];
            for (int x = 0; x < args.length; x++) {
                args[x] = toObject((String) l.get(x));
            }
            return args;
        }
    }

    private final GordianMethods methods;
    private final GordianVariables variables;
    private final Scope container;

    public GordianScope() {
        this.container = null;
        this.methods = new GordianMethods();
        this.variables = new GordianVariables();
    }

    public GordianScope(GordianScope container) {
        this.container = container;
        this.methods = new GordianMethods(container.methods);
        this.variables = new GordianVariables(container.variables);
    }

    public Scope container() {
        return container;
    }

    public Methods methods() {
        return methods;
    }

    public Variables variables() {
        return variables;
    }

    public void run(String s) {
        if (!isProper(s)) {
            s = clean(s);
        }
        if (isInstruction(s)) {
            toObject(s);
        } else if (isBlock(s)) {
            runBlock(s);
        } else if (!Strings.isEmpty(s) && !s.equals(";")) {
            String next = next(s);
            run(next);
            if (s.length() > next.length()) {
                run(s.substring(next.length()));
            }
        }
    }

    private Object[] toObjects(String[] s) {
        Object[] v = new Object[s.length];
        for (int x = 0; x < v.length; x++) {
            v[x] = toObject(s[x]);
        }
        return v;
    }

    public Object toObject(String s) {
        // clean up
        if (s.endsWith(";")) {
            s = s.substring(0, s.length() - 1);
        }

        // number literals
        try {
            double d = Double.parseDouble(s);
            return new GordianNumber(d);
        } catch (NumberFormatException e) {
        }
        // string literals
        if (s.startsWith("\'!") && s.endsWith("!\'")) {
            return GordianString.evaluate(s.substring(2, s.length() - 2));
        }
        // variables
        if (variables().contains(s)) {
            try {
                return variables().get(s);
            } catch (InternalNotFoundException ex) {
                // should never happen
            }
        }

        // adjustments
        if (s.endsWith("++") && variables().contains(s.substring(0, s.length() - 2))) {
            try {
                s = s.substring(0, s.length() - 2) + "="
                        + (((GordianNumber) variables().get(s.substring(0, s.length() - 2))).getValue() + 1);
            } catch (InternalNotFoundException ex) {
                throw new NullPointerException(s.substring(0, s.length() - 2) + " was not a variable name.");
            }
        }
        if (s.endsWith("--") && variables().contains(s.substring(0, s.length() - 2))) {
            try {
                s = s.substring(0, s.length() - 2) + "="
                        + (((GordianNumber) variables().get(s.substring(0, s.length() - 2))).getValue() - 1);
            } catch (InternalNotFoundException ex) {
                throw new NullPointerException(s.substring(0, s.length() - 2) + " was not a variable name.");
            }
        }
        // shorthand declarations
        Iterator i = operations.iterator();
        while (i.hasNext()) {
            Operator o = (Operator) i.next();
            String x = o.getChar() + "=";
            if (Strings.contains(s, x) && isName(s.substring(0, s.indexOf(x)))) {
                s = s.substring(0, s.indexOf(x)) + "="
                        + s.substring(0, s.indexOf(x)) + o.getChar() + s.substring(s.indexOf(x) + 2);
            }
        }
        // declaration
        if (Strings.containsThatIsnt(s, "=", "==") && isName(s.substring(0, Strings.indexThatIsnt(s, "=", "==")))) {
            return variables().set(s.substring(0, Strings.indexThatIsnt(s, "=", "==")),
                    toObject(s.substring(Strings.indexThatIsnt(s, "=", "==") + 1)));
        }

        // parentheses
        if (Strings.contains(s, '(') && Strings.contains(s, ')')
                && (s.indexOf('(') == 0 || !isName("" + s.charAt(s.indexOf('(') - 1)))) {
            return toObject(
                    s.substring(0, s.indexOf('('))
                    + toObject(betweenMatch(s, '(', ')'))
                    + s.substring(betweenMatchLast(s, '(', ')') + 1));
        }

        // reversed
        if (s.startsWith("!")) {
            return new GordianBoolean(!((GordianBoolean) toObject(s.substring(1))).getValue());
        }

        // method
        if (s.indexOf("(") > 0 && s.lastIndexOf(')') == s.length() - 1 && isName(s.substring(0, s.indexOf("(")))) {
            int c = 0;
            boolean method = false;
            for (int x = s.indexOf("("); x < s.length(); x++) {
                if (s.charAt(x) == '(') {
                    c++;
                } else if (s.charAt(x) == ')') {
                    c--;
                }
                if (c == 0) {
                    method = (x == s.length() - 1);
                    break;
                }
            }
            if (method) {
                try {
                    Method m = methods().get(s.substring(0, s.indexOf("(")));
                    Arguments a = new Arguments(getArgs(betweenMatch(s, '(', ')')));
                    if (m.signature().matches(a.getSignature())) {
                        return m.run(a);
                    } else {
                        throw new RuntimeException("Method " + m + " did not receive the correct type / number or arguments");
                    }
                } catch (InternalNotFoundException ex) {
                    throw new RuntimeException("Could not run method \"" + s.substring(0, s.indexOf("(")) + "\"");
                }
            }
        }

        // calculations
        while (Strings.contains(s, "--")) {
            s = Strings.replaceAll(s, "--", "+");
        }
        while (Strings.contains(s, "++")) {
            s = Strings.replaceAll(s, "++", "+");
        }
        while (Strings.contains(s, "+-")) {
            s = Strings.replaceAll(s, "+-", "-");
        }
        while (Strings.contains(s, "-+")) {
            s = Strings.replaceAll(s, "-+", "-");
        }
        Iterator i1 = operations.iterator();
        while (i1.hasNext()) {
            Operator o = (Operator) i1.next();
            String op = o.getChar() + "";
            if (Strings.contains(s, op)
                    && !isBetween(s, o.getChar(), '(', ')')
                    && !isBetween(s, o.getChar(), '[', ']')) {
                try {
                    return new GordianNumber(o.result(((GordianNumber) toObject(s.substring(0, s.lastIndexOf(o.getChar())))).getValue(),
                            ((GordianNumber) toObject(s.substring(s.lastIndexOf(o.getChar()) + 1))).getValue()));
                } catch (ClassCastException ex) {
                    throw new RuntimeException("Calculation failed - " + s);
                }
            }
        }

        // constructor
        if (s.startsWith("new ")) {
            try {
                Class c = (GordianClass) variables().get(s.substring(4, s.indexOf("(")));
                Arguments a = new Arguments(getArgs(s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"))));
                Signature[] signatures = c.contructors();
                for (int x = 0; x < signatures.length; x++) {
                    if (signatures[x].matches(a.getSignature())) {
                        return c.contruct(a);
                    }
                }
            } catch (InternalNotFoundException ex) {
                throw new RuntimeException("Could not find class \"" + s.substring(4, s.indexOf("(")) + "\"");
            }
        }

        // and
        if (Strings.contains(s, "&&")) {
            return new GordianBoolean(((GordianBoolean) toObject(s.substring(0, s.indexOf("&&")))).getValue()
                    && ((GordianBoolean) toObject(s.substring(s.indexOf("&&") + 2))).getValue());
        }

        // or
        if (Strings.contains(s, "||")) {
            return new GordianBoolean(((GordianBoolean) toObject(s.substring(0, s.indexOf("||")))).getValue()
                    || ((GordianBoolean) toObject(s.substring(s.indexOf("||") + 2))).getValue());
        }

        // equals
        if (Strings.contains(s, "==")) {
            return new GordianBoolean(toObject(s.substring(0, s.indexOf("==")))
                    .equals(toObject(s.substring(s.indexOf("==") + 2))));
        }

        // not equals
        if (Strings.contains(s, "!=")) {
            return new GordianBoolean(!toObject(s.substring(0, s.indexOf("!=")))
                    .equals(toObject(s.substring(s.indexOf("!=") + 2))));
        }

        // bigger or equal
        if (Strings.contains(s, ">=")) {
            return new GordianBoolean(((GordianNumber) toObject(s.substring(0, s.indexOf(">=")))).getValue()
                    >= ((GordianNumber) toObject(s.substring(s.indexOf(">=") + 2))).getValue());
        }

        // smaller or equal
        if (Strings.contains(s, "<=")) {
            return new GordianBoolean(((GordianNumber) toObject(s.substring(0, s.indexOf("<=")))).getValue()
                    <= ((GordianNumber) toObject(s.substring(s.indexOf("<=") + 2))).getValue());
        }

        // bigger
        if (Strings.contains(s, ">")) {
            return new GordianBoolean(((GordianNumber) toObject(s.substring(0, s.indexOf(">")))).getValue()
                    > ((GordianNumber) toObject(s.substring(s.indexOf(">") + 1))).getValue());
        }

        // smaller
        if (Strings.contains(s, "<")) {
            return new GordianBoolean(((GordianNumber) toObject(s.substring(0, s.indexOf("<")))).getValue()
                    < ((GordianNumber) toObject(s.substring(s.indexOf("<") + 1))).getValue());
        }

        // object access
        if (s.indexOf(".") > 0 && s.indexOf(".") < s.length() - 1) {
            Object call = toObject(s.substring(0, s.lastIndexOf('.')));

            String r = s.substring(s.lastIndexOf('.') + 1);
            if (isMethod(r.substring(r.lastIndexOf('.') + 1))) {
                try {
                    return call.getMethod(r.substring(0, r.indexOf("("))).run(new Arguments(getArgs(betweenMatch(s, '(', ')'))));
                } catch (InternalNotFoundException ex) {
                    throw new RuntimeException("Could not find context \"" + r.substring(0, r.indexOf("(")) + "\"");
                }
            }

            if (call != null) {
                try {
                    return call.getVariable(r);
                } catch (InternalNotFoundException ex) {
                    throw new RuntimeException(r + " could not found in context " + call);
                }
            }
        }

        throw new NullPointerException(s + " was not a value");
    }

    public void runBlock(String b) {
        try {
            if (b.startsWith("if(")) {
                GordianIf.run(this, b);
            } else if (b.startsWith("for(")) {
                GordianFor.run(this, b);
            } else if (b.startsWith("count(")) {
                GordianCount.run(this, b);
            } else if (b.startsWith("thread{")) {
                GordianThread.run(this, b);
            } else if (b.startsWith("try{")) {
                GordianTry.run(this, b);
            } else if (b.startsWith("while(")) {
                GordianWhile.run(this, b);
            } else if (b.startsWith("def") && isName(b.substring(3, b.indexOf('('))))  {
                methods().put(b.substring(3, b.indexOf('(')), GordianDefinedMethod.get(this, b));
            } else {
                throw new NullPointerException("Block was not recognized - " + b);
            }
        } catch (Break e) {
            // break was called
        }
    }

    private static interface Operator {

        public char getChar();

        public double result(double o, double o1);
    }

    private static class Addition implements Operator {

        public char getChar() {
            return '+';
        }

        public double result(double o, double o1) {
            return o + o1;
        }
    }

    private static class Subtraction implements Operator {

        public char getChar() {
            return '-';
        }

        public double result(double o, double o1) {
            return o - o1;
        }
    }

    private static class Multiplication implements Operator {

        public char getChar() {
            return '*';
        }

        public double result(double o, double o1) {
            return o * o1;
        }
    }

    private static class Division implements Operator {

        public char getChar() {
            return '/';
        }

        public double result(double o, double o1) {
            return o / o1;
        }
    }

    private static class Modulus implements Operator {

        public char getChar() {
            return '%';
        }

        public double result(double o, double o1) {
            return o % o1;
        }
    }
}
