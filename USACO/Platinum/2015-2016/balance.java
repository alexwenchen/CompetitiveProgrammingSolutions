import java.util.*;
import java.io.*;

public class balance {
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
		Reader in = new Reader("balance.in");
		PrintWriter out = new PrintWriter(new File("balance.out"));
		int N = in.nextInt();
		int[] f = new int[N + 2];
		ArrayList<point> points = new ArrayList<point>();
		f[0] = f[N + 1] = 0;
		points.add(new point(0, 0));
		points.add(new point(N + 1, 0));
		for(int i = 1; i <= N; ++i) {
			f[i] = in.nextInt();
			points.add(new point(i, f[i]));
		}
		ArrayList<point> upperHull = convexHull(points);
		Collections.sort(upperHull, (a, b) -> a.x - b.x);
		int last = 0;
		int lastInd = 0;
		int ind = 1;
		for(int i = 1; i <= N; ++i) {
			if(upperHull.get(ind).x == i) {
				out.println(upperHull.get(ind).y * 100000L);
				last = upperHull.get(ind).y;
				++ind;
				lastInd = i;
			}
			else {
				int nextInd = upperHull.get(ind).x;
				int next = upperHull.get(ind).y;
				double slope =  1.0 * (next - last) / (nextInd - lastInd);
				out.println(((long)(10000000L * (last + (slope * (i - lastInd)))))/ 100);
			}
		}
		
		in.close();
		out.close();
	}
	
	static class point{
		int x, y;
		point(int a, int b){
			x = a; y = b;
		}
		public String toString() {
			return x + " " + y;
		}
	}
	static int cross(point v1, point v2) {
	    return v1.x * v2.y - v1.y * v2.x;
	}

	static boolean cw(point a, point b, point c) {
	    point v1 = new point(b.x - a.x, b.y - a.y);
	    point v2 = new point(c.x - b.x, c.y - b.y);
	    return cross(v1, v2) < 0;
	}

	static boolean ccw(point a, point b, point c) {
	    point v1 = new point(b.x - a.x, b.y - a.y);
	    point v2 = new point(c.x - b.x, c.y - b.y);
	    return cross(v1, v2) > 0;
	}
	
	static ArrayList<point> convexHull(ArrayList<point> pts) {
	    TreeSet<point> pts2 = new TreeSet<point>((a, b) -> a.x - b.x);
	    int n = pts.size();
	    Collections.sort(pts, (a, b) -> a.x - b.x);
	    for (int i = 0; i < n; i++) {
	        pts2.add(pts.get(i));
	    }
	    ArrayList<point> ret = new ArrayList<point>();
	    if (n <= 2) {
	        for (point p : pts) {
	        	ret.add(p);
	        }
	        return ret;
	    }
	    ArrayList<point> upper = new ArrayList<point>();
	    upper.add(pts.get(0));
	    for (int i = 1; i < n; i++) {
	        int sz = upper.size();
	        while (sz > 1 && !cw(upper.get(sz - 2), upper.get(sz - 1), pts.get(i))) {
	            upper.remove(sz - 1);
	            sz = upper.size();
	        }

	        upper.add(pts.get(i));
	    }
	    return upper;
	}
	
}