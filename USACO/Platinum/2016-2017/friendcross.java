import java.util.*;
import java.io.*;

public class friendcross {
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
		Reader in = new Reader();
//		Reader in = new Reader("friendcross.in");
		PrintWriter out = new PrintWriter(new File("friendcross.out"));
		N = in.nextInt();
		int K = in.nextInt();
		int[] a = new int[N];
		int[] b = new int[N];
		for (int i = 0; i < N; ++i) {
			a[i] = in.nextInt();
		}
		for (int i = 0; i < N; ++i) {
			b[i] = in.nextInt();
		}
		int[] loc = new int[N + 1];
		for (int i = 0; i < N; ++i) {
			loc[a[i]] = i;
		}
		seq = new int[N + 1];
		SegmentTree mst = new SegmentTree(1, N);
		long ans = 0;
		for (int i = 0; i < N; ++i) {
			int location = loc[b[i]];
			if (b[i] - K - 1 >= 1) {
				ans += mst.query(1, b[i] - K - 1, location);
			}
			if(b[i] + K + 1 <= N) {
				ans += mst.query(b[i] + K + 1, N, location);
			}
			mst.update(b[i], location);
		}
		System.out.println(ans);
		in.close();
		out.close();
	}

	static int[] seq; // seq = new int[N + 1];

	static class SegmentTree {
		public int[] arr;
		public int[] pl;
		public int[] pr;
		public BIT bit;
		public int start;
		public int end;
		public SegmentTree lchild;
		public SegmentTree rchild;

		public SegmentTree(int start, int end) {
			this.start = start;
			this.end = end;
			arr = new int[end - start + 2];
			if (start == end) {
				arr[1] = seq[start];
			} else {
				int mid = (start + end) >> 1;
				lchild = new SegmentTree(start, mid);
				rchild = new SegmentTree(mid + 1, end);
				pl = new int[lchild.arr.length];
				pr = new int[rchild.arr.length];
				int lidx = 1, ridx = 1;

				int idx = 1;
				int[] larr = lchild.arr, rarr = rchild.arr;
				while (lidx < larr.length && ridx < rarr.length) {
					if (larr[lidx] < rarr[ridx]) {
						pl[lidx] = idx;
						arr[idx++] = larr[lidx++];
					} else {
						pr[ridx] = idx;
						arr[idx++] = rarr[ridx++];
					}
				}
				while (lidx < larr.length) {
					pl[lidx] = idx;
					arr[idx++] = larr[lidx++];
				}
				while (ridx < rarr.length) {
					pr[ridx] = idx;
					arr[idx++] = rarr[ridx++];
				}
			}
			bit = new BIT(end - start + 2);
		}

		public int query(int s, int e, int k) {
			if (start == s && end == e) {
				if (k < arr[1])
					return bit.count;
				int lo = 1, hi = arr.length - 1;
				while (lo < hi) {
					int mid = (lo + hi + 1) / 2;
					if (arr[mid] > k)
						hi = mid - 1;
					else
						lo = mid;
				}
				return bit.count - bit.query(lo);
			}
			int mid = (start + end) >> 1;
			if (mid >= e)
				return lchild.query(s, e, k);
			else if (mid < s)
				return rchild.query(s, e, k);
			else
				return lchild.query(s, mid, k) + rchild.query(mid + 1, e, k);
		}

		public int update(int p, int val) {
			if (start == p && end == p) {
				bit.update(1, +1);
				return 1;
			}
			int mid = (start + end) >> 1;
			int apos = -1;
			if (mid >= p)
				apos = pl[lchild.update(p, val)];
			else
				apos = pr[rchild.update(p, val)];
			bit.update(apos, +1);
			return apos;
		}
	}

	static class BIT {
		private int[] tree;
		private int N;
		public int count;

		public BIT(int N) {
			this.N = N;
			this.tree = new int[N + 1];
			this.count = 0;
		}

		public int query(int K) {
			int sum = 0;
			for (int i = K; i > 0; i -= (i & -i))
				sum += tree[i];
			return sum;
		}

		public void update(int K, int val) {
			this.count += val;
			for (int i = K; i <= N; i += (i & -i))
				tree[i] += val;
		}
	}
}