//#include "stations.h"
#include <bits/stdc++.h>
using namespace std;

const int mxN = 1'001;

vector<int> adj[mxN];
int timer;
int temp[mxN];
int depth[mxN];

void dfs(int v, int p){
	if(depth[v] % 2 == 0){
		temp[timer++] = v;
	}
	for(int i : adj[v]) {
		if(i == p) continue;
		depth[i] = depth[v] + 1;
		dfs(i, v);
	}
	if(depth[v] % 2 == 1){
		temp[timer++] = v;
	}
}

vector<int> label(int n, int k, vector<int> u, vector<int> v) {
	vector<int> labels(n);
	for(int i = 0; i < n; ++i){
		adj[i].clear();
	}
	for(int i = 0; i < n - 1; ++i){
		adj[u[i]].push_back(v[i]);
		adj[v[i]].push_back(u[i]);
	}
	timer = 0;
	dfs(0, -1);
	for(int i = 0; i < n; ++i){
		labels[temp[i]] = i;
	}
	return labels;
}

int find_next_station(int s, int t, vector<int> c) {
	sort(c.begin(), c.end());
	int sz = c.size();
	if(sz == 1) return c[0];
	if(s < c[0]) {
		//node is in, rest is out
		if(t < s || (t > c[sz - 2])) {
			return c[sz - 1];
		}
		for(int i : c){
			if(t <= i) {
				return i;
			}
		}
	} else{
		//node is out, rest is in
		if(t <= c[0] || t > s) {
			return c[0];
		}
		int ans = 0;
		for(int i : c){
			if(t < i) break;
			ans = i;
		}
		return ans;
	}
}
/*
int main(){
	int n; cin >> n;
	vector<int> a(n - 1), b(n - 1);
	for(int i = 0; i < n - 1; ++i){
		cin >> a[i] >> b[i];
	}
	vector<int> labels = label(n, 0, a, b);
	for(int i : labels){
		cout << i << " ";
	}
	cout << "\n";
	vector<int> nb;
	nb.emplace_back(labels[1]); nb.emplace_back(labels[2]);
	cout << find_next_station(labels[0], labels[4], nb);
}*/