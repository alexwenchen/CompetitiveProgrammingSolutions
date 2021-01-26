//in contest submission, 8/20
#include <bits/stdc++.h>
using namespace std;

#define int long long
#define all(x) (x).begin(),(x).end()
#define pb push_back

signed main(){
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  int N, M; cin >> N >> M;
  int c[M];
  for(int i = 0; i < M; ++i) {
    cin >> c[i];
  }
  int Q; cin >> Q;
  vector<array<int, 2>> queries(Q);
  for(int i = 0; i < Q; ++i) {
    cin >> queries[i][0] >> queries[i][1];
    --queries[i][0]; --queries[i][1];
  }
  if(N <= 2000 && M <= 2000) {
    int dp[N][M];
    for(int i = 0; i < N; ++i) {
      fill(dp[i], dp[i] + M, 1e18);
    }
    dp[0][0] = 0;
    for(int i = 0; i < N; ++i) {
      for(int j = 0; j < M; ++j) {
        if(i < N - 1) {
          dp[i + 1][j] = min(dp[i + 1][j], dp[i][j] + c[j]);
        }
        if(j < M - 1) {
          dp[i][j + 1] = min(dp[i][j + 1], dp[i][j] + (i + 1) * (i + 1));
        }
      }
    }
    for(int i = 0; i < Q; ++i) {
      cout << dp[queries[i][0]][queries[i][1]] << "\n";
    }
    return 0;
  }
  for(int i = 0; i < Q; ++i) {
    int rr = queries[i][0];
    int cc = queries[i][1];
    int cur = cc + rr * c[cc];
    int l = 0, r = rr;
    while(l < r) {
      int m = (l + r) / 2;
      int a = m * c[0] + (m + 1) * (m + 1) * cc + (rr - m) * c[cc];
      int m2 = m + 1;
      int b = m2 * c[0] + (m2 + 1) * (m2 + 1) * cc + (rr - m2) * c[cc];
      if(a > b) {
        l = m + 1;
      } else {
        r = m;
      }
    }
    int cur2 = l * c[0] + (l + 1) * (l + 1) * cc + (rr - l) * c[cc];
    cout << min(cur, cur2) << "\n";
  }
}
