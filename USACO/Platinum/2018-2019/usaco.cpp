//#pragma GCC optimize("Ofast")
//#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")
//#pragma GCC optimization ("unroll-loops")
#include <bits/stdc++.h>
using namespace std;
 
#define int ll

using ll = long long;
using ull = unsigned ll;
using pii = pair<int, int>;

#define pb push_back
#define mp make_pair
#define eb emplace_back
#define all(x) (x).begin(),(x).end()
#define x first
#define y second

 
const int MOD = 1e9 + 7;
const int dx[] = {0, 0, 1, -1};
const int dy[] = {1, -1, 0, 0}; 
const char dir[] = {'R', 'L', 'D', 'U'};
 
int add(int a, int b){
    a += b;
    if(a < 0) {
        a += MOD;
    }
    if(a >= MOD){
        a -= MOD;
    }
    return a;
}
 
int sub(int a, int b){
    a -= b;
    if(a < 0) a += MOD;
    if(a >= MOD) a -= MOD;
    return a;
}
 
int mult(int a, int b){
    return ((ll) a * b) % MOD;
}
 
void setIO2() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
}

void setIO(string s) {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

const int mxN = 1500;

int N, M, X, Y;
vector<pii> adj[mxN];
int cc;
bool vis[mxN];
vector<int> ccs[mxN];
int ccind[mxN];
unordered_map<int, pii> paths[mxN];

void dfs(int v, int p, int curcc){
    ccind[v] = curcc;
    ccs[curcc].eb(v);
    vis[v] = true;
    for(auto i : adj[v]) {
        if(i.x == p) continue;
        dfs(i.x, v, curcc);
    }
}

void dfs2(int v, int p, int curcc, int len){
    for(auto i : adj[v]) {
        if(i.x == p) continue;
        paths[curcc][min(Y, len + i.y)].x += len + i.y;
        paths[curcc][min(Y, len + i.y)].y++;
        dfs2(i.x, v, curcc, len + i.y);
    }
}

signed main(){
    setIO("mooriokart");
//    setIO2();
    cin >> N >> M >> X >> Y;
    for(int i = 0; i < M; ++i){
        int A, B, D; cin >> A >> B >> D; --A; --B;
        adj[A].eb(mp(B, D));
        adj[B].eb(mp(A, D));
    }
    for(int i = 0; i < N; ++i){
        if(!vis[i]) {
            dfs(i, -1, cc++);
        }
    }
    int p2 = 1;
    for(int i = 1; i < cc; ++i){
        p2 = mult(p2, 2);
    }
    int fact = 1;
    for(int i = 1; i < cc; ++i){
        fact = mult(fact, i);
    }
    Y = max(0LL, Y - X * cc);
    //assert(Y);
    for(int i = 0; i < cc; ++i){
        for(int j : ccs[i]) {
            dfs2(j, -1, i, 0);
        }
        for(auto j : paths[i]) {
            paths[i][j.x].x /= 2;
            paths[i][j.x].y /= 2;
        }
    }
    unordered_map<int, pii> tracks;
    tracks[0] = {0, 1};
    for(int i = 0; i < cc; ++i){
        unordered_map<int, pii> cur;
        for(auto j : paths[i]) {
            for(auto k : tracks){
                int len = min(Y, j.x + k.x);
                cur[len].x = add(cur[len].x, add(mult(k.y.y, j.y.x), mult(k.y.x, j.y.y)));
                cur[len].y = add(cur[len].y, mult(j.y.y, k.y.y));
            }
        }
        tracks = cur;
    }
    cout << mult(mult(p2, fact), tracks[Y].x + mult(tracks[Y].y, mult(cc, X))) << "\n";
}