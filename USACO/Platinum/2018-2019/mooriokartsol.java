import java.util.*;
import java.io.*;

public class mooriokartsol {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };

	static long add(long a, long b) {
		return (a + b) % MOD;
	}
	
	static long sub(long a, long b) {
		return (a - b + MOD) % MOD;
	}
	
	static long mult(long a, long b) {
		return (((long) a * b) % MOD);
	}
	
	static class Reader {
		final private int BUFFER_SIZE = 1 << 16;
		private DataInputStream din;
		private byte[] buffer;
		private int bufferPointer, bytesRead;

		public Reader() {
			din = new DataInputStream(System.in);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public Reader(String file_name) throws IOException {
			din = new DataInputStream(new FileInputStream(file_name));
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public String readLine() throws IOException {
			byte[] buf = new byte[360]; // line length
			int cnt = 0, c;
			while ((c = read()) != -1) {
				if (c == '\n')
					break;
				buf[cnt++] = (byte) c;
			}
			return new String(buf, 0, cnt);
		}

		public int nextInt() throws IOException {
			int ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (neg)
				return -ret;
			return ret;
		}

		public long nextLong() throws IOException {
			long ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');
			if (neg)
				return -ret;
			return ret;
		}

		public double nextDouble() throws IOException {
			double ret = 0, div = 1;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();

			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (c == '.') {
				while ((c = read()) >= '0' && c <= '9') {
					ret += (c - '0') / (div *= 10);
				}
			}

			if (neg)
				return -ret;
			return ret;
		}

		private void fillBuffer() throws IOException {
			bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
			if (bytesRead == -1)
				buffer[0] = -1;
		}

		private byte read() throws IOException {
			if (bufferPointer == bytesRead)
				fillBuffer();
			return buffer[bufferPointer++];
		}

		public void close() throws IOException {
			if (din == null)
				return;
			din.close();
		}
	}

	static ArrayList<edge>[] adj;
	static HashMap<Integer, Long>[] paths;
	static ArrayList<Integer> dist, comp;

	static void dfs1(int v) {
		if(vis[v]) return;
		vis[v] = true;
		comp.add(v);
		for(edge i : adj[v]) {
			dfs1(i.dest);
		}
	}
	
	static void dfs2(int v, int p, int orig, int d) {
		for(edge i : adj[v]) {
			if(i.dest == p) continue;
			if(orig < i.dest) {
				dist.add(d + i.weight);
			}
			dfs2(i.dest, v, orig, d + i.weight);
		}
	}
	
	static pair comb(pair a, pair b) {
		return new pair(add(mult(a.num, b.tot), mult(b.num, a.tot)), mult(a.num, b.num));
	}
	
	static boolean[] vis;
	
	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("mooriokart.in");
		PrintWriter out = new PrintWriter(new File("mooriokart.out"));
		int N = in.nextInt();
		int M = in.nextInt();
		int X = in.nextInt();
		int Y = in.nextInt();
		int K = N - M;
		adj = new ArrayList[N];
		for(int i = 0; i < N; ++i) {
			adj[i] = new ArrayList<edge>();
		}
		vis = new boolean[N];
		for(int i = 0; i < M; ++i) {
			int ai = in.nextInt() - 1;
			int bi = in.nextInt() - 1;
			int di = in.nextInt();
			adj[ai].add(new edge(bi, di));
			adj[bi].add(new edge(ai, di));
		}
		dist = new ArrayList<Integer>();
		comp = new ArrayList<Integer>();
		pair[] overall = new pair[Y + 1];
		pair[] rep = new pair[Y + 1];
		for(int i = 0; i <= Y; ++i) {
			overall[i] = new pair(0, 0);
		}
		overall[Math.min(Y, K * X)] = new pair(K * X, 1);
		for(int i = 0; i < N; ++i) {
			if(!vis[i]) {
				comp.clear();
				dfs1(i);
				dist.clear();
				for(Integer j : comp) {
					dfs2(j, -1, j, 0);
				}
				HashMap<Integer, pair> m = new HashMap<Integer, pair>();
				for(Integer j : dist) {
					int key = Math.min(j, Y);
					if(m.containsKey(key)) {
						m.put(key, new pair(add(m.get(key).tot, j), m.get(key).num + 1));
					}
					else {
						m.put(key, new pair(j,1));
					}
				}
				for(int j = 0; j <= Y; ++j) {
					rep[j] = new pair(0, 0);
				}
				for(Integer j : m.keySet()) {
					for(int k = 0; k <= Y; ++k) {
						int key = Math.min(j + k, Y);
						pair combine = comb(m.get(j), overall[k]);
						rep[key] = new pair(add(rep[key].tot, combine.tot), add(rep[key].num, combine.num));
					}
				}
				for(int j = 0; j <= Y; ++j) {
					overall[j] = rep[j];
				}
			}
		}
		long ans = rep[Y].tot;
		for(int i = 0; i < K - 1; ++i) {
			ans = mult(ans, 2);
		}
		for(int i = 1; i < K; ++i) {
			ans = mult(ans, i);
		}
		out.println(ans);	
		in.close();
		out.close();
	}
	static class edge{
		int dest, weight;
		edge(int d, int w){
			dest = d; weight = w;
		}
	}
	static class pair{
		long tot, num;
		pair(long t, long n){
			tot = t; num = n;
		}
		public String toString() {
			return tot + " " + num;
		}
	}
}