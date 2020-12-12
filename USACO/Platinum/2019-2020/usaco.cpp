#include <bits/stdc++.h>
using namespace std;
 
//#define int ll

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

void fileIO(string s) {
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

const int mxN = 101;

int N;
vector<int> adj[mxN];

struct dsu{
    vector<int> par;
    vector<int> sz;
    dsu() { dsu(1); }
    dsu(int size){
        par.resize(size);
        sz.resize(size);
        iota(all(par), 0);
        fill(all(sz), 1);
    }
    int find(int x){
        if(par[x] == x){
            return x;
        }
        else return x = find(par[x]);
    }
    void merge(int u, int v){
        u = find(u);
        v = find(v);
        if(u == v) return;
        if(sz[u] > sz[v]) {
            swap(u, v);
        }
        par[u] = v;
        sz[v] += sz[u];
    }
};

int fact[mxN];
int power(int x, int y) 
{ 
    int res = 1; 
    while (y > 0) 
    { 
        if (y & 1) 
            res = mult(res, x); 
        y = y>>1;
        x = mult(x, x);  
    } 
    return res; 
}

int modInverse(int a) 
{ 
    return power(a, MOD - 2);
}

int depth[mxN];
int grp[mxN]; //the representative of the chain of the node
int cur[mxN]; //chosen nodes are 1
int sbz[mxN]; //size of subtree
int amt[mxN]; //number of selected nodes in chain
int above[mxN]; //number of selected nodes above current one

void dfs(int v, int p, int cgrp) {
    sbz[v] = 1;
    grp[v] = cgrp;
    for(int i : adj[v]) {
        if(i == p) continue;
        depth[i] = depth[v] + 1;
        dfs(i, v, cgrp);
        sbz[v] += sbz[i];
    }
}

void dfsAdd(int v, int p, int rt) {
    amt[rt] += cur[v];
    for(int i : adj[v]) {
        if(i == p) continue;
        dfsAdd(i, v, rt);
    }
}

void dfsAbove(int v, int p, int curAbove) {
    above[v] = curAbove;
    curAbove += cur[v];
    for(int i : adj[v]) {
        if(i == p) continue;
        dfsAbove(i, v, curAbove);
    }
}

set<vector<int>> vis;
int K;

void brute(vector<int> locs /*size k*/, vector<int> at/*size n*/) {
    if(vis.find(locs) != vis.end()) return;
    vis.insert(locs);
    for(int i = 0; i < K; ++i) {
        int nd = locs[i];
        for(int j : adj[nd]) {
            if(at[j]) continue;
            vector<int> loccpy = locs, atcpy = at;
            loccpy[i] = j;
            atcpy[nd] = 0;
            atcpy[j] = 1;
            brute(loccpy, atcpy);
        }
    }
}

signed main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    fileIO("circus");
    cin >> N;
    for(int i = 0; i < N - 1; ++i) {
        int a, b; cin >> a >> b; --a; --b;
        adj[a].eb(b);
        adj[b].eb(a);
    }
    fact[0] = 1;
    for(int i = 1; i < mxN; ++i) {
        fact[i] = mult(i, fact[i - 1]);
    }
    if(N <= 8) {
        for(int k = 1; k <= N - 2; ++k) {
            K = k;
            vector<int> perms(k);
            for(int i = 0; i < k; ++i) {
                perms[i] = i;
            }
            int ans = 0;
            do {
                if(vis.find(perms) != vis.end()) continue;
                ++ans;
                vector<int> locs(k), at(N);
                for(int i = 0; i < k; ++i) {
                    locs[i] = perms[i];
                    at[perms[i]] = 1;
                }
                brute(locs, at);
            } while(next_permutation(all(perms)));
            cout << ans << "\n";
        }
        cout << fact[N - 1] << "\n" << fact[N] << "\n";
        return 0;
    }
    int root = -1;
    for(int i = 0; i < N; ++i) {
        if(adj[i].size() > 2) {
            root = i;
            break;
        }
    }
    if(root == -1) {
        for(int i = 1; i <= N; ++i) {
            cout << fact[i] << " ";
        }
        return 0;
    }
    for(int i : adj[root]) {
        dfs(i, root, i);
    }
    int chains = adj[root].size();
    for(int k = 1; k < N; ++k) {
        fill(cur, cur + N, 0);
        int nxt = 0;
        vector<int> curset;
        for(int i = 0; i < k; ++i) {
            if(nxt == root) ++nxt;
            ++cur[nxt];
            curset.eb(nxt);
            ++nxt;
        }
        fill(amt, amt + N, 0);
        int totEmpty = 0;
        for(int i : adj[root]) {
            dfsAdd(i, root, i);
            totEmpty += sbz[i] - amt[i];
            dfsAbove(i, root, 0);
        }
        dsu reach = dsu(N);
        for(int i = 0; i < k; ++i) {
            for(int j = i + 1; j < k; ++j) {
                int a = curset[i], b = curset[j];
                int curEmpty = totEmpty - (sbz[grp[a]] - amt[grp[a]]) - (sbz[grp[b]] - amt[grp[b]]) - above[a] - above[b];
                if(grp[a] == grp[b]) {
                    if(depth[a] > depth[b]) {
                        curEmpty = totEmpty - (sbz[grp[a]] - amt[grp[a]]) - above[a];
                    } else{
                        curEmpty = totEmpty - (sbz[grp[a]] - amt[grp[a]]) - above[b];
                    }
                }
                if(curEmpty >= 1) {
                    reach.merge(a, b);
                }
            }
        }
        int curAns = fact[k];
        set<int> vis;
        for(int i = 0; i < N; ++i) {
            int rep = reach.find(i);
            if(vis.find(rep) != vis.end()) continue;
            vis.insert(rep);
            curAns = mult(curAns, modInverse(fact[reach.sz[rep]]));
        }
        cout << curAns << "\n";
    }
    cout << fact[N] << "\n";
}