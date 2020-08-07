import java.util.*;
import java.io.*;

public class fencedin {
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
		Reader in = new Reader("fencedin.in");
		PrintWriter out = new PrintWriter(new File("fencedin.out"));
		int A = in.nextInt(); int B = in.nextInt();
		int n = in.nextInt(); int m = in.nextInt();
		int[] vertFences = new int[n + 2]; 
		int[] horFences = new int[m + 2];
		vertFences[0] = 0; vertFences[n + 1] = A;
		horFences[0] = 0; horFences[m + 1] = B;
		for(int i = 1; i != n + 1; ++i) {
			int ai = in.nextInt();
			vertFences[i] = ai;
		}
		for(int i = 1; i != m + 1; ++i) {
			int bi = in.nextInt();
			horFences[i] = bi;
		}
		Arrays.sort(horFences);
		Arrays.sort(vertFences);
		int[] vertSizes = new int[m + 1];
		int[] horSizes = new int[n + 1];
		for(int i = 0; i != n + 1; ++i) {
			horSizes[i] = vertFences[i + 1] - vertFences[i];
		}
		for(int i = 0; i != m + 1; ++i) {
			vertSizes[i] = horFences[i + 1] - horFences[i];
		}
		Arrays.sort(horSizes);
		Arrays.sort(vertSizes);
		long cost = ((long) horSizes[0]) * ((long) m) + ((long) vertSizes[0]) * ((long) n);
		int horMult = n;
		int vertMult = m;
		int vertInd = 1;
		int horInd = 1;
		while(vertMult >= 1  && horMult >= 1) {
			if(vertSizes[vertInd] >= horSizes[horInd]) {
				cost += vertMult * horSizes[horInd++];
				--horMult;
			}
			else {
				cost += horMult * vertSizes[vertInd++];
				--vertMult;
			}
		}
		out.println(cost);
		in.close();
		out.close();
	}
}