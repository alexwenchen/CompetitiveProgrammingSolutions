#include "molecules.h"
#include <bits/stdc++.h>
using namespace std;

vector<int> find_subset(int l, int u, std::vector<int> w) {
    int n = w.size();
    vector<int> ans;
    vector<pair<int, int>> mlcs;
    for(int i = 0; i < n; ++i){
        mlcs.emplace_back(make_pair(w[i], i));
    }
    sort(mlcs.begin(), mlcs.end());
    int p1 = 0, p2 = 0;
    long long cursum = 0;
    bool poss = false;
    while(p2 < n){
        cursum += mlcs[p2++].first;
        while(cursum > u){
            cursum -= mlcs[p1++].first;
        }
        if(cursum >= l && cursum <= u){
            poss = true;
            break;
        }
    }
    if(!poss) return ans;
    for(int i = p1; i < p2; ++i){
        ans.emplace_back(mlcs[i].second);
    }
    sort(ans.begin(), ans.end());
    return ans;
}
