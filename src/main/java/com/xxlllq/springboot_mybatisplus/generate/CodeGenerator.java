package com.xxlllq.springboot_mybatisplus.generate;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.print.Book;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("xiangxl");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);


        // 数据源配置----修改这里
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/mybatis_plus?useUnicode=true&useSSL=false&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        List<Moulde> list = parseXml(projectPath);
        if (list != null && !list.isEmpty()) {

            list.forEach(m -> {
                // 包配置
                PackageConfig pc = new PackageConfig();
                pc.setModuleName(m.getName());
                pc.setParent("com.xxlllq.springboot_mybatisplus");
                mpg.setPackageInfo(pc);


                // 自定义配置
                InjectionConfig cfg = new InjectionConfig() {
                    @Override
                    public void initMap() {
                        // to do nothing
                    }
                };

                List<FileOutConfig> focList = new ArrayList<>();
                //ftl为使用Freemarker引擎的时候，具体参考https://github.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-generator/src/main/resources/templates
                focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输入文件名称
                        return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                                + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                    }
                });
                cfg.setFileOutConfigList(focList);
                mpg.setCfg(cfg);
                mpg.setTemplate(new TemplateConfig().setXml(null));

                // 策略配置
                StrategyConfig strategy = new StrategyConfig();
                strategy.setNaming(NamingStrategy.underline_to_camel);
                strategy.setColumnNaming(NamingStrategy.underline_to_camel);
                strategy.setSuperEntityClass("com.xxlllq.springboot_mybatisplus.base.entity.BaseEntity");
                strategy.setEntityLombokModel(true);
                strategy.setRestControllerStyle(true);
                strategy.setSuperControllerClass("com.xxlllq.springboot_mybatisplus.base.controller.BaseController");

                String[] strArray = m.getList().toArray(new String[m.getList().size()]);
                strategy.setInclude(strArray);
                strategy.setSuperEntityColumns("id");
                strategy.setControllerMappingHyphenStyle(true);
                strategy.setTablePrefix(pc.getModuleName() + "_");
                mpg.setStrategy(strategy);
                mpg.setTemplateEngine(new FreemarkerTemplateEngine());
                mpg.execute();
            });
        }
    }

    static List<Moulde> parseXml(String path) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(path + "/src/main/resources/generate/" + "generate.xml");
            Element root = doc.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("module");

            if (nodeList != null && nodeList.getLength() > 0) {
                List<Moulde> list = new ArrayList<>();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    // 获取一个节点
                    Node node = nodeList.item(i);
                    // 获取该节点所有属性
                    NamedNodeMap attributes = node.getAttributes();
                    Moulde moulde = new Moulde();

                    //设置模块名称
                    NamedNodeMap namedNodeMap = node.getAttributes();
                    moulde.setName(namedNodeMap.getNamedItem("id").getTextContent());

                    //数据库表
                    NodeList childs = node.getChildNodes();
                    List<String> listStr = new ArrayList<>();
                    for (int j = 1; j < childs.getLength(); j += 2) {
                        Node cNode = childs.item(j);
                        listStr.add(cNode.getFirstChild().getTextContent());
                    }
                    moulde.setList(listStr);
                    list.add(moulde);
                }
                return list;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Data
    public static class Moulde {
        private String name;
        private List<String> list;
    }
}