//TLE on all, dont know why
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

template<int MOD, int RT> struct mint {
    static const int mod = MOD;
    static constexpr mint rt() { return RT; } // primitive root for FFT
    int v; explicit operator int() const { return v; } // explicit -> don't silently convert to int
    mint() { v = 0; }
    mint(ll _v) { v = int((-MOD < _v && _v < MOD) ? _v : _v % MOD);
        if (v < 0) v += MOD; }
    friend bool operator==(const mint& a, const mint& b) { 
        return a.v == b.v; }
    friend bool operator!=(const mint& a, const mint& b) { 
        return !(a == b); }
    friend bool operator<(const mint& a, const mint& b) { 
        return a.v < b.v; }
    mint& operator+=(const mint& m) { 
        if ((v += m.v) >= MOD) v -= MOD; 
        return *this; }
    mint& operator-=(const mint& m) { 
        if ((v -= m.v) < 0) v += MOD; 
        return *this; }
    mint& operator*=(const mint& m) { 
        v = (ll)v*m.v%MOD; return *this; }
    mint& operator/=(const mint& m) { return (*this) *= inv(m); }
    friend mint pow(mint a, ll p) {
        mint ans = 1; //assert(p >= 0);
        for (; p; p /= 2, a *= a) if (p&1) ans *= a;
        return ans; }
    friend mint inv(const mint& a) { //assert(a.v != 0); 
        return pow(a,MOD-2); }
        
    mint operator-() const { return mint(-v); }
    mint& operator++() { return *this += 1; }
    mint& operator--() { return *this -= 1; }
    friend mint operator+(mint a, const mint& b) { return a += b; }
    friend mint operator-(mint a, const mint& b) { return a -= b; }
    friend mint operator*(mint a, const mint& b) { return a *= b; }
    friend mint operator/(mint a, const mint& b) { return a /= b; }
};

typedef mint<MOD, 5> T;
typedef vector<vector<T>> Mat;

Mat makeMat(int r, int c) { return Mat(r,vector<T>(c));}
Mat operator*(const Mat& a, const Mat& b) {
    int x = a.size(), y = a[0].size(), z = b[0].size(); 
    //assert(y == b.size()); 
    Mat c = makeMat(x,z);
    for(int i = 0; i < x; ++i) {
        for(int j = 0; j < y; ++j) {
            for(int k = 0; k < z; ++k) {
                c[i][k] += a[i][j] * b[j][k];
            }
        }
    }
    return c;
}
Mat& operator*=(Mat& a, const Mat& b) { return a = a*b; }

int N, K;
int A[mxN];

Mat st[mxN << 2];

Mat merge(Mat& a, Mat& b){
    Mat c = a * b;
    return c;
}

void build(int nd, int l, int r){
    if(l == r){
        st[nd] = makeMat(K, K);
        for(int i = 0; i < K; ++i) {
            st[nd][i][i] = 1;
        }
        int cur = A[l];
        for(int i = 0; i <= cur; ++i) {
            ++st[nd][i][cur];
        }
        return;
    }
    int m = (l + r) / 2;
    build(nd * 2 + 1, l, m);
    build(nd * 2 + 2, m + 1, r);
    st[nd] = merge(st[nd * 2 + 1], st[nd * 2 + 2]);
}

Mat ans;

void query(int nd, int l, int r, int L, int R){
    if(r < L || R < l) return;
    if(R == r && l == L){
        ans *= st[nd];
        return;
    }
    int m = (l + r) / 2;
    query(nd * 2 + 1, l, m, L, min(R, m));
    query(nd * 2 + 2, m + 1, r, max(L, m + 1), R);
}

signed main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    fileIO("nondec");
    cin >> N >> K;
    for(int i = 0; i < N; ++i) {
        cin >> A[i]; --A[i];
    }
    build(0, 0, N - 1);
    int Q; cin >> Q;
    while(Q--) {
        int l, r; cin >> l >> r; --l; --r;
        ans = makeMat(1, K);
        ans[0][0] = 1;
        query(0, 0, N - 1, l, r);
        T ret = 0;
        for(int i = 0; i < K; ++i) {
            ret += ans[0][i];
        }
        cout << ret.v << "\n";
    }
}
