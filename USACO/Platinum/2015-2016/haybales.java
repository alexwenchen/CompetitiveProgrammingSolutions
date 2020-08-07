import java.util.*;
import java.io.*;

public class haybales {
	static int N;
	final static int MOD = 1000000007;
	final static int intMax = Integer.MAX_VALUE;
	final static int intMin = Integer.MIN_VALUE;
	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("haybales.in"));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		PrintWriter out = new PrintWriter(new File("haybales.out"));
		N = Integer.parseInt(st.nextToken());
		int Q = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());
		int[] bales = new int[N];
		for (int i = 0; i < N; i++) {
			bales[i] = Integer.parseInt(st.nextToken());
		}
		RangeTree lst = new RangeTree(N);
		for(int i = 0; i < N; ++i) {
			lst.update(i, i, bales[i]);
		}
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			char c = st.nextToken().charAt(0);
			if (c == 'M') {
				int A = Integer.parseInt(st.nextToken()) - 1;
				int B = Integer.parseInt(st.nextToken()) - 1;
				out.println(lst.minQuery(A, B));
			} else if (c == 'S') {
				int A = Integer.parseInt(st.nextToken()) - 1;
				int B = Integer.parseInt(st.nextToken()) - 1;
				out.println(lst.sumQuery(A, B));
			} else {
				int A = Integer.parseInt(st.nextToken()) - 1;
				int B = Integer.parseInt(st.nextToken()) - 1;
				int C = Integer.parseInt(st.nextToken());
				lst.update(A, B, C);
			}
		}
		br.close();
		out.close();
	}
	static class RangeTree {
		private long[] min;
		private long[] lazy;
		private long[] sum;
		private int size;
		public RangeTree(int size) {
			this.size = size;
			min = new long[4*size];
			sum = new long[4*size];
			lazy = new long[4*size];
		}
		public void update(int l, int r, int inc) {
			update(1, 0, size-1, l, r, inc);
		}
		private void pushDown(int index, int l, int r) {
			min[index] += lazy[index];
			sum[index] += lazy[index] * (r-l+1);
			if(l != r) {
				lazy[2*index] += lazy[index];
				lazy[2*index+1] += lazy[index];
			}
			lazy[index] = 0;
		}
		private void pullUp(int index, int l, int r) {
			int m = (l+r)/2;
			min[index] = Math.min(evaluateMin(2*index, l, m), evaluateMin(2*index+1, m+1, r));
			sum[index] = evaluateSum(2*index, l, m) + evaluateSum(2*index+1, m+1, r);
		}
		private long evaluateSum(int index, int l, int r) {
			return sum[index] + (r-l+1)*lazy[index];
		}
		private long evaluateMin(int index, int l, int r) {
			return min[index] + lazy[index];
		}
		private void update(int index, int l, int r, int left, int right, int inc) {
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
		public long minQuery(int l, int r) {
			return minQuery(1, 0, size-1, l, r);
		}
		private long minQuery(int index, int l, int r, int left, int right) {
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
		public long sumQuery(int l, int r) {
			return sumQuery(1, 0, size-1, l, r);
		}
		private long sumQuery(int index, int l, int r, int left, int right) {
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
	}
}
