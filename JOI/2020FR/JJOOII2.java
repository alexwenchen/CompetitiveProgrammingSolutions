import java.util.*;
import java.io.*;

public class JJOOII2 {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] dx = { 0, 0, -1, 1 };
	final static int[] dy = { -1, 1, 0, 0 };

	static int T;

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
	
	static int N, K;
	static String s;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		s = br.readLine();
		ArrayList<Integer> Js = new ArrayList<Integer>();
		ArrayList<Integer> Os = new ArrayList<Integer>();
		ArrayList<Integer> Is = new ArrayList<Integer>();
		for(int i = 0; i < N; ++i) {
			if(s.charAt(i) == 'J') {
				Js.add(i);
			}
			else if(s.charAt(i) == 'O') {
				Os.add(i);
			}
			else {
				Is.add(i);
			}
		}
		int ans = 1000000000;
		for(int i = K - 1; i < Js.size(); ++i) {
			int curpos = Js.get(i);
			int l = 0, r = Os.size();
			while(l < r) {
				int mid = l + (r - l) / 2;
				if(mid == Os.size()) break;
				if(Os.get(mid) > curpos) {
					r = mid;
				}
				else {
					l = mid + 1;
				}
			}
			if(l + K - 1 >= Os.size()) break;
			int opos = Os.get(l + K - 1);
			l = 0; r = Is.size();
			while(l < r) {
				int mid = l + (r - l) / 2;
				if(mid == Is.size()) break;
				if(Is.get(mid) > opos) {
					r = mid;
				}
				else {
					l = mid + 1;
				}
			}
			if(l + K - 1 >= Is.size()) break;
			ans = Math.min(ans, Is.get(l + K - 1) - Js.get(i - K + 1) + 1);
		}
		if(ans == 1000000000) System.out.println(-1);
		else System.out.println(ans - K * 3);
//		in.close();
	}
}