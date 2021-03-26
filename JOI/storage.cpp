#include <bits/stdc++.h>
using namespace std;

#define int long long
#define all(x) (x).begin(),(x).end()
#define pb push_back

const int mxN = 200001;

vector<int> adj[mxN];
int n;
int sz[mxN];
int cpar[mxN];
bool vis[mxN];

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

int ans[mxN];
int depth[mxN], ssz[mxN];
set<array<int, 3>, greater<array<int, 3>>> s; //sz, depth 

void init(int v, int p) {
  ssz[v] = 1;
  for(int i : adj[v]) {
    if(vis[i] || i == p) continue;
    depth[i] = depth[v] + 1;
    init(i, v);
    ssz[v] += ssz[i];
  }
}

void add(int v, int p, int rt, int c) {
  s.insert({ssz[v], depth[v], rt});
  int k = 2 * min(ssz[v], ssz[c] - ssz[rt]);
  ans[k] = max(ans[k], depth[v] + 1);
  for(int i : adj[v]) {
    if(vis[i] || i == p) continue;
    add(i, v, rt, c);
  }
}

void solve(int c) {
  depth[c] = 0;
  init(c, -1);
  for(int i : adj[c]) {
    if(vis[i]) continue;
    add(i, c, i, c);
  }
  multiset<int> ls;
  map<int, int> sb;
  for(auto[csz, dp, rt] : s) {
    if(sb[rt] < dp) {
      if(sb[rt] > 0) {
        ls.erase(ls.lower_bound(sb[rt]));
      }
      sb[rt] = dp;
      ls.insert(dp);
    }
    if(ls.size() >= 2) {
      int total = 1;
      auto it = ls.end();
      for(int i = 0; i < 2; ++i) {
        --it;
        total += *it;
      }
      ans[csz * 2] = max(ans[csz * 2], total);
    }
  }
  s.clear();
}

void centroid_decomp(int n=0, int p=-1) {
    int c = centroid(n);
    solve(c);
    vis[c] = true;
    cpar[c] = p;
    for (int v : adj[c]) if (!vis[v]) {
        centroid_decomp(v, c);
    }
}

signed main(){
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  cin >> n;
  for(int i = 0; i < n - 1; ++i) {
    int a, b; cin >> a >> b; --a; --b;
    adj[a].pb(b);
    adj[b].pb(a);
  }
  centroid_decomp();
  for(int i = n - 1; i >= 0; --i) {
    ans[i] = max(ans[i], ans[i + 1]);
  }
  for(int i = 1; i <= n; ++i) {
    if(i % 2) {
      cout << 1 << "\n";
    } else {
      cout << max(1LL, ans[i]) << "\n";
    }
  }
}