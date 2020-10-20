package com.coldraincn.crcgrep.common;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class NFA {
    private final int m;//正则字符串个长度
    private Digraph dg ;
    private String regexp;

    public NFA(String grep){
        this.regexp = grep;
        m = grep.length();
        dg = new Digraph(m+1);
        var ops = new Stack<Integer>();
        for(int i = 0;i < m;i++){
            int lp = i; 
            if (grep.charAt(i) == '(' || grep.charAt(i) == '|') 
                ops.push(i); 
            else if (grep.charAt(i) == ')') {
                int or = ops.pop(); 

                // 2-way or operator
                if (grep.charAt(or) == '|') { 
                    lp = ops.pop();
                    dg.addEdge(lp, or+1);
                    dg.addEdge(or, i);
                }
                else if (grep.charAt(or) == '(')
                    lp = or;
                else assert false;
            } 

            // closure operator (uses 1-character lookahead)
            if (i < m-1 && grep.charAt(i+1) == '*') { 
                dg.addEdge(lp, i+1); 
                dg.addEdge(i+1, lp); 
            } 
            if (grep.charAt(i) == '(' || grep.charAt(i) == '*' || grep.charAt(i) == ')') {
                dg.addEdge(i, i+1);
            }
               
        }
        if (ops.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    }
    public boolean recognizes(String txt){
        var dfs =new DirectedDFS(dg,0);
        int n = txt.length();
        var direList = new ArrayList<Integer>();
        for(int v = 0;v<dg.V();v++){
            if(dfs.marked(v)){
                direList.add(v);
            }
        }
        for(int i = 0;i<n;i++){
            if (txt.charAt(i) == '*' || txt.charAt(i) == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')')
                throw new IllegalArgumentException("text contains the metacharacter '" + txt.charAt(i) + "'");
            var match = new ArrayList<Integer>();
            for(int j:direList){
                if(j == m){
                    continue;
                }
                if(regexp.charAt(j) == txt.charAt(i) || regexp.charAt(j) == '.'){
                    match.add(j+1);
                }
            }
            dfs = new DirectedDFS(dg,match);
            direList = new ArrayList<Integer>();
            for(int v = 0;v<dg.V();v++){
                if(dfs.marked(v)){
                    direList.add(v);
                }
            }
            if(direList.size() == 0){
                return false;
            }
        }
        for (int j:direList){
            if(j == m){
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {
        var stdin = new Scanner(System.in);
        System.out.println("grep:");
        String regexp = "(" + stdin.nextLine() + ")";
        System.out.println("test String:");
        String txt = stdin.nextLine();
        Long startTime = Instant.now().toEpochMilli();
        NFA nfa = new NFA(regexp);
        Long endTime = Instant.now().toEpochMilli();
        System.out.println(nfa.recognizes(txt));
        long duration = endTime - startTime;
        System.out.println("duration:"+duration);
    }
}