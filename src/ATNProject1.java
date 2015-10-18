/**
*
* @author Deepak
*
*/
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.*;
import java.io.BufferedWriter;


class wrapper_class
{
public float cost_of_nw;
public float density_of_nw;
}

class ATNProject1
{
public static int N = 30;
public static int Max_range_of_bij = 5;
public static int K;
public static int K_start_value = 3;
public static int K_end_value = 15;
public static int nonZeroCapacityEdges = 0;
public static Integer a_ij[][] = new Integer[N + 1][N + 1]; // Aij
public static Integer b_ij[][] = new Integer[N + 1][N + 1]; // Bij
public static Integer d[][][] = new Integer[N + 1][N + 1][N + 1]; 
public static Integer total_cost_of_nw_of_paths[][] = new Integer[N + 1][N + 1];
public static Integer path[][] = new Integer[N + 1][N + 1];
public static Integer edge[][] = new Integer[N + 1][N + 1];
public static HashMap<Integer, wrapper_class> outputMap = new HashMap<Integer, wrapper_class>();
public final static String fileName = "OutputGraph.txt";
public static void writeToFile()
{
try
{
FileWriter fileStream = new FileWriter(fileName, true); // this will keep appending the file than erasing it.
BufferedWriter bufferedStream = new BufferedWriter(fileStream);
bufferedStream.write(N + " ");
for (int i = 1; i <= N; i++)
{
for (int j = 1; j <= N; j++)
{
if ((edge[i][j] == 0))
{
bufferedStream.write("0");
}
else
{
bufferedStream.write("1");
nonZeroCapacityEdges++;
}
bufferedStream.write(" ");
}
bufferedStream.write(" ");
}
bufferedStream.close();
}
catch (Exception e)
{
System.out.println(e);
}
}
public static void Floyd_warshall_SP_algorithm(int N) //using FloydWarshall shortest path algorithm
{
try {
int k = 0;
for (int i = 1; i <= N; i++)
{
for (int j = 1; j <= N; j++)
{
d[k][i][j] = a_ij[i][j];
}
}
for (k = 1; k <= N; k++)
{
for (int i = 1; i <= N; i++)
{
for (int j = 1; j <= N; j++)
{
if (d[k - 1][i][j] > (d[k - 1][i][k] + d[k - 1][k][j]))
{
d[k][i][j] = d[k - 1][i][k] + d[k - 1][k][j];
 path[i][j] = k;
}
 else
{
d[k][i][j] = d[k - 1][i][j];
}
}
}
}
for (int i = 1; i <= N; i++)
{
for (int j = 1; j <= N; j++)
{
if (i == j || (path[i][j] == 0))
{
edge[i][j] += b_ij[i][j];
continue;
}
else
{
graph_building(i, j, path[i][j], b_ij[i][j]);
}
}

}

//Print the adjacency matrix
writeToFile();
wrapper_class result = new wrapper_class();
result.cost_of_nw = cost_of_nw_calculation();
result.density_of_nw = density_of_nw_calculation();
outputMap.put(K, result);
}
catch (Exception e)
{
System.out.println(e);
}
}
public static int cost_of_nw_calculation()
{
int total_cost = 0;
// multiplying the traffic b_ij
for (int i = 1; i <= N; i++)
{
for (int j = 1; j <= N; j++)
{
total_cost_of_nw_of_paths[i][j] = d[N][i][j] * b_ij[i][j];
total_cost += total_cost_of_nw_of_paths[i][j];
}
}

return total_cost;
}
public static float density_of_nw_calculation()
{
float totaldensity_of_nw = 0;

totaldensity_of_nw = (float) nonZeroCapacityEdges / (float) ((N * (N - 1)));

return totaldensity_of_nw;
}
public static void graph_building(int i, int j, int Path_ij, int Bij_ij)
// building network topology
{
int node = 0, x = 0, y = 0, traffic = 0;
int a = i;
y = j;
node = Path_ij;
x = Path_ij;
traffic = Bij_ij;
if (node == 0)
{
edge[x][y] += traffic;
}
else
{
edge[a][node] += traffic;
edge[x][y] += traffic;
graph_building(x, y, path[x][y], traffic);
}
}
public static boolean check(Integer[] arr, int number)
// to verify and confirm the array is not obsolete
{
boolean flag = false;
for (int j = 0; j < arr.length; j++)
{
if (arr[j] != null && arr[j] == number)
{
flag = true;
break;
}
}
return flag;
}
public static void main(String[] args)
{
try
{
File file = new File(fileName);
file.createNewFile();
for (K = K_start_value; K <= K_end_value; K++)
{

// Initializing the variables
for (int i = 0; i <= N; i++)
{
for (int j = 0; j <= N; j++)
{
a_ij[i][j] = 0;
b_ij[i][j] = 0;
total_cost_of_nw_of_paths[i][j] = 0;
path[i][j] = 0;
edge[i][j] = 0;
}
}
for (int i = 1; i <= N; i++) /// to build the density_of_nw i.e b_ij matrix
{
for (int j = 1; j <= N; j++)
{
if (i == j)
{
b_ij[i][j] = 0;
}
else
{
b_ij[i][j] = (0 + (int) (Math.random() *
Max_range_of_bij));
}
}
}

for (int i = 1; i <= N; i++)
{
Integer randomIndices[] = new Integer[K];
for (int k = 0; k < K; k++)
{
int random = (1 + (int) (Math.random() * N));
if (check(randomIndices, random))
{
k--;
}
else
{
randomIndices[k] = random;
}
}
for (int j = 1; j <= N; j++)
{
if (i == j)
{
a_ij[i][j] = 0;
continue;
}
else
{
if (check(randomIndices, j))
{
a_ij[i][j] = 1;
}
else
{
a_ij[i][j] = 300;
}
}
}
}

Floyd_warshall_SP_algorithm(N);

}
System.out.println(" ");
System.out.println("K\tTotal cost_of_nw\tDensity_of_nw");
for (K = K_start_value; K <= K_end_value; K++)
{
System.out.println(K + "\t"+
((wrapper_class)outputMap.get(K)).cost_of_nw + "\t\t\t" + ((wrapper_class)outputMap.get(K)).density_of_nw);
}
System.out.println();
System.out.println();
}
catch (Exception e)
{
System.out.println(e);
}
}
}
