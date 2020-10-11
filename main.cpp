#include <bits/stdc++.h>
using namespace std;
 
using ll = long long;
using pii = pair<int, int>;
using ull = unsigned ll;
 
#define pb push_back
#define mp make_pair
#define eb emplace_back
#define all(x) (x).begin(),(x).end()
#define x first
#define y second
 
const int MOD = 1e9 + 7;
//const int MOD = 998244353;
const int dx[] = {0, 0, 1, -1};
const int dy[] = {1, -1, 0, 0}; 
const char dir[] = {'R', 'L', 'D', 'U'};
 
int add(int a, int b){ //(a + b) % 1e9 + 7
    a += b;
    if(a >= MOD){
        a -= MOD;
    }
    return a;
}
 
int sub(int a, int b){
    a -= b;
    if(a < 0) a += MOD;
    return a;
}
 
int mult(int a, int b){
    return ((ll) a * b) % MOD;
}
 
void setIO() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
//    freopen((s+".in").c_str(),"r",stdin);
//    freopen((s+".text").c_str(),"w",stdout);
}

const int mxN = 100'000;
const int mxD = 17;

vector<pii> adj[mxN];
int depth[mxN];
int bLift[mxN][mxD];

void dfsInitialize(int v, int p) {
    for(auto i: adj[v]) {
        if(i.x == p)
            continue;
        depth[i.x] = depth[v] + 1;
        bLift[i.x][0] = v;
        dfsInitialize(i.x, v);
    }
}
void initialize() {
    dfsInitialize(0, -1);
    for(int d = 1; d < mxD; d++) {
        for(int i = 0; i < mxN; i++) {
            bLift[i][d] = bLift[bLift[i][d-1]][d-1];
        }
    }
}
int LCA(int a, int b) {
    if(depth[a] < depth[b]) {
        swap(a, b);
    }
    for(int d = mxD-1; d >= 0; d--) {
        if(depth[a] - (1<<d) >= depth[b]) {
            a = bLift[a][d];
        }
    }
    for(int d = mxD-1; d >= 0; d--) {
        if(bLift[a][d] != bLift[b][d]) {
            a = bLift[a][d];
            b = bLift[b][d];
        }
    }
    if(a != b) {
        a = bLift[a][0];
        b = bLift[b][0];
    }
    return a;
}

set<int> stl[mxN];
vector<int> inds[mxN];
vector<int> rmv[mxN];
int amt[mxN];
pii edges[mxN];

void dfsstl(int v, int p){
    for(auto i : adj[v]){
        if(i.x == p) continue;
        dfsstl(i.x, v);
        amt[i.y] = stl[i.x].size();
        if(stl[i.x].size() > stl[v].size()){
            swap(stl[i.x], stl[v]);
        }
        for(int j : stl[i.x]){
            stl[v].insert(j);
        }
        stl[i.x].clear();
    }
    for(int i : inds[v]){
        stl[v].insert(i);
    }
    for(int i : rmv[v]){
        stl[v].erase(i);
    }
}

int main(){
    setIO();
    int n, m, k; cin >> n >> m >> k;
    for(int i = 0; i < n - 1; ++i){
        int a, b; cin >> a >> b; --a; --b;
        adj[a].eb(mp(b, i));
        adj[b].eb(mp(a, i));
        edges[i] = mp(a, b);
    }
    initialize();
    for(int i = 0; i < m; ++i){
        int s; cin >> s;
        int lca = -1;
        for(int j = 0; j < s; ++j){
            int node; cin >> node; --node;
            if(!j) lca = node;
            lca = LCA(lca, node);
            inds[node].eb(i);
        }
        rmv[lca].eb(i);
    }
    dfsstl(0, -1);
    vector<int> ans;
    for(int i = 0; i < n - 1; ++i){
        if(amt[i] >= k){
            ans.eb(i);
        }
    }
    cout << ans.size() << "\n";
    for(int i : ans){
        cout << i + 1 << " ";
    }
    cout << "\n";
}