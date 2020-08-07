import java.util.*;
import java.io.*;

public class art {
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
		Reader in = new Reader("art.in");
		PrintWriter out = new PrintWriter(new File("art.out"));
		N = in.nextInt();
		int[][] grid = new int[N][N];
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < N; ++j) {
				grid[i][j] = in.nextInt();
			}
		}
		int[][] corners = new int[N * N + 1][4]; //min x, max x, min y, max y
		for(int i = 0; i <= N * N; ++i) {
			corners[i][0] = corners[i][2] = intMax;
			corners[i][1] = corners[i][3] = 0;
		}
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < N; ++j) {
				int curcolor = grid[i][j];
				if(curcolor == 0) continue;
				corners[curcolor][0] = Math.min(corners[curcolor][0], j);
				corners[curcolor][1] = Math.max(corners[curcolor][1], j);
				corners[curcolor][2] = Math.min(corners[curcolor][2], i);
				corners[curcolor][3] = Math.max(corners[curcolor][3], i);
			}
		}
		int numcolors = 0;
		int[][] pref = new int[N + 1][N + 1];
		for(int i = 1; i <= N * N; ++i) {
			if(corners[i][0] != intMax) {
				++numcolors;
				pref[corners[i][2]][corners[i][0]]++;
				pref[corners[i][3] + 1][corners[i][0]]--;
				pref[corners[i][2]][corners[i][1] + 1]--;
				pref[corners[i][3] + 1][corners[i][1] + 1]++;
			}
		}
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < N; ++j) {
				if(i > 0) {
					pref[i][j] += pref[i - 1][j];
				}
				if(j > 0) {
					pref[i][j] += pref[i][j - 1];
					if(i > 0) {
						pref[i][j] -= pref[i - 1][j - 1];
					}
				}
			}
		}
		boolean[] canbefirst = new boolean[N * N + 1];
		Arrays.fill(canbefirst, true);
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < N; ++j) {
				if(pref[i][j] >= 2) {
					canbefirst[grid[i][j]] = false;
				}
			}
		}
		int count = 0;
		for(int i = 1; i <= N * N; ++i) {
			if(canbefirst[i]) ++count;
		}
		if(numcolors == 1) --count;
		out.println(count);
		in.close();
		out.close();
	}
}