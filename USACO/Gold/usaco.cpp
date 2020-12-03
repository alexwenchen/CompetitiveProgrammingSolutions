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

int N, a[200000];
array<int, 2> inv;
int amt[2][2];
set<int> inds[2][2];

void setup(){
    inv[0] = inv[1] = 0;
    memset(amt, 0, sizeof amt);
    for(int i = 0; i < 2; ++i) {
        for(int j = 0; j < 2; ++j) {
            inds[i][j].clear();
        }
    }
    int cnt = 0;
    for(int i = 0; i < N; ++i) {
        if(a[i]) ++cnt;
        else inv[0] += cnt;
        ++amt[0][a[i]];
        inds[0][a[i]].insert(i);
    }
    cnt = 0;
    for(int i = N; i < 2 * N; ++i) {
        if(a[i]) ++cnt;
        else inv[1] += cnt;
        ++amt[1][a[i]];
        inds[1][a[i]].insert(i - N);
    }
}

signed main(){
    setIO("balance");
//    setIO2();
    cin >> N;
    for(int i = 0; i < 2 * N; ++i) {
        cin >> a[i];
    }
    setup();
    int ans = abs(inv[0] - inv[1]);
    //0:1 -> 1:0 swaps at middle only
    //decreases left by # of 1s, decreases right by # of 0s
    
    int sofar = 0, moves = 0;
    while(inds[0][0].size() && inds[1][1].size()) {
        int c0 = *prev(inds[0][0].end()), c1 = *inds[1][1].begin();
        int invl = amt[0][1] - (N - 1 - c0 - sofar), invr = amt[1][0] - (c1 - sofar);
        moves += N - 1 - c0 + c1 + 1;
        inv[0] -= invl, inv[1] -= invr;
        ans = min(ans, moves + abs(inv[0] - inv[1]));
        inds[0][0].erase(c0); inds[1][1].erase(c1);
        ++sofar;
    }
    setup();
    //1:0 -> 0:1 swaps at middle only
    //decreases left by increase left by # of 1s, increase right by # of 0s
    sofar = moves = 0;
    while(inds[0][1].size() && inds[1][0].size()) {
        int c1 = *prev(inds[0][1].end()), c0 = *inds[1][0].begin();
        int invl = N - 1 - c1, invr = c0;
        moves += N - 1 - c1 + c0 + 1;
        --amt[0][1]; --amt[1][0];
        inv[0] -= invl - amt[0][1], inv[1] -= invr - amt[1][0];
        ans = min(ans, moves + abs(inv[0] - inv[1]));
        inds[0][1].erase(c1); inds[1][0].erase(c0);
        ++sofar;
    }
    cout << ans << "\n";
}