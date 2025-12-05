import java.io.*;
import java.util.*;

public class Main {

    // ------------------ GLOBALS ------------------
    static FastReader sc = new FastReader();
    static PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    static final long MOD = 1000000007;

    public static void main(String[] args) throws Exception {
        int t = 1;
        t = sc.nextInt();  // uncomment if needed

        while (t-- > 0) solve();

        out.flush();
        out.close();
    }

    // ------------------ SOLUTION ------------------
    static void solve() throws Exception {
        // Example
        int n = sc.nextInt();
        int[] arr = sc.readArrayInt(n);

        ruffleSort(arr);

        out.println(n);
        for (int x : arr) out.print(x + " ");
        out.println();
    }

    // ------------------ UTILITIES ------------------

    // Ruffle Sort (Anti-hack random shuffle + sort)
    static void ruffleSort(int[] a) {
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            int j = r.nextInt(a.length);
            int temp = a[i]; a[i] = a[j]; a[j] = temp;
        }
        Arrays.sort(a);
    }

    // GCD & LCM
    static long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    // Fast Power
    static long power(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % MOD;
            base = (base * base) % MOD;
            exp >>= 1;
        }
        return result;
    }

    // Reverse array
    static void reverse(int[] a) {
        int i = 0, j = a.length - 1;
        while (i < j) {
            int t = a[i]; a[i] = a[j]; a[j] = t;
            i++; j--;
        }
    }

    // ------------------ MODULAR MATH (nCr, inverse) ------------------
    static long[] fact = new long[2000005];
    static long[] inv = new long[2000005];

    static void initFactorials(int n) {
        fact[0] = 1;
        for (int i = 1; i <= n; i++) fact[i] = fact[i - 1] * i % MOD;
        inv[n] = power(fact[n], MOD - 2);
        for (int i = n - 1; i >= 0; i--) inv[i] = inv[i + 1] * (i + 1) % MOD;
    }

    static long nCr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return fact[n] * inv[r] % MOD * inv[n - r] % MOD;
    }

    // ------------------ DSU / UNION-FIND ------------------
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
            if (parent[x] == x) return x;
            return parent[x] = find(parent[x]);
        }

        void union(int a, int b) {
            int pa = find(a), pb = find(b);
            if (pa == pb) return;
            if (size[pa] < size[pb]) {
                int t = pa; pa = pb; pb = t;
            }
            parent[pb] = pa;
            size[pa] += size[pb];
        }
    }

    // ------------------ GRAPH BFS/DFS ------------------
    static void bfs(List<Integer>[] g, int src, int[] dist) {
        Queue<Integer> q = new ArrayDeque<>();
        q.add(src);
        dist[src] = 0;

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
            long d = p.x; int u = p.y;

            if (d != dist[u]) continue;

            for (Pair e : g[u]) {
                int v = e.y;
                long w = e.x;
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

        void update(int idx, long val) {
            for (; idx <= n; idx += idx & -idx) bit[idx] += val;
        }

        long query(int idx) {
            long sum = 0;
            for (; idx > 0; idx -= idx & -idx) sum += bit[idx];
            return sum;
        }

        long rangeSum(int l, int r) {
            return query(r) - query(l - 1);
        }
    }

    // ------------------ Segment Tree ------------------
    static class SegTree {
        int n;
        long[] tree;

        SegTree(int n) {
            this.n = n;
            tree = new long[4 * n];
        }

        void build(int idx, int l, int r, int[] arr) {
            if (l == r) {
                tree[idx] = arr[l];
                return;
            }
            int mid = (l + r) / 2;
            build(idx * 2, l, mid, arr);
            build(idx * 2 + 1, mid + 1, r, arr);
            tree[idx] = tree[idx * 2] + tree[idx * 2 + 1];
        }

        long query(int idx, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return 0;
            if (ql <= l && r <= qr) return tree[idx];
            int mid = (l + r) / 2;
            return query(idx * 2, l, mid, ql, qr)
                 + query(idx * 2 + 1, mid + 1, r, ql, qr);
        }
    }

    // ------------------ LAZY SEGMENT TREE ------------------
    static class LazySegTree {
        long[] tree, lazy;
        int n;

        LazySegTree(int n) {
            this.n = n;
            tree = new long[4 * n];
            lazy = new long[4 * n];
        }

        void push(int idx, int l, int r) {
            if (lazy[idx] != 0) {
                tree[idx] += lazy[idx] * (r - l + 1);
                if (l != r) {
                    lazy[idx * 2] += lazy[idx];
                    lazy[idx * 2 + 1] += lazy[idx];
                }
                lazy[idx] = 0;
            }
        }

        void update(int idx, int l, int r, int ql, int qr, long val) {
            push(idx, l, r);
            if (qr < l || r < ql) return;
            if (ql <= l && r <= qr) {
                lazy[idx] += val;
                push(idx, l, r);
                return;
            }
            int mid = (l + r) / 2;
            update(idx * 2, l, mid, ql, qr, val);
            update(idx * 2 + 1, mid + 1, r, ql, qr, val);
            tree[idx] = tree[idx * 2] + tree[idx * 2 + 1];
        }

        long query(int idx, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return 0;
            push(idx, l, r);
            if (ql <= l && r <= qr) return tree[idx];
            int mid = (l + r) / 2;
            return query(idx * 2, l, mid, ql, qr)
                 + query(idx * 2 + 1, mid + 1, r, ql, qr);
        }
    }

    // ------------------ PAIR CLASS ------------------
    static class Pair implements Comparable<Pair> {
        long x; 
        int y;
        Pair(long x, int y) { this.x = x; this.y = y; }
        public int compareTo(Pair o) {
            return Long.compare(this.x, o.x);
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

        long[] readArrayLong(int n) throws IOException {
            long[] a = new long[n];
            for (int i = 0; i < n; i++) a[i] = nextLong();
            return a;
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do ret = ret * 10 + c - '0';
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -ret : ret;
        }

        public long nextLong() throws IOException {
            long ret = 0;
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
