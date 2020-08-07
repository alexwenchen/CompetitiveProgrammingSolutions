import java.util.*;
import java.io.*;

public class fortmoo {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };
	
	static int[][] pref, suff;
	
	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("fortmoo.in"));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		PrintWriter out = new PrintWriter(new File("fortmoo.out"));
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int[][] grid = new int[N + 1][M + 1];
		pref = new int[N + 1][M + 1];
		suff = new int[N + 1][M + 1];
		for(int i = 0; i < N; ++i) {
			String s = br.readLine();
			for(int j = 0; j < M; ++j) {
				grid[i][j] = s.charAt(j) == 'X' ? 1 : 0;
				pref[i + 1][j + 1] = grid[i][j];
				suff[i][j] = grid[i][j];
			}
		}
		for(int i = 1; i <= N; ++i) {
			for(int j = 1; j <= M; ++j) {
				pref[i][j] += pref[i - 1][j] + pref[i][j - 1] - pref[i - 1][j - 1];
			}
		}
		for(int i = N - 1; i >= 0; --i) {
			for(int j = M - 1; j >= 0; --j) {
				suff[i][j] += suff[i + 1][j] + suff[i][j + 1] - suff[i + 1][j + 1];
			}
		}
		int mxArea = 0;
		for(int c1 = 1; c1 <= M; ++c1) {
			for(int c2 = c1; c2 <= M; ++c2) {
				int start = -1;
				for(int r = 1; r <= N; ++r) {
					if(grid[r - 1][c1 - 1] == 1 || grid[r - 1][c2 - 1] == 1) {
						start = -1;
						continue;
					}
					if(valid(r, c1, r, c2)) {
						if(start == -1) {
							start = r;
							mxArea = Math.max(mxArea, c2 - c1 + 1);
						}
						else {
							mxArea = Math.max(mxArea, (r - start + 1) * (c2 - c1 + 1));
						}
					}
				}
			}
		}
		out.println(mxArea);
		br.close();
		out.close();
	}
	static boolean valid(int r1, int c1, int r2, int c2) {
		return pref[r2][c2] - pref[r2 - 1][c2 - 1] - (pref[r1 - 1][c2] - pref[r1 - 1][c2 - 1]) - (pref[r2][c1 - 1] - pref[r2 - 1][c1 - 1]) 
				+ suff[r1 - 1][c1 - 1] - suff[r1][c1] - (suff[r2][c1 - 1] - suff[r2][c1]) - (suff[r1 - 1][c2] - suff[r1][c2]) == 0;
	}
}