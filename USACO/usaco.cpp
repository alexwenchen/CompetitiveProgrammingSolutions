//#pragma GCC optimize("Ofast")
//#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")
//#pragma GCC optimization ("unroll-loops")
#include <bits/stdc++.h>
using namespace std;
 
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
//    freopen((s+".in").c_str(),"r",stdin);
//    freopen((s+".text").c_str(),"w",stdout);
}

void setIO(string s) {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

int n;
int g[501][501];

pii convert(int round, int diag, bool tl){
    if(tl){
        return mp(diag, round - diag);
    } else{
        return mp(n - 1 - diag, n - 1 - (round - diag));
    }
}

int main(){
//    setIO("palpath");
    setIO2();
    cin >> n;
    int g[n][n];
    for(int i = 0; i < n; ++i){
        string s; cin >> s;
        for(int j = 0; j < n; ++j){
            g[i][j] = s[j] - 'A';
        }
    }
    if(g[0][0] != g[n - 1][n - 1]){
        cout << "0\n";
        return 0;
    }
    int dp[n + 1][n + 1];
    int temp[n + 1][n + 1];
    memset(dp, 0, sizeof dp);
    memset(temp, 0, sizeof temp);
    int dxt[2] = {1, 0}, dyt[2] = {0, 1}, dxb[2] = {-1, 0}, dyb[2] = {0, -1};
    temp[0][0] = 1;
    for(int r = 1; r < n; ++r){
        for(int tl = 0; tl < r; ++tl){
            for(int br = 0; br < r; ++br){
                pii ctl = convert(r - 1, tl, true);
                pii cbr = convert(r - 1, br, false);
                if(temp[tl][br] == 0 || g[ctl.x][ctl.y] != g[cbr.x][cbr.y]) continue;
                for(int dt = 0; dt < 2; ++dt){
                    for(int db = 0; db < 2; ++db){
                        int nxtl = ctl.x + dxt[dt];
                        int nytl = ctl.y + dyt[dt];
                        int nxbr = cbr.x + dxb[db];
                        int nybr = cbr.y + dyb[db];
                        //diag depends on row
                        if(g[nxtl][nytl] != g[nxbr][nybr]) continue;
                        dp[nxtl][n - 1 - nxbr] = add(dp[nxtl][n - 1 - nxbr], temp[tl][br]);
                    }
                }
            }
        }
        for(int tl = 0; tl < n; ++tl){
            for(int br = 0; br < n; ++br){
                temp[tl][br] = dp[tl][br];
            }
        }
        memset(dp, 0, sizeof dp);
    }
    int ans = 0;
    for(int i = 0; i < n; ++i){
        ans = add(ans, temp[i][n - 1 - i]);
    }
    cout << ans << "\n";
}