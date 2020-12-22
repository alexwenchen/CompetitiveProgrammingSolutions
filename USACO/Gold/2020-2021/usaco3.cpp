#include <bits/stdc++.h>
using namespace std;

bool comp(array<int, 2> a, array<int, 2> b) {
    return a[0] < b[0];
}

bool comp2(array<int, 2> a, array<int, 2> b) {
    return a[1] < b[1];
}

signed main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    int N; cin >> N;
    vector<array<int, 2>> pnts;
    for(int i = 0; i < N; ++i) {
        int x, y; cin >> x >> y;
        pnts.push_back({x, y});
    }
    sort(pnts.begin(), pnts.end(), comp);
    //horizontal
    vector<array<int, 4>> possib;
    for(int i = 0; i < N; ++i) {
        vector<int> cur;
        vector<int> cpnts;
        cpnts.push_back(pnts[i][1]);
        cur.push_back(pnts[i][1] - 1);
        cur.push_back(pnts[i][1]);
        cur.push_back(pnts[i][1] + 1);
        for(int j = i + 1; j < N; ++j) {
            cpnts.push_back(pnts[j][1]);
            cur.push_back(pnts[j][1]);
            cur.push_back(pnts[j][1] + 1);
            cur.push_back(pnts[j][1] - 1);
            if(abs(pnts[j][1] - pnts[i][1]) > pnts[j][0] - pnts[i][0]) continue; //if y difference is bigger, continue
            sort(cpnts.begin(), cpnts.end());
            sort(cur.begin(), cur.end());
            cur.erase(unique(cur.begin(), cur.end()), cur.end());
            int lx = pnts[i][0], rx = pnts[j][0]; //lower and upper x
            int xdiff = rx - lx + 1;
            int mny = min(pnts[i][1], pnts[j][1]);
            int mxy = max(pnts[i][1], pnts[j][1]);
            for(int k : cur) {
                if(k >= mxy && k - xdiff + 1 <= mny) {
                    int mx = 0, mn = 1e9;
                    int lb = k - xdiff + 1;
                    int left = 0, right = cpnts.size() - 1;
                    while(left < right) {
                        int mid = left + (right - left + 1) / 2;
                        if(cpnts[mid] > k) {
                            right = mid - 1;
                        } else left = mid;
                    }
                    mx = max(mx, cpnts[left]);
                    left = 0, right = cpnts.size() - 1;
                    while(left < right) {
                        int mid = left + (right - left) / 2;
                        if(cpnts[mid] < lb) {
                            left = mid + 1;
                        } else right = mid;
                    }
                    mn = min(mn, cpnts[left]);
                    possib.push_back({lx, rx, mn, mx});
                } else if(k <= mny && k + xdiff - 1 >= mxy) {
                    int ub = k + xdiff - 1;
                    int mx = 0, mn = 1e9;
                    int left = 0, right = cpnts.size() - 1;
                    while(left < right) {
                        int mid = left + (right - left + 1) / 2;
                        if(cpnts[mid] > ub) {
                            right = mid - 1;
                        } else left = mid;
                    }
                    mx = max(mx, cpnts[left]);
                    left = 0, right = cpnts.size() - 1;
                    while(left < right) {
                        int mid = left + (right - left) / 2;
                        if(cpnts[mid] < k) {
                            left = mid + 1;
                        } else right = mid;
                    }
                    mn = min(mn, cpnts[left]);
                    possib.push_back({lx, rx, mn, mx});
                }
            }
        }
    }
    sort(pnts.begin(), pnts.end(), comp2);
    //vertical
    int dupes = 0;
    for(int i = 0; i < N; ++i) {
        vector<int> cur;
        vector<int> cpnts;
        cpnts.push_back(pnts[i][0]);
        cur.push_back(pnts[i][0]);
        cur.push_back(pnts[i][0] - 1);
        cur.push_back(pnts[i][0] + 1);
        for(int j = i + 1; j < N; ++j) {
            cpnts.push_back(pnts[j][0]);
            cur.push_back(pnts[j][0]);
            cur.push_back(pnts[j][0] - 1);
            cur.push_back(pnts[j][0] + 1);
            if(abs(pnts[j][0] - pnts[i][0]) >= pnts[j][1] - pnts[i][1]) continue; //if x difference is >=, continue
            sort(cpnts.begin(), cpnts.end());
            sort(cur.begin(), cur.end());
            cur.erase(unique(cur.begin(), cur.end()), cur.end());
            int ly = pnts[i][1], ry = pnts[j][1]; //lower and upper y
            int ydiff = ry - ly + 1;
            int mnx = min(pnts[i][0], pnts[j][0]);
            int mxx = max(pnts[i][0], pnts[j][0]);
            for(int k : cur) {
                if(k >= mxx && k - ydiff + 1 <= mnx) {
                    int lb = k - ydiff + 1;
                    int mx = 0, mn = 1e9;
                    int left = 0, right = cpnts.size() - 1;
                    while(left < right) {
                        int mid = left + (right - left + 1) / 2;
                        if(cpnts[mid] > k) {
                            right = mid - 1;
                        } else left = mid;
                    }
                    mx = max(mx, cpnts[left]);
                    left = 0, right = cpnts.size() - 1;
                    while(left < right) {
                        int mid = left + (right - left) / 2;
                        if(cpnts[mid] < lb) {
                            left = mid + 1;
                        } else right = mid;
                    }
                    mn = min(mn, cpnts[left]);
                    possib.push_back({mn, mx, ly, ry});
                } else if(k <= mnx && k + ydiff - 1 >= mxx) {
                    int ub = k + ydiff - 1;
                    int mx = 0, mn = 1e9;
                    int left = 0, right = cpnts.size() - 1;
                    while(left < right) {
                        int mid = left + (right - left + 1) / 2;
                        if(cpnts[mid] > ub) {
                            right = mid - 1;
                        } else left = mid;
                    }
                    mx = max(mx, cpnts[left]);
                    left = 0, right = cpnts.size() - 1;
                    while(left < right) {
                        int mid = left + (right - left) / 2;
                        if(cpnts[mid] < k) {
                            left = mid + 1;
                        } else right = mid;
                    }
                    mn = min(mn, cpnts[left]);
                    possib.push_back({mn, mx, ly, ry});
                }
            }
        }
    }
    sort(possib.begin(), possib.end());
    possib.erase(unique(possib.begin(), possib.end()), possib.end());
    cout << possib.size() + N + 1 << "\n";
}