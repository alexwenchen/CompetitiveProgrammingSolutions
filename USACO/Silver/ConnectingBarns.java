// Question 2
//credit: Aditya Ramanathan
import java.io.*;
import java.util.*;
 
public class ConnectingBarns {
 
    private static ArrayList<ArrayList<Integer>> farm;
    private static boolean[] visited;
    private static ArrayList<ArrayList<Integer>> components;
    private static int nthFieldComponent;
 
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
        int t = Integer.parseInt(br.readLine());
 
        for(int sc = 1; sc <= t; sc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
 
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
 
            farm = new ArrayList<ArrayList<Integer>>();
            components = new ArrayList<ArrayList<Integer>>();
            visited = new boolean[n];
 
            for(int i = 0; i < n; i++) {
                farm.add(new ArrayList<Integer>());
            }
 
            for(int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine());
                int node1 = Integer.parseInt(st.nextToken()) - 1;
                int node2 = Integer.parseInt(st.nextToken()) - 1;
                farm.get(node1).add(node2);
                farm.get(node2).add(node1);
            }
 
            System.out.println(solve(n, m));
        }
    }
 
    public static long solve(int n, int m) {
        int componentIndex = 0;
        for(int i = 0; i < n; i++) {
            if(!visited[i]) {
                components.add(new ArrayList<Integer>());
                dfs(i, componentIndex);
                componentIndex++;
            }
        }
 
        ArrayList<Integer> firstComp = components.get(0);
        ArrayList<Integer> nthComp = components.get(nthFieldComponent);
 
        Collections.sort(firstComp);
        Collections.sort(nthComp);
 
        long minCost = (long) Math.pow((n - 1), 2);
 
        // Case 1 - Directly connect 1st and nth components
 
        for(int node: firstComp) {
            int lower = 0;
            int upper = nthComp.size() - 1;
 
            int leftIndex = binarySearchLeft(lower, upper, nthComp, node);
            long leftCost = (long) Math.pow((node - nthComp.get(leftIndex)), 2);
 
            int rightIndex = binarySearchRight(lower, upper, nthComp, node);
            long rightCost = (long) Math.pow((node - nthComp.get(rightIndex)), 2);
 
            minCost = Math.min(leftCost, minCost);
            minCost = Math.min(rightCost, minCost);
        }
 
        // Case 2 - Use an intermediate component

	long minFirst[] = new long[components.size()];
	long minNth[] = new long[components.size()];

	Arrays.fill(minFirst, 1000000000000000000L);
	Arrays.fill(minNth, 1000000000000000000L);
 
        for(int i = 1; i < components.size(); i++) {
            if(i == nthFieldComponent) continue;
            for(int node: components.get(i)) {
                int lower = 0;
                int upper = firstComp.size() - 1;
 
                int leftFirstIndex = binarySearchLeft(lower, upper, firstComp, node);
                long leftCostFirst = (long) Math.pow((node - firstComp.get(leftFirstIndex)), 2);
 
                int rightFirstIndex = binarySearchRight(lower, upper, firstComp, node);
                long rightCostFirst = (long) Math.pow((node - firstComp.get(rightFirstIndex)), 2);
 
                lower = 0;
                upper = nthComp.size() - 1;
 
                int leftNthIndex = binarySearchLeft(lower, upper, nthComp, node);
                long leftCostNth = (long) Math.pow((node - nthComp.get(leftNthIndex)), 2);
 
                int rightNthIndex = binarySearchRight(lower, upper, nthComp, node);
                long rightCostNth = (long) Math.pow((node - nthComp.get(rightNthIndex)), 2);
 
		minFirst[i] = Math.min(minFirst[i], Math.min(leftCostFirst, rightCostFirst));
		minNth[i] = Math.min(minNth[i], Math.min(leftCostNth, rightCostNth));
            }
		minCost = Math.min(minCost, minFirst[i] + minNth[i]);
        }
        return minCost;
    }
 
    public static void dfs(int node, int component) {
        if(visited[node]) {
            return;
        }
        visited[node] = true;
        components.get(component).add(node);
 
        if(node == visited.length - 1) {
            nthFieldComponent = component;
        }
 
        for(int i: farm.get(node)) {
            dfs(i, component);
        }
    }
 
    public static int binarySearchLeft(int lower, int upper, ArrayList<Integer> comp, int node) {
        while(lower != upper) {
            int mid = (lower + upper + 1)/2;
            if(comp.get(mid) < node) {
                lower = mid;
            }
            else {
                upper = mid - 1;
            }
        }
        return lower;
    }
 
    public static int binarySearchRight(int lower, int upper, ArrayList<Integer> comp, int node) {
        while(lower != upper) {
            int mid = (lower + upper)/2;
            if(comp.get(mid) > node) {
                upper = mid;
            }
            else {
                lower = mid + 1;
            }
        }
        return lower;
    }
 
}
