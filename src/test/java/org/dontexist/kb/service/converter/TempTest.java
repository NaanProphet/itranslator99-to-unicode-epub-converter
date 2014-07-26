//package org.dontexist.kb.service.converter;
//
//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
//import org.htmlparser.Node;
//import org.htmlparser.Parser;
//import org.htmlparser.filters.HasAttributeFilter;
//import org.htmlparser.filters.TagNameFilter;
//import org.htmlparser.tags.MetaTag;
//import org.htmlparser.util.NodeList;
//import org.htmlparser.util.ParserException;
//import org.htmlparser.util.SimpleNodeIterator;
//import org.junit.Test;
//import java.io.IOException;
//import java.net.URL;
//import java.util.LinkedList;
//import java.util.List;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//public class TempTest {
//
//    @Test
//    public void test() throws Exception {
//        String nestedSpanInput = "<span class=\"sans\">hari OM is<span class=\"pal\">japa</span>for the mind.</span>";
//
//        Parser parser = new Parser();
//
////        HasAttributeFilter filter = new HasAttributeFilter("class", "sans");
//        TagNameFilter filter = new TagNameFilter("span");
//        try {
//            parser.setInputHTML(nestedSpanInput);
//            NodeList list = parser.parse(filter);
//            Node node = list.elementAt(0);
//            SimpleNodeIterator iterator = list.elements();
//            while(iterator.hasMoreNodes()) {
//                final Node ithNode = iterator.nextNode();
//                System.out.println(ithNode.toHtml());
//                // getChildren returns the content of the span block
//                final String content1 = ithNode.getChildren().toHtml();
//                System.out.println(content1);
//                parser.setInputHTML(content1);
//                final NodeList list2 = parser.parse(filter);
//                SimpleNodeIterator iterator2 = list2.elements();
//                while(iterator2.hasMoreNodes()) {
//                    final Node ithNode2 = iterator2.nextNode();
//                    System.out.println(ithNode2.toHtml());
//                }
//
//                ithNode.getChildren().elementAt(0).
//
//                System.out.println("");
//            }
//
//            if (node instanceof MetaTag) {
//                MetaTag meta = (MetaTag) node;
//                String description = meta.getAttribute("content");
//
//                System.out.println(description);
//                // Prints: "YouTube is a place to discover, watch, upload and share videos."
//            }
//
//        } catch (ParserException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<String> getRssLinks() throws ParserConfigurationException,
//            SAXException, IOException
//    {
//        final List<String> rssLinks = new LinkedList<String>();
//        final URL url = new URL("http://news.yahoo.com/rss/");
//        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
//                .parse(url.openStream());
//        final org.w3c.dom.NodeList linkNodes = doc.getElementsByTagName("link");
//        for(int i = 0; i < linkNodes.getLength(); i++) {
//            final Element linkElement = (Element) linkNodes.item(i);
//            linkElement.getNodeValue()
//            rssLinks.add(linkElement.getTextContent());
//        }
//
//
//}
//
//
