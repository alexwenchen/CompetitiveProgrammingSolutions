#include <bits/stdc++.h>
using namespace std;

#define int long long
#define all(x) (x).begin(),(x).end()
#define pb push_back

const int mxN = 200000;

vector<int> adj[mxN];

void solve() {
  int N, M; cin >> N >> M;
  for(int i = 0; i < 2 * N; ++i) {
    adj[i].clear();
  }
  for(int i = 0; i < M; ++i) {
    int a, b; cin >> a >> b; --a; --b;
    adj[a].pb(b + N);
    adj[b].pb(a + N);
    adj[a + N].pb(b);
    adj[b + N].pb(a);
  }
  int dist[2 * N];
  fill(dist, dist + 2 * N, 1e9);
  queue<array<int, 2>> q;
  q.push({0, 0});
  dist[0] = 0;
  while(q.size()) {
    auto[node, dst] = q.front(); q.pop();
    if(dist[node] != dst) continue;
    for(int i : adj[node]) {
      if(dst + 1 < dist[i]) {
        dist[i] = dst + 1;
        q.push({i, dist[i]});
      }
    }
  }
  int amt[N + 1];
  fill(amt, amt + N + 1, 0);
  int mn[N + 1];
  fill(mn, mn + N + 1, 1e9);
  vector<int> dists[N + 1];
  for(int i = 0; i < N; ++i) {
    mn[min(dist[i], dist[i + N])] = min(mn[min(dist[i], dist[i + N])], max(dist[i], dist[i + N]));
    dists[min(dist[i], dist[i + N])].pb(max(dist[i], dist[i + N]));
  }
  for(int i = 0; i < N; ++i) {
    if(mn[min(dist[i], dist[i + N])] == max(dist[i], dist[i + N])) ++amt[min(dist[i], dist[i + N])];
  }
  bool all = 1;
  for(int i = 0; i < N; ++i) {
    all &= abs(dist[i] - dist[i + N]) == 1;
  }
  if(dist[N] == 1e9) {
    cout << N - 1 << "\n";
  } else if(all) {
    cout << N << "\n";
  } else {
    map<int, int> left;
    int ans = N - 1;
    for(int i = N; i >= 0; --i) {
      map<int, int> nw;
      int cadd = 0;
      for(int j : dists[i]) {
        if(j - 2 * i == mn[i]) continue;
        if(j == i + 1) {
          ++cadd;
          ++nw[1];
        } else {
          int levels = j - i - 2;
          if(left[levels] > 0) {
            --left[levels];
          } else {
            ++ans;
          }
          ++nw[j - i];
        }
      }
      left = nw;
      ans += (cadd + 1) / 2;
    }
    cout << min(ans, M) << "\n";
  }
}

signed main(){
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  int T; cin >> T;
  while(T--) {
    solve();
  }
}
//8 cases: Sun, Feb 28, 2021 19:48:14 EST (C++17)