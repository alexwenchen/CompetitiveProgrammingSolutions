#include <bits/stdc++.h>
using namespace std;
 
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

const int mxN = 50001;
const int mxQ = 200001;

int N, K, A[mxN], Q;
array<int, 2> qs[mxQ];
int ans[mxQ];

int lf[mxN][20], rt[mxN][20];
int amtl[mxN][20], amtr[mxN][20];

void solve(int l, int r) {
    if(l == r) {
        for(int i = 0; i < Q; ++i) {
            if(ans[i] != -1) continue;
            if(qs[i][0] == qs[i][1] && qs[i][0] == l) {
                ans[i] = 2;
            }
        }
        return;
    }
    int m = (l + r) / 2;
    vector<int> curq;
    for(int i = 0; i < Q; ++i) {
        if(ans[i] != -1) continue;
        if(qs[i][0] <= m && qs[i][1] > m) {
            curq.eb(i);
        }
    }
    bool vis[K];
    fill(vis, vis + K, false);
    for(int i = m; i >= l; --i) {
        int cur = A[i];
        if(vis[cur]) continue;
        vis[cur] = true;
        lf[i][cur] = 1;
        amtl[i][cur] = 1;
        for(int j = i - 1; j >= l; --j) {
            int val = A[j];
            for(int k = cur; k >= 0; --k) {
                lf[j][k] = lf[j + 1][k];
            }
            if(val == cur) ++lf[j][cur];
            if(val <= cur) {
                for(int k = val; k < K; ++k) {
                    lf[j][val] = add(lf[j][val], lf[j + 1][k]);
                }
            }
            for(int k = 0; k < K; ++k) {
                amtl[j][cur] = add(amtl[j][cur], lf[j][k]);
            }
        }
        for(int j = i; j >= l; --j) {
            for(int k = 0; k < K; ++k) {
                lf[j][k] = 0;
            }
        }
    }
    fill(vis, vis + K, false);
    for(int i = m + 1; i <= r; ++i) {
        int cur = A[i];
        if(vis[cur]) continue;
        vis[cur] = true;
        rt[i][cur] = 1;
        amtr[i][cur] = 1;
        for(int j = i + 1; j <= r; ++j) {
            int val = A[j];
            for(int k = cur; k < K; ++k) {
                rt[j][k] = rt[j - 1][k];
            }
            if(val == cur) ++rt[j][cur];
            if(val >= cur){
                for(int k = val; k >= 0; --k) {
                    rt[j][val] = add(rt[j][val], rt[j - 1][k]);
                }
            }
            for(int k = 0; k < K; ++k) {
                amtr[j][cur] = add(amtr[j][cur], rt[j][k]);
            }
        }
        for(int j = i; j <= r; ++j) {
            for(int k = 0; k < K; ++k) {
                rt[j][k] = 0;
            }
        }
    }
    for(int i : curq) {
        int lb = qs[i][0], rb = qs[i][1];
        int suff[K];
        for(int j = K - 1; j >= 0; --j) {
            suff[j] = amtr[rb][j];
            if(j < K - 1) {
                suff[j] = add(suff[j], suff[j + 1]);
            }
        }
        int curans = 0;
        for(int j = 0; j < K; ++j) {
            curans = add(curans, mult(amtl[lb][j], suff[j] + 1));
        }
        curans = add(curans, suff[0] + 1);
        ans[i] = curans;
    }
    for(int i = l; i <= r; ++i) {
        for(int k = 0; k < K; ++k) {
            rt[i][k] = lf[i][k] = 0;
            amtl[i][k] = amtr[i][k] = 0;
        }
    }
    solve(l, m);
    solve(m + 1, r);
}

signed main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    //fileIO("nondec");
    cin >> N >> K;
    for(int i = 0; i < N; ++i) {
        cin >> A[i]; --A[i];
    }
    cin >> Q;
    fill(ans, ans + Q, -1);
    for(int i = 0; i < Q; ++i) {
        int l, r; cin >> l >> r; --l; --r;
        qs[i] = {l, r};
        if(l == r) {
            ans[i] = 2;
        }
    }
    solve(0, N - 1);
    for(int i = 0; i < Q; ++i) {
        cout << ans[i] << "\n";
    }
}
