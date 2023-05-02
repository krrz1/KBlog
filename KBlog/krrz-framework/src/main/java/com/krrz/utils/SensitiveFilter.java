package com.krrz.utils;

import com.alibaba.excel.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger= LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT="***";

    //根节点
    private TrieNode rootTrieNode=new TrieNode();

    @PostConstruct
    public void init(){
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader=new BufferedReader(new InputStreamReader(resourceAsStream));
            {
                String keyword;
                while((keyword=reader.readLine())!=null){
                    //添加到前缀树
                    this.addKeyWord(keyword);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //将一个敏感词添加到树中去
    private void addKeyWord(String keyword) {
        TrieNode tempNode=rootTrieNode;
        for(int i=0;i<keyword.length();i++){
            char c=keyword.charAt(i);
            TrieNode subNode=tempNode.getSubNode(c);
            if(subNode==null){
                //初始化子节点
                subNode=new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            tempNode=subNode;
            //设置结束标识
            if(i==keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /*
     *@Description: 过滤敏感词
     *@Params:带过滤的文本
     *@Return:过滤后的文本
     *@Author:krrz
     *@Date:2023/4/26
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针1
        TrieNode tempNode=rootTrieNode;
        //指针2
        int begin=0;
        //指针3
        int position=0;
        StringBuilder ans=new StringBuilder();
        while(position<text.length()){
            char c=text.charAt(position);
//            if(isSymbol(c)){
//                if(tempNode==rootTrieNode){
//                    ans.append(c);
//                    begin++;
//                }
//                //无论符号在开头还是中间
//                position++;
//                continue;
//            }
            //检查下节点
            tempNode=tempNode.getSubNode(c);
            if(tempNode==null){
                //以begin开头的字符串不是敏感词
                ans.append(text.charAt(begin));
                position=++begin;
                tempNode=rootTrieNode;
            }else if(tempNode.isKeywordEnd()){
                //发现敏感词
                ans.append(REPLACEMENT);
                begin=++position;
                tempNode=rootTrieNode;
            }else{
                //检查下一个字符
                position++;
            }
        }

        //将最后一批字符计入结果
        ans.append(text.substring(begin));

        return ans.toString();
    }

    private boolean isSymbol(Character c){
        return !(c<0x2E80 || c>0x9FFF);
    }

    //前缀树节点
    private class TrieNode{
        //关键词结束标识
        private boolean isKeywordEnd=false;

        private Map<Character,TrieNode> subNode=new HashMap<>();

        public void addSubNode(Character c,TrieNode node){
            subNode.put(c,node);
        }

        public TrieNode getSubNode(Character c){
            return subNode.get(c);
        }

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
    }
}
