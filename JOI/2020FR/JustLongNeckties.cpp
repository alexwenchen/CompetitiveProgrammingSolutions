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
    int N; cin >> N;
    pii A[N + 1];
    int B[N];
    for(int i = 0; i <= N; ++i){
        cin >> A[i].x;
        A[i].y = i;
    }
    for(int i = 0; i < N; ++i){
        cin >> B[i];
    }
    sort(A, A + N + 1);
    sort(B, B + N);
    int ans[N + 1], dif1[N], dif2[N], suffmx[N], prefmx[N];
    fill(ans, ans + N + 1, 0);
    for(int i = 0; i < N; ++i) {
        dif1[i] = max(A[i].x - B[i], 0);
        dif2[i] = max(A[i + 1].x - B[i], 0);
    }
    for(int i = 0; i < N; ++i) {
        prefmx[i] = dif1[i];
        if(i > 0) prefmx[i] = max(prefmx[i - 1], prefmx[i]);
    }
    for(int i = N - 1; i >= 0; --i) {
        suffmx[i] = dif2[i];
        if(i < N - 1) suffmx[i] = max(suffmx[i], suffmx[i + 1]);
    }
    for(int i = 0; i <= N; ++i) {
        if(i > 0) ans[A[i].y] = prefmx[i - 1];
        if(i < N) ans[A[i].y] = max(ans[A[i].y], suffmx[i]);
    }
    for(int i = 0; i <= N; ++i) {
        cout << ans[i] << " ";
    }
    cout << "\n";
}
