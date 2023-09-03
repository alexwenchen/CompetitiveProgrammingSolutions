//solution credits: shiv
#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
using namespace std;
 
struct d_word {
    string s;
    int i;
};
 
bool by_s(d_word a, d_word b) {
    return a.s < b.s;
}
 
int main() {
    ifstream fin("auto.in");
    ofstream fout("auto.out");
 
    int W, N;
    fin >> W >> N;
    d_word words[W];
    for (int i = 0; i < W; i++) {
        fin >> words[i].s;
        words[i].i = i + 1;
    }
    sort(words, words + W, by_s);
    for (int i = 0; i < N; i++) {
        int nth_perm;
        string pref;
        fin >> nth_perm >> pref;
        int low = 0, high = W - 1;
        while (low != high) {
            int mid = (low + high) / 2;
            if (words[mid].s.substr(0, pref.size()) < pref) {
                low = mid + 1;
            } else {
                high = mid;
            }
            // if (words[mid].s.substr(0, pref.size()) == pref) {
            //     high = mid;
            // } else {
            //     if (words[mid].s.substr(0, pref.size()) < pref) {
            //         low = mid + 1;
            //     } else {
            //         high = mid - 1;
            //     }
            // }
        }
        if (!(words[low].s.rfind(pref, 0) == 0)) {
            fout << "-1\n";
            continue;
        }
        if (low + nth_perm - 1 < W && words[low + nth_perm - 1].s.rfind(pref, 0) == 0) {
            fout << words[low + nth_perm - 1].i << "\n";
        } else {
            fout << "-1\n";
        }
    }
    return 0;
}
