import java.util.*;
import java.io.*;

public class triangles2020feb {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] dx = { 0, -1, 0, 1 };
	final static int[] dy = { -1, 0, 1, 0 };

	static int add(int a, int b) {
		return (a + b) % MOD;
	}
	
	static int sub(int a, int b) {
		return (a - b + MOD) % MOD;
	}
	
	static int mult(int a, int b) {
		return (int)((((long)(a)) * b) % MOD);
	}
	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("triangles.in"));
		PrintWriter out = new PrintWriter(new File("triangles.out"));
		int N = Integer.parseInt(br.readLine());
		int[][] g = new int[N][N];
		for(int i = 0; i < N; ++i) {
			String s = br.readLine();
			for(int j = 0; j < N; ++j) {
				g[i][j] = s.charAt(j) == '*' ? 1 : 0;
			}
		}
		int prefd1[][] = new int[N][N];
		int prefd2[][] = new int[N][N];
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < N; ++j) {
				prefd1[i][j] = g[i][j] == 1 ? 1 : 0;
				if(i > 0 && j > 0) {
					prefd1[i][j] += prefd1[i - 1][j - 1];
				}
			}
		}
		for(int i = 0; i < N; ++i) {
			for(int j = N - 1; j >= 0; --j) {
				prefd2[i][j] = g[i][j] == 1 ? 1 : 0;
				if(i > 0 && j < N - 1) {
					prefd2[i][j] += prefd2[i - 1][j + 1];
				}
			}
		}
		int cnt = 0;
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < N; ++j) {
				for(int len = 1; len < N; ++len) {
					int works = 0;
					lp: for(int k = 0; k < 4; ++k) {
						for(int l = k; l < k + 2; ++l) {
							int ind = l % 4;
							int ni = i + dx[ind] * len;
							int nj = j + dy[ind] * len;
							if(ni < 0 || ni >= N || nj < 0 || nj >= N || g[ni][nj] == 0) continue lp;
						}
						++works;
						if(k == 0) {
							int fi = i;
							int fj = j + len;
							int si = i + len;
							int sj = j;
							if(fj >= N && si >= N) {
								if(fi + fj - N + 1 >= N || fi + fj - N + 1 < 0) continue;
								cnt += prefd2[N - 1][fi + fj - N + 1];
							}
							else if(fj >= N) {
								cnt += prefd2[si][sj];
							}
							else if(si >= N) {
								if(fi + fj - N + 1 >= N || fi + fj - N + 1 < 0) continue;
								cnt += prefd2[N - 1][fi + fj - N + 1] - prefd2[fi][fj];
								cnt += g[fi][fj] == 1 ? 1 : 0;
							}
							else {
								cnt += prefd2[si][sj] - prefd2[fi][fj] + (g[fi][fj] == 1 ? 1 : 0);
							}
						}
						else if(k == 1) {
							int fi = i;
							int fj = j - len;
							int si = i + len;
							int sj = j;
							if(fj < 0 && si >= N) {
								if(N - 1 + fj - fi >= N || N - 1 + fj - fi < 0) continue;
								cnt += prefd1[N - 1][N - 1 + fj - fi];
							}
							else if(fj < 0) {
								cnt += prefd1[si][sj];
							}
							else if(si >= N) {
								if(N - 1 + fj - fi >= N || N - 1 + fj - fi < 0) continue;
								cnt += prefd1[N - 1][N - 1 + fj - fi] - prefd1[fi][fj] + (g[fi][fj] == 1 ? 1 : 0);
							}
							else {
								cnt += prefd1[si][sj] - prefd1[fi][fj] + (g[fi][fj] == 1 ? 1 : 0);
							}
						}
						else if(k == 2) {
							int fi = i - len;
							int fj = j;
							int si = i;
							int sj = j - len;
							if(fi < 0 && sj < 0) {
								if(fi + fj >= N || fi + fj < 0) continue;
								cnt += prefd2[fi + fj][0];
							}
							else if(fi < 0) {
								cnt += prefd2[si][sj];
							}
							else if(sj < 0) {
								if(fi + fj >= N || fi + fj < 0) continue;
								cnt += prefd2[fi + fj][0] - prefd2[fi][fj] + (g[fi][fj] == 1 ? 1 : 0);
							}
							else {
								cnt += prefd2[si][sj] - prefd2[fi][fj] + (g[fi][fj] == 1 ? 1 : 0);
							}
						}
						else {
							int fi = i - len;
							int fj = j;
							int si = i;
							int sj = j + len;
							if(fi < 0 && sj >= N) {
								if(fi - fj + N - 1 >= N || fi - fj + N - 1 < 0) continue; 
								cnt += prefd1[fi - fj + N - 1][N - 1];
							}
							else if(fi < 0) {
								cnt += prefd1[si][sj];
							}
							else if(sj >= N) {
								if(fi - fj + N - 1 >= N || fi - fj + N - 1 < 0) continue; 
								cnt += prefd1[fi - fj + N - 1][N - 1] - prefd1[fi][fj] + (g[fi][fj] == 1 ? 1 : 0);
							}
							else {
								cnt += prefd1[si][sj] - prefd1[fi][fj] + (g[fi][fj] == 1 ? 1 : 0);
							}
						}
					}
					if(works == 2) {
						--cnt;
					}
					else if(works == 4) {
						cnt -= 4;
					}
				}
			}
		}
		out.println(cnt);
		br.close();
		out.close();
	}
}