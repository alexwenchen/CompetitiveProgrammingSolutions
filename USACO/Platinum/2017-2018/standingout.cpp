//java version wasn't a complete solve, realsolved this time

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

struct s_a {
    vector<int> sa, lcp;
    s_a(){};
    s_a(string s) {
        suffix_array(s);
        lcp_construction(s, sa);
    }
    void suffix_array(string S) {
        int N = S.size();
        sa.resize(N);
        vector<int> classes(N);
        for (int i = 0; i < N; i++) {
            sa[i] = N - 1 - i;
            classes[i] = S[i];
        }
        stable_sort(sa.begin(), sa.end(), [&S](int i, int j) {
            return S[i] < S[j];
        });
        for (int len = 1; len < N; len *= 2) {
            vector<int> c(classes);
            for (int i = 0; i < N; i++) {
                bool same = i && sa[i - 1] + len < N
                              && c[sa[i]] == c[sa[i - 1]]
                              && c[sa[i] + len / 2] == c[sa[i - 1] + len / 2];
                classes[sa[i]] = same ? classes[sa[i - 1]] : i;
            }
            vector<int> cnt(N), s(sa);
            for (int i = 0; i < N; i++)
                cnt[i] = i;
            for (int i = 0; i < N; i++) {
                int s1 = s[i] - len;
                if (s1 >= 0)
                    sa[cnt[classes[s1]]++] = s1;
            }
        }
    }

    void lcp_construction(string const& s, vector<int> const& p) {
        int n = s.size();
        lcp.resize(n);
        vector<int> rank(n, 0);
        for (int i = 0; i < n; i++)
            rank[p[i]] = i;
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] == n - 1) {
                k = 0;
                continue;
            }
            int j = p[rank[i] + 1];
            while (i + k < n && j + k < n && s[i+k] == s[j+k] && s[i + k] != '?' && s[j + k] != '?')
                k++;
            lcp[rank[i]] = k;
            if (k)
                k--;
        }
    }
};

int dmin[32][200001];
 
int getMin(int i, int j) {
    if(i==j) {
        return dmin[0][i];
    }
    int k = 31-__builtin_clz(j-i);
    return min(dmin[k][i], dmin[k][j-(1<<k)+1]);
}

signed main(){
    setIO("standingout");
//    setIO2();
    int n; cin >> n;
    string s[n];
    s_a sas[n];
    int startInd[n];
    vector<int> saInd;
    string allNames = "";
    for(int i = 0; i < n; ++i){
        cin >> s[i];
        sas[i] = s_a(s[i]);
        startInd[i] = allNames.size();
        allNames += s[i] + "?";
        for(int j = startInd[i]; j < allNames.size(); ++j) {
            saInd.eb(i);
        }
    }
    s_a all = s_a(allNames);
    int N = allNames.size();
    for(int i = 0; i < N; ++i){
        dmin[0][i] = all.lcp[i];
    }
    for(int i = 1; 1<<i<=N; i++) {
        for(int j = 0; j+(1<<i)-1<N; j++) {
            dmin[i][j] = min(dmin[i-1][j], dmin[i-1][j+(1<<i-1)]);
        }
    }
    vector<int> lcpout(N);
    int ind = 0;
    while(ind < N) {
        if(allNames[all.sa[ind]] == '?') {
            ++ind; continue;
        }
        int st = ind, stind = saInd[all.sa[ind]];
        while(ind < N && saInd[all.sa[ind]] == stind) ++ind;
        int end = ind;
        if(end == N) break;
        for(int i = st; i < end; ++i) {
            lcpout[i] = getMin(i, end - 1);
        }
    }
    ind = N - 1;
    while(ind >= 0) {
        if(allNames[all.sa[ind]] == '?') break;
        int st = ind, stind = saInd[all.sa[ind]];
        while(ind >= 0 && saInd[all.sa[ind]] == stind) --ind;
        int end = ind;
        if(end < 0 || allNames[all.sa[end]] == '?') break;
        for(int i = st; i > end; --i) {
            lcpout[i] = max(lcpout[i], getMin(end, i - 1));
        }
    }
    vector<int> lcp(allNames.size());
    for(int i = 0; i < allNames.size(); ++i) {
        lcp[all.sa[i]] = lcpout[i];
    }
    for(int i = 0; i < n; ++i){
        int ans = 0;
        int sz = s[i].size();
        vector<int> clcp(sz);
        for(int j = 0; j < sz; ++j) {
            clcp[sas[i].sa[j]] = sas[i].lcp[j];
        }
        for(int j = 0; j < sz; ++j) {
            int curlcp = lcp[startInd[i] + j];
            int wordlcp = clcp[j];
            int left = sz - j;
            ans += left - max(curlcp, wordlcp);
        }
        cout << ans << "\n";
    }
}
