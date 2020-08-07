import java.util.*;
import java.io.*;

public class nocross {
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
// 		Reader in = new Reader();
		Reader in = new Reader("nocross.in");
		PrintWriter out = new PrintWriter(new File("nocross.out"));
		N = in.nextInt();
		int[] a = new int[N];
		int[] loc = new int[N];
		for (int i = 0; i < N; ++i) {
			a[i] = in.nextInt() - 1;
		}
		for (int i = 0; i < N; ++i) {
			loc[in.nextInt() - 1] = i;
		}
		W = new int[N];
		MT = new int[4 * N];
		for(int i = 0; i < N; ++i) {
			HashMap<Integer, Integer> updates = new HashMap<Integer, Integer>();
			for(int j = Math.max(0, a[i] - 4); j <= Math.min(N - 1, a[i] + 4); ++j) {
				int mx = 0;
				if(loc[j] != 0) {
					mx = getMax(0, loc[j] - 1);
				}
				++mx;
				updates.put(loc[j], mx);
			}
			for(Integer j : updates.keySet()) {
				int amt = getMax(j, j);
				add(j, -amt);
				add(j, Math.max(amt, updates.get(j)));
			}
		}
		out.println(getMax(0, N - 1));
		in.close();
		out.close();
	}

	static int[] MT;
	static int[] W;

	static void buildMax() {
		buildMax(0, 0, N - 1);
	}

	static void buildMax(int node, int start, int end) {
		if (start == end) {
			MT[node] = W[start];
		} else {
			int mid = (start + end) / 2;

			buildMax(2 * node + 1, start, mid);
			buildMax(2 * node + 2, mid + 1, end);

			MT[node] = Math.max(MT[2 * node + 1], MT[2 * node + 2]);
		}
	}

	static void add(int node, int start, int end, int loc, int val) {
		if (start == end) {
			W[loc] += val;
			MT[node] += val;
		} else {
			int mid = (start + end) / 2;
			if (start <= loc && loc <= mid) {
				add(2 * node + 1, start, mid, loc, val);
			} else {
				add(2 * node + 2, mid + 1, end, loc, val);
			}
			MT[node] = Math.max(MT[2 * node + 1], MT[2 * node + 2]);
		}
	}

	static void add(int loc, int val) {
		add(0, 0, N - 1, loc, val);
	}

	static int getMax(int node, int start, int end, int low, int high) {
		if (high < start || end < low)
			return 0;
		if (low <= start && end <= high)
			return MT[node];
		int mid = (start + end) / 2;
		int s1 = getMax(2 * node + 1, start, mid, low, high);
		int s2 = getMax(2 * node + 2, mid + 1, end, low, high);
		return Math.max(s1, s2);
	}

	static int getMax(int low, int high) {
		return getMax(0, 0, N - 1, low, high);
	}
}