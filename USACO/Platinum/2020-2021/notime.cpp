#include <bits/stdc++.h>
using namespace std;

//#define int long long
#define all(x) (x).begin(),(x).end()
#define pb push_back

const int mxN = 200010;

int bit[mxN];

void update(int ind, int add) {
  ++ind;
  while(ind < mxN) {
    bit[ind] += add;
    ind += ind & (-ind);
  }
}

int get(int ind) {
  ++ind;
  int ret = 0;
  while(ind > 0) {
    ret += bit[ind];
    ind -= ind & (-ind);
  }
  return ret;
}

int rget(int l, int r) {
  return get(r) - get(l - 1);
}

int stable[32][mxN];

int rmin(int l, int r) {
  if(l > r) return 1e9;
  if(l == r) return stable[0][l];
  int k = 31 - __builtin_clz(r - l);
  return min(stable[k][l], stable[k][r - (1 << k) + 1]);
}

signed main(){
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  int N, Q; cin >> N >> Q;
  int a[N];
  for(int i = 0; i < N; ++i) {
    cin >> a[i];
    stable[0][i] = a[i];
  }
  for(int i = 1; (1 << i) <= N; ++i) {
    for(int j = 0; j + (1 << i) - 1 < N; ++j) {
      stable[i][j] = min(stable[i - 1][j], stable[i - 1][j + (1 << (i - 1))]); 
    }
  }
  int val[N];
  int last[N + 1];
  fill(last, last + N + 1, -1);
  vector<int> inds[N + 1];
  for(int i = 0; i < N; ++i) {
    if(last[a[i]] == -1) {
      val[i] = 1;
    } else {
      int mn = rmin(last[a[i]] + 1, i - 1);
      if(mn < a[i]) {
        val[i] = 1;
      } else {
        val[i] = 0;
      }
    }
    update(i, val[i]);
    last[a[i]] = i;
  }
  vector<array<int, 3>> qs[N];
  for(int i = 0; i < Q; ++i) {
    int l, r; cin >> l >> r; --l; --r;
    qs[l].pb({l, r, i});
  }
  int ans[Q];
  for(int i = N - 1; i >= 0; --i) {
    inds[a[i]].pb(i);
  }
  for(int i = 0; i <= N; ++i) {
    if(inds[i].size()) {
      update(inds[i].back(), 1 - val[inds[i].back()]);
    }
  }
  for(int i = 0; i < N; ++i) {
    for(auto[l, r, ind] : qs[i]) {
      ans[ind] = rget(l, r);
    }
    inds[a[i]].pop_back();
    if(inds[a[i]].size()) update(inds[a[i]].back(), 1 - val[inds[a[i]].back()]);
  }
  for(int i = 0; i < Q; ++i) {
    cout << ans[i] << "\n";
  }
}
