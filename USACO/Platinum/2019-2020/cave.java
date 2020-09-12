import java.util.*;
import java.io.*;

public class cave {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] dx = { 0, 0, -1, 1 };
	final static int[] dy = { -1, 1, 0, 0 };

	static int add(int a, int b) {
		return (a + b) % MOD;
	}
	
	static int sub(int a, int b) {
		return (a - b + MOD) % MOD;
	}
	
	static int mult(int a, int b) {
		return (int)((((long)(a)) * b) % MOD);
	}
	
	static int[][] grid;
	
	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("cave.in"));
		PrintWriter out = new PrintWriter(new File("cave.out"));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		grid = new int[N][M];
		for(int i = 0; i < N; ++i) {
			String s = br.readLine();
			for(int j = 0; j < M; ++j) {
				grid[i][j] = s.charAt(j) == '#' ? 1 : 0;
			}
		}
		//id is col + row * M
		par = new int[N * M];
		for(int i = 0; i < N * M; ++i) {
			par[i] = i;
		}
		combs = new int[N * M];
		lowerLevels = new boolean[N * M];
		for(int i = N - 2; i >= 1; --i) {
			HashSet<Integer> added1 = new HashSet<Integer>();
			HashSet<Integer> added2 = new HashSet<Integer>();
			for(int j = 1; j < M - 1; ++j) {
				if(grid[i][j] == 1) continue;
				int ind = j + i * M;
				if(grid[i][j] == 0) {
					if(grid[i][j - 1] == 0) {
						int pind = find(ind - 1);
						boolean ll = lowerLevels[pind] || lowerLevels[find(ind)];
						merge(ind - 1, ind);
						if(added1.contains(pind)) added1.add(find(ind - 1));
						if(added2.contains(pind)) added2.add(find(ind - 1));
						if(ll) lowerLevels[find(ind)] = true;
					}
					if(grid[i + 1][j] == 0) {
						if(find(ind + M) != find(ind)) {
							merge(ind + M, ind);
						}
						lowerLevels[find(ind)] = true;
					}
					
				}
			}
			for(int j = 1; j < M - 1; ++j) {
				int ind = j + i * M;
				if(!lowerLevels[find(ind)]) {
					if(!added2.contains(find(ind))) {
						update(ind, 2);
						added2.add(find(ind));
					}
				}
				else {
					if(!added1.contains(find(ind))) {
						update(ind, 1);
						added1.add(find(ind));
						
					}
				}
			}
		} 
		boolean[] vis = new boolean[N * M];
		int ans = 1;
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < M; ++j) {
				int rep = find(j + i * M);
				if(vis[rep] || combs[rep] == 0 || grid[i][j] == 1) continue;
				ans = mult(ans, combs[rep]);
				vis[rep] = true;
			}
		}
		out.println(ans);
		br.close();
		out.close();
	}
	static boolean lowerLevels[];
	static int[] par;
	static int[] combs;
	static int find(int x) {
		return par[x] == x ? x : (par[x] = find(par[x]));
	}
	static void merge(int x, int y) {
		int fx = find(x);
		int fy = find(y);
		par[fx] = fy;
		if(combs[fy] > 0) {
			combs[fy] = mult(combs[fy], combs[fx]);
		}
		else {
			combs[fy] = combs[fx];
		}
	}
	static void update(int ind, int num) {
		int par = find(ind);
		combs[par] = add(combs[par], num);
	}
}
