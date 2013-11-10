package org.gordian.scope;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class GordianTry extends GordianScope {

    public static void run(GordianScope scope, String s) {
        GordianTry gt = new GordianTry(scope);
        if (s.contains("}catch{")) {
            gt.runWithCatch(s.substring(s.indexOf("{") + 1, s.indexOf("}catch{")),
                    s.substring(s.indexOf("}catch{") + 7, s.lastIndexOf("}")));
        } else {
            gt.runWithoutCatch(s.substring(s.indexOf("{") + 1, s.lastIndexOf("}")));
        }
    }

    public GordianTry(GordianScope container) {
        super(container);
    }

    public void runWithCatch(String t, String c) {
        try {
            super.run(t);
        } catch (Exception ex) {
            super.run(c);
        }
    }

    public void runWithoutCatch(String t) {
        try {
            super.run(t);
        } catch (Exception ex) {
        }
    }
}
