package com.coldraincn.crcgrep.common;

import java.util.Stack;

public class NFA {
    int m;//正则字符串个长度
    Digraph dg ;

    public NFA(String grep){
        m = grep.length();
        dg = new Digraph(m+1);
        Stack<Integer> ops = new Stack<Integer>();
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
    
}