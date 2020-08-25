package com.coldraincn.crcgrep.common;

import java.util.ArrayList;
import java.util.List;

public class Digraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;//顶点的数量
    private int E;//边的数量
    private List<Integer>[] adj;
    private int[] indegree;

    public Digraph(int V){
        if(V < 0){
            throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        }
        this.V = V;
        this.E = 0;
        indegree = new int[V];
        adj = (List<Integer>[]) new ArrayList[V];
        for(int v = 0; v < V;v++){
            adj[v] = new ArrayList<Integer>();
        }
    }
    // public Digraph(System.in in){
    //     if (in == null) {
    //         throw new IllegalArgumentException("argument is null");
    //     }
    //     try {
    //         this.V = in.readInt()
    //     }
    // }
    public Digraph(Digraph G){
        if(G == null){
            throw new IllegalArgumentException("argument is null");
        }
        this.V = G.V();
        this.E = G.E();
        if(V < 0){
            throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        }
        indegree = new int[V];
        for(int v = 0;v < V;v++){
            this.indegree[v] = G.indegree(v);
        }
        adj = (List<Integer>[]) new ArrayList[V];
        for(int v = 0;v < V;v++){
            adj[v] = new ArrayList<Integer>();
        }
        
    }
    public int V(){
        return V;
    }
    public int E(){
        return E;
    }
    private void validateVertex(int v){
        if(v < 0 || v >= V){
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1))
        }
    }
    public void addEdge(int v,int w){
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        indegree[w]++;
        E++;
    }
    public Iterable<Integer> adj(int v){
        validateVertex(v);
        return adj[v];
    }
    public int outdegree(int v){
        validateVertex(v);
        return adj[v].size();
    }
    public int indegree(int v){
        validateVertex(v);
        return indegree[v];
    }

    public Digraph reverse(){

    }
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(V+" vertices, "+E+" edges"+NEWLINE);
        for(int v = 0;v<V;v++){
            s.append(String.format("%d: ", v)); 
            for(int w:adj[v]){
                s.append(String.format("%d", w));
            } 
            s.append(NEWLINE);       
        }
        return s.toString();

    }
}