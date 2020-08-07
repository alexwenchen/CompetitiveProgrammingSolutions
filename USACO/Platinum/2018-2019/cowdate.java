import java.util.*;
import java.io.*;

public class cowdate {
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
		Reader in = new Reader("cowdate.in");
		PrintWriter out = new PrintWriter(new File("cowdate.out"));
		int N = in.nextInt();
		int[] p = new int[N];
		for(int i = 0; i < N; ++i) {
			p[i] = in.nextInt();
		}
		//cumul = (1 - cur) * cumul + cur * none
		//none *= 1 - cur
		//to remove
		//none /= 1 - rem;
		//cumul = (cumul - none * rem) / (1 - rem)
		double mx = 0.0;
		double none = 1.0;
		double cumul = 0.0;
		int p1 = 0, p2 = -1;
		while(p2 + 1 < N) {
			++p2;
			double cur = 1.0 * p[p2] / 1000000;
			cumul = (1 - cur) * cumul + cur * none;
			none *= (1 - cur);
			mx = Math.max(mx, cumul);
			boolean hurts = true;
			while(p1 <= p2 && hurts) {
				double rem = 1.0 * p[p1] / 1000000;
				double nwnone = none / (1 - rem);
				double nw = (cumul - nwnone * rem) / (1 - rem);
				if(nw > cumul) {
					++p1;
					cumul = nw;
					none = nwnone;
					mx = Math.max(mx, cumul);
				}
				else {
					hurts = false;
				}
			}
		}
		out.println((int)(mx * 1000000));
		in.close();
		out.close();
	}
}