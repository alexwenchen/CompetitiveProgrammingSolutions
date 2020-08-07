import java.util.*;
import java.io.*;

public class tracking2 {
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
		Reader in = new Reader("tracking2.in");
		PrintWriter out = new PrintWriter(new File("tracking2.out"));
		int N = in.nextInt();
		int K = in.nextInt();
		int[] c = new int[N - K + 1];
		for(int i = 0; i < N - K + 1; ++i) {
			c[i] = in.nextInt();
		}
		int[] know = new int[N];
		boolean[] ignore = new boolean[N];
		int[] dom = new int[N];
		for(int i = 0; i < N - K + 1; ++i) {
			if(i != 0) {
				if(c[i] > c[i - 1]) {
					know[i - 1] = c[i - 1];
				}
			}
			if(i != N - K) {
				if(c[i] > c[i + 1]) {
					know[i + K] = c[i + 1];
					ignore[i + K] = true;
				}
			}
		}
		TreeMap<Integer, Integer> active = new TreeMap<Integer, Integer>();
		for(int i = 0; i < N; ++i) {
			if(i < N - K + 1) {
				if(active.containsKey(c[i])) {
					active.put(c[i], active.get(c[i]) + 1);
				}
				else {
					active.put(c[i], 1);
				}
			}
			if(i >= K) {
				if(active.get(c[i - K]) == 1) {
					active.remove(c[i - K]);
				}
				else {
					active.put(c[i - K], active.get(c[i - K]) - 1);
				}
			}
			dom[i] = active.lastKey();
		}
		ArrayList<Long> segments = new ArrayList<Long>();
		int lastind = 0;
		for(int i = 1; i < N; ++i) {
			if(dom[i] != dom[i - 1]) {
				int end = i - 1;
				int sz = end - lastind + 1;
				if(sz <= 1) {
					lastind = i;
					continue;
				}
				long[] dp = new long[sz + 1];
				int x = intMax - dom[i - 1];
				dp[0] = 1; dp[1] = 1;
				long prefmult = x + 1;
				if(K == 1) prefmult -= x;
				long xtothek = 1;
				if(sz >= K) {
					for(int j = 1; j <= K; ++j) {
						xtothek = ((xtothek * x) % MOD + MOD) % MOD;
					}
				}
				for(int j = 2; j <= sz; ++j) {
					dp[j] = prefmult;
					prefmult = ((prefmult * x) % MOD + MOD) % MOD;
					prefmult = ((prefmult + dp[j]) % MOD + MOD) % MOD; 
					if(j >= K) {
						prefmult = ((prefmult - ((dp[j - K] * xtothek) % MOD + MOD) % MOD) + MOD) % MOD;
					}
				}
				if(know[end] == 0) {
					segments.add(prefmult);
				}
				else {
					segments.add(dp[sz]);
				}
				lastind = i;
				if(ignore[i]) {
					++i;
					++lastind;
				}
			}
		}
		int end = N - 1;
		int sz = end - lastind + 1;
		long[] dp = new long[sz + 1];
		if(sz > 0) {
			int x = intMax - dom[N - 1];
			dp[0] = 1; dp[1] = 1;
			long prefmult = x + 1;
			if(K == 1) prefmult -= x;
			long xtothek = 1;
			if(sz >= K) {
				for(int j = 1; j <= K; ++j) {
					xtothek = ((xtothek * x) % MOD + MOD) % MOD;
				}
			}
			for(int j = 2; j <= sz; ++j) {
				dp[j] = prefmult;
				prefmult = ((prefmult * x) % MOD + MOD) % MOD;
				prefmult = ((prefmult + dp[j]) % MOD + MOD) % MOD; 
				if(j >= K) {
					prefmult = ((prefmult - ((dp[j - K] * xtothek) % MOD + MOD) % MOD) + MOD) % MOD;
				}
			}
			if(know[end] > 0) {
				segments.add(dp[sz]);
			}
			else {
				segments.add(prefmult);
			}
		}
		long ans = 1;
		for(Long i : segments) {
			ans = ((ans * i) % MOD + MOD) % MOD;
		}
		out.println(ans);
		in.close();
		out.close();
	}
}