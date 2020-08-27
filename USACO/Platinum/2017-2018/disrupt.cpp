#include <bits/stdc++.h>
using namespace std;
 
using ll = long long;
using pii = pair<int, int>;
 
#define pb push_back
#define mp make_pair
#define eb emplace_back
#define all(x) (x).begin(),(x).end()
#define x first
#define y second
 
const int MOD = 1e9 + 7;
const int mxN = 5e4 + 5;
const int mxM = 5e4 + 5;
const int mxD = 16;
const int intMax = 1e9;

void setIO(string s) {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

void setIO2() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
//    freopen((s + ".in").c_str(), "r", stdin);
//    freopen((s + ".out").c_str(), "w", stdout);
}

struct path{
    int a, b, length;
    ll comp;
    path(int x, int y, int l){
        a = x; b = y; length = l;
        comp = a + b * 50000LL + length * 2500000000LL;
    }
    bool operator<(const path o) const{
        return comp < o.comp;
    }
};

struct edge{
    int a, b;
    edge(){}
    edge(int x, int y){
        a = x; b = y;
    }
};

vector<int> adj[mxN];
set<path> sets[mxN], add[mxN], rmv[mxN];
int ans[mxN], numEdge[mxN], depth[mxN];
int lca[mxN][mxD];
void dfssetup(int curr, int par) {
    for(int out: adj[curr]) {
        if(out == par)
            continue;
        depth[out] = depth[curr] + 1;
        lca[out][0] = curr;
        dfssetup(out, curr);
    }
}
int LCA(int a, int b) {
    if(depth[a] < depth[b]) {
        int temp = a;
        a = b;
        b = temp;
    }
    for(int d = mxD-1; d >= 0; d--) {
        if(depth[a] - (1<<d) >= depth[b]) {
            a = lca[a][d];
        }
    }
    for(int d = mxD-1; d >= 0; d--) {
        if(lca[a][d] != lca[b][d]) {
            a = lca[a][d];
            b = lca[b][d];
        }
    }
    if(a != b) {
        a = lca[a][0];
        b = lca[b][0];
    }
    return a;
}
void initLCA() {
    for(int d = 1; d < mxD; d++) {
        for(int i = 0; i < mxN; i++) {
            lca[i][d] = lca[lca[i][d-1]][d-1];
        }
    }
}

void dfs(int v, int p){
    for(int i : adj[v]){
        if(i == p) continue;
        dfs(i, v);
        if(sets[i].size() > sets[v].size()){
            swap(sets[i], sets[v]);
        }
        for(path j : sets[i]){
            sets[v].insert(j);
        }
        sets[i].clear();
    }
    if(p == -1) return;
    for(path i : add[v]) {
        sets[v].insert(i);
    }
    for(path i : rmv[v]) {
        sets[v].erase(i);
    }
    if(sets[v].size()) {
        ans[numEdge[v]] = (int) ((*sets[v].begin()).comp / 2500000000L);
    }
    else {
        ans[numEdge[v]] = -1;
    }
}

int main(){
    setIO("disrupt");
//    setIO2();
    int N, M; cin >> N >> M;
    edge edges[N - 1];
    for(int i = 0; i < N - 1; ++i){
        int a, b; cin >> a >> b;
        --a; --b;
        edges[i] = (edge){a, b};
        adj[a].eb(b);
        adj[b].eb(a);
    }
    dfssetup(0, -1);
    initLCA();
    for(int i = 0; i < N - 1; ++i){
        int a = edges[i].a;
        int b = edges[i].b;
        int lca = LCA(a, b);
        if(lca == a){
            numEdge[b] = i;
        }
        else{
            numEdge[a] = i;
        }
    }
    for(int i = 0; i < M; ++i){
        int a, b, c; cin >> a >> b >> c;
        --a; --b;
        int lca = LCA(a, b);
        path cur = (path){a, b, c};
        if(lca == a){
            rmv[a].insert(cur);
            add[b].insert(cur);
        }
        else if(lca == b){
            rmv[b].insert(cur);
            add[a].insert(cur);
        }
        else{
            rmv[lca].insert(cur);
            add[a].insert(cur);
            add[b].insert(cur);
        }
    }
    dfs(0, -1);
    for(int i = 0; i < N - 1; ++i){
        cout << ans[i] << "\n";
    }
}