//credit: William Wang
#include <iostream>
#include <algorithm>
#include <utility>
#include <vector>
#include <stack>
#include <map>
#include <queue>
#include <set>
#include <unordered_set>
#include <unordered_map>
#include <cstring>
#include <cmath>
#include <functional>
#include <cassert>
#include <iomanip>
#include <numeric>
#include <bitset>
//#include <bit>
 
#define INF 2e9
#define LL_INF 9223372036854775806
#define ff first
#define ss second
#define mod 1000000007
#define ll long long
#define PB push_back
#define MP make_pair
#define MT make_tuple
#define EB emplace_back
#define PoB pop_back
#define LOG log2
#define FOR(i,a,b) for (int i = (a); i < (b); ++i)
#define F0R(i,a) FOR(i,0,a)
#define ROF(i,a,b) for (int i = (b)-1; i >= (a); --i)
#define R0F(i,a) ROF(i,0,a)
#define fch(t, v) for (auto t : v)
#define sz(x) int(x.size())
#define rsz resize
#define gp(x) vector<vector<x>>
#define btree vector<pii>
#define vc(x) vector<x>
#define vll vector<ll>
#define Max(a, b, c) max(max(a,b),c)
#define fMax(a, b, c, d) max(Max(a, b, c), dp)
#define Min(a, b, c) min(min(a,b),c)
#define Mid(a, b, c) max(min(a, b), min(max(a, b), c))
#define st(a) set<a>
#define gr(x) greater<x>
#define gi greater<int>
#define all(x) (x).begin(),(x).end()
#define tri(x) tuple<x,x,x>
#define pil pair<int, long>
#define ull unsigned long long
 
using namespace std;
 
void __print(int x) {cerr << x;}
void __print(long x) {cerr << x;}
void __print(long long x) {cerr << x;}
void __print(unsigned x) {cerr << x;}
void __print(unsigned long x) {cerr << x;}
void __print(unsigned long long x) {cerr << x;}
void __print(float x) {cerr << x;}
void __print(double x) {cerr << x;}
void __print(long double x) {cerr << x;}
void __print(char x) {cerr << '\'' << x << '\'';}
void __print(const char *x) {cerr << '\"' << x << '\"';}
void __print(const string &x) {cerr << '\"' << x << '\"';}
void __print(bool x) {cerr << (x ? "true" : "false");}
 
template<typename T, typename V>
void __print(const pair<T, V> &x) {cerr << '{'; __print(x.first); cerr << ", "; __print(x.second); cerr << '}';}
template<typename T>
void __print(const T &x) {int f = 0; cerr << '{'; for (auto &i: x) cerr << (f++ ? ", " : ""), __print(i); cerr << "}";}
void _print() {cerr << "]\n";}
template <typename T, typename... V>
void _print(T t, V... v) {__print(t); if (sizeof...(v)) cerr << ", "; _print(v...);}
void println() {cerr << ">--------------------<" << endl;}
void printm(vector<vector<int>>& mat) {
    cerr << "matrix: " << endl;
    for (int i = 0; i<(int)mat.size(); i++) {for (int j = 0; j<(int)mat[0].size(); j++) {cerr << mat[i][j] << " ";} cerr << endl;}
}
 
#ifndef ONLINE_JUDGE
#define debug(x...) cerr << "[" << #x << "] = ["; _print(x)
#else
#define debug(x...)
#endif
 
typedef pair<int, int> pii;
typedef pair<ll, ll> pll;
typedef vector<int> vi;
 
// templates
template <class T> bool ckmin(T &a, const T &b) {return b<a ? a = b, 1 : 0;}
template <class T> bool ckmax(T &a, const T &b) {return b>a ? a = b, 1 : 0;}
 
void setIO(const string& str) {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    if (str.empty()) return;
    freopen((str + ".in").c_str(), "r", stdin);
    freopen((str + ".out").c_str(), "w", stdout);
}
 
