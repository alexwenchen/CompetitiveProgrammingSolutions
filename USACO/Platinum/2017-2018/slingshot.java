import java.util.*;
import java.io.*;

public class slingshot {
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
		// 500 lines
//		Reader in = new Reader();
		Reader in = new Reader("slingshot.in");
		PrintWriter out = new PrintWriter(new File("slingshot.out"));
		int N = in.nextInt();
		int M = in.nextInt();
		ss[] slingshots = new ss[N];
		mn[] manure = new mn[M];
		ArrayList<event> forw = new ArrayList<event>();
		ArrayList<event> back = new ArrayList<event>();
		for(int i = 0; i < N; ++i) {
			int xi = in.nextInt();
			int yi = in.nextInt();
			int ti = in.nextInt();
			slingshots[i] = new ss(xi, yi, ti, i);
			if(xi < yi) {
				forw.add(new event(0, xi, i, true));
				forw.add(new event(0, yi, i, false));
			}
			else {
				back.add(new event(0, xi, i, true));
				back.add(new event(0, yi, i, false));
			}
		}
		for(int i  = 0; i < M; ++i) {
			int ai = in.nextInt();
			int bi = in.nextInt();
			manure[i] = new mn(ai, bi, i);
			if(ai < bi) {
				forw.add(new event(1, ai, i, true));
				forw.add(new event(1, bi, i, false));
			}
			else {
				back.add(new event(1, ai, i, true));
				back.add(new event(1, bi, i, false));
			}
		}
		long[] ans = new long[M];
		Arrays.fill(ans, intMax);
		Collections.sort(forw, (a, b) -> a.coord - b.coord);
		Collections.sort(back, (a, b) -> a.coord - b.coord);
		int cpcd = 0;
		int fsz = forw.size();
		for(event i : forw) {
			if(i.tp == 0) {
				if(i.st) slingshots[i.ind].cpcdx = cpcd;
				else slingshots[i.ind].cpcdy = cpcd;
			}
			else {
				if(i.st) manure[i.ind].cpcdx = cpcd;
				else manure[i.ind].cpcdy = cpcd;
			}
			++cpcd;
		}
		cpcd = 0;
		int bsz = back.size();
		for(event i : back) {
			if(i.tp == 0) {
				if(i.st) slingshots[i.ind].cpcdx = cpcd;
				else slingshots[i.ind].cpcdy = cpcd;
			}
			else {
				if(i.st) manure[i.ind].cpcdx = cpcd;
				else manure[i.ind].cpcdy = cpcd;
			}
			++cpcd;
		}
		
		/*case 1 : c m m c: when hit cb, add at ce. when hit mb, query me to end
		 *case 2 : m c c m: when hit ce, add at cb. when hit me, query mb to me 
		 *case 3 : c m c m: when hit cb, add at ce. when hit mb, query mb to me
		 *case 4 : m c m c: when hit cb, add at ce. when hit me, query me to end 
		 */	
		//case 1
		if(fsz > 0) {
			MinSegTree mnst = new MinSegTree(fsz);
			for(event i : forw) {
				if(i.tp == 0) {
					if(i.st) { //cb
						ss cur = slingshots[i.ind];
						mnst.add(cur.cpcdy, (long) cur.y - cur.x + cur.t - Long.MAX_VALUE / 2);
					}
				}
				else {
					if(i.st) { //mb
						mn cur = manure[i.ind];
						ans[cur.ind] = Math.min(ans[cur.ind], (mnst.getMin(cur.cpcdy, fsz - 1) - (cur.b - cur.a)));
					}
				}
			}
			//case 2
			MaxSegTree mxst = new MaxSegTree(fsz);
			for(event i : forw) {
				if(i.tp == 0) {
					if(!i.st) { //ce
						ss cur = slingshots[i.ind];
						mxst.add(cur.cpcdx, (long) cur.y - cur.x - cur.t);
					}
				}
				else {
					if(!i.st) { //me
						mn cur = manure[i.ind];
						ans[cur.ind] = Math.min(ans[cur.ind], (cur.b - cur.a) - (mxst.getMax(cur.cpcdx, cur.cpcdy)));
					}
				}
			}
			//case 3
			mxst = new MaxSegTree(fsz);
			for(event i : forw) {
				if(i.tp == 0) {
					if(i.st) { //cb
						ss cur = slingshots[i.ind];
						mxst.add(cur.cpcdy, (long) cur.y + cur.x - cur.t);
					}
				}
				else {
					if(i.st) { //mb
						mn cur = manure[i.ind];
						ans[cur.ind] = Math.min(ans[cur.ind], (cur.b + cur.a) - (mxst.getMax(cur.cpcdx, cur.cpcdy)));
					}
				}
			}
			//case 4
			Collections.sort(forw, (a, b) -> b.coord - a.coord);
			mnst = new MinSegTree(fsz);
			for(event i : forw) {
				if(i.tp == 0) {
					if(!i.st) { //ce
						ss cur = slingshots[i.ind];
						mnst.add(cur.cpcdx, (long) cur.y + cur.x + cur.t - Long.MAX_VALUE / 2);
					}
				}
				else {
					if(!i.st) { //me
						mn cur = manure[i.ind];
						ans[cur.ind] = Math.min(ans[cur.ind], (mnst.getMin(cur.cpcdx, cur.cpcdy) - (cur.b + cur.a)));
					}
				}
			}
		}
		
