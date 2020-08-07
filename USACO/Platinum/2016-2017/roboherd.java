import java.util.*;
import java.io.*;

public class roboherd {
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
	
	static ArrayList<ArrayList<Integer>> diffs;
	static int[] fs;//dif between cheapest & second cheapest
	static int amt, N, K;
	static long saved, cheapest;
	
	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("roboherd.in");
		PrintWriter out = new PrintWriter(new File("roboherd.out"));
		N = in.nextInt();
		K = in.nextInt();
		ArrayList<Integer>[] parts = new ArrayList[N];
		diffs = new ArrayList<ArrayList<Integer>>();
		amt = 0; cheapest = 0; saved = 0;
		long max = 0;
		for(int i = 0; i < N; ++i) {
			int mi = in.nextInt();
			parts[i] = new ArrayList<Integer>();
			for(int j = 0; j < mi; ++j) {
				parts[i].add(in.nextInt());
			}
			Collections.sort(parts[i]);
			cheapest += parts[i].get(0);
			if(mi == 1) {
				--i;
				--N;
				continue;
			}
			ArrayList<Integer> add = new ArrayList<Integer>();
			for(int j = 1; j < mi; ++j) {
				add.add(parts[i].get(j) - parts[i].get(0));
			}
			diffs.add(add);
			max += add.get(mi - 2);
		}
		fs = new int[N];
		Collections.sort(diffs, (a, b) -> a.get(0) - b.get(0));
		for(int i = 0; i < N; ++i) {
			fs[i] = diffs.get(i).get(0);
		}
		long l = 0, r = max;
		while(l < r) {
			long mid = l + (r - l) / 2;
			amt = 0;
			countBots(N - 1, mid);
			if(amt >= K) {
				r = mid;
			}
			else {
				l = mid + 1;
			}
		}
		if(l > 0) {
			countSavings(N - 1, l);
		}
		out.println((cheapest + l) * K - saved);
		in.close();
		out.close();
	}
	static void countBots(int ind, long budget) {
		if(amt >= K) return;
		if(ind != -1 && budget < diffs.get(ind).get(0)) {
			ind = Arrays.binarySearch(fs, (int) budget + 1);
			if(ind < 0) {
				ind = -ind - 2;
			}
			else {
				while(ind >= 0 && fs[ind] > budget) {
					--ind;
				}
			}
		}
		if(ind == -1) {
			++amt;
			return;
		}
		countBots(ind - 1, budget);
		for(int i = 0; i < diffs.get(ind).size() && diffs.get(ind).get(i) <= budget; ++i) {
			countBots(ind - 1, budget - diffs.get(ind).get(i));
		}
	}
	static void countSavings(int ind, long budget) {
		if(ind != -1 && budget < diffs.get(ind).get(0)) {
			ind = Arrays.binarySearch(fs, (int) budget + 1);
			if(ind < 0) {
				ind = -ind - 2;
			}
			else {
				while(ind >= 0 && fs[ind] > budget) {
					--ind;
				}
			}
		}
		if(ind == -1) {
			saved += budget;
			return;
		}
		countSavings(ind - 1, budget);
		for(int i = 0; i < diffs.get(ind).size() && diffs.get(ind).get(i) <= budget; ++i) {
			countSavings(ind - 1, budget - diffs.get(ind).get(i));
		}
	}
}