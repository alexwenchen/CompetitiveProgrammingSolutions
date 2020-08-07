import java.util.*;
import java.io.*;

public class promote {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };

	static int N;

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

	static int[] p;
	static ArrayList<Integer>[] adj;
	static int[] ans;
	static int[] sizes;
	static ArrayList<Integer> order;

	static int dfs(int v, int par) {
		order.add(v);
		if (adj[v].size() == 1) {
			sizes[v] = 0;
			return 1;
		}
		for (Integer i : adj[v]) {
			if (i == par)
				continue;
			sizes[v] += dfs(i, v);
		}
		return sizes[v] + 1;
	}

	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("promote.in");
		PrintWriter out = new PrintWriter(new File("promote.out"));
		N = in.nextInt();
		p = new int[N];
		ans = new int[N];
		adj = new ArrayList[N];
		sizes = new int[N];
		order = new ArrayList<Integer>();
		for (int i = 0; i < N; ++i) {
			p[i] = in.nextInt();
			adj[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < N - 1; ++i) {
			int par = in.nextInt() - 1;
			adj[par].add(i + 1);
			adj[i + 1].add(par);
		}
		dfs(0, -1);
		tree = new int[N + 5];
		node[] nodes = new node[N];
		for(int i = 0; i < N; ++i) {
			nodes[i] = new node(p[order.get(i)], i, order.get(i));
		}
		Arrays.sort(nodes, (a, b) -> b.val - a.val);
		for(int i = 0; i < N; ++i) {
			ans[nodes[i].orig] = query(nodes[i].ind, nodes[i].ind + sizes[nodes[i].orig]);
			update(nodes[i].ind, 1);
		}
		for(int i = 0; i < N; ++i) {
			out.println(ans[i]);
		}
		in.close();
		out.close();
	}
	static class node{
		int val, ind, orig;
		node(int v, int i, int o){
			val = v; ind = i; orig = o;
		}
	}
	static int[] tree;
	static void update(int index, int val) {
		index++;
		while (index < tree.length) {
			tree[index] += val;
			index += index & -index;
		}
	}
	static int query(int index) {
		int ret = 0;
		index++;
		while (index > 0) {
			ret += tree[index];
			index -= index & -index;
		}
		return ret;
	}
	static int query(int a, int b) {
		return query(b) - query(a - 1);
	}
}