		//TODO: run same on backward array
		/*case 1 : c m m c: when hit cb, add at ce. when hit mb, query me to end
		 *case 2 : m c c m: when hit ce, add at cb. when hit me, query mb to me 
		 *case 3 : c m c m: when hit cb, add at ce. when hit me, query me to end 
		 *case 4 : m c m c: when hit cb, add at ce. when hit mb, query mb to me
		 */	
		Collections.sort(back, (a, b) -> b.coord - a.coord);
		//case 1
		if(bsz > 0) {
			MinSegTree mnst = new MinSegTree(bsz);
			for(event i : back) {
				if(i.tp == 0) {
					if(i.st) { //cb
						ss cur = slingshots[i.ind];
						mnst.add(cur.cpcdy, (long) cur.x - cur.y + cur.t - Long.MAX_VALUE / 2);
					}
				}
				else {
					if(i.st) { //mb
						mn cur = manure[i.ind];
						ans[cur.ind] = Math.min(ans[cur.ind], (mnst.getMin(0, cur.cpcdy) - (cur.a - cur.b)));
					}
				}
			}
			//case 2
			MaxSegTree mxst = new MaxSegTree(bsz);
			for(event i : back) {
				if(i.tp == 0) {
					if(!i.st) { //ce
						ss cur = slingshots[i.ind];
						mxst.add(cur.cpcdx, (long) cur.x - cur.y - cur.t);
					}
				}
				else {
					if(!i.st) { //me
						mn cur = manure[i.ind];
						ans[cur.ind] = Math.min(ans[cur.ind], (cur.a - cur.b) - (mxst.getMax(cur.cpcdy, cur.cpcdx)));
					}
				}
			}
			//case 3
			mnst = new MinSegTree(bsz);
			for(event i : back) {
				if(i.tp == 0) {
					if(i.st) { //cb
						ss cur = slingshots[i.ind];
						mnst.add(cur.cpcdy, (long) cur.y + cur.x + cur.t - Long.MAX_VALUE / 2);
					}
				}
				else {
					if(i.st) { //mb
						mn cur = manure[i.ind];
						ans[cur.ind] = Math.min(ans[cur.ind], (mnst.getMin(cur.cpcdy, cur.cpcdx) - (cur.b + cur.a)));
					}
				}
			}
			//case 4
			Collections.sort(back, (a, b) -> a.coord - b.coord);
			mxst = new MaxSegTree(bsz);
			for(event i : back) {
				if(i.tp == 0) {
					if(!i.st) { //ce
						ss cur = slingshots[i.ind];
						mxst.add(cur.cpcdx, (long) cur.y + cur.x - cur.t);
					}
				}
				else {
					if(!i.st) { //me
						mn cur = manure[i.ind];
						ans[cur.ind] = Math.min(ans[cur.ind], (cur.b + cur.a) - (mxst.getMax(cur.cpcdy, cur.cpcdx)));
					}
				}
			}
		}
		for(int i = 0; i < M; ++i) {
			out.println(Math.min(ans[i], Math.abs(manure[i].a - manure[i].b)));
		}
		in.close();
		out.close();
	}
	static class event{
		int tp, coord, ind; // type, coordinate, compressed coordinate, index
		boolean st;
		//0 is slingshot, 1 is manure
		event(int t, int tm, int i, boolean s){
			tp = t;
			coord = tm;
			st = s;
			ind = i;
		}
		public String toString() {
			return tp + " " + coord + " " + ind + " " + st;
		}
	}
	static class ss{
		int x, y, t, cpcdx, cpcdy, ind;
		ss(int a, int b, int c, int i){
			x = a;
			y = b;
			t = c;
			ind = i;
		}
	}
	static class mn{
		int a, b, ind, cpcdx, cpcdy;
		mn(int x, int y, int i){
			a = x;
			b = y;
			ind = i;
		}
	}
	static class MinSegTree{
		long[] MT;
		int N; 
		long[] W;
		
		MinSegTree(int n){
			N = n; W = new long[n];
			Arrays.fill(W, Long.MAX_VALUE / 2);
			MT = new long[4*N-1];
			buildMin();
		}
		void buildMin() {
			buildMin(0, 0, N-1);
		}
		void buildMin(int node, int start, int end) {
			if( start==end ) {
				MT[node] = W[start];
			}
			else {
				int mid = (start + end)/2;
				buildMin(2*node+1, start, mid); 
				buildMin(2*node+2, mid+1, end);
				MT[node] = Math.min(MT[2*node+1],MT[2*node+2]);
			}
		}
		void add(int node, int start, int end, int loc, long val) {
			if( start==end ) {
				W[loc] += val;
				MT[node] += val;
			}
			else {
				int mid = (start+end)/2;
				if( start<=loc && loc<=mid ) {
					add(2*node+1, start, mid, loc, val);
				}
				else { 
					add(2*node+2, mid+1, end, loc, val);
				}
				MT[node] = Math.min(MT[2*node+1],MT[2*node+2]);
			}
		}
		void add(int loc, long val) {
			add(0, 0, N-1, loc, val);
		}
		long getMin(int node, int start, int end, int low, int high) {
			if( high < start || end < low )
				return Long.MAX_VALUE / 2;
			if( low <= start && end <= high )
				return MT[node];
			int mid = (start+end)/2;
			long s1 = getMin(2*node+1, start, mid, low, high);
			long s2 = getMin(2*node+2, mid+1, end, low, high);
			return Math.min(s1, s2);
		}
		long getMin(int low, int high) {
			return getMin(0, 0, N-1, low, high);
		}
	}
	static class MaxSegTree{
		long[] MT;  // tree of maxes
		int N;     // length of A
		long[] W;   // array of spiciness
		
		MaxSegTree(int n){
			N = n; W = new long[n];
			MT = new long[4*N-1];
			buildMax();
		}
		// build the whole segment tree [ O(n) ]
		void buildMax() {
			buildMax(0, 0, N-1);
		}
		//builds spiciness segtree where parent is max of children
		void buildMax(int node, int start, int end) {
			if( start==end ) {
				MT[node] = W[start];
			}
			else {
				int mid = (start + end)/2;
			
				buildMax(2*node+1, start, mid);  // build left child
				buildMax(2*node+2, mid+1, end);  // build right child
				
				MT[node] = Math.max(MT[2*node+1],MT[2*node+2]);
			}
		}
		// add "val" to A[loc]
		void add(int node, int start, int end, int loc, long val) {
			if( start==end ) {  // leaf node
				W[loc] += val;
				MT[node] += val;
			}
			else {
				int mid = (start+end)/2;
				if( start<=loc && loc<=mid ) { // loc is in the left child
					add(2*node+1, start, mid, loc, val);
				}
				else { // loc is in the right child
					add(2*node+2, mid+1, end, loc, val);
				}
				MT[node] = Math.max(MT[2*node+1],MT[2*node+2]);
			}
		}
		
		// add "val" to A[loc]
		void add(int loc, long val) {
			add(0, 0, N-1, loc, val);
		}
		
		//find the max of W[low] to W[high]
		long getMax(int node, int start, int end, int low, int high) {
			// range represented by node is all outside of [low, high]
			if( high < start || end < low )
				return 0;
		
			// range represented by node is all inside [low, high]
			if( low <= start && end <= high )
				return MT[node];
		
			// there is overlap between [start, end] and [low, high]
			int mid = (start+end)/2;
			long s1 = getMax(2*node+1, start, mid, low, high);
			long s2 = getMax(2*node+2, mid+1, end, low, high);
			return Math.max(s1, s2);
		}
		
		//find the max of W[low][1] to W[high][1]
		long getMax(int low, int high) {
			return getMax(0, 0, N-1, low, high);
		}
	}
}