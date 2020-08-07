//      10/12
import java.util.*;
import java.io.*;

public class standingout {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };
	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("standingout.in"));
		PrintWriter out = new PrintWriter(new File("standingout.out"));
		int N = Integer.parseInt(br.readLine());
		String[] cows = new String[N];
		StringBuilder overall = new StringBuilder();
		int[] amtDistinct = new int[N];
		for(int i = 0; i < N; ++i) {
			cows[i] = br.readLine();
			overall.append(cows[i]);
			overall.append("?");
			amtDistinct[i] = countDistinctSubstring(cows[i]);
		}	
		String concat = overall.toString();
		int[] indexToWord = new int[concat.length()];
		int ind = 0;
		for(int i = 0; i < concat.length(); ++i) { 
			if(concat.charAt(i) == '?') {
				indexToWord[i] = -1;
				++ind;
			}
			else {
				indexToWord[i] = ind;
			}
		}
		int[] sa = suffixArray(concat);
		int[] lcp = LCP(concat, sa);
		HashSet<String>[] erased = new HashSet[N];
		for(int i = 0; i < N; ++i) {
			erased[i] = new HashSet<String>();
		}
		for(int i = N; i < lcp.length - 1; ++i) {
			if(lcp[i] == 0) continue;
			int first = indexToWord[sa[i]];
			int second = indexToWord[sa[i + 1]];
			if(first == second) continue;
			StringBuilder pref = new StringBuilder();
			for(int j = 1; j <= lcp[i]; ++j) {
				pref.append(concat.charAt(sa[i] + j - 1));
				if(!erased[first].contains(pref.toString())) {
					erased[first].add(pref.toString());
					--amtDistinct[first];
				}
				if(!erased[second].contains(pref.toString())) {
					erased[second].add(pref.toString());
					--amtDistinct[second];
				}
			}
		}
		for(int i = 0; i < N; ++i) {
			out.println(amtDistinct[i]);
		}
		out.close();
	}
	static int countDistinctSubstring(String txt) 
	{ 
	    int n = txt.length(); 
	   	int[] suffixArr = suffixArray(txt); 
	   	int[] lcp = LCP(txt, suffixArr); 
	    int result = n - suffixArr[0]; 
	    for (int i = 1; i < lcp.length; i++) 
	        result += (n - suffixArr[i]) - lcp[i - 1]; 
	    return result; 
	} 
	static class Suffix implements Comparable<Suffix> {
		int index;
		int rank;
		int next;

		public Suffix(int ind, int r, int nr) {
			index = ind;
			rank = r;
			next = nr;
		}

		public int compareTo(Suffix s) {
			if (rank != s.rank)
				return Integer.compare(rank, s.rank);
			return Integer.compare(next, s.next);
		}
	}
	static int[] suffixArray(String s)  
    { 
        int n = s.length(); 
        Suffix[] su = new Suffix[n]; 
        for (int i = 0; i < n; i++)  
        { 
            su[i] = new Suffix(i, s.charAt(i) - '$', 0); 
        } 
        for (int i = 0; i < n; i++)  
            su[i].next = (i + 1 < n ? su[i + 1].rank : -1); 
        Arrays.sort(su); 
        int[] ind = new int[n]; 
        for (int length = 4; length < 2 * n; length <<= 1)  
        { 
            int rank = 0, prev = su[0].rank; 
            su[0].rank = rank; 
            ind[su[0].index] = 0; 
            for (int i = 1; i < n; i++) 
            { 
                if (su[i].rank == prev && 
                    su[i].next == su[i - 1].next) 
                { 
                    prev = su[i].rank; 
                    su[i].rank = rank; 
                }  
                else 
                {  
                    prev = su[i].rank; 
                    su[i].rank = ++rank; 
                } 
                ind[su[i].index] = i; 
            } 
            for (int i = 0; i < n; i++)  
            { 
                int nextP = su[i].index + length / 2; 
                su[i].next = nextP < n ?  
                   su[ind[nextP]].rank : -1; 
            } 
            Arrays.sort(su); 
        } 
        int[] suf = new int[n]; 
        for (int i = 0; i < n; i++)  
            suf[i] = su[i].index;  
        return suf; 
    }
	static int[] LCP(String txt, int[] suffixArr) 
	{ 
	    int n = suffixArr.length; 
	    int[] lcp = new int[n];
	    int[] invSuff = new int[n];
	    for (int i=0; i < n; i++) 
	        invSuff[suffixArr[i]] = i; 
	    int k = 0; 
	    for (int i=0; i<n; i++) 
	    { 
	        if (invSuff[i] == n-1) 
	        { 
	            k = 0; 
	            continue; 
	        } 
	        int j = suffixArr[invSuff[i]+1]; 
	        while (i+k<n && j+k<n && txt.charAt(i + k)==txt.charAt(j + k) && txt.charAt(i + k) != '?' && txt.charAt(j + k) != '?') 
	            k++; 
	        lcp[invSuff[i]] = k;
	        if (k>0) 
	            k--; 
	    } 
	    return lcp; 
	} 
}
