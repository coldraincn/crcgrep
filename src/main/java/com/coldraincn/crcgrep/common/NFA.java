package com.coldraincn.crcgrep.common;

import java.util.Stack;

public class NFA {
    int m;//正则字符串个长度
    Digraph dg ;

    public NFA(String grep){
        m = grep.length();
        dg = new Digraph(m+1);
        var stack = new Stack<Integer>();
        for(int i = 0;i < m;i++){
            //左括号和分隔符，存入栈中
            if(grep.charAt(i) == '(' || grep.charAt(i) == '|'){
                stack.push(i);
            }   
            //右括号，处理分隔符和*   
            if(grep.charAt(i) == ')'){
                if(grep.charAt(i+1) == '*'){
                    var stackHead = stack.peek();
                    if(stackHead != '('){
                        stackHead =stack.peek();
                    }
                    dg.addEdge(stackHead, i+1);
                    dg.addEdge(i+1, stackHead);
                }else{
                    var stackHead = stack.peek();
                    if(grep.charAt(stackHead) == '|'){
                        dg.addEdge(stackHead, i+1);
                        stack.pop();
                        var leftClose = stack.pop();
                        dg.addEdge(leftClose, stackHead+1);
                    }else{
                        stack.pop();
                    }
                }
                
            }else{
                dg.addEdge(i, i+1);
            }
        }
    }
    
}