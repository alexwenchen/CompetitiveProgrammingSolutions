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
    int h, w; cin >> h >> w;
    int grid[h][w];
    for(int i = 0; i < h; ++i){
        string s; cin >> s;
        for(int j = 0; j < w; ++j){
            grid[i][j] = s[j] == 'J' ? 0 : s[j] == 'O' ? 1 : 2;
        }
    }
    int suffo[h][w], suffi[h][w];
    for(int i = 0; i < h; ++i){
        for(int j = 0; j < w; ++j){
            suffo[i][j] = 0;
            suffi[i][j] = 0;
        }
    }
    for(int i = 0; i < h; ++i){
        for(int j = w - 1; j >= 0; --j){
            suffo[i][j] = grid[i][j] == 1 ? 1 : 0;
            if(j < w - 1) suffo[i][j] += suffo[i][j + 1];
        }
    }
    for(int i = 0; i < w; ++i){
        for(int j = h - 1; j >= 0; --j){
            suffi[j][i] = grid[j][i] == 2 ? 1 : 0;
            if(j < h - 1) suffi[j][i] += suffi[j + 1][i];
        }
    }
    ll ans = 0;
    for(int i = 0; i < h; ++i){
        for(int j = 0; j < w; ++j){
            if(!grid[i][j]) {
                ans += (ll) suffo[i][j] * suffi[i][j];
            }
        }
    }
    cout << ans << "\n";
}
