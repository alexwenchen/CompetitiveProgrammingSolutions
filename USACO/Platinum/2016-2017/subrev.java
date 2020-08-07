//      8/10
import java.util.*;
import java.io.*;

public class subrev {
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

	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("subrev.in");
		PrintWriter out = new PrintWriter(new File("subrev.out"));
		N = in.nextInt();
		int[] a = new int[N];
		for(int i = 0; i < N; ++i) {
			a[i] = in.nextInt();
		}
		state[][] dp = new state[N + 2][N + 2]; //last ind of first of pair + 1, last ind of second of pair
		int curlis = 0;
		int[] length = new int[N];
		for(int i = 0; i < N; ++i) {
			length[i] = 1;
			for(int j = 0; j < i; ++j) {
				if(a[j] <= a[i]) {
					length[i] = Math.max(length[i], length[j] + 1);
				}
			}
			curlis = Math.max(curlis, length[i]);
		}
		int ans = curlis;
		dp[0][N + 1] = new state(a.clone(), curlis);
		for(int i = 1; i <= N; ++i) {
			for(int j = N; j > i; --j) {
				int max = 0;
				int[] arr = new int[N];
				for(int k = i - 1; k >= 0; --k) {
					for(int l = j + 1; l <= N + 1; ++l) {
						if(dp[k][l] == null) continue;
						if(dp[k][l].lis > max) {
							max = dp[k][l].lis;
							arr = dp[k][l].arr.clone();
						}
						int[] cur = dp[k][l].arr.clone();
						int temp = cur[i - 1];
						cur[i - 1] = cur[j - 1];
						cur[j - 1] = temp;
						int[] len = new int[N];
						int mxis = 0;
						for(int m = 0; m < N; ++m) {
							len[m] = 1;
							for(int n = 0; n < m; ++n) {
								if(cur[n] <= cur[m]) {
									len[m] = Math.max(len[m], len[n] + 1);
								}
							}
							mxis = Math.max(mxis, len[m]);
						}
						if(mxis > max) {
							max = mxis;
							arr = cur.clone();
						}
					}
				}
				dp[i][j] = new state(arr, max);
			}
		}
		for(int i = 0; i <= N + 1; ++i) {
			for(int j = 0; j <= N + 1; ++j) {
				if(dp[i][j] != null) {
					ans = Math.max(ans, dp[i][j].lis);
				}
			}
		}
		out.println(ans);
		in.close();
		out.close();
	}
	static class state{
		int[] arr;
		int lis;
		state(int[] a, int l){
			arr = a; lis = l;
		}
	}
}
