#include <bits/stdc++.h>
using namespace std;

#define int long long
#define all(x) (x).begin(),(x).end()
#define pb push_back

const int B = 250;
const int mxN = 200001;
const int Bs = mxN / B + 1;

int total[Bs], zeroes[Bs], add[Bs], active[Bs];
int amt[mxN];
bool first[mxN];

void alter(int x, bool ad) {
  if(ad) { //switch 0 to 1
    first[x] = 1;
    int b = x / B;
    total[b] += amt[x] + add[b];
    --zeroes[b];
  } else {
    first[x] = 0;
    int b = x / B;
    total[b] -= amt[x] + add[b];
    ++zeroes[b];
  }
}

void update(int l, int r, int sign) {
  if(r < l) return;
  int bl = l / B, br = r / B;
  for(int i = bl + 1; i < br; ++i) {
    total[i] += sign * active[i];
    add[i] += sign;
    total[i] -= sign * zeroes[i];
  }
  for(int i = 0; i < B; ++i) {
    int cur = B * bl + i;
    if(cur >= l && cur <= r) {
      amt[cur] += sign;
      if(first[cur]) total[bl] += sign;
    }
  }
  if(bl != br) {
    for(int i = 0; i < B; ++i) {
      int cur = B * br + i;
      if(cur >= l && cur <= r) {
        amt[cur] += sign;
        if(first[cur]) total[br] += sign;
      }
    }
  }
}

int query(int l, int r) {
  if(r < l) return 0;
  int bl = l / B, br = r / B;
  int ret = 0;
  for(int i = bl + 1; i < br; ++i) {
    ret += total[i];
  }
  for(int i = 0; i < B; ++i) {
    int cur = B * bl + i;
    if(first[cur] && cur >= l && cur <= r) {
      ret += amt[cur] + add[bl];
    }
  }
  if(bl != br) {
    for(int i = 0; i < B; ++i) {
      int cur = B * br + i;
      if(first[cur] && cur >= l && cur <= r) {
        ret += amt[cur] + add[br];
      }
    }
  }
  return ret;
} 

signed main(){
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  int N; cin >> N;
  int a[N];
  for(int i = 0; i < N; ++i) {
    cin >> a[i]; --a[i];
  }
  vector<int> inds[N];
  for(int i = N - 1; i >= 0; --i) {
    inds[a[i]].pb(i);
  }
  int last[N];
  fill(last, last + N, -1);
  for(int i = 0; i < N; ++i) {
    active[i / B]++;
    if(last[a[i]] == -1) {
      first[i] = 1;
    } else {
      ++zeroes[i / B];
    }
    last[a[i]] = i;
  }
  fill(last, last + N, -1);
  for(int i = 0; i < N; ++i) {
    inds[a[i]].pop_back();
    if(last[a[i]] == -1) {
      if(inds[a[i]].size()) {
        update(i + 1, inds[a[i]].back() - 1, 1);
      } else {
        update(i + 1, N - 1, 1);
      }
    }
    last[a[i]] = i;
  }
  for(int i = N - 1; i >= 0; --i) {
    inds[a[i]].pb(i);
  }
  int ans = 0;
  for(int i = 0; i < N; ++i) {
    inds[a[i]].pop_back();
    alter(i, 0);
    if(inds[a[i]].size()) {
      update(i + 1, inds[a[i]].back() - 1, -1);
      ans += query(i + 1, inds[a[i]].back() - 1);
    } else {
      update(i + 1, N - 1, -1);
      ans += query(i + 1, N - 1);
    }
    if(inds[a[i]].size()) {
      alter(inds[a[i]].back(), 1);
      if(inds[a[i]].size() == 1) {
        update(inds[a[i]].back() + 1, N - 1, 1);
      } else {
        int sz = inds[a[i]].size();
        update(inds[a[i]][sz - 1] + 1, inds[a[i]][sz - 2] - 1, 1);
      }
    }
  }
  cout << ans << "\n";
}
