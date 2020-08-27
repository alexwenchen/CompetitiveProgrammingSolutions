//        7/13, but c++ version with same idea gets AC
import java.util.*;
import java.io.*;

public class disrupt {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };

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

	static ArrayList<Integer>[] adj;
	static TreeSet<path>[] sets;
	static HashSet<path>[] add, remove;
	static int[] ans, numEdge;
	
	static void dfs(int v, int p) {
		for(Integer i : adj[v]) {
			if(i == p) continue;
			dfs(i, v);
			if(sets[i].size() > sets[v].size()) {
				TreeSet<path> temp = (TreeSet<path>) sets[v].clone();
				sets[v] = (TreeSet<path>) sets[i].clone();
				sets[i] = temp;
			}
			for(path j : sets[i]) {
				sets[v].add(j);
			}
			sets[i].clear();
		}
		if(p == -1) return;
		for(path i : add[v]) {
			sets[v].add(i);
		}
		for(path i : remove[v]) {
			sets[v].remove(i);
		}
		if(!sets[v].isEmpty()) {
			ans[numEdge[v]] = (int) (sets[v].first().comp / 2500000000L);
		}
		else {
			ans[numEdge[v]] = -1;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Reader in = new Reader();
//		Reader in = new Reader("disrupt.in");
		PrintWriter out = new PrintWriter(new File("disrupt.out"));
		int N = in.nextInt();
		int M = in.nextInt();
		adj = new ArrayList[N];
		sets = new TreeSet[N];
		add = new HashSet[N];
		remove = new HashSet[N];
		ans = new int[N - 1];
		numEdge = new int[N];
		for(int i = 0; i < N; ++i) {
			adj[i] = new ArrayList<Integer>();
			sets[i] = new TreeSet<path>((a, b) -> Long.compare(a.comp, b.comp));
			add[i] = new HashSet<path>();
			remove[i] = new HashSet<path>();
		}
		edge[] edges = new edge[N - 1];
		for(int i = 0; i < N - 1; ++i) {
			int a = in.nextInt() - 1;
			int b = in.nextInt() - 1;
			edges[i] = new edge(a, b);
			adj[a].add(b);
			adj[b].add(a);
		}
		dfssetup(0, -1);
		initLCA();
		for(int i = 0; i < N - 1; ++i) {
			int a = edges[i].a;
			int b = edges[i].b;
			int lca = LCA(a, b);
			if(lca == a) {
				numEdge[b] = i;
			}
			else {
				numEdge[a] = i;
			}
		}
		for(int i = 0; i < M; ++i) {
			int a = in.nextInt() - 1;
			int b = in.nextInt() - 1;
			int c = in.nextInt();
			int lca = LCA(a, b);
			path cur = new path(a, b, c);
			if(lca == a) {
				remove[a].add(cur);
				add[b].add(cur);
			}
			else if(lca == b){
				remove[b].add(cur);
				add[a].add(cur);
			}
			else {
				remove[lca].add(cur);
				add[a].add(cur);
				add[b].add(cur);
			}
		}
		dfs(0, -1);
		for(int i = 0; i < N - 1; ++i) {
			out.println(ans[i]);
		}
		in.close();
		out.close();
	}
	static class edge{
		int a, b;
		edge(int x, int y){
			a = x; b = y;
		}
	}
	static class path{
		int a, b, length; long comp;
		path(int x, int y, int l){
			a = x; b = y; length = l;
			comp = a + b * 50000L + length * 2500000000L;
		}
		@Override
		public int hashCode() {
			return Long.hashCode(comp);
		}
		@Override
		public boolean equals(Object obj) {
			path other = (path) obj;
			if (a != other.a)
				return false;
			if (b != other.b)
				return false;
			if (comp != other.comp)
				return false;
			if (length != other.length)
				return false;
			return true;
		}
	}
	static int MAX_D = 16;
	static int MAX_N = 50000;
	static int[] depth = new int[MAX_N];
	static int[][] lca = new int[MAX_N][MAX_D];
	static void dfssetup(int curr, int par) {
		for(int out: adj[curr]) {
			if(out == par)
				continue;
			depth[out] = depth[curr] + 1;
			lca[out][0] = curr;
			dfssetup(out, curr);
		}
	}
	static int LCA(int a, int b) {
		if(depth[a] < depth[b]) {
			int temp = a;
			a = b;
			b = temp;
		}
	  	for(int d = MAX_D-1; d >= 0; d--) {
	  		if(depth[a] - (1<<d) >= depth[b]) {
	  			a = lca[a][d];
	  		}
	  	}
	  	for(int d = MAX_D-1; d >= 0; d--) {
	  		if(lca[a][d] != lca[b][d]) {
	  			a = lca[a][d];
	  			b = lca[b][d];
	    	}
	  	}
	  	if(a != b) {
		  	a = lca[a][0];
	    	b = lca[b][0];
	  	}
	  	return a;
	}
	static void initLCA() {
		for(int d = 1; d < MAX_D; d++) {
			for(int i = 0; i < MAX_N; i++) {
				lca[i][d] = lca[lca[i][d-1]][d-1];
			}
		}
	}
}
