package com.houlik.libhoulik.android.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by houlik on 2018/5/3.
 */

public class XMLUtils {

    /**
     * 父节点：Parent Node
     * 子节点：Children Node
     * 同级节点：Sibling Node
     *
     * 典型的 DOM 属性：
     * x.nodeName - x 的名称
     * x.nodeValue - x 的值
     * x.parentNode - x 的父节点
     * x.childNodes - x 的子节点
     * x.attributes - x 的属性节点
     *
     *
     * x.getElementsByTagName(name) - 获取带有指定标签名称的所有元素
     * x.appendChild(node) - 向 x 插入子节点
     * x.removeChild(node) - 从 x 删除子节点
     *
     * documentElement 属性是根节点。
     * nodeName 属性是节点的名称。
     * nodeType 属性是节点的类型。
     *
     *
     * nodeName 属性 是只读的
     * 元素节点的 nodeName 与标签名相同
     * 属性节点的 nodeName 是属性的名称
     * 文本节点的 nodeName 永远是 #text
     * 文档节点的 nodeName 永远是 #document
     *
     * nodeValue 属性 属性规定节点的值。
     * 元素节点的 nodeValue 是 undefined
     * 文本节点的 nodeValue 是文本自身
     * 属性节点的 nodeValue 是属性的值
     *
     * nodeType 属性 属性规定节点的类型 是只读的。
     * 最重要的节点类型是：
     * 元素类型 	节点类型
     * 元素 	    1
     * 属性 	    2
     * 文本 	    3
     * 注释 	    8
     * 文档 	    9
     *
     * NodeList
     * NamedNodeMap
     *
     *
     *
     *
     *
     *
     */


    private File file;
    private String url;
    private InputStream inputStream;
    public enum PATH_TYPE {FILE, URL , INPUTSTREAM}
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document document;
    private List<Map<String,String>> list = new ArrayList<>();

    public XMLUtils(File file, PATH_TYPE type) throws IOException, SAXException, ParserConfigurationException {
        this.file = file;
        initXML(type);
    }

    public XMLUtils(String url, PATH_TYPE type) throws IOException, SAXException, ParserConfigurationException {
        this.url = url;
        initXML(type);
    }

    public XMLUtils(InputStream inputStream, PATH_TYPE type) throws IOException, SAXException, ParserConfigurationException {
        this.inputStream = inputStream;
        initXML(type);
    }

    private void initXML(PATH_TYPE type) throws ParserConfigurationException, IOException, SAXException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        switch (type){
            case FILE:
                document = builder.parse(file);
                break;
            case URL:
                document = builder.parse(url);
                break;
            case INPUTSTREAM:
                document = builder.parse(inputStream);
                break;
        }
    }

    /**
     * 取得根节点元素
     * @param document
     * @return
     */
    public Element getXmlElement(Document document){
        return document.getDocumentElement();
    }

    /**
     * 取得子节点元素
     * @param element
     * @return
     */
    public NodeList getXmlNodeList(Element element){
        return element.getChildNodes();
    }

    /**
     * 取得子节点元素的值
     * @param nodeList
     * @return
     */
    public Node getXmlNode(NodeList nodeList){
        Node tmpNode = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            tmpNode = nodeList.item(i);
        }
        return tmpNode;
    }

    /**
     * 取得子节点元素的子节点元素
     * @param node
     * @return
     */
    public NodeList getXmlChildNode(Node node){
        return node.getChildNodes();
    }


    //############################################################################
    //未解析完

    public void parseXML(){



    }
}
