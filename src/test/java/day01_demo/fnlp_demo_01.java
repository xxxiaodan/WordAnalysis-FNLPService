package day01_demo;

import org.fnlp.nlp.cn.CNFactory;
import org.junit.Test;

public class fnlp_demo_01 {
    public static void main(String[] args) throws Exception {

        // 创建中文处理工厂对象，并使用“models”目录下的模型文件初始化
        CNFactory factory = CNFactory.getInstance("F:/IdeaProjects/fnlp/models");

        // 使用标注器对中文句子进行标注，得到标注结果
        String result = factory.tag2String("关注自然语言处理、语音识别、深度学习等方向的前沿技术和业界动态。");

        // 显示标注结果
        System.out.println(result);

    }
}
