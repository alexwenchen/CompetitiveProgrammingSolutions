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

const int mxA = 51;
int dp[mxA][mxA][mxA][mxA];

signed main(){
    setIO("subrev");
//    setIO2();
    int n; cin >> n;
    int a[n];
    for(int i = 0; i < n; ++i){
        cin >> a[i];
    }
    for(int i = 0; i < n; ++i){
        dp[i][i][a[i]][a[i]] = 1;
    }
    for(int len = 2; len <= n; ++len){
        for(int st = 0; st + len <= n; ++st){
            int ed = st + len - 1;
            int left = a[st], right = a[ed];
            if(len == 2) {
                if(left > right) {
                    dp[st][ed][right][left] = 2;
                } else {
                    dp[st][ed][left][right] = 2;
                }
                continue;
            }
            for(int st2 = st; st2 <= ed; ++st2) {
                for(int ed2 = st2; ed2 <= ed; ++ed2) {
                    if(st2 == st && ed2 == ed) continue;
                    for(int l = 1; l <= 50; ++l) {
                        for(int r = l; r <= 50; ++r) {
                            if(dp[st2][ed2][l][r] == 0) continue;
                            if(left <= l && st2 != st) {
                                dp[st][ed][left][r] = max(dp[st][ed][left][r], dp[st2][ed2][l][r] + 1);
                            }
                            if(right >= r && ed2 != ed) {
                                dp[st][ed][l][right] = max(dp[st][ed][l][right], dp[st2][ed2][l][r] + 1);
                                if(left <= l && st2 != st) {
                                    dp[st][ed][left][right] = max(dp[st][ed][left][right], dp[st2][ed2][l][r] + 2);
                                }
                            }
                            if(st2 == st || ed2 == ed) continue; 
                            swap(left, right);
                            if(left <= l && st2 != st) {
                                dp[st][ed][left][r] = max(dp[st][ed][left][r], dp[st2][ed2][l][r] + 1);
                            }
                            if(right >= r && ed2 != ed) {
                                dp[st][ed][l][right] = max(dp[st][ed][l][right], dp[st2][ed2][l][r] + 1);
                                if(left <= l && st2 != st) {
                                    dp[st][ed][left][right] = max(dp[st][ed][left][right], dp[st2][ed2][l][r] + 2);
                                }
                            }
                            swap(left, right);
                        }
                    }
                }
            }
        }
    }
    int ans = 0;
    for(int st = 0; st < n; ++st){
        for(int ed = st; ed < n; ++ed) {
            for(int i = 1; i <= 50; ++i){
                for(int j = 1; j <= 50; ++j){
                    ans = max(ans, dp[st][ed][i][j]);
                }
            }
        }
    }
    cout << ans << "\n";
}