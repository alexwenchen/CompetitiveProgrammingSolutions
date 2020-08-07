import java.util.*;
import java.io.*;

public class valleys {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] dx = { 0, 0, -1, 1 };
	final static int[] dy = { -1, 1, 0, 0 };

	static int add(int a, int b) {
		return (a + b) % MOD;
	}
	
	static int sub(int a, int b) {
		return (a - b + MOD) % MOD;
	}
	
	static int mult(int a, int b) {
		return (int)((((long)(a)) * b) % MOD);
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

	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("valleys.in");
		PrintWriter out = new PrintWriter(new File("valleys.out"));
		int N = in.nextInt();
		int[][] grid = new int[N][N];
		pnt[] loc = new pnt[N * N];
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < N; ++j) {
				grid[i][j] = in.nextInt();
				loc[i * N + j] = new pnt(grid[i][j], i * N + j);
			}
		}
		Arrays.sort(loc, (a, b) -> a.ht - b.ht);
		int dx2[] = {1, 1, 1, 0, 0, -1, -1, -1};
		int dy2[] = {1, 0, -1, 1, -1, 1, 0, -1};
		dsu dsu1 = new dsu(N * N + 1);
		int[] nh = new int[N * N + 1]; nh[N * N] = 0;
		for(int i = N * N - 1; i >= 0; --i) {
			nh[i] = nh[i + 1] + 1;
			for(int j = 0; j < 8; ++j) {
				int x = loc[i].ind / N;
				int y = loc[i].ind % N;
				int x2 = x + dx2[j];
				int y2 = y + dy2[j];
				int i2 = (0 <= x2 && x2 < N && 0 <= y2 && y2 < N ? x2 * N + y2 : N * N);
				if(dsu1.find(i2) != dsu1.find(loc[i].ind) && (i2 == N * N || grid[x][y] < grid[x2][y2])) {
					--nh[i];
					dsu1.merge(loc[i].ind, i2);
				}
			}
		}
		dsu dsu2 = new dsu(N * N);
		long ans = 0;
		for(int i = 0; i < N * N; ++i) {
			for(int j = 0; j < 4; ++j) {
				int x = loc[i].ind / N;
				int y = loc[i].ind % N;
				int x2 = x + dx[j];
				int y2 = y + dy[j];
				if(0 <= x2 && x2 < N && 0 <= y2 && y2 < N) {
					int i2 = x2 * N + y2;
					if(dsu2.find(i2) != dsu2.find(loc[i].ind) && grid[x][y] > grid[x2][y2]) {
						dsu2.merge(loc[i].ind, i2);
					}
				}
			}
			dsu2.holes[dsu2.find(loc[i].ind)] += nh[i + 1] - nh[i];
			if(dsu2.holes[dsu2.find(loc[i].ind)] == 0) ans += dsu2.sz[dsu2.find(loc[i].ind)];
		}
		out.println(ans);
		in.close();
		out.close();
	}
	static class pnt{
		int ht, ind;
		pnt(int h, int i){
			ht = h; ind = i;
		}
	}
	static class coord{
		int x, y;
		coord(int a, int b){
			x = a;y = b;
		}
	}
	static class dsu{
		int[] par;
		int[] sz;
		int[] holes;
		dsu(int n){
			par = new int[n];
			sz = new int[n];
			holes = new int[n];
			for(int i = 0; i < n; ++i) {
				par[i] = i;
			}
			Arrays.fill(sz, 1);
		}
		int find(int x) {
			while(x != par[x]) {
				par[x] = par[par[x]];
				x = par[x];
			}
			return x;
		}
		void merge(int x, int y) {
			int fx = find(x);
			int fy = find(y);
			if(fx == fy) return;
			if(sz[fx] < sz[fy]) {
				par[fx] = fy;
				sz[fy] += sz[fx];
				holes[fy] += holes[fx];
			}
			else {
				par[fy] = fx;
				sz[fx] += sz[fy];
				holes[fx] += holes[fy];
			}
		}
	}
}