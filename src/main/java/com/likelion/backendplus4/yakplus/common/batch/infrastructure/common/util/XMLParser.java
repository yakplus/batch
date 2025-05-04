package com.likelion.backendplus4.yakplus.common.batch.infrastructure.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchError;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchException;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.application.service.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.application.service.exception.error.ScraperErrorCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * XML 문자열을 파싱하여 JSON 문자열로 변환하는 클래스입니다.
 *
 * @since 2025-04-21
 */
public class XMLParser {
    private static final Pattern DECIMAL_ENTITY_REGEX = Pattern.compile("&#(\\d+);");
    private static final Pattern HEX_ENTITY_REGEX = Pattern.compile("&#x([0-9A-Fa-f]+);");

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    /**
     * XML 문자열을 파싱하여 JSON 문자열로 변환합니다.
     *
     * @param xml 변환할 XML 문자열
     * @return 변환된 JSON 문자열
     * @author 함예정, 이해창
     * @since 2025-04-21
     */
    public static String toJson(String xml) {

        if (isXmlNull(xml)) {
            return "{\"\": \"\"}";
        }

        Document doc = parseXmlString(xml);
        Element root = doc.getDocumentElement();

        List<SectionTag> allSections = new ArrayList<>();
        List<ArticleTag> allArticles = new ArrayList<>();
        List<ParagraphTag> allParagraphs = new ArrayList<>();

        Map<Element, SectionTag> sectionMap = new HashMap<>();
        Map<Element, ArticleTag> articleMap = new HashMap<>();

        DocTag docTag = new DocTag(root, allSections);
        parseSections(root, allSections, sectionMap);
        parseArticles(root, allArticles, articleMap, sectionMap);
        parseParagraph(root, allParagraphs, articleMap);
        return convertJson(docTag);
    }

    /**
     * DocTag 객체를 JSON 문자열로 변환합니다.
     *
     * @param docTag 변환할 DocTag 객체
     * @return JSON 문자열
     * @throws RuntimeException JSON 변환 실패 시
     * @author 함예정, 이해창
     * @since 2025-04-21
     */
    private static String convertJson(DocTag docTag) {
        try {
            return mapper.writeValueAsString(docTag);
        } catch (JsonProcessingException e) {
            log(LogLevel.ERROR, "JSON 변환 실패", e);
            throw new ParserBatchException(ParserBatchError.JSON_TYPE_CHANGE_FAIL);
        }
    }

    /**
     * XML에서 PARAGRAPH 태그를 파싱하여 ParagraphTag 리스트에 추가합니다.
     *
     * @param root          XML 루트 엘리먼트
     * @param allParagraphs 파싱된 ParagraphTag 리스트
     * @param articleMap    ARTICLE 엘리먼트와 ArticleTag 매핑 정보
     * @author 함예정, 이해창
     * @since 2025-04-21
     */
    private static void parseParagraph(Element root, List<ParagraphTag> allParagraphs, Map<Element, ArticleTag> articleMap) {
        NodeList paraNodes = root.getElementsByTagName("PARAGRAPH");

        if (paraNodes.getLength() != 0) {
            for (int i = 0; i < paraNodes.getLength(); i++) {
                Element paragraphElement = (Element) paraNodes.item(i);
                ParagraphTag paragraphTag = new ParagraphTag();
                paragraphTag.tagName = cleanText(paragraphElement.getAttribute("tagName"));
                paragraphTag.textIndent = cleanText(paragraphElement.getAttribute("textIndent"));
                paragraphTag.marginLeft = cleanText(paragraphElement.getAttribute("marginLeft"));
                paragraphTag.text = cleanText(paragraphElement.getTextContent().trim());

                if (!isEmptyTagNameOrTagText(paragraphTag)) {
                    allParagraphs.add(paragraphTag);
                }

                mapSectionFromArticle(articleMap, paragraphTag, paragraphElement);
            }
        }

    }

    private static boolean isEmptyTagNameOrTagText(ParagraphTag paragraphTag) {
        return paragraphTag.tagName.isEmpty() || paragraphTag.text.isEmpty();
    }

