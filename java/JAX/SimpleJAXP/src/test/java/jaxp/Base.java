package jaxp;

public class Base {

    private static final long ELAPSE_TIME = 10 * 1000; // 10s
    private long begin;
    private long count;

    public void initPerf() {
        begin = System.currentTimeMillis();
        count = 0;
    }

    public void termPerf() {
        long elapsed = System.currentTimeMillis() - begin;
        System.out.printf("elapsed=%dms, count=%d, call=%sms, tps=%s\n",
                elapsed, count, (float) elapsed / count, (float) count * 1000 / elapsed);
    }

    public boolean computePerf() {
        ++count;
        return System.currentTimeMillis() - begin < ELAPSE_TIME;
    }
}
