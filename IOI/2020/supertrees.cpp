#include "supertrees.h"
#include <vector>
#include <set>
const int mxN = 1000;

int parent[mxN];
int rnk[mxN];

int find(int x) 
{ 
    if(parent[x] == -1)
        parent[x] = x;
    if (parent[x] != x) { 
        parent[x] = find(parent[x]); 
    }
    return parent[x]; 
}
bool merge(int x, int y) 
{ 
    int xRoot = find(x), yRoot = find(y); 
    if (xRoot == yRoot) 
        return false; 
    if (rnk[xRoot] < rnk[yRoot]) 
        parent[xRoot] = yRoot; 
    else if (rnk[yRoot] < rnk[xRoot]) 
        parent[yRoot] = xRoot; 
    else 
    { 
        parent[yRoot] = xRoot; 
        rnk[xRoot] = rnk[xRoot] + 1; 
    } 
    return true;
} 


int construct(std::vector<std::vector<int>> p) {
    int n = p.size();
    std::vector<std::vector<int>> answer;
    for (int i = 0; i < n; i++) {
        std::vector<int> row;
        row.resize(n);
        answer.push_back(row);
    }
    bool zero = false, one = false, two = false, three = false;
    for(int i = 0; i < n; ++i){
        for(int j = i + 1; j < n; ++j){
            if(p[i][j] == 0) zero = true;
            else if(p[i][j] == 1) one = true;
            else if(p[i][j] == 2) two = true;
            else three = true;
        }
    }
    for(int i = 0; i < n; ++i){
        parent[i] = i;
    }
    if(!one && !three){
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(!p[i][j] && find(i) == find(j)){
                    return 0;
                }
                if(p[i][j]) merge(i, j);
            }
        }
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(!p[i][j] && find(i) == find(j)) return 0;
            }
        }
        std::vector<int> sets[n];
        for(int i = 0; i < n; ++i){
            int rep = find(i);
            sets[rep].emplace_back(i);
        }
        for(int i = 0; i < n; ++i){
            int sz = sets[i].size();
            if(sz == 2) return 0;
            for(int j = 0; j < sz - 1; ++j){
                answer[sets[i][j]][sets[i][j + 1]] = 1;
                answer[sets[i][j + 1]][sets[i][j]] = 1;
            }
            if(sz > 1){
                answer[sets[i][0]][sets[i][sz-1]] = 1;
                answer[sets[i][sz-1]][sets[i][0]] = 1;
            }
        }
    }
    else if(!two && !three){
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(!p[i][j] && find(i) == find(j)){
                    return 0;
                }
                if(p[i][j]) merge(i, j);
            }
        }
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(!p[i][j] && find(i) == find(j)) return 0;
            }
        }
        for(int i = 0; i < n; ++i){
            int rep = find(i);
            if(rep == i) continue;
            answer[rep][i] = 1;
            answer[i][rep] = 1;
        }
    }
    else if(!three){
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(!p[i][j] && find(i) == find(j)){
                    return 0;
                }
                if(p[i][j]) merge(i, j);
            }
        }
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(!p[i][j] && find(i) == find(j)) return 0;
            }
        }
        std::set<int> sets[n];
        for(int i = 0; i < n; ++i){
            int rep = find(i);
            sets[rep].insert(i);
        }
        std::set<int> trees[n];
        for(int rep = 0; rep < n; ++rep){
            if(sets[rep].size() < 2) continue;
            if(sets[rep].size() == 2){
                if(p[*sets[rep].begin()][*prev(sets[rep].end())] == 2) return 0;
            }
            std::vector<int> circle;
            std::set<int> remove;
            for(int i : sets[rep]){
                if(remove.find(i) != remove.end()) continue;
                for(int j = 0; j < n; ++j){
                    if(j == i) continue;
                    if(p[i][j] == 1){
                        answer[i][j] = 1;
                        answer[j][i] = 1;
                        remove.insert(j);
                        trees[i].insert(j);
                    }
                }
                trees[i].insert(i);
                circle.emplace_back(i);
            }
            int sz = circle.size();
            for(int i = 0; i < sz - 1; ++i){
                answer[circle[i]][circle[i + 1]] = 1;
                answer[circle[i + 1]][circle[i]] = 1;
            }
            if(sz > 1){
                answer[circle[0]][circle[sz-1]] = 1;
                answer[circle[sz-1]][circle[0]] = 1;
            }
            for(int i = 0; i < sz; ++i){
                for(int j = i + 1; j < sz; ++j){
                    if(p[circle[i]][circle[j]] != 2) return 0;
                }
            }
            for(int i = 0; i < n; ++i){
                for(int j : trees[i]){
                    for(int k = 0; k < n; ++k){
                        if(j == k) continue;
                        if((p[j][k] == 1 && trees[i].find(k) == trees[i].end()) || (p[j][k] == 2 && trees[i].find(k) != trees[i].end())) return 0;
                    }
                }
            }
        }
    }
    else{
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(!p[i][j] && find(i) == find(j)){
                    return 0;
                }
                if(p[i][j]) merge(i, j);
            }
        }
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(!p[i][j] && find(i) == find(j)) return 0;
            }
        }
        std::set<int> sets[n];
        for(int i = 0; i < n; ++i){
            int rep = find(i);
            sets[rep].insert(i);
        }
        std::set<int> trees[n];
        for(int rep = 0; rep < n; ++rep){
            if(sets[rep].size() < 2) continue;
            if(sets[rep].size() == 2){
                if(p[*sets[rep].begin()][*prev(sets[rep].end())] >= 2) return 0;
            }
            std::vector<int> circle;
            std::set<int> remove;
            for(int i : sets[rep]){
                if(remove.find(i) != remove.end()) continue;
                for(int j = 0; j < n; ++j){
                    if(j == i) continue;
                    if(p[i][j] == 1){
                        answer[i][j] = 1;
                        answer[j][i] = 1;
                        remove.insert(j);
                        trees[i].insert(j);
                    }
                }
                trees[i].insert(i);
                circle.emplace_back(i);
            }
            int sz = circle.size();
            for(int i = 0; i < sz - 1; ++i){
                answer[circle[i]][circle[i + 1]] = 1;
                answer[circle[i + 1]][circle[i]] = 1;
            }
            if(sz > 1){
                answer[circle[0]][circle[sz-1]] = 1;
                answer[circle[sz-1]][circle[0]] = 1;
            }
            int tp = 0;
            if(sz > 1){
                tp = p[circle[0]][circle[1]];
            }
            for(int i = 0; i < sz; ++i){
                for(int j = i + 1; j < sz; ++j){
                    if(p[circle[i]][circle[j]] != tp) return 0;
                }
            }
            for(int i = 0; i < n; ++i){
                for(int j : trees[i]){
                    for(int k = 0; k < n; ++k){
                        if(j == k) continue;
                        if((p[j][k] == 1 && trees[i].find(k) == trees[i].end()) || (p[j][k] == tp && trees[i].find(k) != trees[i].end())) return 0;
                    }
                }
            }
            if(tp == 3){
                return 0;
            }
        }
    }
    build(answer);
    return 1;
}
