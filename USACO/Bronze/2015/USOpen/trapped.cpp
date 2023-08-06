#include <bits/stdc++.h>
using namespace std;
 
#define int long long
#define all(x) (x).begin(),(x).end()
#define pb push_back

void setIO(string s) {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

signed main(){
    setIO("trapped");

    int n; cin >> n;
    array<int, 2> bales[n];
    for(int i = 0; i < n; ++i) {
        cin >> bales[i][1] >> bales[i][0];
    }
    sort(bales, bales + n);
    int res = 0;
    for(int i = 0; i < n; ++i) {
        if(i == 0) {
            continue;
        }
        int ni = i;
        int size = bales[i][1], pos = bales[i][0];
        int originalsize = bales[i][1], originalpos = bales[i][0];
        int othersize = bales[i - 1][1];
        int otherpos = bales[i - 1][0];
        int originalothersize = bales[i - 1][1], originalotherpos = bales[i - 1][0];
        int othern = i - 1;
        bool breakfree = false;
        while(1) {
            bool change = false;
            int momentum = pos - otherpos;
            if(size < momentum) {
                ni++;
                change = true;
            }
            if(othersize < momentum) {
                othern--;
                change = true;
            }
            if(othern == -1 || ni == n) {
                breakfree = true;
                break;
            }
            if(!change) {
                breakfree = false;
                break;
            }
            othersize = bales[othern][1];
            otherpos = bales[othern][0];
            size = bales[ni][1];
            pos = bales[ni][0];
        }
        if(!breakfree) {
            res += originalpos - originalotherpos;
        }
    }
    cout << res << "\n";
}
