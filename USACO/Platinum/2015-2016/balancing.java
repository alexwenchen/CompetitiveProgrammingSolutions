import java.util.*;
import java.io.*;

public class balancing {
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
		Reader in = new Reader("balancing.in");
		PrintWriter out = new PrintWriter(new File("balancing.out"));
		N = in.nextInt();
		cow[] cows = new cow[N];
		SegTree left = new SegTree(1000001);
		SegTree right = new SegTree(1000001);
		for(int i = 0; i != N; ++i) {
			int x = in.nextInt();
			int y = in.nextInt();
			cows[i] = new cow(x, y);
			right.add(y, 1);
		}
		Arrays.sort(cows, (a, b) -> a.x == b.x ? a.y - b.y: a.x - b.x);
		ArrayList<Integer> vFences = new ArrayList<Integer>();
		HashSet<Integer> added = new HashSet<Integer>();
		vFences.add(cows[0].x - 1);
		for(int i = 0; i != N; ++i) {
			if(!added.contains(cows[i].x + 1)) {
				vFences.add(cows[i].x + 1);
				added.add(cows[i].x + 1);
			}
		}
		int lrSweepInd = 0; int ans = intMax;
		for(Integer vertFence : vFences) {
			for(; lrSweepInd < N && cows[lrSweepInd].x < vertFence; ++lrSweepInd) {
				left.add(cows[lrSweepInd].y, 1);
				right.add(cows[lrSweepInd].y, -1);
			}
			int min = 0; int max = 1000000;
			while(min < max) {
				int horFence = (min + max) / 2;
				if(horFence % 2 == 1) ++horFence;
				int topLeft = left.getSum(horFence, 1000000);
				int topRight = right.getSum(horFence, 1000000);
				int bottomLeft = left.getSum(0, horFence);
				int bottomRight = right.getSum(0, horFence);
				ans = Math.min(ans, Math.max(Math.max(topLeft, topRight),
						Math.max(bottomLeft, bottomRight)));
				if(Math.max(topLeft, bottomLeft) > Math.max(topRight, bottomRight)) {
					if(topLeft > bottomLeft) {
						min = horFence + 1;
					}
					else{
						max = horFence - 1;
					}
				}
				else {
					if(topRight > bottomRight) {
						min = horFence + 1;
					}
					else {
						max = horFence - 1;
					}
				}
			}
		}
		out.println(ans);
		in.close();
		out.close();
	}
	static class cow{
		int x, y;
		cow(int a, int b){
			x = a; y = b;
		}
	}
	static class SegTree {
		int[] T;
		int[] A;
		int N;

		SegTree(int n) {
			N = n;
			A = new int[N];
			T = new int[4 * N];
		}

		void add(int node, int start, int end, int loc, int val) {
			if (start == end) {
				A[loc] += val;
				T[node] += val;
			} else {
				int mid = (start + end) / 2;

				if (start <= loc && loc <= mid) { 
					add(2 * node + 1, start, mid, loc, val);
				} else { 
					add(2 * node + 2, mid + 1, end, loc, val);
				}
				T[node] = T[2 * node + 1] + T[2 * node + 2];
			}
		}

		void add(int loc, int val) {
			add(0, 0, N - 1, loc, val);
		}

		int getSum(int node, int start, int end, int low, int high) {
			if (high < start || end < low)
				return 0;
			if (low <= start && end <= high)
				return T[node];
			int mid = (start + end) / 2;
			int s1 = getSum(2 * node + 1, start, mid, low, high);
			int s2 = getSum(2 * node + 2, mid + 1, end, low, high);
			return s1 + s2;
		}

		int getSum(int low, int high) {
			return getSum(0, 0, N - 1, low, high);
		}
	}
}