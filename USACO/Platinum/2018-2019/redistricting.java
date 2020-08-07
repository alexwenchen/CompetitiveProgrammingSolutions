import java.util.*;
import java.io.*;

public class redistricting {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };
	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("redistricting.in"));
		PrintWriter out = new PrintWriter(new File("redistricting.out"));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		String s = br.readLine();
		int[] pastures = new int[N + 1];
		for(int i = 0; i < N; ++i) {
			pastures[i + 1] = s.charAt(i) == 'G' ? -1 : 1;
		}
		int[] dp = new int[N + 1];
		int add = 0;
		TreeMap<Integer, Integer> active = new TreeMap<Integer, Integer>();
		TreeMap<Integer, Integer>[] difs = new TreeMap[N + 1];
		for(int i = 0; i <= N; ++i) {
			difs[i] = new TreeMap<Integer, Integer>();
		}
		int[] added = new int[N + 1];
		active.put(0, 1);
		difs[0].put(0, 1);
		for(int i = 1; i <= N; ++i) {
			if(i > K) {
				if(active.get(dp[i - K - 1]) == 1) {
					active.remove(dp[i - K - 1]);
				}
				else{
					active.put(dp[i - K - 1], active.get(dp[i - K - 1]) - 1);
				}
				if(difs[dp[i - K - 1]].get(added[i - K - 1]) == 1) {
					difs[dp[i - K - 1]].remove(added[i - K - 1]);
				}
				else {
					difs[dp[i - K - 1]].put(added[i - K - 1], difs[dp[i - K - 1]].get(added[i - K - 1]) - 1);
				}
			}
			add += pastures[i];
			int mn = active.firstKey();
			int last = difs[mn].lastKey() + add;
			if(last <= 0) ++mn;
			dp[i] = mn;
			if(active.containsKey(dp[i])) {
				active.put(dp[i], active.get(dp[i]) + 1);
			}
			else {
				active.put(dp[i], 1);
			}
			if(difs[dp[i]].containsKey(-add)) {
				difs[dp[i]].put(-add, difs[dp[i]].get(-add) + 1);
			}
			else {
				difs[dp[i]].put(-add, 1);
			}
			added[i] = -add;
		}
		out.println(dp[N]);
		br.close();
		out.close();
	}
}