    /**
     * XML에서 ARTICLE 태그를 파싱하여 ArticleTag 리스트에 추가합니다.
     *
     * @param root        XML 루트 엘리먼트
     * @param allArticles 파싱된 ArticleTag 리스트
     * @param articleMap  ARTICLE 엘리먼트와 ArticleTag 매핑 정보
     * @param sectionMap  SECTION 엘리먼트와 SectionTag 매핑 정보
     * @author 함예정, 이해창
     * @since 2025-04-21
     */
    private static void parseArticles(Element root, List<ArticleTag> allArticles,
                                      Map<Element, ArticleTag> articleMap,
                                      Map<Element, SectionTag> sectionMap) {
        NodeList artNodes = root.getElementsByTagName("ARTICLE");
        if (artNodes.getLength() > 0) {
            for (int i = 0; i < artNodes.getLength(); i++) {
                Element artElement = (Element) artNodes.item(i);
                ArticleTag articleTag = new ArticleTag();
                articleTag.title = cleanText(artElement.getAttribute("title"));
                articleTag.paragraphs = new ArrayList<>();

                allArticles.add(articleTag);
                articleMap.put(artElement, articleTag);
                mapSectionFromArticle(sectionMap, articleTag, artElement);
            }
        }

    }

    /**
     * 상위 엘리먼트를 기반으로 해당 태그를 부모 태그에 연결합니다.
     *
     * @param map     상위 엘리먼트와 태그 매핑 정보
     * @param tags    현재 태그
     * @param element 현재 엘리먼트
     * @author 함예정, 이해창
     * @since 2025-04-21
     */
    private static void mapSectionFromArticle(Map<Element, ? extends Tags> map, Tags tags, Element element) {
        Element parentElement = (Element) element.getParentNode();
        Tags parentTag = map.get(parentElement);
        if (parentTag != null) {
            parentTag.addTag(tags);
        }
    }

    /**
     * XML에서 SECTION 태그를 파싱하여 SectionTag 리스트에 추가합니다.
     *
     * @param root        XML 루트 엘리먼트
     * @param allSections 파싱된 SectionTag 리스트
     * @param sectionMap  SECTION 엘리먼트와 SectionTag 매핑 정보
     * @author 함예정, 이해창
     * @since 2025-04-21
     */
    private static void parseSections(Element root, List<SectionTag> allSections, Map<Element, SectionTag> sectionMap) {
        NodeList secNodes = root.getElementsByTagName("SECTION");

        if (secNodes.getLength() > 0) {
            for (int i = 0; i < secNodes.getLength(); i++) {
                Element secEl = (Element) secNodes.item(i);
                SectionTag secDto = new SectionTag();
                secDto.title = cleanText(secEl.getAttribute("title"));
                secDto.articles = new ArrayList<>();

                allSections.add(secDto);
                sectionMap.put(secEl, secDto);
            }
        }
    }

    /**
     * XML 문자열을 파싱하여 Document 객체로 변환합니다.
     *
     * @param xml 파싱할 XML 문자열
     * @return 파싱된 Document 객체
     * @author 함예정, 이해창
     * @since 2025-04-21
     */
    private static Document parseXmlString(String xml) {
        try {
            return documentBuilderFactory.newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            log(LogLevel.ERROR, "XML 파싱 실패", e);
            throw new ScraperException(ScraperErrorCode.PARSING_ERROR);
        }
    }

