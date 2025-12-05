import java.io.*;
import java.util.*;

public class Main {

    static FastReader sc = new FastReader();
    static PrintWriter out = new PrintWriter(System.out);
    static final long MOD = 1000000007;

    public static void main(String[] args) throws Exception {
        int t = 1;
        t = sc.nextInt();     // multiple testcases
        while (t-- > 0) solve();
        out.flush();
    }

    static void solve() throws Exception {
        int n = sc.nextInt();
        int[] a = sc.readIntArray(n);

        ruffleSort(a);  // optional but safe

        for (int x : a) out.print(x + " ");
        out.println();
    }

    // ----------- UTILITIES -----------

    static void ruffleSort(int[] a) {
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            int j = r.nextInt(a.length);
            int temp = a[i]; a[i] = a[j]; a[j] = temp;
        }
        Arrays.sort(a);
    }

    static long gcd(long a, long b) {
        while (b != 0) { long t = b; b = a % b; a = t; }
        return a;
    }

    static long power(long a, long b) {
        long res = 1; a %= MOD;
        while (b > 0) {
            if ((b & 1) == 1) res = (res * a) % MOD;
            a = (a * a) % MOD;
            b >>= 1;
        }
        return res;
    }

    // ----------- FAST INPUT -----------
    static class FastReader {
        private final int BUFFER_SIZE = 1 << 16;
        private DataInputStream din = new DataInputStream(System.in);
        private byte[] buffer = new byte[BUFFER_SIZE];
        private int bufferPointer = 0, bytesRead = 0;

        int nextInt() throws IOException {
            int x = 0; byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-'); 
            if (neg) c = read();
            do { x = x * 10 + c - '0'; }
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        int[] readIntArray(int n) throws IOException {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = nextInt();
            return a;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) buffer[0] = -1;
        }
    }
}
