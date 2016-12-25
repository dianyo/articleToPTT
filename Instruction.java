import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.lang.model.util.Elements;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by dianyo on 2016/12/4.
 */
public class Instruction {
    private String fileName;
    private int[] articleNum;
    private int loginCount;
    private String[][] login;
    private String[][] board;
    private String[][] title;
    private String[][] content;

    public Instruction(String fileName) {
        this.fileName = fileName;
        this.dealWithFile();
    }

    private void dealWithFile() {
        try {
            //open file and add root element
            File xmlFile = new File(fileName);
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            String allFile = new String(encoded, "utf-8");
            allFile = allFile.replaceAll("</PASS>((.*?(\n)*?)*?)<EXIT>", "</PASS><ARTICLE>$1</ARTICLE><EXIT>");
            allFile = allFile.replaceAll("(<ID>|<PASS>|<BOARD>|<P>)(.*)(</ID>|</PASS>|</BOARD>|</P>)", "$1<![CDATA[$2]]>$3");
            allFile = allFile.replaceAll("<CONTENT>((.*?(\n)*?)*?)</CONTENT>", "<CONTENT><![CDATA[$1]]></CONTENT>");
            allFile = allFile.replaceAll("<EXIT>", "<EXIT></EXIT>");
            System.out.print(allFile);
            List<InputStream> streams =
                    Arrays.asList(new ByteArrayInputStream("<root>".getBytes()),
                            new ByteArrayInputStream(allFile.getBytes()),
                            new ByteArrayInputStream("</root>".getBytes()));
            InputStream cntr = new SequenceInputStream(Collections.enumeration(streams));
            //parse xml
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(cntr);
            doc.getDocumentElement().normalize();
            //set variables
            loginCount = doc.getElementsByTagName("ID").getLength();
            login = new String[loginCount][2];
            articleNum = new int[loginCount];
            board = new String[loginCount][1000];
            title = new String[loginCount][1000];
            content = new String[loginCount][1000];
            for (int i = 0 ; i < loginCount; i++) {
                login[i][0] = doc.getElementsByTagName("ID").item(i).getTextContent();
                login[i][1] = doc.getElementsByTagName("PASS").item(i).getTextContent();
                articleNum[i] = (doc.getElementsByTagName("ARTICLE").item(i).getChildNodes()
                        .getLength() - 1) / 6;
                for (int j = 0; j < articleNum[i]; j++) {
                    board[i][j] = doc.getElementsByTagName("BOARD").item(i).getTextContent();
                    title[i][j] = doc.getElementsByTagName("P").item(i).getTextContent();
                    content[i][j] = doc.getElementsByTagName("CONTENT").item(i).getTextContent();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int getArticleNum(int i) {
        return this.articleNum[i];
    }

    public int getLoginCount() {
        return this.loginCount;
    }

    public String[] getLogin(int i) {
        return this.login[i];
    }

    public String getBoard(int i, int j) {
        return this.board[i][j];
    }

    public String getTitle(int i, int j) {
        return  this.title[i][j];
    }

    public String getContent(int i, int j) {
        return this.content[i][j];
    }
}
