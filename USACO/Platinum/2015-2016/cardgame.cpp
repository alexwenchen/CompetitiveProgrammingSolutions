#include <bits/stdc++.h>
using namespace std;

const int MOD = 1e9 + 7;
const int MAX_N = 50005;

int N;
int elsie[MAX_N], bessie[MAX_N];

void setIO(string s) {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	freopen((s + ".in").c_str(), "r", stdin);
	freopen((s + ".out").c_str(), "w", stdout);
}

int main() {
	cin >> N;
	unordered_set<int> elsiesCards;
	for (int i = 0; i != N; ++i) {
		cin >> elsie[i];
		elsiesCards.insert(elsie[i]);
	}
	int ind = 0;
	for (int i = 1; i != 2 * N + 1; ++i) {
		if (elsiesCards.find(i) == elsiesCards.end())
			bessie[ind++] = i;
	}
	sort(bessie, bessie + N);
	int pref[N], suff[N + 1];
	fill(pref, pref + N, 0);
	fill(suff, suff + N + 1, 0);
	multiset<int, greater<int>> reverse;
	multiset<int, greater<int>>::iterator highest;
	multiset<int> bCards;
	multiset<int>::iterator lowest;
	for (int i = 0; i != N; ++i){
		bCards.insert(bessie[i]);
		reverse.insert(bessie[i]);
	}
	for(int i = 0; i != N; ++i){
		if(i) pref[i] = pref[i - 1];
		lowest = bCards.lower_bound(elsie[i]);
		if(lowest == bCards.end()) continue;
		++pref[i]; bCards.erase(*lowest);
	}
	for(int i = N - 1; i >= 0; --i){
		suff[i] = suff[i + 1];
		highest = reverse.lower_bound(elsie[i]);
		if(highest == reverse.end()) continue;
		++suff[i]; reverse.erase(*highest);
	}
	int mx = max(suff[0], pref[N - 1]);
	for(int i = 0; i != N - 1; ++i){
		mx = max(mx, pref[i] + suff[i + 1]);
	}
	cout << mx << "\n";
}
