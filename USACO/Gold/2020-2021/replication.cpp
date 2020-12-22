#include <bits/stdc++.h>
using namespace std;

struct comp{
	bool operator() (const array<int, 3>& a, const array<int, 3>& b) const {
		return a[2] == b[2] ? (a[0] == b[0] ? a[1] < b[1] : a[0] < b[0]) : a[2] > b[2];
	}
};

signed main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    int N, D; cin >> N >> D;
    int grid[N][N];
    for(int i = 0; i < N; ++i) {
    	string s; cin >> s;
    	for(int j = 0; j < N; ++j) {
    		if(s[j] == '.'){
    			grid[i][j] = 0;
    		} else if(s[j] == '#') {
    			grid[i][j] = 1; 
    		} else grid[i][j] = 2;
    	}
    }
    queue<array<int, 3>> q;
    int mndist[N][N];
    memset(mndist, 0, sizeof mndist);
    for(int i = 0; i < N; ++i) {
    	for(int j = 0; j < N; ++j) {
    		if(grid[i][j] == 1) {
    			q.push({i, j, -1});
    		}
    	}
    }
    bool vis[N][N];
    memset(vis, 0, sizeof vis);
    int dx[] = {0, 0, 1, -1};
	int dy[] = {1, -1, 0, 0}; 
    while(q.size()) {
    	auto cur = q.front(); q.pop();
    	int r = cur[0], c = cur[1], dst = cur[2];
    	for(int i = 0; i < 4; ++i) {
    		int nr = r + dx[i];
    		int nc = c + dy[i];
    		if(nr < 0 || nr >= N || nc < 0 || nc >= N || vis[nr][nc] || grid[nr][nc] == 1) continue;
    		vis[nr][nc] = true;
    		mndist[nr][nc] = dst + 2;
    		q.push({nr, nc, dst + 1});
    	}
    }
    bool ans[N][N];
    memset(ans, 0, sizeof ans);
    memset(vis, 0, sizeof vis);
    for(int i = 0; i < N; ++i) {
    	for(int j = 0; j < N; ++j) {
    		if(grid[i][j] == 2) {
    			q.push({i, j, -1});
    			ans[i][j] = true;
    		}
    	}
    }
    int distReached[N][N];
    memset(distReached, 0, sizeof distReached);
    while(q.size()) {
    	auto cur = q.front(); q.pop();
    	int r = cur[0], c = cur[1], dst = cur[2] + 1;
        if(dst / D == mndist[r][c]) distReached[r][c] = max(distReached[r][c], dst / D - 1);
    	if(dst / D + 1 > mndist[r][c]) continue;
        distReached[r][c] = max(distReached[r][c], dst / D);
    	for(int i = 0; i < 4; ++i) {
    		int nr = r + dx[i];
    		int nc = c + dy[i];
    		if(nr < 0 || nr >= N || nc < 0 || nc >= N || vis[nr][nc] || grid[nr][nc] == 1) continue;
    		vis[nr][nc] = true;
            ans[nr][nc] = true;
    		q.push({nr, nc, dst});
    	}
    }
    memset(vis, 0, sizeof vis);
    set<array<int, 3>, comp> pq;
    for(int i = 0; i < N; ++i) {
    	for(int j = 0; j < N; ++j) {
    		if(distReached[i][j] > 0 && grid[i][j] != 1) {
    			pq.insert({i, j, distReached[i][j]});
    			ans[i][j] = true;
                vis[i][j] = true;
    		}
    	}
    }
    while(pq.size()) {
    	auto cur = *pq.begin(); pq.erase(pq.begin());
    	int r = cur[0], c = cur[1], dst = cur[2] - 1;
    	for(int i = 0; i < 4; ++i) {
    		int nr = r + dx[i];
    		int nc = c + dy[i];
    		if(nr < 0 || nr >= N || nc < 0 || nc >= N || vis[nr][nc] || grid[nr][nc] == 1) continue;
    		vis[nr][nc] = true;
    		ans[nr][nc] = true;
    		if(dst > 0) pq.insert({nr, nc, dst});
    	}
    }
    int ret = 0;
    for(int i = 0; i < N; ++i) {
    	for(int j = 0; j < N; ++j) {
    		if(grid[i][j] != 1) ret += ans[i][j];
    	}
    }
    cout << ret << "\n";
}
