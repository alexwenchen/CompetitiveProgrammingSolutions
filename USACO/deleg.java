import java.util.*;
import java.io.*;

public class deleg {
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

	static ArrayList<Integer>[] adj;
	
	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("deleg.in");
		PrintWriter out = new PrintWriter(new File("deleg.out"));
		int N = in.nextInt();
		adj = new ArrayList[N];
		for(int i = 0; i < N; ++i) {
			adj[i] = new ArrayList<Integer>();
		}
		for(int i = 0; i < N - 1; ++i) {
			int a = in.nextInt() - 1;
			int b = in.nextInt() - 1;
			adj[a].add(b);
			adj[b].add(a);
		}
		boolean[] poss = new boolean[N];
		for(int i = 1; i < N; ++i) {
			if((N - 1) % i == 0) poss[i] = true;
		}
		for(int i = 1; i < N; ++i) {
			if(!poss[i]) continue;
			if(dfs(0, -1, i) != 1) {
				poss[i] = false;
			}
		}
		for(int i = 1; i < N; ++i) {
			if(poss[i]) out.print(1);
			else out.print(0);
		}
		in.close();
		out.close();
	}
	static int dfs(int node, int par, int K) {
		if(adj[node].size() == 1 && par != -1) {
			return 1;
		}
		TreeMap<Integer, Integer> lengths = new TreeMap<Integer, Integer>();
		for(Integer i : adj[node]) {
			if(i == par) continue;
			int subtree = dfs(i, node, K);
			if(subtree == -1) return -1;
			if(subtree == K) continue;
			if(lengths.containsKey(subtree)) lengths.put(subtree, lengths.get(subtree) + 1);
			else lengths.put(subtree, 1);
		}
		int excess = -1;
		boolean alr = false;  //already have an excess
		while(lengths.keySet().size() > 0) {
			int shortest = lengths.firstKey();
			int longest = lengths.lastKey();
			if(shortest == longest && lengths.get(shortest) == 1) {
				if(alr) {
					return -1;
				}
				else {
					excess = shortest;
					break;
				}
			}
			if(shortest + longest == K) {
				if(lengths.get(shortest) == 1) lengths.remove(shortest);
				else lengths.put(shortest, lengths.get(shortest) - 1);
				if(lengths.get(longest) == 1) lengths.remove(longest);
				else lengths.put(longest, lengths.get(longest) - 1);
			}
			else if(shortest + longest < K) {
				if(alr) {
					return -1;
				}
				else {
					excess = shortest;
					alr = true;
					if(lengths.get(shortest) == 1) lengths.remove(shortest);
					else lengths.put(shortest, lengths.get(shortest) - 1);
				}
			}
			else {
				if(alr) {
					return -1;
				}
				else {
					excess = longest;
					alr = true;
					if(lengths.get(longest) == 1) lengths.remove(longest);
					else lengths.put(longest, lengths.get(longest) - 1);
				}
			}
		}
		if(excess == -1) return 1;
		else return excess + 1;
	}
}