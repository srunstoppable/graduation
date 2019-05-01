package com.example.graduation.util;


import java.util.ArrayList;


/**
 * @author s r
 * @date 2019/1/29
 */
public class AjacencyGraph implements Graph {

    final static int MaxValue = 1000;
    private int n;
    private int e;
    private int type;
    private int[][] a;

    private int[][] getArray() {
        return a;
    }

    public AjacencyGraph(int n, int t) {
        if (n < 1 || t < 0 || t > 3) {
            System.out.println("初始化图的参数值错，退出运行！");
            System.exit(1);
        }
        this.n = n;
        type = t;
        e = 0;
        a = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (i == j) a[i][j] = 0;
                else if (type == 0 || type == 2)
                    a[i][i] = 0;
                else a[i][j] = MaxValue;
            }

    }

    @Override
    public boolean createGraph(EdgeElement[] d) {
        for (int i = 0; i < d.length; i++) {
            if (d[i] == null) {
                System.out.println("边集中元素为空，停止建图，返回假！");
                return false;
            }
            int v1, v2;
            v1 = d[i].fromvex;
            v2 = d[i].endvex;
            if (v1 < 0 || v1 > n - 1 || v2 < 0 || v2 > n - 1 || v1 == v2) {
                System.out.println("边的定点序号无效");
                return false;    //定点序号无效，停止建图
            }
            if (a[v1][v2] != 0 && a[v1][v2] != MaxValue) {
                System.out.println("边已经存在，停止建图，返回假!");
                return false;
            }
            if (type == 0)
                a[v1][v2] = a[v2][v1] - 1;
            else if (type == 1)
                a[v1][v2] = a[v2][v1] = d[i].weight;
            else if (type == 2)
                a[v1][v2] = 1;
            else a[v1][v2] = d[i].weight;
        }
        e = d.length;
        return true;
    }

    @Override
    public int graphType() {  //图类型
        return type;
    }

    @Override
    public int vartices() {   //顶点数
        return n;
    }

    @Override
    public int edges() {   //边数
        return e;
    }

    @Override
    public boolean find(int i, int j) {
        if (i < 0 || i > n - i || j < 0 || j > n - 1 || i == j) {
            System.exit(i);
        }
        if (a[i][j] != 0 && a[i][j] != MaxValue) return true;
        else return false;
    }

    @Override
    public boolean putEdge(EdgeElement edgeElement) {
        int v1, v2;
        v1 = edgeElement.fromvex;
        v2 = edgeElement.endvex;
        if (v1 < 0 || v1 > n - 1 || v2 < 0 || v2 > n - 1 || v1 == v2) {
            return false;    //定点序号无效，停止建图
        }
        if (a[v1][v2] == 0 || a[v1][v2] == MaxValue) e++;
        else {
            System.out.println("边已存在");
            return false;
        }
        if (type == 0 || type == 1) {
            if (type == 0) a[v1][v2] = a[v2][v1] = 1;
            else a[v1][v2] = a[v2][v1] = edgeElement.weight;
        } else {
            if (type == 2) a[v1][v2] = 1;
            else a[v2][v1] = edgeElement.weight;
        }
        return true;
    }

    @Override
    public boolean removeEdge(int i, int j) {
        if (i < 0 || i > n - 1 || j < 0 || j > n - 1 || i == j) {
            return false;    //定点序号无效，停止建图
        }
        if (a[i][j] == 0 || a[i][j] == MaxValue) {
            return false;
        }
        if (type == 0) a[i][j] = a[j][i] = 0;
        else if (type == 1) a[i][j] = a[j][i] = MaxValue;
        else if (type == 2) a[i][j] = 0;
        else a[i][j] = MaxValue;
        e--;
        return true;
    }

    @Override
    public int degree(int i) {
        if (i < 0 || i > n - 1) {
            System.out.println("顶点序号无效，退出运行！");
            System.exit(1);
        }
        int k = 0;
        if (type == 0 || type == 1) {
            for (int j = 0; j < n; j++)
                if (a[i][j] != 0 && a[j][i] != MaxValue) k++;
            return k;
        } else return inDegree(i) + outDegree(i);
    }

    @Override
    public int inDegree(int i) {
        if (i < 0 || i > n - 1) {
            System.out.println("参数的顶点序号无效，退出运行！");
            System.exit(1);
        }
        if (type == 0 || type == 1) return degree(i);
        int k = 0;
        for (int j = 0; j < n; j++)
            if (a[j][i] != 0 && a[j][i] != MaxValue) k++;
        return k;
    }

    @Override
    public int outDegree(int i) {
        if (i < 0 || i > n - 1) {
            System.out.println("参数的顶点序号无效，退出运行！");
            System.exit(1);
        }
        int k = 0;
        for (int j = 0; j < n; j++)
            if (a[j][i] != 0 && a[j][i] != MaxValue) k++;
        return k;
    }

    @Override
    public void output() {
        int i, j;
        System.out.print("V={");
        for (i = 0; i < n - 1; i++) {
            System.out.print(i + ",");
        }
        System.out.println(n - 1 + "}");
        System.out.print("E={");
        if (type == 0 || type == 2) {
            for (i = 0; i < n; i++)
                for (j = 0; j < n; j++)
                    if (a[i][j] != 0 && a[i][j] != MaxValue)
                        if (type == 0) {
                            if (i < j) System.out.print("(" + i + "," + j + "),");
                        } else System.out.print("<" + i + "," + j + ">, ");
        } else {
            for (i = 0; i < n; i++)
                for (j = 0; j < n; j++)
                    if (a[i][j] != 0 && a[i][j] != MaxValue)
                        if (type == 1) {
                            if (i < j) System.out.print("(" + i + "，" + j + ")" + a[i][j] + ",");
                        } else System.out.print("<" + i + "," + j + ">" + a[i][j] + ",");
        }
        System.out.println("}");
    }

    @Override
    public void depthFirstSearch(int v) {
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++)
            visited[i] = false;
        dfs(v, visited);
        System.out.println();

    }

    public void dfs(int i, boolean[] visited) {
        System.out.println(i + "");
        visited[i] = true;
        for (int j = 0; j < n; j++) {
            if (a[i][j] != 0 && a[i][j] != MaxValue && !visited[j]) {
                dfs(j, visited);
            }
        }
    }


    public void bfs(int i, boolean[] visited) {
        Queue q = new Sequence();
        System.out.println(i + "");
        visited[i] = true;
        q.enter(i);
        while (!q.isEmpty()) {
            int k = (Integer) q.leave();
            for (int j = 0; j < n; j++) {
                if (a[k][j] != 0 && a[k][j] != MaxValue && !visited[j]) {
                    System.out.println(j + "");
                    visited[j] = true;
                    q.enter(j);
                }
            }
        }

    }

    @Override
    public void breadthFirstSearch(int v) {
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++)
            visited[i] = false;
        bfs(v, visited);
        System.out.println();
    }

    @Override
    public void clearGraph() {
        n = e = type = 0;
        a = null;
    }

    public static void Dijkstra(AjacencyGraph gr, int i, int[] dist, ListRe[] path) {
        int n = gr.vartices();
        int[][] a = gr.getArray();
        boolean[] s = new boolean[n];
        for (int v = 0; v < n; v++)
            if (v == i) s[v] = true;
            else s[v] = false;
        for (int v = 0; v < n; v++) dist[v] = a[i][v];
        for (int v = 0; v < n; v++) {
            path[v] = new SequenceList();
            if (a[i][v] != gr.MaxValue && v != i) {
                path[v].add(i, 1);
                path[v].add(v, 2);
            }
        }
        for (int k = 1; k <= n - 2; k++) {
            int w = gr.MaxValue;
            int m = i;
            for (int j = 0; j < n; j++)
                if (s[j] == false && dist[j] < w) {
                    w = dist[j];
                    m = j;
                }
            if (m != i) s[m] = true;
            else break;
            for (int j = 0; j < n; j++) {
                if (s[j] == false && dist[m] + a[m][j] < dist[j]) {
                    dist[j] = dist[m] + a[m][j];
                    path[j].clear();
                    for (int pos = 1; pos <= path[m].size(); pos++)
                        path[j].add(path[m].value(pos), pos);
                    path[j].add(j, path[j].size() + 1);

                }
            }

        }


    }
}
