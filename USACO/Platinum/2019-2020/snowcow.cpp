//cheesed it before, this is realsolve
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

const int mxN = 100001;

int N, Q;
vector<int> adj[mxN];
int timer, tin[mxN], tout[mxN];

void euler(int v, int p) {
    tin[v] = timer++;
    for(int i : adj[v]) {
        if(i == p) continue;
        euler(i, v);
    }
    tout[v] = timer - 1;
}

struct LazySegmentTree {
    ll st[4 * mxN];
    ll lazy_sum[4 * mxN];
    ll lazy_set[4 * mxN];
    bool to_set[4 * mxN];
    
    LazySegmentTree(){
        fill(st, st + 4 * mxN, 0);
        fill(lazy_sum, lazy_sum + 4 * mxN, 0);
        fill(lazy_set, lazy_set + 4 * mxN, 0);
        fill(to_set, to_set + 4 * mxN, 0);
    }

    void push(int v, int l, int r) {
        if (l != r) {
            int m = (l + r) / 2;
            if (to_set[v]) {
                st[2 * v] = (m - l + 1) * lazy_set[v];
                st[2 * v + 1] = (r - m) * lazy_set[v];
                to_set[2 * v] = true;
                to_set[2 * v + 1] = true;
                lazy_set[2 * v] = lazy_set[v];
                lazy_set[2 * v + 1] = lazy_set[v];
                lazy_sum[2 * v] = 0;
                lazy_sum[2 * v + 1] = 0;
            }
            st[2 * v] += (m - l + 1) * lazy_sum[v];
            st[2 * v + 1] += (r - m) * lazy_sum[v];
            lazy_sum[2 * v] += lazy_sum[v];
            lazy_sum[2 * v + 1] += lazy_sum[v];
        }

        to_set[v] = false;
        lazy_sum[v] = 0;
        lazy_set[v] = 0;
    }

    void upd_sum(int v, int l, int r, int x, int y, ll n) {
        if (y < l || x > r) return;
        push(v, l, r);
        if (x <= l && r <= y) {
            lazy_sum[v] += n;
            st[v] += (r - l + 1) * n;
        } else {
            int m = (l + r) / 2;
            upd_sum(2 * v, l, m, x, y, n);
            upd_sum(2 * v + 1, m + 1, r, x, y, n);
            st[v] = st[2 * v] + st[2 * v + 1];
        }
    }

    void upd_set(int v, int l, int r, int x, int y, ll n) {
        if (y < l || x > r) return;
        push(v, l, r);
        if (x <= l && r <= y) {
            to_set[v] = true;
            lazy_set[v] = n;
            st[v] = (r - l + 1) * n;
        } else {
            int m = (l + r) / 2;
            upd_set(2 * v, l, m, x, y, n);
            upd_set(2 * v + 1, m + 1, r, x, y, n);
            st[v] = st[2 * v] + st[2 * v + 1];
        }
    }

    ll qry(int v, int l, int r, int x, int y) {
        if (y < l || x > r) return 0;
        if (x <= l && r <= y) {
            return st[v];
        } else {
            int m = (l + r) / 2;
            push(v, l, r);
            return qry(2 * v, l, m, x, y) + qry(2 * v + 1, m + 1, r, x, y);
        }
    }

    ll query(int v) {
        return qry(1, 0, N - 1, v, v);
    }

    ll rquery(int l, int r) {
        return qry(1, 0, N - 1, l, r);
    }

    void rupdate(int l, int r, int val) {
        upd_sum(1, 0, N - 1, l, r, val);
    }

    void rset(int l, int r, int val) {
        upd_set(1, 0, N - 1, l, r, val);
    }
};

signed main(){
    setIO("snowcow");
//    setIO2();
    cin >> N >> Q;
    for(int i = 0; i < N - 1; ++i) {
        int a, b; cin >> a >> b; --a; --b;
        adj[a].eb(b);
        adj[b].eb(a);
    }
    array<int, 3> qs[Q];
    vector<int> qcolor[mxN];
    for(int i = 0; i < Q; ++i) {
        int tp; cin >> tp;
        if(tp == 1) {
            int x, c; cin >> x >> c; --x;
            qs[i] = {tp, x, c};
            qcolor[c].eb(i);
        } else{
            int x; cin >> x; --x;
            qs[i] = {tp, x, -1};
        }
    }
    euler(0, -1);
    assert(timer == N);
    vector<int> remove[Q];
    bool skip[Q];
    fill(skip, skip + Q, false);
    LazySegmentTree lst;
    for(int i = 1; i < mxN; ++i) {
        if(qcolor[i].size() == 0) continue;
        for(int j : qcolor[i]) {
            int node = qs[j][1];
            if(lst.query(tin[node]) == 1) {
                skip[j] = true;
            } else{
                lst.rset(tin[node], tout[node], 1);
            }
        }
        lst.rset(0, N - 1, -1);
        for(int ind = qcolor[i].size() - 1; ind >= 0; --ind) {
            int j = qcolor[i][ind];
            if(skip[j]) continue;
            int node = qs[j][1];
            int qry = lst.query(tin[node]);
            if(qry != -1) {
                remove[qry].eb(j);
            }
            lst.rset(tin[node], tout[node], qcolor[i][ind]);
        }
        lst.rset(0, N - 1, 0);
    }
    for(int i = 0; i < Q; ++i) {
        if(skip[i]) continue;
        if(qs[i][0] == 1) {
            for(int j : remove[i]) {
                int node = qs[j][1];
                lst.rupdate(tin[node], tout[node], -1);
            }
            lst.rupdate(tin[qs[i][1]], tout[qs[i][1]], 1);
        } else {
            cout << lst.rquery(tin[qs[i][1]], tout[qs[i][1]]) << "\n";
        }
    }
}
