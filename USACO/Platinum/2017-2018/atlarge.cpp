//solved using this idea: https://codeforces.com/blog/entry/57170?#comment-409608
#include <bits/stdc++.h>
 
using namespace std;
 
#define int long long
#define pb push_back
#define all(x) (x).begin(),(x).end()

void fileIO(string s) {
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

const int mxN = 140000;

int size = mxN;

int lazy[4 * mxN];
int sum[4 * mxN];
int evaluateSum(int index, int l, int r) {
    return sum[index] + (r-l+1)*lazy[index];
}
void pushDown(int index, int l, int r) {
    sum[index] += lazy[index] * (r-l+1);
    if(l != r) {
        lazy[2*index] += lazy[index];
        lazy[2*index+1] += lazy[index];
    }
    lazy[index] = 0;
}
void pullUp(int index, int l, int r) {
    int m = (l+r)/2;
    sum[index] = evaluateSum(2*index, l, m) + evaluateSum(2*index+1, m+1, r);
}
void update(int index, int l, int r, int left, int right, int inc) {
    if(r < left || l > right) return;
    if(l >= left && r <= right) {
        lazy[index] += inc;
        return;
    }
    pushDown(index, l, r);
    int m = (l+r)/2;
    update(2*index, l, m, left, right, inc);
    update(2*index+1, m+1, r, left, right, inc);
    pullUp(index, l, r);
}
void update(int l, int r, int inc) {
    update(1, 0, size-1, l, r, inc);
}
int sumQuery(int index, int l, int r, int left, int right) {
    if(r < left || l > right) return 0;
    if(l >= left && r <= right) {
        return evaluateSum(index, l, r);
    }
    pushDown(index, l, r);
    int m = (l+r)/2;
    int ret = 0;
    ret += sumQuery(2*index, l, m, left, right);
    ret += sumQuery(2*index+1, m+1, r, left, right);
    pullUp(index, l, r);
    return ret;
}
int sumQuery(int l, int r) {
    return sumQuery(1, 0, size-1, l, r);
}

int n;
vector<int> adj[mxN];
int sz[mxN];
int cpar[mxN];
bool vis[mxN];
int mindistsb[mxN];
int mindist[mxN];
int ans[mxN];

void dfsmindist1(int v, int p) {
    if(adj[v].size() == 1) mindistsb[v] = 0;
    for(int i : adj[v]) {
        if(i == p) continue;
        dfsmindist1(i, v);
        mindistsb[v] = min(mindistsb[v], mindistsb[i] + 1);
    }
}

void dfsmindist2(int v, int p) {
    mindist[v] = mindistsb[v];
    if(p != -1) mindist[v] = min(mindist[v], mindist[p] + 1);
    for(int i : adj[v]) {
        if(i == p) continue;
        dfsmindist2(i, v);
    }
}

void dfs(int n, int p=-1) {
    sz[n] = 1;
    for (int v : adj[n]) if (v != p && !vis[v]) {
        dfs(v, n);
        sz[n] += sz[v];
    }
}

int centroid(int n) {
    dfs(n);
    int num = sz[n];
    int p = -1;
    do {
        int nxt = -1;
        for (int v : adj[n]) if (v != p && !vis[v]) {
            if (2 * sz[v] > num)
                nxt = v;
        }
        p = n, n = nxt;
    } while (~n);
    return p;
}

int depth[mxN], aa[mxN], bb[mxN];

//1. depth[u] + depth[v] >= mindist[v] -> mindist[v] - depth[v] <= depth[u]
//2. depth[u] + depth[x] < mindist[x] -> depth[u] < min(mindist[x] - depth[x])
//1+2. mindist[v] - depth[v] <= depth[u] < min(midist[x] - depth[x])
//3. depth[u] - depth[x] < mindist[x] -> depth[u] < min(depth[x] + mindist[x]) where x is ancestor of u

//final conditions:
//mindist[v] - depth[v] <= depth[u] < min(mindist[x] - depth[x]) where x is an ancestor of v
//depth[u] < min(depth[x] + mindist[x]) where x is ancestor of u

//aa is mindist[x] - depth[x], bb is depth[x] + mindist[x];

void dd(int v, int p, int va, int vb) { //va is aa, vb is bb
    aa[v] = va;
    bb[v] = vb;
    va = min(va, mindist[v] - depth[v]);
    if(p != -1) {
        vb = min(vb, depth[v] + mindist[v]);
    }
    for(int i : adj[v]) {
        if(i == p || vis[i]) continue;
        depth[i] = depth[v] + 1;
        dd(i, v, va, vb);
    }
}

void dda(int v, int p) {
    update(mindist[v] - depth[v], aa[v] - 1, 1);
    for(int i : adj[v]) {
        if(i == p || vis[i]) continue;
        dda(i, v);
    }
}

void ddb(int v, int p) {
    if(depth[v] < bb[v]) {
        ans[v] += sumQuery(depth[v], depth[v]);
    }
    for(int i : adj[v]) {
        if(i == p || vis[i]) continue;
        ddb(i, v);
    }
}

void resetdda(int v, int p) {
    update(mindist[v] - depth[v], aa[v] - 1, -1);
    for(int i : adj[v]) {
        if(i == p || vis[i]) continue;
        resetdda(i, v);
    }
}

void centroid_decomp(int n=0, int p=-1) {
    int c = centroid(n);
    depth[c] = 0;
    dd(c, -1, mxN, mxN);
    dda(c, -1);
    ddb(c, -1);
    resetdda(c, -1);
    for(int i : adj[c]) {
        if(vis[i]) continue;
        resetdda(i, c);
        ddb(i, c);
        dda(i, c);
    }
    vis[c] = true;
    cpar[c] = p;
    for (int v : adj[c]) if (!vis[v]) {
        centroid_decomp(v, c);
    }
}

signed main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    fileIO("atlarge");
    cin >> n;
    for(int i = 0; i < n - 1; ++i) {
        int a, b; cin >> a >> b; --a; --b;
        adj[a].pb(b);
        adj[b].pb(a);
    }
    fill(mindistsb, mindistsb + n, 1e9);
    dfsmindist1(0, -1);
    dfsmindist2(0, -1);
    centroid_decomp();
    for(int i = 0; i < n; ++i) {
        if(adj[i].size() == 1) --ans[i];
        cout << ans[i] << "\n";
    }
}
