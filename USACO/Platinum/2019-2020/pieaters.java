import java.util.*;
import java.io.*;

public class pieaters {
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

	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("pieaters.in");
		PrintWriter out = new PrintWriter(new File("pieaters.out"));
		int N = in.nextInt();
		int M = in.nextInt();
		cow[] cows = new cow[M];
		int[][][] mx = new int[N][N][N];
		for(int i = 0; i < M; ++i) {
			int wi = in.nextInt();
			int li = in.nextInt() - 1;
			int ri = in.nextInt() - 1;
			cows[i] = new cow(wi, li, ri);
			for(int j = li; j <= ri; ++j) {
				mx[j][li][ri] = Math.max(mx[j][li][ri], wi);
			}
		}
		for(int i = 0; i < N; ++i) {
			 for(int j = i; j >= 0; --j) {
				 for(int k = i; k < N; ++k) {
					 if(j > 0) mx[i][j - 1][k] = Math.max(mx[i][j - 1][k], mx[i][j][k]);
					 if(k < N - 1) mx[i][j][k + 1] = Math.max(mx[i][j][k + 1], mx[i][j][k]);
				 }
			 }
		}
		int[][] dp = new int[N][N];
		for(int i = N - 1; i >= 0; --i) {
			for(int j = i; j < N; ++j) {
				for(int k = i; k < j; ++k) {
					dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k + 1][j]);
				}
				for(int k = i; k <= j; ++k) {
					int res = mx[k][i][j];
					if(k > i) res += dp[i][k - 1];
					if(k < j) res += dp[k + 1][j];
					dp[i][j] = Math.max(dp[i][j], res);
				}
			}
		}
		out.println(dp[0][N - 1]);
		in.close();
		out.close();
	}
	static class cow{
		int weight, l, r;
		cow(int w, int a, int b){
			weight = w; l = a; r = b;
		}
	}
}