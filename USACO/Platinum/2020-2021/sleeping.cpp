#include <bits/stdc++.h>
#include <bits/extc++.h>
#include <ext/pb_ds/assoc_container.hpp> // Common file
#include <ext/pb_ds/tree_policy.hpp> // Including tree_order_statistics_node_update
 
using namespace std;
using namespace __gnu_pbds;
template<typename T> using ordered_set = tree<T, null_type, less<T>, rb_tree_tag, tree_order_statistics_node_update>;
 
//#define int ll

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

void fileIO(string s) {
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

bool comp(array<int, 3> a, array<int, 3> b) {
    return a[0] == b[0] ? a[1] < b[1] : a[0] < b[0];
}

signed main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0);
//    fileIO("sleepy");
    int n; cin >> n;
    int s[n];
    for(int i = 0; i < n; ++i) {
        cin >> s[i];
    }
    int t[n];
    for(int i = 0; i < n; ++i) {
        cin >> t[i];
    }
    sort(s, s + n);
    sort(t, t + n);
    int dp[2 * n + 1][n + 1][2]; //ways in first i events, j cows to cover, k = 0 if removed any, 1 if not
    memset(dp, 0, sizeof dp);
    vector<array<int, 3>> ev;
    for(int j = 0; j < n; ++j) {
        ev.pb({s[j], 0, j});
        ev.pb({t[j], 1, j});
    }
    dp[0][0][0] = 1;
    sort(all(ev), comp);
    int id = 1;
    for(auto it : ev) {
        int sz = it[0], tp = it[1], ind = it[2];
        if(tp == 0) {
            //include cow
            for(int i = 1; i <= n; ++i) {
                dp[id][i][0] = add(dp[id][i][0], dp[id - 1][i - 1][0]);
                dp[id][i][1] = add(dp[id][i][1], dp[id - 1][i - 1][1]);
            }
            //dont include cow
            for(int i = 0; i <= n; ++i) {
                //first time
                dp[id][i][1] = add(dp[id][i][1], dp[id - 1][i][0]);
                //>= 2nd time
                dp[id][i][1] = add(dp[id][i][1], dp[id - 1][i][1]);
            }
        } else {
            //include barn
            for(int i = 0; i < n; ++i) {
                dp[id][i][0] = add(dp[id][i][0], mult(i + 1, dp[id - 1][i + 1][0]));
                dp[id][i][1] = add(dp[id][i][1], mult(i + 1, dp[id - 1][i + 1][1]));
            }
            //dont include barn
            for(int i = 0; i <= n; ++i) {
                dp[id][i][0] = add(dp[id][i][0], dp[id - 1][i][0]);
            }
        }
        ++id;
    }
    cout << add(dp[2 * n][0][0], dp[2 * n][0][1]) << "\n";
}
