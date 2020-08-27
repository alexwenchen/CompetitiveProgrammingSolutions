const int mxN = 1e5 + 5;

struct LazySegmentTree {
    int mn[4 * mxN];
    int lazy[4 * mxN];
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

