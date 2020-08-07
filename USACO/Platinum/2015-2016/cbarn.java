import java.util.*;
import java.io.*;

public class cbarn {
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
	
	static int N, K;
	static int[] barns;
	static long[][] dp;
	static long[][] pref;
	static int start;
	
	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("cbarn.in");
		PrintWriter out = new PrintWriter(new File("cbarn.out"));
		N = in.nextInt();
		K = in.nextInt();
		barns = new int[N];
		for(int i = 0; i < N; ++i) {
			barns[i] = in.nextInt();
		}
		pref = new long[N][N];
		for(int i = 0; i < N; ++i) {
			for(int j = i; j < i + N; ++j) {
				if(i == j) pref[i][j] = 0;
				else {
					pref[i][j % N] = pref[i][(j - 1) % N] + ((long) (j - i)) * barns[j % N];
				}
			}
		}
		long ans = Long.MAX_VALUE;
		for(start = 0; start < N; ++start) {
			dp = new long[K + 1][N + 1];
			for(int i = 0; i <= K; ++i) {
				Arrays.fill(dp[i], Long.MAX_VALUE / 3);
			}
			dp[0][N] = 0;
			for(int i = 1; i <= K; ++i) {
				solve(i, 0, N - 1, 1, N + 1);
			}
			ans = Math.min(ans, dp[K][0]);
			rotate(barns);
		}
		out.println(ans);
		in.close();
		out.close();
	}
	
	static long dist(int i, int j) {
		return pref[i][j];
	}
	
	static void rotate(int[] arr) {
		int temp = arr[0];
		for(int i = 0; i < N - 1; ++i) {
			arr[i] = arr[i + 1];
		}
		arr[N - 1] = temp;
	}
	
	static void solve(int k, int l, int r, int ja, int jb) {
		if(l == r) return;
		int i = (l + r) / 2;
		int newjb = -1;
		dp[k][i] = Long.MAX_VALUE / 3;
		for(int j = Math.max(i + 1, ja); j < jb; ++j) {
			long val = dist((start + i) % N, (start + j - 1) % N) + dp[k - 1][j];
			if(val < dp[k][i]) {
				dp[k][i] = val;
				newjb = j;
			}
		}
		solve(k, l, i, ja, newjb + 1);
		solve(k, i + 1, r, newjb, jb);
	}
	
}