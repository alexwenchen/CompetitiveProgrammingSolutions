#include "grader.h"
#include <bits/stdc++.h>
using namespace std;

const int MAX_N = 100000;
const int MAX_D = 17;
int N, Q;
int col;
vector<int> adj[MAX_N];
int rows[MAX_N], depth[MAX_N], size[MAX_N];
int lca[MAX_N][MAX_D];
pair<int, int> coords[MAX_N];
int LCA(int a, int b) {
  if(depth[a] < depth[b]) swap(a, b);
  for(int d = MAX_D-1; d >= 0; d--) {
    if(depth[a] - (1<<d) >= depth[b]) {
      a = lca[a][d];
    }
  }
  for(int d = MAX_D-1; d >= 0; d--) {
    if(lca[a][d] != lca[b][d]) {
      a = lca[a][d];
      b = lca[b][d];
    }
  }
  if(a != b) {
    a = lca[a][0];
    b = lca[b][0];
  }
  return a;
}

void initLCA() {
  for(int d = 1; d < MAX_D; d++) {
    for(int i = 0; i < MAX_N; i++) {
      lca[i][d] = lca[lca[i][d-1]][d-1];
    }
  }
}

void dfsForSize(int curr, int par) {
  size[curr]++;
  for(int out: adj[curr]) {
    if(out == par) continue;
    depth[out] = depth[curr] + 1;
    lca[out][0] = curr;
    dfsForSize(out, curr);
    size[curr] += size[out];
  }
}

int cy(int y){
    return N - y;
}

void dfs(int v, int p){
    int totsize = 0;
    int row = rows[v] + 1;
    for(int i : adj[v]){
        if(i == p) continue;
        totsize += size[i];
    }
    for(int i : adj[v]){
        if(i == p) continue;
        totsize -= size[i];
        rows[i] = row + totsize;
        coords[i] = make_pair(col, cy(row + totsize - 1));
        setFarmLocation(i, col++, cy(row + totsize - 1));
        dfs(i, v);
    }
}

void addRoad(int a, int b){
    adj[a].emplace_back(b);
    adj[b].emplace_back(a);
}

void buildFarms(){
    N = getN();
    Q = getQ();
    col = 2;
    dfsForSize(0,-1);
    initLCA();
    setFarmLocation(0, 1, cy(0));
    coords[0] = make_pair(1, cy(0));
    rows[0] = 1;
    dfs(0, -1);
}

void notifyFJ(int a, int b){
    int lca = LCA(a, b);
    int mnxa = min(coords[a].first, coords[lca].first);
    int mxxa = max(coords[a].first, coords[lca].first);
    int mnya = min(coords[a].second, coords[lca].second);
    int mxya = max(coords[a].second, coords[lca].second);
    int mnxb = min(coords[lca].first, coords[b].first);
    int mxxb = max(coords[lca].first, coords[b].first);
    int mnyb = min(coords[lca].second, coords[b].second);
    int mxyb = max(coords[lca].second, coords[b].second);
    if(lca == a) {
        addBox(mnxb, mnyb, mxxb, mxyb);
    }
    else if(lca == b) {
        addBox(mnxa, mnya, mxxa, mxya);
    }
    else {
        addBox(mnxa, mnya, mxxa, mxya);
        addBox(mnxb + 1, mnyb, mxxb, mxyb);
    }
}