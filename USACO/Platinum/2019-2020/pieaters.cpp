//lets go, solved by myself this time 
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

bool comp(array<int, 3> a, array<int, 3> b){
    return a[0] > b[0];
}

signed main(){
    setIO("pieaters");
//    setIO2();
    int N, M; cin >> N >> M;
    array<int, 3> cows[M];
    for(int i = 0; i < M; ++i) {
        int w, l, r; cin >> w >> l >> r; --l; --r;
        cows[i] = {w, l, r};
    }
    sort(cows, cows + M, comp);
    int mx[N][N][N];
    memset(mx, 0, sizeof mx);
    for(int i = 0; i < N; ++i) {
        vector<array<int, 2>> endAt[N];
        for(int j = 0; j < M; ++j) {
            if(cows[j][1] < i) continue;
            endAt[cows[j][2]].pb({cows[j][0], cows[j][1]});
        }
        for(int j = i; j < N; ++j) {
            int mn = j + 1;
            for(auto k : endAt[j]) {
                int w = k[0], l = k[1];
                if(l < mn) {
                    for(int a = mn - 1; a >= l; --a) {
                        mx[i][j][a] = w;
                    }
                    mn = l;
                }
            }
            if(j != i) {
                for(int k = 0; k < N; ++k) {
                    mx[i][j][k] = max(mx[i][j][k], mx[i][j - 1][k]);
                }
            }
        }
    }
    int range[N][N];
    memset(range, 0, sizeof range);
    for(int i = 0; i < M; ++i) {
        range[cows[i][1]][cows[i][2]] = cows[i][0];
    }
    int dp[N][N];
    memset(dp, 0, sizeof dp);
    for(int i = 0; i < N; ++i) {
        dp[i][i] = range[i][i];
    }
    for(int len = 2; len <= N; ++len) {
        for(int st = 0; st + len - 1 < N; ++st) {
            int ed = st + len - 1;
            //first, just sub ranges
            for(int i = st; i < ed; ++i) {
                dp[st][ed] = max(dp[st][ed], dp[st][i] + dp[i + 1][ed]);
            }
            //now considering a cow eats current pie
            for(int i = st; i <= ed; ++i) {
                int mxw = mx[st][ed][i];
                if(i == st) {
                    dp[st][ed] = max(dp[st][ed], mxw + dp[i + 1][ed]);
                } else if(i == ed) {
                    dp[st][ed] = max(dp[st][ed], mxw + dp[st][i - 1]);
                } else{
                    dp[st][ed] = max(dp[st][ed], mxw + dp[st][i - 1] + dp[i + 1][ed]);
                }
            }
        }
    }
    cout << dp[0][N - 1] << "\n";
}
