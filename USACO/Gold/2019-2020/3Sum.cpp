#include "bits/stdc++.h"
#define fast ios_base::sync_with_stdio(0) , cin.tie(0) , cout.tie(0)
#define endl '\n'
using namespace std;
  
const int mx = 1e6 + 1;
int a[5005];
long long dp[5005][5005];
int freq[2000005];
 
signed main()
{
    freopen("threesum.in", "r", stdin); freopen("threesum.out", "w", stdout);
    fast;
    int n, q;
    cin >> n >> q;
    for(int i = 0; i < n; i++) cin >> a[i];
    //base case
    for(int i = 0; i < n-2; i++){
        if(a[i] + a[i+1] + a[i+2] == 0) dp[i][i+2] = 1;
        else dp[i][i+2] = 0;
    }
    //transitions
    for(int i = n - 1; i >= 0; --i) {
        for(int j = i + 1; j < n; ++j) {
            dp[i][j] = dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1];
            int ind = mx - a[i] - a[j];
            if(ind >= 0 && ind <= 2000000) {
                dp[i][j] += freq[(0 - a[i] - a[j]) + mx];
            }
            freq[a[j] + mx]++;
        }
        for(int j = i + 1; j < n; ++j) {
            freq[a[j] + mx]--;
        }
    }
    // for(int d = 3; d < n; d++){
    //     for(int i = 1; i < d; i++) freq[a[i] + mx]++;
    //     for(int i = 0; i + d < n; i++){
    //         int j = i + d;
    //         dp[i][j] = dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1];
    //         int ind = mx - a[i] - a[j];
    //         if(ind >= 0 && ind <= 2000000) {
    //             dp[i][j] += freq[(0 - a[i] - a[j]) + mx];
    //         }
    //         freq[a[i+1] + mx]--;
    //         freq[a[j] + mx]++;
    //     }
    //     for(int i = n-d; i < n; i++) freq[a[i] + mx] = 0;
    // }
    //ans case
    for(int i = 0; i < q; i++){
        int x, y;
        cin >> x >> y;
        x--, y--;
        cout << dp[x][y] << endl;
    }
}