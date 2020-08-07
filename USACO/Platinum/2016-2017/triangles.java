import java.util.*;
import java.io.*;

public class triangles {
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
		Reader in = new Reader("triangles.in");
		PrintWriter out = new PrintWriter(new File("triangles.out"));
		N = in.nextInt();
		p[] pnts = new p[N];
		for (int i = 0; i < N; ++i) {
			int xi = in.nextInt();
			int yi = in.nextInt();
			pnts[i] = new p(xi, yi);
		}
		Arrays.sort(pnts, (a, b) -> a.y == b.y ? a.x - b.x : a.y - b.y);
		int[][] lines = new int[N][N];
		for(int i = 0; i < N; ++i) {
			for(int j = i + 1; j < N; ++j) {
				int cnt = 0;
				for(int check = 0; check < N; ++check) {
					if(check == i || check == j) continue;
					if(pntInTriangle(pnts[i], new p(pnts[i].x, 0), new p(pnts[j].x,0), pnts[check]) 
							|| pntInTriangle(pnts[i], pnts[j], new p(pnts[j].x, 0), pnts[check])) {
						++cnt;
					}
				}
				lines[i][j] = cnt;
				lines[j][i] = cnt;
			}
		}
		int[] v = new int[N - 2];
		for(int one = 0; one < N; ++one) {
			for(int two = one + 1; two < N; ++two) {
				for(int three = two + 1; three < N; ++three) {
					int cnt = 0;
					// type1 has 1 upper line, 2 lower lines
					boolean type1 = pntInTriangle(pnts[one], new p(pnts[one].x, 0), new p(pnts[three].x, 0), pnts[two])
							|| pntInTriangle(pnts[one], pnts[three], new p(pnts[three].x, 0), pnts[two])
							|| pntInTriangle(pnts[one], new p(pnts[one].x, 0), new p(pnts[two].x, 0), pnts[three])
							|| pntInTriangle(pnts[one], pnts[two], new p(pnts[two].x, 0), pnts[three])
							|| pntInTriangle(pnts[two], new p(pnts[two].x, 0), new p(pnts[three].x, 0), pnts[one])
							|| pntInTriangle(pnts[two], pnts[three], new p(pnts[three].x, 0), pnts[one]);
					if(type1) {
						if(pnts[three].x > Math.max(pnts[one].x, pnts[two].x)) {
							if(pnts[one].x > pnts[two].x) {
								cnt += lines[two][three];
								cnt -= lines[one][three];
								cnt -= lines[one][two];
							}
							else {
								cnt += lines[one][three];
								cnt -= lines[one][two];
								cnt -= lines[two][three];
							}
						}
						else{
							if(pnts[one].x > pnts[two].x) {
								cnt += lines[one][three];
								cnt -= lines[one][two];
								cnt -= lines[two][three];
							}
							else {
								cnt += lines[two][three];
								cnt -= lines[one][three];
								cnt -= lines[one][two];
							}
						}
					}
					else {
						if(pnts[three].x > Math.min(pnts[one].x, pnts[two].x) && pnts[three].x < Math.max(pnts[one].x, pnts[two].x)) {
							cnt += lines[one][three];
							cnt += lines[two][three];
							cnt -= lines[one][two];
						}
						else {
							cnt += lines[one][two];
							cnt += lines[two][three];
							cnt -= lines[one][three];
						}
					}
					if(type1) --cnt;
					if(cnt >= 0) v[cnt]++;
				}
			}
		}
		for(int i = 0; i <= N - 3; ++i) {
			out.println(v[i]);
		}
		in.close();
		out.close();
	}

	static boolean pntInTriangle(p one, p two, p three, p check) {
		double px = check.x * 1.0; double py = check.y * 1.0;
		double x1 = one.x * 1.0; double y1 = one.y * 1.0;
		double x2 = two.x * 1.0; double y2 = two.y * 1.0;
		double x3 = three.x * 1.0; double y3 = three.y * 1.0;
	    double o1 = getOrientationResult(x1, y1, x2, y2, px, py);
	    double o2 = getOrientationResult(x2, y2, x3, y3, px, py);
	    double o3 = getOrientationResult(x3, y3, x1, y1, px, py);
	    return (o1 == o2) && (o2 == o3);
	}

	static int getOrientationResult(double x1, double y1, double x2, double y2, double px, double py) {
	    double orientation = ((x2 - x1) * (py - y1)) - ((px - x1) * (y2 - y1));
	    if (orientation > 0) {
	        return 1;
	    }
	    else if (orientation < 0) {
	        return -1;
	    }
	    else {
	        return 0;
	    }
	}

	static class p {
		int x, y;

		p(int xi, int yi) {
			x = xi;
			y = yi;
		}
		
		public String toString() {
			return x + " " + y;
		}
	}
}