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

const int mxN = 100000;

vector<int> adj[mxN];
int n;
int p[mxN];
int ans[mxN];
int tin[mxN], tout[mxN];
vector<int> et;

int tree[mxN + 5];
void update(int index, int val) {
    index++;
    while(index < mxN + 5) {
        tree[index] += val;
        index += index & -index;
    }
}
int query(int index) {
    int ret = 0;
    index++;
    while(index > 0) {
        ret += tree[index];
        index -= index & -index;
    }
    return ret;
}
int rquery(int a, int b) {
    return query(b)-query(a-1);
}

void dfs(int v, int p){
    tin[v] = et.size();
    et.eb(v);
    for(int i : adj[v]){
        if(i == p) continue;
        dfs(i, v);
    }
    tout[v] = et.size() - 1;
}

int main(){
    setIO("promote");
    //setIO2();
    cin >> n;
    for(int i = 0; i < n; ++i){
        cin >> p[i];
    }
    for(int i = 1; i < n; ++i){
        int par; cin >> par; --par;
        adj[par].eb(i);
        adj[i].eb(par);
    }
    dfs(0, -1);
    pii inv[n];
    for(int i = 0; i < n; ++i){
        inv[i] = mp(p[i], i);
    }
    sort(inv, inv + n);
    for(int i = n - 1; i >= 0; --i){
        ans[inv[i].y] = rquery(tin[inv[i].y], tout[inv[i].y]);
        update(tin[inv[i].y], 1);
    }
    for(int i = 0; i < n; ++i){
        cout << ans[i] << "\n";
    }
}