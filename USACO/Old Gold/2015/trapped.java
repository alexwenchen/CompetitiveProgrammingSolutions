import java.util.*;
import java.io.*;

public class trapped {
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
		Reader in = new Reader("trapped.in");
		PrintWriter out = new PrintWriter(new File("trapped.out"));
		N = in.nextInt();
		bale[] bales = new bale[N];
		for(int i = 0; i < N; ++i) {
			int si = in.nextInt();
			int pi = in.nextInt();
			bales[i] = new bale(si, pi);
		}
		Arrays.sort(bales, (a, b) -> a.p - b.p);
		for(int i = 0; i < N; ++i) {
			bales[i].ind = i;
		}
		Arrays.sort(bales, (a, b) -> b.s - a.s);
		LazySegmentTree lst = new LazySegmentTree(N); 
		TreeSet<bale> road = new TreeSet<bale>();
		new bale(0, 0);
		for(int i = 0; i < N; ++i) {
			bale cur = bales[i];
			if(!road.headSet(cur).isEmpty()) {
				bale nextLeft = road.headSet(cur).last();
				int dist = cur.p - nextLeft.p;
				if(dist <= cur.s && dist <= nextLeft.s) {
					lst.update(nextLeft.ind, cur.ind - 1, 1);
				}
			}
			if(!road.tailSet(cur).isEmpty()) {
				bale nextRight = road.tailSet(cur).first();
				int dist = nextRight.p - cur.p;
				if(dist <= nextRight.s && dist <= cur.s) {
					lst.update(cur.ind, nextRight.ind - 1, 1);
				}
			}
			road.add(cur);
		}
		int area = 0;
		Arrays.sort(bales, (a, b) -> a.p - b.p);
		for(int i = 0; i < N - 1; ++i) {
			if(lst.sumQuery(i, i) > 0) {
				area += bales[i + 1].p - bales[i].p;
			}
		}
		out.println(area);
		in.close();
		out.close();
	}
	static class bale implements Comparable<bale>{
		int s, p, ind;
		bale(int si, int pi){
			s = si; p = pi;
		}
		public int compareTo(bale o) {
			return p - o.p;
		}
	}
	static class LazySegmentTree {
		private long[] min;
		private long[] lazy;
		private long[] sum;
		private long[] max;
		private int size;
		public LazySegmentTree(int size) {
			this.size = size;
			min = new long[4*size];
			sum = new long[4*size];
			max = new long[4*size];
			lazy = new long[4*size];
		}
		void update(int l, int r, int inc) {
			update(1, 0, size-1, l, r, inc);
		}
		void pushDown(int index, int l, int r) {
			min[index] += lazy[index];
			max[index] += lazy[index];
			sum[index] += lazy[index] * (r-l+1);
			if(l != r) {
				lazy[2*index] += lazy[index];
				lazy[2*index+1] += lazy[index];
			}
			lazy[index] = 0;
		}
		void pullUp(int index, int l, int r) {
			int m = (l+r)/2;
			min[index] = Math.min(evaluateMin(2*index, l, m), evaluateMin(2*index+1, m+1, r));
			max[index] = Math.max(evaluateMax(2*index, l, m), evaluateMax(2*index+1, m+1, r));
			sum[index] = evaluateSum(2*index, l, m) + evaluateSum(2*index+1, m+1, r);
		}
		long evaluateSum(int index, int l, int r) {
			return sum[index] + (r-l+1)*lazy[index];
		}
		long evaluateMin(int index, int l, int r) {
			return min[index] + lazy[index];
		}
		long evaluateMax(int index, int l, int r) {
			return max[index] + lazy[index];
		}
		void update(int index, int l, int r, int left, int right, int inc) {
			if(r < left || l > right) return;
			if(l >= left && r <= right) {
				lazy[index] += inc;
				return;
			}
			pushDown(index, l, r);
			int m = (l+r)/2;
			update(2*index, l, m, left, right, inc);
			update(2*index+1, m+1, r, left, right, inc);
			pullUp(index, l, r);
		}
		long minQuery(int l, int r) {
			return minQuery(1, 0, size-1, l, r);
		}
		long minQuery(int index, int l, int r, int left, int right) {
			if(r < left || l > right) return Long.MAX_VALUE;
			if(l >= left && r <= right) {
				return evaluateMin(index, l, r);
			}
			pushDown(index, l, r);
			int m = (l+r)/2;
			long ret = Long.MAX_VALUE;
			ret = Math.min(ret, minQuery(2*index, l, m, left, right));
			ret = Math.min(ret, minQuery(2*index+1, m+1, r, left, right));
			pullUp(index, l, r);
			return ret;
		}
		long sumQuery(int l, int r) {
			return sumQuery(1, 0, size-1, l, r);
		}
		long sumQuery(int index, int l, int r, int left, int right) {
			if(r < left || l > right) return 0;
			if(l >= left && r <= right) {
				return evaluateSum(index, l, r);
			}
			pushDown(index, l, r);
			int m = (l+r)/2;
			long ret = 0;
			ret += sumQuery(2*index, l, m, left, right);
			ret += sumQuery(2*index+1, m+1, r, left, right);
			pullUp(index, l, r);
			return ret;
		}
		long maxQuery(int l, int r) {
			return maxQuery(1, 0, size-1, l, r);
		}
		long maxQuery(int index, int l, int r, int left, int right) {
			if(r < left || l > right) return Long.MIN_VALUE;
			if(l >= left && r <= right) {
				return evaluateMax(index, l, r);
			}
			pushDown(index, l, r);
			int m = (l+r)/2;
			long ret = 0;
			ret = Math.max(ret, maxQuery(2*index, l, m, left, right));
			ret = Math.max(ret, maxQuery(2*index+1, m+1, r, left, right));
			pullUp(index, l, r);
			return ret;
		}
	}
}