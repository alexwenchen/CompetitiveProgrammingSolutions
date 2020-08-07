import java.util.*;
import java.io.*;

public class sprinklers {
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
		Reader in = new Reader("sprinklers.in");
		PrintWriter out = new PrintWriter(new File("sprinklers.out"));
		int N = in.nextInt();
		spkr[] sprinklers = new spkr[N];
		for(int i = 0; i < N; ++i) {
			int a = in.nextInt();
			int b = in.nextInt();
			sprinklers[i] = new spkr(a, b);
		}
		int[] ys = new int[N];
		int[] suffmx = new int[N];
		Arrays.sort(sprinklers, (a, b) -> a.x - b.x);
		for(int i = N - 1; i >= 0; --i) {
			ys[i] = sprinklers[i].y;
			suffmx[i] = sprinklers[i].y;
			if(i + 1 < N) suffmx[i] = Math.max(suffmx[i], suffmx[i + 1]);
		}
		int low = N;
		int[] left = new int[N];
		for(int i=0;i<N;i++) {
			while(ys[i] < low) left[--low] = i;
		}
		while(low > 0) {
			left[--low] = N;
		}
		long[] sumLeft = new long[N];
		long[] stairSumLeft = new long[N];
		sumLeft[N-1] = left[N-1];
		for(int j=N-2;j>=0;j--)
			sumLeft[j] = (sumLeft[j+1] + left[j])%MOD;
		stairSumLeft[N-1] = left[N-1];
		for(int j=N-2;j>=0;j--)
			stairSumLeft[j] = (stairSumLeft[j+1] + ((long)left[j])*(N-j))%MOD;
		int prefmin = ys[0];
		long ans = 0;
		for(int i = 1; i < N; ++i) {
			int range = suffmx[i] - prefmin;
			long all = (((((long)range) * (range + 1) / 2) % MOD) * i) % MOD;
			ans += (all - (stairSumLeft[prefmin] - stairSumLeft[suffmx[i]] - ((sumLeft[prefmin] - sumLeft[suffmx[i]]) * (N - suffmx[i])) % MOD)) % MOD;
			ans = (ans + MOD) % MOD;
			prefmin = Math.min(prefmin, ys[i]);
		}
		out.println(ans);
		in.close();
		out.close();
	}
	static class spkr{
		int x, y;
		spkr(int i, int j){
			x = i; y = j;
		}
	}
	
}