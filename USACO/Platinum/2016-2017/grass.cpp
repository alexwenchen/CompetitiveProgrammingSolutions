//this idea is used: https://codeforces.com/blog/entry/50889?#comment-348725
// AC !!!
#include <bits/stdc++.h>
using namespace std;
 
using ll = long long;
using pii = pair<int, int>;
 
#define pb push_back
#define mp make_pair
#define eb emplace_back
#define all(x) (x).begin(),(x).end()
#define x first
#define y second
 
const int MOD = 1e9 + 7;
const int mxN = 2e5 + 5;
const int mxQ = 2e5 + 5;
const int mxM = 2e5 + 5;
const int intMax = 1e7;
const int off = 1e8;

void setIO(string s) {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

void setIO2() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
//    freopen((s + ".in").c_str(), "r", stdin);
//    freopen((s + ".out").c_str(), "w", stdout);
}

struct two{
    int par, type;
    two(){
        par = -1; type = -1;
    }
    two(int a, int b){
        par = a; type = b;
    }
};

struct range{
    int type, l, r;
    range(){}
    range(int a, int b){
        type = a; l = b;
    }
    range(int a, int b, int c){
        type = a; l = b; r = c;
    }
    bool operator<(const range o)const{
        return type < o.type;
    }
};

struct query{
    int node, type, move, ind;
    query(){}
    query(int a, int b, int d){
        node = a; type = b; ind = d;
    }
    query(int a, int b, int c, int d){
        node = a; type = b; move = c; ind = d;
    }
};

struct edge{
    int dest, weight;
    edge(){}
    edge(int a, int b){
        dest = a; weight = b;
    }
};

struct fullEdge{
    int src, dest, weight;
    fullEdge(){}
    fullEdge(int a, int b, int c){
        src = a; dest = b; weight = c;
    }
    bool operator<(const fullEdge o) const{
        return weight < o.weight;
    }
};
struct LazySegmentTree {
    int mn[4 * (mxN + mxQ)];
    int lazy[4 * (mxN + mxQ)];
    int size;
    LazySegmentTree(int sz) {
        size = sz;
    }
    void update(int l, int r, int inc) {
        update(1, 0, size - 1, l, r, inc);
    }
    void pushDown(int index, int l, int r) {
        mn[index] += lazy[index];
        if (l != r) {
            lazy[2 * index] += lazy[index];
            lazy[2 * index + 1] += lazy[index];
        }
        lazy[index] = 0;
    }

    int evaluateMin(int index, int l, int r) {
        return mn[index] + lazy[index];
    }

    void pullUp(int index, int l, int r) {
        int m = (l + r) / 2;
        mn[index] = min(evaluateMin(2 * index, l, m), evaluateMin(2 * index + 1, m + 1, r));
    }
    
    void update(int index, int l, int r, int left, int right, int inc) {
        if (r < left || l > right)
            return;
        if (l >= left && r <= right) {
            lazy[index] += inc;
            return;
        }
        pushDown(index, l, r);
        int m = (l + r) / 2;
        update(2 * index, l, m, left, right, inc);
        update(2 * index + 1, m + 1, r, left, right, inc);
        pullUp(index, l, r);
    }
    int minQuery(int index, int l, int r, int left, int right) {
        if (r < left || l > right)
            return 1e9;
        if (l >= left && r <= right) {
            return evaluateMin(index, l, r);
        }
        pushDown(index, l, r);
        int m = (l + r) / 2;
        int ret = 1e9;
        ret = min(ret, minQuery(2 * index, l, m, left, right));
        ret = min(ret, minQuery(2 * index + 1, m + 1, r, left, right));
        pullUp(index, l, r);
        return ret;
    }
    int minQuery(int l, int r) {
        return minQuery(1, 0, size - 1, l, r);
    }
};

int N, M, K, Q;
vector<edge> adj[mxN];
query queries[mxQ];
int neworder[mxN + mxQ], order[mxN], par[mxN], types[mxN], orderToActual[mxN], loc[mxN], weights[mxN], parent[mxN], rnk[mxN];
int ind = 0;
set<range> ranges[mxN];
fullEdge edges[mxM];
two stats[mxN + mxQ];

bool dfsComp(int a, int b){
    return types[a] < types[b];
}

bool queryComp1(query a, query b){
    return orderToActual[par[a.node]] == orderToActual[par[b.node]] ? a.type < b.type : orderToActual[a.node] < orderToActual[b.node];
}

bool queryComp2(query a, query b){
    return a.ind < b.ind;
}

int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);
    }
    return parent[x];
}

void merge(int x, int y) {
    int xRoot = find(x), yRoot = find(y);
    if (xRoot == yRoot)
        return;
    if (rnk[xRoot] < rnk[yRoot])
        parent[xRoot] = yRoot;
    else if (rnk[yRoot] < rnk[xRoot])
        parent[yRoot] = xRoot;
    else {
        parent[yRoot] = xRoot;
        rnk[xRoot] = rnk[xRoot] + 1;
    }
}

void KruskalsAlgorithm() {
    sort(edges, edges + M);
    for (int i = 0; i < N; ++i) {
        parent[i] = i;
    }
    int numEdges = 0;
    int edgeIndex = 0;
    while (numEdges < N - 1) {
        fullEdge next = edges[edgeIndex];
        int x = find(next.src);
        int y = find(next.dest);
        int sc = next.src;
        int dst = next.dest;
        int wt = next.weight;
        if (x != y) {
            adj[sc].eb((edge){dst, wt});
            adj[dst].eb((edge){sc, wt});
            merge(x, y);
            ++numEdges;
        }
        ++edgeIndex;
    }
}

