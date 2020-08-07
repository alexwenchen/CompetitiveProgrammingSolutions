//       7/10
import java.util.*;
import java.io.*;

public class lifeguards {
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
		Reader in = new Reader("lifeguards.in");
		PrintWriter out = new PrintWriter(new File("lifeguards.out"));
		int N = in.nextInt();
		int K = in.nextInt();
		guard[] guards = new guard[N];
		event[] events = new event[2 * N];
		for(int i = 0; i < N; ++i) {
			int s = in.nextInt();
			int e = in.nextInt();
			guards[i] = new guard(s, e);
			events[i * 2] = new event(guards[i].s, i, true);
			events[i * 2 + 1] = new event(guards[i].e, i, false);
		}
		Arrays.sort(guards, (a, b) -> a.s == b.s ? b.e - a.e : a.s - b.s);
		boolean excluded[] = new boolean[N];
		int mxEnd = 0;
		for(int i = 0; i < N; ++i) {
			if(mxEnd >= guards[i].e) {
				excluded[i] = true;
			}
			mxEnd = Math.max(mxEnd, guards[i].e);
		}
		int newN = 0;
		for(int i = 0; i < N; ++i) {
			if(!excluded[i]) ++newN;
		}
		guard[] g = new guard[newN];
		int ind = 0;
		for(int i = 0; i < N; ++i) {
			if(excluded[i]) continue;
			g[ind++] = guards[i];
		}
		HashSet<Integer> curOn = new HashSet<Integer>();
		int total = 0;
		Arrays.sort(events, (a, b) -> a.time - b.time);
		for(int i = 0; i < 2 * N; ++i) {
			event cur = events[i];
			if(cur.start) {
				curOn.add(cur.ind);
			}
			else {
				curOn.remove(cur.ind);
			}
			if(curOn.size() > 0) {
				total += events[i + 1].time - events[i].time;
			}
		}
		boolean done = N - newN >= K;
		K -= N - newN;
		N = newN;
		Arrays.sort(g, (a, b) -> a.s - b.s);
		int[][] dp = new int[N + 1][Math.max(1, K + 1)];
		/*
		 * 3 cases for dp[i][j]
		 * case 1: only take lifeguard i, works if j <= i - 1 : interval of i
		 * case 2: take lifeguard i and first lifeguard to intersect with i : interval of i + dp[first][j - (i - first)] - intersect of i and first
		 * case 3: take lifeguard i and no lifeguards that intersect with i : interval of i + dp[last no intersect][j - (i - last)]
		 */
		int first = 0;
		for(int i = 1; i <= N; ++i) {
			while(g[first].e < g[i - 1].s) {
				++first;
			}
			for(int j = Math.min(K, i - 1); j >= 0; --j) {
				dp[i][j] = g[i - 1].e - g[i - 1].s;
				if(j < K) dp[i][j] = Math.max(dp[i][j], dp[i][j + 1]);
				if(j - (i - first - 2) >= 0 && i - first - 2 >= 0) {
					dp[i][j] = Math.max(dp[i][j], g[i - 1].e - g[i - 1].s + dp[first + 1][j - (i - first - 2)] - (g[first].e - g[i - 1].s));
				}
				if(first - 1 >= 0 && i - first - 1 >= 0 && j - (i - first - 1) >= 0) {
					dp[i][j] = Math.max(dp[i][j], g[i - 1].e - g[i - 1].s + dp[first][j - (i - first - 1)]);
				}
			}
		}
		int mx = 0;
		for(int i = N; i >= 1; --i){
			mx = Math.max(mx, dp[i][Math.max(0, K - (N - i))]);
		}
		if(done) out.println(total);
		else out.println(mx);
		in.close();
		out.close();
	}
	static class guard{
		int s, e;
		guard(int start, int end){
			s = start;
			e = end;
		}
	}
	static class event{
		int time, ind;
		boolean start;
		event(int t, int i, boolean s){
			time = t;
			ind = i;
			start = s;
		}
	}
}
