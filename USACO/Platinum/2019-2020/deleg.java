import java.util.*;
import java.io.*;

public class deleg {
	static ArrayList<Integer>[] adj;

	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("deleg.in"));
		StringTokenizer st;
//    	st = new StringTokenizer(br.readLine());
		PrintWriter out = new PrintWriter(new File("deleg.out"));
		int N = Integer.parseInt(br.readLine());
		adj = new ArrayList[N];
		for (int i = 0; i < N; ++i) {
			adj[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < N - 1; ++i) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken()) - 1;
			int b = Integer.parseInt(st.nextToken()) - 1;
			adj[a].add(b);
			adj[b].add(a);
		}
		int l = 0, r = N - 1;
		while (l < r) {
			int K = l + (r - l) / 2 + 1;
			int cur = dfs(0, -1, K);
			if (cur != -1) {
				l = K;
			} else {
				r = K - 1;
			}
		}
		out.println(l);
		out.close();
		br.close();
	}
	static int dfs(int node, int par, int K) {
		if (adj[node].size() == 1 && par != -1) {
			return 1;
		}
		ArrayList<Integer> all = new ArrayList<Integer>();
		for (Integer i : adj[node]) {
			if (i == par)
				continue;
			int childpath = dfs(i, node, K);
			if (childpath == -1)
				return -1;
			all.add(childpath);
		}
		Collections.sort(all);
		boolean anyvalid = false;
		int l = -2, r = all.size() - 1;
		while (l < r) {
			int mid = l + (r - l) / 2 + 1;
			if (valid(all, mid, K)) {
				l = mid;
				anyvalid = true;
			} else {
				r = mid - 1;
			}
		}
		if(par == -1) {
			if(!valid(all, -1, K)) return -1;
		}
		if(!anyvalid) return -1;
		if(l == -1) return 1;
		else return all.get(l) + 1;
	}

	static boolean valid(ArrayList<Integer> all, int skip, int K) {
		TreeMap<Integer, Integer> lengths = new TreeMap<Integer, Integer>();
		int extras = 0; // amt of paths with length greater than K
		for (int i = 0; i < all.size(); ++i) {
			if (i == skip)
				continue;
			int cur = all.get(i);
			if (cur >= K) {
				++extras;
			} else {
				if (lengths.containsKey(cur))
					lengths.put(cur, lengths.get(cur) + 1);
				else
					lengths.put(cur, 1);
			}
		}
		while (lengths.size() > 0) {
			int shortest = lengths.firstKey();
			int longest = lengths.lastKey();
			if (shortest == longest && lengths.get(shortest) == 1) {
				if (extras == 0) {
					return false;
				} else {
					return true;
				}
			}
			if (shortest + longest >= K) {
				if (lengths.get(shortest) == 1)
					lengths.remove(shortest);
				else
					lengths.put(shortest, lengths.get(shortest) - 1);
				if (lengths.get(longest) == 1)
					lengths.remove(longest);
				else
					lengths.put(longest, lengths.get(longest) - 1);
			} else {
				if (extras == 0)
					return false;
				--extras;
				if (lengths.get(shortest) == 1)
					lengths.remove(shortest);
				else
					lengths.put(shortest, lengths.get(shortest) - 1);
			}
		}
		return true;
	}
}