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

int calculate(vector<int>& arr, int st, int ed) {
	int N = arr.size();
	if(arr.size() == 0) return 1;
	for(int i = 0; i < N - 1; ++i) {
		if(arr[i] == arr[i + 1] && arr[i] != 4) return 0;
	}
	int dp[N][4]; //ways to create string if last is j
	memset(dp, 0, sizeof dp);
	for(int i = 0; i < 4; ++i) {
		if((i != arr[0] && arr[0] != 4) || i == st) continue;
		dp[0][i] = 1;
	}
	for(int i = 1; i < N; ++i) {
		if(arr[i] == 4) {
			for(int j = 0; j < 4; ++j) {
				for(int k = 0; k < 4; ++k) {
					if(j == k) continue;
					dp[i][j] = add(dp[i][j], dp[i - 1][k]);
				}
			}
		} else {
			int cur = arr[i];
			for(int j = 0; j < 4; ++j) {
				if(cur == j) continue;
				dp[i][cur] = add(dp[i][cur], dp[i - 1][j]);
			}
		}
	}
	int ret = 0;
	for(int i = 0; i < 4; ++i) {
		if(i == ed) continue;
		ret = add(ret, dp[N - 1][i]);
	}
	return ret;
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
    int dp[N + 1][4];
    memset(dp, 0, sizeof dp);
    for(int i = 0; i < 4; ++i) {
    	dp[0][i] = 1;
    }
    for(int i = 1; i <= N; ++i) {
    	int cur = a[i - 1];
    	//only flip current letter
    	for(int j = 0; j < 4; ++j) {
    		if(j != cur && cur != 4) continue;
    		dp[i][j] = add(dp[i][j], dp[i - 1][j]);
    	}
    	//flip more
    	for(int j = i - 1; j >= 1; --j) {
    		//flip from j to i
    		int cur2 = a[j - 1];
    		vector<int> mid;
    		for(int k = j + 1; k <= i - 1; ++k) {
    			mid.push_back(a[k - 1]);
    		}
    		for(int tpi = 0; tpi < 4; ++tpi) { //types of i
    			if(tpi != cur && cur != 4) continue;
    			for(int tpj = 0; tpj < 4; ++tpj) { //types of j
                    if(j == i - 1 && tpi == tpj) continue;
    				if(tpj != cur2 && cur2 != 4) continue;
                    int mlt = calculate(mid, tpj, tpi);
    				dp[i][tpj] = add(dp[i][tpj], mult(mlt, dp[j - 1][tpi]));
    			}
    		}
    	}
    }
    int ans = 0;
    for(int i = 0; i < 4; ++i) {
    	ans = add(ans, dp[N][i]);
    }
    cout << ans << "\n";
}
