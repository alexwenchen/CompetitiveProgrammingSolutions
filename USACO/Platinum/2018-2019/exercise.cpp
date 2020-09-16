//#pragma GCC optimize("Ofast")
//#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")
//#pragma GCC optimization ("unroll-loops")
#include <bits/stdc++.h>
using namespace std;
 
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
 
void setIO2() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
//    freopen((s+".in").c_str(),"r",stdin);
//    freopen((s+".text").c_str(),"w",stdout);
}

void setIO(string s) {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	freopen((s + ".in").c_str(), "r", stdin);
	freopen((s + ".out").c_str(), "w", stdout);
}

const int mxN = 2e5 + 5;
const int mxM = 2e5 + 5;
const int MAXSEG = 262144;

struct edge{
    int u, v, i;
    edge(){}
    edge(int a, int b, int c){
        u = a; v = b; i = c;
    }
};

struct dirEdge{
    int d, i;
    dirEdge(){}
    dirEdge(int a, int b){
        d = a; i = b;
    }
};

vector<dirEdge> adj[mxN];
edge extra[mxM];
edge sedges[mxN - 1];
int parEdge[mxN];
int startEdge[mxN - 1];
int startAnc[mxN];
int startAt[mxN], endAt[mxN];
int N;

const int mxD = 18;
int depth[mxN];
int bLift[mxN][mxD];
void dfsInitialize(int v, int p) {
    for(dirEdge i: adj[v]) {
        if(i.d == p)
            continue;
        parEdge[i.d] = i.i;
        depth[i.d] = depth[v] + 1;
        bLift[i.d][0] = v;
        dfsInitialize(i.d, v);
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
int getEdge(int a, int b){ //b is anc
    int ans = a;
    for(int d = mxD-1; d >= 0; d--) {
        if(depth[ans] - (1<<d) >= depth[b] + 1) {
            ans = bLift[ans][d];
        }
    }
    return parEdge[ans];
}
void dfsSum(int v, int p){
    for(dirEdge i : adj[v]){
        if(i.d == p) continue;
        startAnc[i.d] = startAnc[v] + startEdge[i.i];
        dfsSum(i.d, v);
    }
}
ll dfsSolve(int v, int p){
    ll ans = 0;
    ans -= (ll) startAt[v] * startAnc[v];
    ans += (ll) endAt[v] * (startAnc[v] - 1);
    for(dirEdge i : adj[v]){
        if(i.d == p) continue;
        ans += dfsSolve(i.d, v);
    }
    return ans;
}

int main(){
    setIO("exercise");
//    setIO2();
    int M; cin >> N >> M;
    for(int i = 0; i < N; ++i){
        adj[i].clear();
    }
    int Ns = M - (N - 1);
    for(int i = 0; i < N - 1; ++i){
        int a,b; cin >> a >> b;
        --a; --b;
        adj[a].eb((dirEdge){b, i});
        adj[b].eb((dirEdge){a, i});
        sedges[i] = (edge){a, b, i};
    }
    for(int i = 0; i < Ns; ++i){
        int a, b; cin >> a >> b;
        --a; --b;
        extra[i] = (edge){a, b, i};
    }
    initialize();
    parEdge[0] = -1;
    for(int i = 0; i < Ns; ++i){
        int a = extra[i].u;
        int b = extra[i].v;
        int l = LCA(a, b);
        if(l == a){
            ++startEdge[getEdge(b, l)];
            ++startAt[l];
            ++endAt[b];
        }
        else if(l == b){
            ++startEdge[getEdge(a, l)];
            ++startAt[l];
            ++endAt[a];
        }
        else{
            ++startEdge[getEdge(a, l)];
            ++startEdge[getEdge(b, l)];
            ++startAt[l];
            ++endAt[a];
            ++startAt[l];
            ++endAt[b];
        }
    }
    dfsSum(0, -1);
    ll ans = dfsSolve(0, -1);
    for(int i = 0; i < N - 1; ++i){
        if(startEdge[i]) {
            ans -= (ll) startEdge[i] * (startEdge[i] - 1) / 2;
        }
    }
    map<pair<pii, int>, int> amtSame;
    for(int i = 0; i < Ns; ++i){
        int l = LCA(extra[i].u, extra[i].v);
        if(l == extra[i].u || l == extra[i].v) continue;
        pair<pii, int> cur = mp(mp(getEdge(extra[i].u, l), getEdge(extra[i].v, l)), l);
        if(cur.x.x < cur.x.y) swap(cur.x.x, cur.x.y);
        if(cur.x.y != -1 && cur.x.x != -1) ans -= amtSame[cur];
        ++amtSame[cur];
    }
    cout << ans << "\n";
}