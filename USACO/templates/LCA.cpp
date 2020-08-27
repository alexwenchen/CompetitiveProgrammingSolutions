const int mxN = 1e5 + 5;
const int mxD = 17;
int depth[mxN];
int lca[mxN][mxD];
void dfssetup(int curr, int par) {
    for(int out: adj[curr]) {
        if(out == par)
            continue;
        depth[out] = depth[curr] + 1;
        lca[out][0] = curr;
        dfssetup(out, curr);
    }
}
int LCA(int a, int b) {
    if(depth[a] < depth[b]) {
        int temp = a;
        a = b;
        b = temp;
    }
    for(int d = mxD-1; d >= 0; d--) {
        if(depth[a] - (1<<d) >= depth[b]) {
            a = lca[a][d];
        }
    }
    for(int d = mxD-1; d >= 0; d--) {
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
    for(int d = 1; d < mxD; d++) {
        for(int i = 0; i < mxN; i++) {
            lca[i][d] = lca[lca[i][d-1]][d-1];
        }
    }
}
