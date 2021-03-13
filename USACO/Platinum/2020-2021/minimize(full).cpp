//credit to tim for the idea and clean af implementation
#include <bits/stdc++.h>
using namespace std;

#define int long long
#define all(x) (x).begin(),(x).end()
#define pb push_back

const int mxN = 200002;

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
  if(dist[N] == 1e9) {
    cout << N - 1 << "\n";
    return;
  } 
  multiset<int> diffs[N];
  for(int i = 0; i < N; ++i) {
    diffs[min(dist[i], dist[i + N])].insert(abs(dist[i] - dist[i + N]));
  }
  multiset<int> prev = diffs[0];
  int ans = N - 1;
  for(int i = 1; i < N; ++i) {
    multiset<int> unmatched = diffs[i];
    multiset<int> left;
    //match as many prev with cur as we can
    for(int j : prev) {
      auto match = unmatched.find(j - 2);
      if(match == unmatched.end()) { //cant match with current layer, so we need to add an edge for just it
        ++ans;
      } else { //match
        unmatched.erase(match);
        left.insert(j - 2);
      }
    }
    for(int j : unmatched) { //for all unmatched, try to assign to a parent
      auto parent = diffs[i - 1].find(j);
      if(parent == diffs[i - 1].end()) { //just connect to an arbitrary node in last layer
        left.insert(j);
      }
    }
    int add = 0;
    while(left.size() && (*left.begin()) == 1) {
      left.erase(left.begin());
      ++add;
    }
    ans += (add + 1) / 2;
    prev = left;
  }
  cout << ans << "\n";
}

signed main(){
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  int T; cin >> T;
  while(T--) {
    solve();
  }
}
