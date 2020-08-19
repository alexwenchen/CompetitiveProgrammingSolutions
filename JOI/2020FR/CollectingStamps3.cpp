#include <bits/stdc++.h>
using namespace std;
 
using ll = long long;
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
    return ((ll) a * b) % MOD;
}
 
void setIO() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
//    freopen((s+".in").c_str(),"r",stdin);
//    freopen((s+".text").c_str(),"w",stdout);
}
 
//ctrl q is comment
//ctrl b is run
//ctrl s is compile
//alt r is search & replace
//alt d is delete a line
 
int main(){
    setIO();
    int N, L; cin >> N >> L;
    int X[N + 1], X2[N + 1], T[N];
    for(int i = 1; i <= N; ++i){
        cin >> X[i];
    }
    for(int i = 0; i < N; ++i){
        cin >> T[i];
    }
    for(int i = 1; i <= N; ++i){
        X2[N - i + 1] = L - X[i];
    }
    X[0] = 0; X2[0] = 0;
    ll dp[N+1][N+1][N+1][2];
    for(int i = 0; i <= N; ++i) {
        for(int j = 0; j <= N; ++j) {
            for(int k = 0; k <= N; ++k) {
                dp[i][j][k][0] = 1e18;
                dp[i][j][k][1] = 1e18;
            }
        }
    }
    dp[0][0][0][0] = 0;
    dp[0][0][0][1] = 0;
    int ans = 0;
    for(int i = 0; i <= N; ++i) {
        for(int j = 0; j <= N; ++j) {
            for(int k = 0; k <= N; ++k) {
                if(j + k > N || j + k < i) continue;
                if(j > 0) {
                    //transition from dp[i - 1][j - 1][k][0], dp[i - 1][j - 1][k][1], dp[i][j - 1][k][0], dp[i][j - 1][k][1]
                    dp[i][j][k][0] = dp[i][j - 1][k][0] + X2[j] - X2[j - 1];
                    if(i > 0 && dp[i - 1][j - 1][k][0] + X2[j] - X2[j - 1] <= T[N - j]) 
                        dp[i][j][k][0] = min(dp[i][j][k][0], dp[i - 1][j - 1][k][0] + X2[j] - X2[j - 1]);
                    dp[i][j][k][0] = min(dp[i][j][k][0], dp[i][j - 1][k][1] + X2[j] + X[k]);
                    if(i > 0 && dp[i - 1][j - 1][k][1] + X2[j] + X[k] <= T[N - j])
                        dp[i][j][k][0] = min(dp[i][j][k][0], dp[i - 1][j - 1][k][1] + X2[j] + X[k]);
                }
                if(k > 0) {
                    dp[i][j][k][1] = dp[i][j][k - 1][0] + X2[j] + X[k];
                    if(i > 0 && dp[i - 1][j][k - 1][0] + X2[j] + X[k] <= T[k - 1])
                        dp[i][j][k][1] = min(dp[i][j][k][1], dp[i - 1][j][k - 1][0] + X2[j] + X[k]);
                    dp[i][j][k][1] = min(dp[i][j][k][1], dp[i][j][k - 1][1] + X[k] - X[k - 1]);
                    if(i > 0 && dp[i - 1][j][k - 1][1] + X[k] - X[k - 1] <= T[k - 1])
                        dp[i][j][k][1] = min(dp[i][j][k][1], dp[i - 1][j][k - 1][1] + X[k] - X[k - 1]);
                }
                if(dp[i][j][k][0] < 1e18 || dp[i][j][k][1] < 1e18) ans = i;
            }
        }
    }
    cout << ans << "\n";
}