void dfs(int v, int p){
    par[v] = max(0, p);
    if(p == -1){
        order[ind++] = v;
    }
    vector<int> children;
    for(edge i : adj[v]){
        if(i.dest == p) continue;
        children.eb(i.dest);
    }
    sort(all(children), dfsComp);
    vector<range> cands;
    int cur = -1;
    for(int i : children){
        if(cur == -1){
            cur = types[i];
            cands.eb((range){cur, ind});
        }
        if(types[i] != cur){
            cands[cands.size() - 1].r = ind - 1;
            cands.eb((range){types[i], ind});
            cur = types[i];
        }
        order[ind++] = i;
    }
    if(cands.size()){
        cands[cands.size() - 1].r = ind - 1;
    }
    for(range i : cands){
        ranges[v].insert(i);
    }
    for(edge i : adj[v]){
        if(i.dest == p) continue;
        weights[i.dest] = i.weight;
        dfs(i.dest, v);
    }
}

int main(){
    setIO("grass");
//    setIO2();
    cin >> N >> M >> K >> Q;
    for(int i = 0; i < M; ++i){
        int a, b, l; cin >> a >> b >> l;
        --a; --b;
        edges[i] = (fullEdge){a, b, l};
    }
    for(int i = 0; i < N; ++i){
        cin >> types[i];
    }
    for(int i = 0; i < Q; ++i){
        int a, b; cin >> a >> b;
        --a;
        queries[i] = (query){a, b, i};
    }
    KruskalsAlgorithm();
    LazySegmentTree lst(N + Q);
    dfs(0, -1);
    for(int i = 0; i < N; ++i){
        orderToActual[order[i]] = i;
    }
    sort(queries, queries + Q, queryComp1);
    fill(neworder, neworder + N + Q, -1);
    int amtforward = 0;
    for(int i = 0; i < Q; ++i){
        if(queries[i].node == 0)
            continue;
        int curnode = queries[i].node;
        int curtype = queries[i].type;
        range dummy = (range){curtype, 0};
        auto it = ranges[par[curnode]].lower_bound(dummy);
        range next = *it;
        if(it == ranges[par[curnode]].end()) {
            --it;
            next = *it;
            neworder[next.r + amtforward + 1] = -2;
            stats[next.r + amtforward + 1] = (two){par[curnode], curtype};
            queries[i].move = next.r + amtforward + 1;
        } else {
            neworder[next.l + amtforward] = -2;
            queries[i].move = next.l + amtforward;
            stats[next.l + amtforward] = (two){par[curnode], curtype};
        }
        ++amtforward;
    }
    amtforward = 0;
    for (int i = 0; i < N; ++i) {
        while (neworder[i + amtforward] == -2)
            ++amtforward;
        neworder[i + amtforward] = order[i];
    }
    for (int i = 0; i < N + Q; ++i) {
        if (neworder[i] <= 0) {
            lst.update(i, i, off);
        } else {
            stats[i] = (two){par[neworder[i]], types[neworder[i]]};
            loc[neworder[i]] = i;
            lst.update(i, i, weights[neworder[i]]);
        }
    }
    for (int i = 0; i < N; ++i) {
        ranges[i].clear();
    }
    for (int i = 0; i < N + Q; ++i) {
        if (stats[i].type == -1)
            continue;
        int curpar = stats[i].par;
        int curtype = stats[i].type;
        int ind = i;
        while (ind < N + Q && stats[ind].type != -1 && stats[ind].par == curpar && stats[ind].type == curtype) {
            ++ind;
        }
        ranges[curpar].insert((range){curtype, i, ind - 1});
        i = ind - 1;
    }
    for (int i = 0; i < N; ++i) {
        if (i != 0) {
            if (types[i] == types[par[i]]) {
                lst.update(loc[i], loc[i], intMax);
            }
        }
    }
    sort(queries, queries + Q, queryComp2);
    for(int i = 0; i < Q; ++i){
        int node = queries[i].node;
        int oldtype = types[node];
        types[node] = queries[i].type;
        int newtype = types[node];
        int move = queries[i].move;
        if (node != 0) {
            lst.update(loc[node], loc[node], 2 * intMax);
            loc[node] = move;
            if (newtype == types[par[node]]) {
                lst.update(move, move, -lst.minQuery(move,move) + intMax + weights[node]);
            }
            else {
                lst.update(move, move, -lst.minQuery(move,move) + weights[node]);
            }
        }
        range dummy1 = (range){oldtype, 0}; // remove intmax from these
        auto it = ranges[node].lower_bound(dummy1);
        range remove = *it;
        if (it != ranges[node].end() && remove.type == oldtype) {
            lst.update(remove.l, remove.r, -intMax);
        }
        range dummy2 = (range){newtype, 0}; // add intmax to these
        auto it2 = ranges[node].lower_bound(dummy2);
        range add = *it2;
        if (it2 != ranges[node].end() && add.type == newtype) {
            lst.update(add.l, add.r, intMax);
        }
        int ans = lst.minQuery(0, N + Q - 1);
        if (ans < intMax) {
            cout << ans << "\n";
        }
    }
}