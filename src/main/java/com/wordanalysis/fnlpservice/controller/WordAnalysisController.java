package com.wordanalysis.fnlpservice.controller;


import com.alibaba.fastjson.JSONObject;
import com.wordanalysis.fnlpservice.util.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.fnlp.ml.types.Dictionary;
import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.nlp.cn.tag.CWSTagger;
import org.fnlp.nlp.cn.tag.NERTagger;
import org.fnlp.nlp.cn.tag.POSTagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.wordanalysis.fnlpservice.util.FNLPProperties;
import java.io.*;
import java.util.HashMap;


@Api(tags="词法分析器")
@RestController("/WordAnalysis")
@EnableConfigurationProperties(FNLPProperties.class)
public class WordAnalysisController {

    @Autowired
    @Qualifier("FNLPProperties")
    private FNLPProperties fnlpProperties;


    @ApiOperation("FNLPService-file")
    @RequestMapping(value="/FNLPService-file",method= RequestMethod.POST)
    public Res wordAnalysis(@ApiParam("待处理数据(文件)") @RequestParam MultipartFile text_file,
                            @ApiParam("文本长度上限(默认：500Kb)") @RequestParam(defaultValue = "500") Long max_input,
                            @ApiParam("用户词典") @RequestParam MultipartFile customdict_file) throws Exception {
        if (text_file.isEmpty()) return Res.error("上传文件为空");
        if (text_file.getSize() > max_input*1024) return Res.error("上传文件超过上限(<=" + max_input + "Kb)");

        String text = new String(text_file.getBytes(),"UTF-8");
        String sentence = text.replace("\r\n", "");

        if (!customdict_file.isEmpty() && customdict_file.getName()!=null){
            String filePath = fnlpProperties.getTempFilePath() + customdict_file.getOriginalFilename();
            File tempFile = new File(filePath);
            FileUtils.copyInputStreamToFile(customdict_file.getInputStream(), tempFile);


            CWSTagger cwstagger = new CWSTagger(fnlpProperties.getSegPath(),new Dictionary(filePath));
            String cwstagger_words = cwstagger.tag(sentence);
            System.out.println(cwstagger_words);

            POSTagger postagger = new POSTagger(fnlpProperties.getSegPath(),fnlpProperties.getPosPath(),new Dictionary(filePath));
            String postagger_words = postagger.tag(sentence);
            System.out.println(postagger_words);

            NERTagger nertagger = new NERTagger(postagger);
            HashMap<String, String> nertagger_words = nertagger.tag(sentence);
            System.out.println(nertagger_words);


            JSONObject sent_obj = new JSONObject();
            sent_obj.put("sentence",sentence);
            sent_obj.put("cutsom_dictionary_use","yes");
            JSONObject items = new JSONObject();
            items.put("cwstagger_words",cwstagger_words);
            items.put("postagger_words",postagger_words);
            items.put("nertagger_words",nertagger_words);
            sent_obj.put("items",items);

            tempFile.delete();
            return Res.ok().put("results",sent_obj);
        }

        CNFactory seg_factory = CNFactory.getInstance(fnlpProperties.getRoot());
        String[] seg_words = seg_factory.seg(sentence);
        System.out.println(seg_words);

        CNFactory pos_factory = CNFactory.getInstance(fnlpProperties.getRoot());
        String pos_words = pos_factory.tag2String(sentence);
        System.out.println(pos_words);

        CNFactory ner_factory = CNFactory.getInstance(fnlpProperties.getRoot());
        HashMap<String, String> ner_words = ner_factory.ner(sentence);
        System.out.println(ner_words);

        JSONObject sent_obj = new JSONObject();
        sent_obj.put("sentence",sentence);
        sent_obj.put("cutsom_dictionary_use","no");
        JSONObject items = new JSONObject();
        items.put("seg_words",seg_words);
        items.put("pos_words",pos_words);
        items.put("ner_words",ner_words);
        sent_obj.put("items",items);
        return Res.ok().put("results",sent_obj);
    }

    @ApiOperation("FNLPService-text")
    @RequestMapping(value="/FNLPService-text",method= RequestMethod.POST)
    public Res wordAnalysis(@ApiParam("待处理数据(文本)") @RequestParam  String sentence,
                            @ApiParam("文本长度上限(默认：200)") @RequestParam(defaultValue = "200") int max_input,
                            @ApiParam("用户词典") @RequestParam MultipartFile customdict_file) throws Exception {
        if (sentence.length() > max_input) return Res.error("输入文本长度超过上限" + "(≤" + max_input + ")");
        sentence = sentence.replace("\r\n","");

        if (!customdict_file.isEmpty() && customdict_file.getName()!= null){
            String filePath = fnlpProperties.getTempFilePath() + customdict_file.getOriginalFilename();
            File tempFile = new File(filePath);
            FileUtils.copyInputStreamToFile(customdict_file.getInputStream(), tempFile);

            CWSTagger cwstagger = new CWSTagger(fnlpProperties.getSegPath(),new Dictionary(filePath));
            String cwstagger_words = cwstagger.tag(sentence);
            System.out.println(cwstagger_words);

            POSTagger postagger = new POSTagger(fnlpProperties.getSegPath(),fnlpProperties.getPosPath(),new Dictionary(filePath));
            String postagger_words = postagger.tag(sentence);
            System.out.println(postagger_words);

            NERTagger nertagger = new NERTagger(postagger);
            HashMap<String, String> nertagger_words = nertagger.tag(sentence);
            System.out.println(nertagger_words);


            JSONObject sent_obj = new JSONObject();
            sent_obj.put("sentence",sentence);
            sent_obj.put("cutsom_dictionary_use","yes");
            JSONObject items = new JSONObject();
            items.put("cwstagger_words",cwstagger_words);
            items.put("postagger_words",postagger_words);
            items.put("nertagger_words",nertagger_words);
            sent_obj.put("items",items);

            tempFile.delete();
            return Res.ok().put("results",sent_obj);
        }

        CNFactory seg_factory = CNFactory.getInstance(fnlpProperties.getRoot());
        String[] seg_words = seg_factory.seg(sentence);
        System.out.println(seg_words);

        CNFactory pos_factory = CNFactory.getInstance(fnlpProperties.getRoot());
        String pos_words = pos_factory.tag2String(sentence);
        System.out.println(pos_words);

        CNFactory ner_factory = CNFactory.getInstance(fnlpProperties.getRoot());
        HashMap<String, String> ner_words = ner_factory.ner(sentence);
        System.out.println(ner_words);

        JSONObject sent_obj = new JSONObject();
        sent_obj.put("sentence",sentence);
        sent_obj.put("cutsom_dictionary_use","no");
        JSONObject items = new JSONObject();
        items.put("seg_words",seg_words);
        items.put("pos_words",pos_words);
        items.put("ner_words",ner_words);
        sent_obj.put("items",items);
        return Res.ok().put("results",sent_obj);
    }
}

