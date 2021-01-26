#include <bits/stdc++.h>
using namespace std;

//#define int long long
#define all(x) (x).begin(),(x).end()
#define pb push_back

const int MOD = 1e9 + 7;
const int mxK = 50000;

int add(int a, int b) {
  a += b;
  if(a < 0){
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
  return a;
}
 
int mult(int a, int b){
  return ((long long)a * b) % MOD;
}

int parity;
vector<vector<int>> adj2[mxK];
vector<array<int, 2>> sd[mxK];
vector<array<int, 2>> sz(mxK);

bool comp(array<int, 2> a, array<int, 2> b) {
  return sd[a[1]][a[0]][parity] < sd[b[1]][b[0]][parity];
}

bool comp2(array<int, 2> a, array<int, 2> b) {
  return max(sd[a[1]][a[0]][0], sd[a[1]][a[0]][1]) < max(sd[b[1]][b[0]][0], sd[b[1]][b[0]][1]);
}

int K;

vector<int> seg;

void pull(int p) { 
  seg[p] = mult(seg[2 * p], seg[2 * p + 1]); 
}
void update(int p, int val) {
  seg[p += K] = val; 
  for (p /= 2; p; p /= 2) {
    pull(p); 
  }
}
int query(int l, int r) {
  int ra = 1, rb = 1; 
  for (l += K, r += K + 1; l < r; l /= 2, r /= 2) {
    if (l & 1) ra = mult(ra, seg[l++]);
    if (r & 1) rb = mult(seg[--r], rb);
  }
  return mult(ra,rb);
}

signed main(){
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  cin >> K;
  for(int i = 0; i < K; ++i) {
    int N, M; cin >> N >> M;
    adj2[i].resize(2 * N);
    sd[i].resize(N);
    sz[i] = {N, M};
    for(int j = 0; j < N; ++j) {
      sd[i][j] = {MOD, MOD};
    }
    for(int j = 0; j < M; ++j) {
      int a, b; cin >> a >> b; --a; --b;
      adj2[i][a].pb(b + N);
      adj2[i][b].pb(a + N);
      adj2[i][b + N].pb(a);
      adj2[i][a + N].pb(b);
    }
    queue<array<int, 2>> q;
    int mdist[2 * N];
    fill(mdist, mdist + 2 * N, MOD);
    mdist[0] = 0;
    q.push({0, 0});
    while(q.size()) {
      auto cur = q.front(); q.pop();
      int dist = cur[0];
      int node = cur[1];
      if(dist != mdist[node]) {
        continue;
      }
      for(int j : adj2[i][node]) {
        int ndist = dist + 1;
        if(ndist < mdist[j]) {
          mdist[j] = ndist;
          q.push({ndist, j});
        }
      }
    }
    for(int j = 0; j < N; ++j) {
      sd[i][j][0] = mdist[j];
      sd[i][j][1] = mdist[j + N];
    }
  }
  seg.resize(4 * K);
  int ans = 0;
  for(parity = 0; parity < 2; ++parity) {
    vector<array<int, 2>> nodes;
    for(int i = 0; i < K; ++i) {
      for(int j = 0; j < sz[i][0]; ++j) {
        if(sd[i][j][parity] != MOD) {
          nodes.pb({j, i});
        }
      }
    }
    sort(all(nodes), comp);
    for(auto i : nodes) {
      int node = i[0];
      int graph = i[1];
      int prod = 1;
      if(graph) prod = mult(prod, query(0, graph - 1));
      if(graph < K - 1) prod = mult(prod, query(graph + 1, K - 1));
      ans = add(ans, mult(prod, sd[graph][node][parity]));
      update(graph, query(graph, graph) + 1);
    }
    fill(all(seg), 0);
  }
  vector<array<int, 2>> nodes;
  for(int i = 0; i < K; ++i) {
    for(int j = 0; j < sz[i][0]; ++j) {
      if(sd[i][j][0] != MOD && sd[i][j][1] != MOD) {
        nodes.pb({j, i});
      }
    }
  }
  sort(all(nodes), comp2);
  for(auto i : nodes) {
    int node = i[0];
    int graph = i[1];
    int prod = 1;
    if(graph) prod = mult(prod, query(0, graph - 1));
    if(graph < K - 1) prod = mult(prod, query(graph + 1, K - 1));
    ans = sub(ans, mult(prod, max(sd[graph][node][0], sd[graph][node][1])));
    update(graph, query(graph, graph) + 1);
  }
  cout << ans << "\n";
}
