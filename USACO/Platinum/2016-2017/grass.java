import java.util.*;
import java.io.*;

//idea : https://codeforces.com/blog/entry/50889?#comment-348725

//TLE :( but same idea gets AC in c++

public class grass {
	final static int MOD = 1000000007;
	final static int intMax = 10000000;
	final static int off = 100000000;
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
	
	static class Sb_Output implements Closeable, Flushable{
	    StringBuilder sb;
	    OutputStream os;
	    int BUFFER_SIZE;
	    public Sb_Output(String s) throws Exception {
	    	os = new BufferedOutputStream(new FileOutputStream(new File(s)));
	    	BUFFER_SIZE = 1 << 16;
	    	sb = new StringBuilder(BUFFER_SIZE);
	    }
	    public void print(String s) {
	        sb.append(s);
	        if(sb.length() >= (BUFFER_SIZE>>1)) {
	            flush();
	        }
	    }
	    public void println(String s) {
	        print(s);
	        print("\n");
	    }
	    public void flush() {
	        try {
	            os.write(sb.toString().getBytes());
	            sb = new StringBuilder(BUFFER_SIZE);
	        }catch(IOException e) {
	            e.printStackTrace();
	        }
	    }
	    public void close() {
	        flush();
	        try {
	            os.close();
	        }catch(IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	static int max(int a, int b) {
		return a > b ? a : b;
	}
	
	static int min(int a, int b) {
		return a < b ? a : b;
	}
	
	static int N, M, K, Q;
	static ArrayList<edge>[] adj;
	static query[] queries;
	static int[] neworder, order, par, types, orderToActual, loc, weights;
	static int ind = 0;
	static TreeSet<range>[] ranges;
	static fullEdge edges[];
	static int[] parent, rank;

	static void dfs(int v, int p) {
		par[v] = max(0, p);
		if (p == -1)
			order[ind++] = v;
		ArrayList<Integer> children = new ArrayList<Integer>();
		for (edge i : adj[v]) {
			if (i.dest == p)
				continue;
			children.add(i.dest);
		}
		Collections.sort(children, (a, b) -> types[a] - types[b]);
		ArrayList<range> cands = new ArrayList<range>();
		int cur = -1;
		for (Integer i : children) {
			if (cur == -1) {
				cur = types[i];
				cands.add(new range(cur, ind));
			}
			if (types[i] != cur) {
				cands.get(cands.size() - 1).r = ind - 1;
				cands.add(new range(types[i], ind));
				cur = types[i];
			}
			order[ind++] = i;
		}
		if (cands.size() > 0)
			cands.get(cands.size() - 1).r = ind - 1;
		for (range i : cands) {
			ranges[v].add(i);
		}
		for (edge i : adj[v]) {
			if (i.dest == p)
				continue;
			weights[i.dest] = i.weight;
			dfs(i.dest, v);
		}
	}
	

	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("grass.in");
		Sb_Output pw = new Sb_Output("grass.out");
		N = in.nextInt();
		M = in.nextInt();
		K = in.nextInt();
		Q = in.nextInt();
		adj = new ArrayList[N];
		parent = new int[N];
		rank = new int[N];
		queries = new query[Q];
		par = new int[N];
		order = new int[N];
		ranges = new TreeSet[N];
		neworder = new int[N + Q];
		orderToActual = new int[N];
		loc = new int[N];
		weights = new int[N];
		for (int i = 0; i < N; ++i) {
			adj[i] = new ArrayList<edge>();
			ranges[i] = new TreeSet<range>((a, b) -> a.type - b.type);
		}
		edges = new fullEdge[M];
		for (int i = 0; i < M; ++i) {
			int A = in.nextInt() - 1;
			int B = in.nextInt() - 1;
			int L = in.nextInt();
			edges[i] = new fullEdge(A, B, L);
		}
		types = new int[N];
		for (int i = 0; i < N; ++i) {
			types[i] = in.nextInt();
		}
		for (int i = 0; i < Q; ++i) {
			int A = in.nextInt() - 1;
			int B = in.nextInt();
			queries[i] = new query(A, B, i);
		}
		KruskalsAlgorithm();
		LazySegmentTree lst = new LazySegmentTree(N + Q);
		dfs(0, -1);
		for (int i = 0; i < N; ++i) {
			orderToActual[order[i]] = i;
		}
		Arrays.sort(queries, (a, b) -> orderToActual[par[a.node]] == orderToActual[par[b.node]] ? a.type - b.type
				: orderToActual[a.node] - orderToActual[b.node]);
		Arrays.fill(neworder, -1);
		int amtforward = 0;
		two[] stats = new two[N + Q];
		for (int i = 0; i < Q; ++i) {
			if (queries[i].node == 0)
				continue;
			int curnode = queries[i].node;
			int curtype = queries[i].type;
			range dummy = new range(curtype, 0);
			range next = ranges[par[curnode]].ceiling(dummy);
			if (next == null) {
				next = ranges[par[curnode]].floor(dummy);
				neworder[next.r + amtforward + 1] = -2;
				stats[next.r + amtforward + 1] = new two(par[curnode], curtype);
				queries[i].move = next.r + amtforward + 1;
			} else {
				neworder[next.l + amtforward] = -2;
				queries[i].move = next.l + amtforward;
				stats[next.l + amtforward] = new two(par[curnode], curtype);
			}
			++amtforward;
		}
		amtforward = 0;
		// -2 if filled with a query
		for (int i = 0; i < N; ++i) {
			while (neworder[i + amtforward] == -2)
				++amtforward;
			neworder[i + amtforward] = order[i];
		}
		for (int i = 0; i < N + Q; ++i) {
			if (neworder[i] <= 0) {
				lst.update(i, i, off);
			} else {
				stats[i] = new two(par[neworder[i]], types[neworder[i]]);
				loc[neworder[i]] = i;
				lst.update(i, i, weights[neworder[i]]);
			}
		}
		// update ranges
		for (int i = 0; i < N; ++i) {
			ranges[i].clear();
		}
		for (int i = 0; i < N + Q; ++i) {
			if (stats[i] == null)
				continue;
			int curpar = stats[i].par;
			int curtype = stats[i].type;
			int ind = i;
			while (ind < N + Q && stats[ind] != null && stats[ind].par == curpar && stats[ind].type == curtype) {
				++ind;
			}
			ranges[curpar].add(new range(curtype, i, ind - 1));
			i = ind - 1;
		}
		// done updating ranges
		for (int i = 0; i < N; ++i) {
			if (i != 0) {
				if (types[i] == types[par[i]]) {
					lst.update(loc[i], loc[i], intMax);
				}
			}
		}

		Arrays.sort(queries, (a, b) -> a.ind - b.ind);
		for (int i = 0; i < Q; ++i) {
			//p11
			int node = queries[i].node;
			int oldtype = types[node];
			types[node] = queries[i].type;
			int newtype = types[node];
			int move = queries[i].move;
			if (node != 0) {
				lst.update(loc[node], loc[node], 2 * intMax);
				loc[node] = move;
				if (newtype == types[par[node]]) {
					lst.update(move, move, -lst.minQuery(move, move) + intMax + weights[node]);
				}
				else {
					lst.update(move, move, -lst.minQuery(move, move) + weights[node]);
				}
			}
			
			range dummy1 = new range(oldtype, 0); // remove intmax from these
			range remove = ranges[node].ceiling(dummy1);
			if (remove != null && remove.type == oldtype) {
				lst.update(remove.l, remove.r, -intMax);
			}
			range dummy2 = new range(newtype, 0); // add intmax to these
			range add = ranges[node].ceiling(dummy2);
			if (add != null && add.type == newtype) {
				lst.update(add.l, add.r, intMax);
			}
			int ans = lst.minQuery(0, N + Q - 1);
			if (ans < intMax) {
				pw.println("" + ans);
			}
		}
		pw.close();
		in.close();
	}

	static class two {
		int par, type;

		two(int p, int t) {
			par = p;
			type = t;
		}
	}

	static class range {
		int type, l, r;

		range(int c, int a) {
			type = c;
			l = a;
		}

		range(int c, int a, int b) {
			type = c;
			l = a;
			r = b;
		}

		public String toString() {
			return type + " " + l + " " + r;
		}
	}

	static class query {
		int node, type, move, ind;

		query(int n, int c, int i) {
			node = n;
			type = c;
			ind = i;
		}

		public String toString() {
			return node + " " + type + " " + move;
		}
	}

	static class edge {
		int dest, weight;

		edge(int d, int w) {
			dest = d;
			weight = w;
		}
		public String toString() {
			return dest + " " + weight;
		}
	}

	static class fullEdge {
		int src, dest, weight;

		fullEdge(int s, int d, int w) {
			src = s;
			dest = d;
			weight = w;
		}
	}

	static int find(int x) {
		if (parent[x] != x) {
			parent[x] = find(parent[x]);
		}
		return parent[x];
	}

	static void union(int x, int y) {
		int xRoot = find(x), yRoot = find(y);
		if (xRoot == yRoot)
			return;
		if (rank[xRoot] < rank[yRoot])
			parent[xRoot] = yRoot;
		else if (rank[yRoot] < rank[xRoot])
			parent[yRoot] = xRoot;
		else {
			parent[yRoot] = xRoot;
			rank[xRoot] = rank[xRoot] + 1;
		}
	}

	static void KruskalsAlgorithm() {
		Arrays.sort(edges, (a, b) -> a.weight - b.weight);
		for (int i = 0; i < N; ++i) {
			parent[i] = i;
		}
		int numEdges = 0;
		int edgeIndex = 0;
		while (numEdges < N - 1) {
			fullEdge next = edges[edgeIndex];
			int x = find(next.src);
			int y = find(next.dest);
			int sc = next.src;
			int dst = next.dest;
			int wt = next.weight;
			if (x != y) {
				adj[sc].add(new edge(dst, wt));
				adj[dst].add(new edge(sc, wt));
				union(x, y);
				++numEdges;
			}
			++edgeIndex;
		}
	}
	static class LazySegmentTree {
		private int[] min;
		private int[] lazy;
		private int size;

		public LazySegmentTree(int size) {
			this.size = size;
			min = new int[4 * size];
			lazy = new int[4 * size];
		}

		public void update(int l, int r, int inc) {
			update(1, 0, size - 1, l, r, inc);
		}

		private void pushDown(int index, int l, int r) {
			min[index] += lazy[index];
			if (l != r) {
				lazy[2 * index] += lazy[index];
				lazy[2 * index + 1] += lazy[index];
			}
			lazy[index] = 0;
		}

		private void pullUp(int index, int l, int r) {
			int m = (l + r) / 2;
			min[index] = min(evaluateMin(2 * index, l, m), evaluateMin(2 * index + 1, m + 1, r));
		}

		private int evaluateMin(int index, int l, int r) {
			return min[index] + lazy[index];
		}
		
		private void update(int index, int l, int r, int left, int right, int inc) {
			if (r < left || l > right)
				return;
			if (l >= left && r <= right) {
				lazy[index] += inc;
				return;
			}
			pushDown(index, l, r);
			int m = (l + r) / 2;
			update(2 * index, l, m, left, right, inc);
			update(2 * index + 1, m + 1, r, left, right, inc);
			pullUp(index, l, r);
		}

		public int minQuery(int l, int r) {
			return minQuery(1, 0, size - 1, l, r);
		}

		private int minQuery(int index, int l, int r, int left, int right) {
			if (r < left || l > right)
				return Integer.MAX_VALUE;
			if (l >= left && r <= right) {
				return evaluateMin(index, l, r);
			}
			pushDown(index, l, r);
			int m = (l + r) / 2;
			int ret = Integer.MAX_VALUE;
			ret = min(ret, minQuery(2 * index, l, m, left, right));
			ret = min(ret, minQuery(2 * index + 1, m + 1, r, left, right));
			pullUp(index, l, r);
			return ret;
		}
	}
	
}