#include <bits/stdc++.h>
using namespace std;

const int MOD = 1e9 + 7;

int add(int a, int b){
    a += b;
    if(a < 0){
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
    return a;
}
 
int mult(int a, int b){
    return ((long long) a * b) % MOD;
}

signed main() {
	ios_base::sync_with_stdio(0);
    cin.tie(0);
    string s; cin >> s;
    int N = s.length();
    vector<int> a;
    for(int i = 0; i < N; ++i) {
    	if(s[i] == 'A') {
    		a.push_back(0);
    	} else if(s[i] == 'C') {
    		a.push_back(1);
    	} else if(s[i] == 'G') {
    		a.push_back(2);
    	} else if(s[i] == 'T') {
    		a.push_back(3);
    	} else a.push_back(4);
    }
    //0: A, 1: C, 2: G, 3: T, 4: ?
    int dp[N][4][4][4];
    //first i letters, first of 2nd to last is j, first of last is k, last of last is l
    memset(dp, 0, sizeof dp);
    for(int i = 0; i < 4; ++i) {
        for(int j = 0; j < 4; ++j) {
            if(a[0] == j || a[0] == 4) {
                dp[0][i][j][j] = 1;
            }
        }
    }
    for(int i = 1; i < N; ++i) {
        for(int j = 0; j < 4; ++j) {
            for(int k = 0; k < 4; ++k) {
                for(int l = 0; l < 4; ++l) {
                    for(int m = 0; m < 4; ++m) {
                        if(a[i] != m && a[i] != 4) continue;
                        //first transition: extend old string by 1
                        if(m != l) dp[i][j][k][m] = add(dp[i][j][k][m], dp[i - 1][j][k][l]);
                        //second transition: create new string
                        if(j == l) dp[i][k][m][m] = add(dp[i][k][m][m], dp[i - 1][j][k][l]);
                    }
                }
            }
        }
    }
    int ans = 0;
    for(int i = 0; i < 4; ++i){
        for(int j = 0; j < 4; ++j) {
            ans = add(ans, dp[N - 1][i][j][i]);
        }
    }
    cout << ans << "\n";
}
