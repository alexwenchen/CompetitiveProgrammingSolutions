#include "combo.h"
using namespace std;
#include <string>
#include <vector>
#include <set>

string guess_sequence(int N) {
    string ans = "";
    set<string> strs = {"A", "B", "X", "Y"};
    int ab = press("AB");
    if(ab >= 1){
        int a = press("A");
        if(a == 1){
            ans = "A";
        }
        else ans = "B";
    }
    else{
        int x = press("X");
        if(x == 1){
            ans = "X";
        }
        else ans = "Y";
    }
    if(N == 1) return ans;
    strs.erase(ans);
    vector<string> left;
    for(string s : strs){
        left.emplace_back(s);
    }
    for(int i = 1; i < N - 1; ++i){
        int cur = press(ans + left[1] + ans + left[2] + left[0] + ans + left[2] + left[1] + ans + left[2] + left[2]);
        if(cur == i){
            ans += left[0];
        }
        else if(cur == i + 1){
            ans += left[1];
        }
        else ans += left[2];
    }
    int fst = press(ans + left[0] + ans + left[1]);
    if(fst == N){
        int zro = press(ans + left[0]);
        if(zro == N){
            ans += left[0];
        }
        else ans += left[1];
    }
    else ans += left[2];
    return ans;
}