int xDir[4] = { 0, 1, 0, -1};
int yDir[4] = { 1, 0, -1, 0};
 
int N, D;
int vst[1005][1005], dp[1005][1005];
char grid[1005][1005];
 
void bfs1() {
    memset(dp, 0, sizeof dp);
    queue<pii> q;
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            if (grid[i][j] == '#') {
                q.push({i, j});
            }
        }
    }
    while (!q.empty()) {
        int x = q.front().ff;
        int y = q.front().ss;
        q.pop();
        for (int i = 0; i < 4; ++i) {
            int nx = x+xDir[i], ny = y+yDir[i];
            if (nx >= 0 && nx < N && ny >= 0 && ny < N && grid[nx][ny] != '#' && !dp[nx][ny]) {
                dp[nx][ny] = dp[x][y] + 1;
                q.push({nx, ny});
            }
        }
    }
}
 
vc(vc(pii)) centers;
 
void bfs2() {
    queue<pair<pii, pii>> q;
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            if (grid[i][j] == 'S') {
                vst[i][j] = 1;
                q.push({{i, j}, {0, 0}});
            }
        }
    }
    while (!q.empty()) {
        int x = q.front().ff.ff;
        int y = q.front().ff.ss;
        int l = q.front().ss.ff;
        int r = q.front().ss.ss;
        q.pop();
        if ((l % D) == 0 && l > 0) r++;
        if (l / D > dp[x][y] - 1) {
            continue;
        }
        for (int i = 0; i < 4; ++i) {
            int nx = x + xDir[i], ny = y + yDir[i]; // new center
            if (nx >= 0 && nx < N && ny >= 0 && ny < N && grid[nx][ny] != '#' && !vst[nx][ny]) {
                vst[nx][ny] = 1;
                q.push({{nx,    ny},{l + 1, r}});
            }
        }
    }
}
 
queue<pair<pii, pii>> q;
//vc(vi) visited;
 
void bfs3(int len) {
    while (!q.empty()) {
        int x = q.front().ff.ff;
        int y = q.front().ff.ss;
        int x0 = q.front().ss.ff;
        int y0 = q.front().ss.ss;
        q.pop();
        for (int i = 0; i < 4; ++i) {
            int nx = x+xDir[i], ny = y+yDir[i];
            if (nx >= 0 && nx < N && ny >= 0 && ny < N && grid[nx][ny] != '#' && vst[x][y] > 1 && (!vst[nx][ny] || vst[nx][ny] < vst[x][y] - 1)) {    
                vst[nx][ny] = max(vst[nx][ny], vst[x][y] - 1);
                q.push({{nx, ny}, {x0, y0}});
            }
        }
    }
}
 
int main() { // time yourself !!!
    setIO("");
    cin >> N >> D;
    centers.rsz(N+1);
    for (int i = 0; i < N; ++i) {
        string s;
        cin >> s;
        for (int j = 0; j < N; ++j) {
            grid[i][j] = s[j];
        }
    }
    bfs1();
    bfs2();
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            if (vst[i][j] && dp[i][j] > 0) {
                centers[dp[i][j]-1].PB({i, j});
            }
            // cout << dp[i][j] << " ";
        }
        // cout << endl;
    }
    // cout << "\n\n\n";
    for (int i = N - 2; i >= 0; --i) {
        //visited.clear(), visited.rsz(N, vi(N, 0));
        for (int j = 0; j < sz(centers[i]); ++j) {
            //visited[centers[i][j].ff][centers[i][j].ss] = 1;
            q.push({centers[i][j], centers[i][j]});
            vst[centers[i][j].ff][centers[i][j].ss] = i + 1;
        }
        bfs3(i);
    }
    int ans = 0;
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            if (vst[i][j]) {
                ans++;
            }
            // cout << vst[i][j];
        }
        // cout << endl;
    }
    cout << ans;
    return 0;
}
 
// check long longs, binary search on ans?
// Do something, start simpler
// IBM motto: THINK
