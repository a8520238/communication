package com.wenda.communicationsystem.service;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 20:51 2020/5/26
 */
@Service
public class SentiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SentiveService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
            read.close();
        } catch(Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    public void addWord(String lineTxt) {
        TrieNode tempNode = root;
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            if (isSymbol(c)) {
                continue;
            }

            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;
            if (i == lineTxt.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        StringBuffer stringBuffer = new StringBuffer();
        String replacement = "***";
        TrieNode tempNode = root;
        int begin = 0;
        int position = 0;
        while (position < text.length()) {
            char c = text.charAt(position);

            if(isSymbol(c)) {
                if (tempNode == root) {
                    stringBuffer.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                stringBuffer.append(text.charAt(begin));
                position = begin + 1;
                begin++;
                tempNode = root;
            } else if (tempNode.isKeyWordEnd()) {
                stringBuffer.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = root;
            } else {
                position++;
            }
        }
        stringBuffer.append(text.substring(begin));
        return stringBuffer.toString();
    }

    private class TrieNode {
        private boolean end = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public  void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        TrieNode getSubNode(Character Key) {
            return subNodes.get(Key);
        }

        boolean isKeyWordEnd() {
            return end;
        }

        void setKeyWordEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode root = new TrieNode();

    private boolean isSymbol(char c) {
        int ic = (int) c;
        //东亚文字 0x2E80-0X9FFF
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public static void main(String[] argv) {
        SentiveService sentiveService = new SentiveService();
        sentiveService.addWord("赌博");
        sentiveService.addWord("色情");
        System.out.println(sentiveService.filter("你好色 情"));
    }
}
