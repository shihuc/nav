package com.shihuc.up.nav.path.util;

import java.util.Stack;

/**
 * @Author: chengsh05
 * @Date: 2019/12/9 10:25
 */
public class DJMatrix {

    private static int INF = Integer.MAX_VALUE;

    public static void dijkstra(int vs, int mMatrix[][], int[] prev, int[] dist) {
        // flag[i]=true表示"顶点vs"到"顶点i"的最短路径已成功获取
        boolean[] flag = new boolean[mMatrix.length];

        // 初始化
        for (int i = 0; i < mMatrix.length; i++) {
            // 顶点i的最短路径还没获取到。
            flag[i] = false;
            // 顶点i的前驱顶点为0,此数组的价值在于计算出最终具体路径信息。
            prev[i] = 0;
            // 顶点i的最短路径为"顶点vs"到"顶点i"的权。
            dist[i] = mMatrix[vs][i];
        }

        // 对"顶点vs"自身进行初始化
        flag[vs] = true;
        dist[vs] = 0;

        // 遍历所有顶点；每次找出一个顶点的最短路径。
        int k=0;
        for (int i = 1; i < mMatrix.length; i++) {
            // 寻找当前最小的路径， 即，在未获取最短路径的顶点中，找到离vs最近的顶点(k)。
            int min = INF;
            for (int j = 0; j < mMatrix.length; j++) {
                if (flag[j]==false && dist[j]<min) {
                    min = dist[j];
                    k = j;
                }
            }
            // 标记"顶点k"为已经获取到最短路径
            flag[k] = true;

            // 修正当前最短路径和前驱顶点
            // 即，当已经求出"顶点k的最短路径"之后，更新"未获取最短路径的顶点的最短路径和前驱顶点"。
            for (int j = 0; j < mMatrix.length; j++) {
                int tmp = (mMatrix[k][j]==INF ? INF : (min + mMatrix[k][j]));
                if (flag[j]==false && (tmp<dist[j]) ) {
                    dist[j] = tmp;
                    prev[j] = k;
                }
            }
        }
    }

    public static String calcPath(int vs, int ve, int prev[], Stack<Integer> pathOut) {
        String path = "" + ve;
        pathOut.push(ve);
        int vep = prev[ve];
        while (vep != 0 && vs != vep) {
            path = vep + "->" + path;
            pathOut.push(vep);
            vep = prev[vep];
        }
        pathOut.push(vs);
        return vs + "->" + path;
    }

    public static void main(String []args) {
        int stops[][] = new int [][] {
                {0,   12,INF,INF,INF,16,14},
                {12,   0,10,INF,INF, 7,INF},
                {INF, 10, 0, 3, 5, 6,INF},
                {INF,INF, 3, 0, 4,INF,INF},
                {INF,INF, 5, 4, 0, 2, 8},
                {16,   7, 6, INF, 2, 0, 9},
                {14, INF,INF,INF, 8, 9, 0}
        };

        int vs = 0;
        int prev[] = new int[stops.length];
        int dist[] = new int[stops.length];
        dijkstra(vs, stops, prev, dist);
    }
}
