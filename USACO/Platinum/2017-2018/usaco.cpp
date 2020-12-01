//#pragma GCC optimize("Ofast")
//#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")
//#pragma GCC optimization ("unroll-loops")
#include <bits/stdc++.h>
using namespace std;
 
#define int ll

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
 
void setIO2() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
}

void setIO(string s) {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

bool comp(array<int, 2> a, array<int, 2> b) {
    return a[0] < b[0];
}

signed main(){
    setIO("lifeguards");
//    setIO2();
    int N, K; cin >> N >> K;
    vector<array<int, 2>> lgs(N);
    for(int i = 0; i < N; ++i) {
        cin >> lgs[i][0] >> lgs[i][1];
    }
    sort(all(lgs), comp);
    bool removed[N];
    fill(removed, removed + N, false);
    int mx = 0;
    for(int i = 0; i < N; ++i) {
        if(lgs[i][1] <= mx) {
            removed[i] = true;
        }
        mx = max(mx, lgs[i][1]);
    }
    vector<array<int, 2>> lg;
    vector<int> ends;
    int M = 0;
    for(int i = 0; i < N; ++i) {
        if(!removed[i]) {
            lg.pb({lgs[i][0], lgs[i][1]});
            ends.pb(lgs[i][1]);
            ++M;
        } else --K;
    }
    N = M;
    int dp[N + 1][K + 2];
    memset(dp, -0x3f, sizeof dp);
    dp[0][0] = 0;
    for(int i = 1; i <= N; ++i) {
        int fst = lower_bound(all(ends), lg[i - 1][0]) - ends.begin() + 1;
        int betw = i - fst - 1;
        for(int j = 0; j <= K; ++j) {
            if(j) dp[i][j] = max(dp[i][j], dp[i - 1][j - 1]);
            if(fst < i && j >= betw) {
                dp[i][j] = max(dp[i][j], dp[fst][j - betw] + lg[i - 1][1] - ends[fst - 1]);
            }
            if(fst > 0 && j > betw) {
                dp[i][j] = max(dp[i][j], dp[fst - 1][j - betw - 1] + lg[i - 1][1] - lg[i - 1][0]);
            }
        }
    }
    cout << dp[N][K] << "\n";
}