    /**
     * XML 문자열이 null 이거나 비어있는지 확인합니다.
     *
     * @param xml 확인할 XML 문자열
     * @return null 또는 비어있으면 true, 그렇지 않으면 false
     * @author 함예정, 이해창
     * @since 2025-04-21
     */
    private static boolean isXmlNull(String xml) {
        if (xml == null || xml.trim().isEmpty() || xml == "null") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * XML 루트 태그를 표현하는 클래스입니다.
     * XML Parser 내부에서만 사용되는 클래스입니다.
     *
     * @since 2025-04-21
     */
    private static class DocTag implements Tags {
        public String title;
        public String type;
        public List<SectionTag> sections;

        DocTag(Element root, List sections) {
            this.title = cleanText(root.getAttribute("title"));
            this.type = root.getAttribute("type");
            this.sections = sections;
        }

        /**
         * DocTag에 Section 태그를 추가합니다.
         * Json 형태의 객체를 만드는 데 사용됩니다.
         *
         * @param tags 추가할 태그
         * @author 함예정
         * @since 2025-04-21
         */
        @Override
        public void addTag(Tags tags) {
            sections.add((SectionTag) tags);
        }

    }

    /**
     * SECTION 태그를 표현하는 클래스입니다.
     *
     * @since 2025-04-21
     */
    private static class SectionTag implements Tags {
        public String title;
        public List<ArticleTag> articles;

        /**
         * SectionTag에 Article 태그를 추가합니다.
         * Json 형태의 객체를 만드는 데 사용됩니다.
         *
         * @param tags
         * @author 함예정
         * @since 2025-04-21
         */
        @Override
        public void addTag(Tags tags) {
            articles.add((ArticleTag) tags);
        }

    }

    /**
     * ARTICLE 태그를 표현하는 클래스입니다.
     *
     * @since 2025-04-21
     */
    private static class ArticleTag implements Tags {
        public String title;
        public List<ParagraphTag> paragraphs;

        /**
         * ArticleTag에 Paragraph 태그를 추가합니다.
         * Json 형태의 객체를 만드는 데 사용됩니다.
         *
         * @param tags
         * @author 함예정
         * @since 2025-04-21
         */
        @Override
        public void addTag(Tags tags) {
            paragraphs.add((ParagraphTag) tags);
        }

    }

    /**
     * PARAGRAPH 태그를 표현하는 클래스입니다.
     *
     * @since 2025-04-21
     */
    private static class ParagraphTag implements Tags {
        public String tagName;
        public String textIndent;
        public String marginLeft;
        public String text;

        /**
         * ParagraphTag는 하위 태그를 가지지 않으므로 addTag 메서드는 구현하지 않습니다.
         *
         * @param tags
         * @author 함예정
         * @since 2025-04-21
         */
        @Override
        public void addTag(Tags tags) {
            log(LogLevel.WARN, "ParagraphTag는 하위 태그를 가지지 않습니다.");
        }

    }

    /**
     * 태그 클래스 간 공통 인터페이스입니다.
     *
     * @since 2025-04-21
     */
    private interface Tags {
        /**
         * 해당 클래스의 하위 태그를 추가하는 메서드입니다.
         * Json 형태의 객체를 만드는 데 사용됩니다.
         *
         * @param tags 추가할 태그
         * @author 함예정
         * @since 2025-04-21
         */
        void addTag(Tags tags);
    }

    /**
     * 불필요한 문자를 제거하여 텍스트를 정리합니다.
     *
     * @param text
     * @return String 정리된 텍스트
     * @author 박찬병
     * @since 2025-04-21
     */
    private static String cleanText(String text) {
        Pattern TAG_REGEX = Pattern.compile("<[^>]+>");
        String tempText = TAG_REGEX.matcher(text)
                .replaceAll("")
                .replaceAll("&nbsp;", " ")
                .replaceAll("● ", "")
                .replaceAll("○ ", "")
                .replaceAll("∎ ", "")
                .replaceAll("- ", "");
        return decodeHtml(tempText).trim();
    }

    /**
     * HTML 엔티티(10진수 &#DDD; 및 16진수 &#xHHHH;)를 대응하는 문자로 디코딩합니다. 예: "foo&#8226;bar" → "foo•bar",
     * "foo&#x2022;bar" → "foo•bar"
     *
     * @param input 엔티티를 포함한 문자열
     * @return 디코딩된 문자열
     * @author 박찬병
     * @modify 2025-05-03 함예정
     * - 메소드 분리
     */
    private static String decodeHtml(String input) {
        String tempText = decimalEntityDecode(input);
        String result = hexEntityDecode(tempText);
        return result;
    }

    /**
     * 문자열 내의 16진수 HTML 엔티티(&#xHHHH;)를 해당 유니코드 문자로 변환합니다.
     *
     * @param result 16진수 엔티티를 포함한 문자열
     * @return 변환된 문자열
     * @author 박찬병
     * @modify 2025-05-03 함예정
     * - 메소드 분리
     */
    private static String hexEntityDecode(String result) {
        Matcher hexMatcher = HEX_ENTITY_REGEX.matcher(result);
        StringBuffer sb = new StringBuffer();
        while (hexMatcher.find()) {
            int code = Integer.parseInt(hexMatcher.group(1), 16);
            hexMatcher.appendReplacement(sb,
                    Matcher.quoteReplacement(Character.toString((char) code)));
        }
        hexMatcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 문자열 내의 10진수 HTML 엔티티(&#DDD;)를 해당 유니코드 문자로 변환합니다.
     *
     * @param result 10진수 엔티티를 포함한 문자열
     * @return 변환된 문자열
     * @author 박찬병
     * @modify 2025-05-03 함예정
     * - 메소드 분리
     */
    private static String decimalEntityDecode(String result) {
        Matcher decMatcher = DECIMAL_ENTITY_REGEX.matcher(result);
        StringBuffer sb = new StringBuffer();
        while (decMatcher.find()) {
            int code = Integer.parseInt(decMatcher.group(1));
            decMatcher.appendReplacement(sb,
                    Matcher.quoteReplacement(Character.toString((char) code)));
        }
        decMatcher.appendTail(sb);
        return sb.toString();
    }
}