package de.viadee.unsafeThreadsExample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An illustrative example, based on.
 * 
 * @link http://www.codefutures.com/weblog/andygrove/2007/10/simpledateformat-and -thread-safety.html
 * 
 *       This kind of setting is common in all kinds of servlets and web services.
 */
public class ProveNotSafe {

    static SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

    static String testdata[] = { "01-Jan-1999", "14-Feb-2001", "31-Dec-2007" };

    public static void main(final String[] args) {
        final Runnable r[] = new Runnable[testdata.length];
        // Create and start one thread per test date item
        for (int i = 0; i < r.length; i++) {
            final int i2 = i;
            r[i] = new Runnable() {

                public void run() {
                    try {
                        // One thousand times: parse and format using the same formatter
                        for (int j = 0; j < 1000; j++) {
                            final String str = testdata[i2];
                            String str2 = null;
                            /* synchronized(df) */ {
                                final Date d = df.parse(str);
                                str2 = df.format(d);
                            }
                            if (!str.equals(str2)) {
                                throw new RuntimeException("date conversion failed after " + j
                                        + " iterations. Expected " + str + " but got " + str2);
                            }
                        }
                    } catch (final ParseException e) {
                        throw new RuntimeException("parse failed");
                    }
                }
            };
            new Thread(r[i]).start();
        }
    }
}