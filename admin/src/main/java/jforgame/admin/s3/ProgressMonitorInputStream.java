package jforgame.admin.s3;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProgressMonitorInputStream extends FilterInputStream {

    private ProgressMonitor progressMonitor;
    private long nRead = 0;

    public ProgressMonitorInputStream(InputStream in, ProgressMonitor progressMonitor) {
        super(in);
        this.progressMonitor = progressMonitor;
    }

    @Override
    public int read() throws IOException {
        int c = super.read();
        if (c != -1) {
            nRead++;
            updateProgress();
        }
        return c;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int n = super.read(b, off, len);
        if (n != -1) {
            nRead += n;
            updateProgress();
        }
        return n;
    }

    @Override
    public long skip(long n) throws IOException {
        long skipped = super.skip(n);
        nRead += skipped;
        updateProgress();
        return skipped;
    }

    private void updateProgress() {
        progressMonitor.setProgress(nRead);
    }

}