//credit to Anshul Raghav
import java.io.*;
import java.util.*;
 
public class milkvisits {
    public static int numCows;
    public static int numFarmers;
    public static String cowTypes;
    public static ArrayList<Integer>[] connectionMap;
    public static int col[];
    public static boolean[] visitedArr;
    public static void main(String[] args) throws IOException {
        // Kattio io = new Kattio("milkvisits");
        BufferedReader br = new BufferedReader(new FileReader("milkvisits.in"));
        StringTokenizer st = new StringTokenizer(br.readLine());
        numCows = Integer.parseInt(st.nextToken());
        numFarmers = Integer.parseInt(st.nextToken());
        cowTypes = br.readLine();
        connectionMap = new ArrayList[numCows];
        col = new int[numCows];
        for(int i = 0; i < numCows; ++i) {
            connectionMap[i] = new ArrayList<Integer>();
        }
        for (int i=0; i<numCows-1; i++) {
            st = new StringTokenizer(br.readLine());
            int firstCow = Integer.parseInt(st.nextToken())-1;
            int secondCow = Integer.parseInt(st.nextToken())-1;
            connectionMap[firstCow].add(secondCow);
            connectionMap[secondCow].add(firstCow);
        }
        Arrays.fill(col, -1);
        visitedArr = new boolean[numCows];
        int currentColor = 0;
        for (int i=0; i<numCows; i++) {
            if (col[i] != -1) {
                continue;
            }
            floodfill(cowTypes.charAt(i), i, currentColor);
            currentColor++;
        }
        PrintWriter out = new PrintWriter(new File("milkvisits.out"));
        for (int i=0; i<numFarmers; i++) {
            st = new StringTokenizer(br.readLine());
            int firstPos = Integer.parseInt(st.nextToken())-1;
            int secondPos = Integer.parseInt(st.nextToken())-1;
            char milkType = st.nextToken().charAt(0);
            if (cowTypes.charAt(firstPos) == milkType || col[firstPos] != col[secondPos]) {
                out.print("1");
            }
            else {
                out.print("0");
            }
        }
        //io.println(componentMap);
        //io.println(typeMap);
        //io.println(connectionMap);
        out.close();
    }
    public static void floodfill(char type, int position, int color) {
        visitedArr[position] = true;
        col[position] = color;
        for (int i : connectionMap[position]) {
            if(visitedArr[i]) continue;
            if(cowTypes.charAt(i) == type) floodfill(type, i, color);
        }
    }
}
