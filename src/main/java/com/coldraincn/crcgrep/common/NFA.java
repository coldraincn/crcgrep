package com.coldraincn.crcgrep.common;

import java.util.ArrayList;
import java.util.Stack;

public class NFA {
    int m;//正则字符串个长度
    Digraph dg ;
    String regexp; 

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
            char nowChar = txt.charAt(i);
            var match = new ArrayList<Integer>();
            for(int j:direList){
                if(j == m){
                    continue;
                }
                if(regexp.charAt(j) == nowChar | regexp.charAt(j) == '.'){
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
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        NFA nfa = new NFA(regexp);
        System.out.println(nfa.recognizes(txt));
    }
}