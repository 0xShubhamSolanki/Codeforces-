import java.io.*;
import java.util.*;

public class Main {

    // ------------------ GLOBALS ------------------
    static FastReader sc = new FastReader();
    static PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    static final long MOD = 1000000007;

    public static void main(String[] args) throws Exception {
        int t = 1;
        t = sc.nextInt();        // uncomment if needed

        while (t-- > 0) solve();

        out.flush();
        out.close();
    }

    // ------------------ SOLUTION ------------------
    static void solve() throws Exception {
        int n = sc.nextInt();
        int[] a = sc.readArrayInt(n);

        ruffle(a);

        for (int x : a) out.print(x + " ");
        out.println();
    }

    // ------------------ UTILITIES ------------------

    // Ruffle Sort (random shuffle + sort)
    static void ruffle(int[] a) {
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            int j = r.nextInt(a.length);
            int tmp = a[i]; a[i] = a[j]; a[j] = tmp;
        }
        Arrays.sort(a);
    }

    // GCD
    static long gcd(long a, long b) {
        while (b != 0) {
            long t = b; b = a % b; a = t;
        }
        return a;
    }

    // Fast Power (Binary Exponentiation)
    static long power(long a, long b) {
        long res = 1;
        a %= MOD;
        while (b > 0) {
            if ((b & 1) == 1) res = (res * a) % MOD;
            a = (a * a) % MOD;
            b >>= 1;
        }
        return res;
    }

    // ------------------ DSU ------------------
    static class DSU {
        int[] parent, size;
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        int find(int x) {
            return parent[x] == x ? x : (parent[x] = find(parent[x]));
        }
        void union(int a, int b) {
            a = find(a); b = find(b);
            if (a != b) {
                if (size[a] < size[b]) { int t = a; a = b; b = t; }
                parent[b] = a;
                size[a] += size[b];
            }
        }
    }

    // ------------------ GRAPH BFS ------------------
    static void bfs(List<Integer>[] g, int src, int[] dist) {
        Arrays.fill(dist, -1);
        Queue<Integer> q = new ArrayDeque<>();
        q.add(src); dist[src] = 0;

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : g[u]) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.add(v);
                }
            }
        }
    }

    // ------------------ DFS ------------------
    static void dfs(List<Integer>[] g, int u, boolean[] vis) {
        vis[u] = true;
        for (int v : g[u])
            if (!vis[v]) dfs(g, v, vis);
    }

    // ------------------ DIJKSTRA ------------------
    static long[] dijkstra(List<Pair>[] g, int src) {
        int n = g.length;
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);

        PriorityQueue<Pair> pq = new PriorityQueue<>();
        dist[src] = 0;
        pq.add(new Pair(0, src));

        while (!pq.isEmpty()) {
            Pair p = pq.poll();
            long d = p.w; int u = p.v;

            if (d != dist[u]) continue;

            for (Pair e : g[u]) {
                int v = e.v;
                long w = e.w;
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.add(new Pair(dist[v], v));
                }
            }
        }
        return dist;
    }

    // ------------------ Fenwick Tree (BIT) ------------------
    static class Fenwick {
        int n;
        long[] bit;

        Fenwick(int n) {
            this.n = n;
            bit = new long[n + 1];
        }

        void update(int i, long val) {
            for (; i <= n; i += i & -i) bit[i] += val;
        }

        long query(int i) {
            long s = 0;
            for (; i > 0; i -= i & -i) s += bit[i];
            return s;
        }

        long range(int l, int r) {
            return query(r) - query(l - 1);
        }
    }

    // ------------------ Pair Class ------------------
    static class Pair implements Comparable<Pair> {
        long w; int v;
        Pair(long w, int v) { this.w = w; this.v = v; }
        public int compareTo(Pair o) {
            return Long.compare(this.w, o.w);
        }
    }

    // ------------------ FAST READER ------------------
    static class FastReader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public FastReader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        int[] readArrayInt(int n) throws IOException {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = nextInt();
            return a;
        }

        long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do ret = ret * 10 + c - '0';
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -ret : ret;
        }

        int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do ret = ret * 10 + c - '0';
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -ret : ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }
    }
}
