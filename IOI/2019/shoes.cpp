//#include "shoes.h"
#include <bits/stdc++.h>
using namespace std;
 
const int mxN = 2e5;
int tree[mxN + 5];
void update(int index, int val) {
    index++;
    while(index < mxN + 5) {
        tree[index] += val;
        index += index & -index;
    }
}
int query(int index) {
    int ret = 0;
    index++;
    while(index > 0) {
        ret += tree[index];
        index -= index & -index;
    }
    return ret;
}
int rquery(int a, int b) {
    if(b < a) return 0;
    return query(b)-query(a-1);
}
 
long long count_swaps(vector<int> s) {
    int n = s.size() / 2;
    map<int, set<int>> inds;
    for(int i = 0; i < 2 * n; ++i){
        inds[s[i]].insert(i);
    }
    for(int i = 0; i < 2 * n; ++i){
        update(i, 1);
    }
    bool left[2 * n];
    fill(left, left + 2 * n, true);
    long long ans = 0;
    for(int i = 0; i < 2 * n; ++i){
        if(!left[i]) continue;
        int cur = s[i];
        int first = *inds[-cur].begin();
        left[first] = false;
        ans += rquery(i + 1, first - 1);
        if(cur > 0) ++ans;
        update(first, -1);
        inds[-cur].erase(inds[-cur].begin());
        inds[cur].erase(i);
    }
    return ans;
}
