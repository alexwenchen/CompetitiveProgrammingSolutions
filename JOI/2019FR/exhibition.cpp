#include <bits/stdc++.h>
//#include <bits/extc++.h>
//#include <ext/pb_ds/assoc_container.hpp> // Common file
//#include <ext/pb_ds/tree_policy.hpp> // Including tree_order_statistics_node_update

using namespace std;
//using namespace __gnu_pbds;
//template<typename T> using ordered_set = tree<T, null_type, less<T>, rb_tree_tag, tree_order_statistics_node_update>;

using ll = long long;
using pii = pair<int, int>;
using pll = pair<ll, ll>;
using ull = unsigned ll;
 
#define pb push_back
#define mp make_pair
#define eb emplace_back
#define all(x) (x).begin(),(x).end()
#define x first
#define y second

//const int MOD = 1e9 + 7;
const int MOD = 998244353;
const int dx[] = {0, 0, 1, -1};
const int dy[] = {1, -1, 0, 0}; 
const char dir[] = {'R', 'L', 'D', 'U'};
 
int add(int a, int b){ //(a + b) % 1e9 + 7
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
    return ((ll) a * b) % MOD;
}

void setIO() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
//    freopen((s+".in").c_str(),"r",stdin);
//    freopen((s+".text").c_str(),"w",stdout);
}

bool comp(pii a, pii b){
	return a.y == b.y ? a.x < b.x : a.y < b.y;
}

int main(){
    setIO();
    //CHECK FOR LONG LONG!!!
    //LONG LONG OVERFLOW??
    int n, m; cin >> n >> m;
    pii pics[n];
    int frames[m];
    int mnsz = 1e9 + 1;
    for(int i = 0; i < n; ++i){
    	cin >> pics[i].x >> pics[i].y;
    	mnsz = min(mnsz, pics[i].x);
    }
    int mx = 0;
    for(int i = 0; i < m; ++i){
    	cin >> frames[i];
    	mx = max(mx, frames[i]);
    }
    if(mx < mnsz){
    	cout << "0\n";
    	return 0;
    }
    sort(frames, frames + m);
    sort(pics, pics + n, comp);
    int l = 1, r = m;
    while(l < r){
    	int mid = l + (r - l + 1) / 2;
    	int p1 = 0, p2 = m - mid;
    	while(p1 < n && p2 < m){
    		if(pics[p1].x <= frames[p2]){
    			++p1; ++p2;
    		}
    		else{
    			++p1;
    		}
    	}
    	if(p2 == m){
    		l = mid;
    	}
    	else{
    		r = mid - 1;
    	}
    }
    cout << l << "\n";
    //CHECK FOR LONG LONG!!!
}
