package com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail.support;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.domain.exception.error.ScraperErrorCode;

/**
 * XML 문자열을 파싱하여 JSON 문자열로 변환하는 클래스입니다.
 */
public class XMLParser {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

	/**
	 * XML 문자열을 파싱하여 JSON 문자열로 변환합니다.
	 *
	 * @param xml 변환할 XML 문자열
	 * @return 변환된 JSON 문자열
	 */
	public static String toJson(String xml) {

		if(isXmlNull(xml)) {
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
		parseSesctions(root, allSections, sectionMap);
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
	 */
	private static String convertJson(DocTag docTag) {
		try {
			return mapper.writeValueAsString(docTag);
			//TODO: 예외처리 후 삭제
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * XML에서 PARAGRAPH 태그를 파싱하여 ParagraphTag 리스트에 추가합니다.
	 *
	 * @param root XML 루트 엘리먼트
	 * @param allParagraphs 파싱된 ParagraphTag 리스트
	 * @param articleMap ARTICLE 엘리먼트와 ArticleTag 매핑 정보
	 */
	private static void parseParagraph(Element root, List<ParagraphTag> allParagraphs, Map<Element, ArticleTag> articleMap) {
		NodeList paraNodes = root.getElementsByTagName("PARAGRAPH");
		
		if(paraNodes.getLength() != 0){
			for (int i = 0; i < paraNodes.getLength(); i++) {
				Element paragraphElement  = (Element) paraNodes.item(i);
				ParagraphTag paragraphTag = new ParagraphTag();
				paragraphTag.tagName      = paragraphElement.getAttribute("tagName");
				paragraphTag.textIndent   = paragraphElement.getAttribute("textIndent");
				paragraphTag.marginLeft   = paragraphElement.getAttribute("marginLeft");
				paragraphTag.text         = paragraphElement.getTextContent().trim();

				allParagraphs.add(paragraphTag);
				mapSectionFromArticle(articleMap, paragraphTag, paragraphElement);
			}
		}

	}

	/**
	 * XML에서 ARTICLE 태그를 파싱하여 ArticleTag 리스트에 추가합니다.
	 *
	 * @param root XML 루트 엘리먼트
	 * @param allArticles 파싱된 ArticleTag 리스트
	 * @param articleMap ARTICLE 엘리먼트와 ArticleTag 매핑 정보
	 * @param sectionMap SECTION 엘리먼트와 SectionTag 매핑 정보
	 */
	private static void parseArticles(Element root, List<ArticleTag> allArticles,
									Map<Element, ArticleTag> articleMap,
									Map<Element, SectionTag> sectionMap) {
		NodeList artNodes = root.getElementsByTagName("ARTICLE");
		if(artNodes.getLength() > 0) {
			for (int i = 0; i < artNodes.getLength(); i++) {
				Element artElement = (Element) artNodes.item(i);
				ArticleTag articleTag  = new ArticleTag();
				articleTag.title       = artElement.getAttribute("title");
				articleTag.paragraphs  = new ArrayList<>();

				allArticles.add(articleTag);
				articleMap.put(artElement, articleTag);
				mapSectionFromArticle(sectionMap, articleTag, artElement);
			}
		}

	}

	/**
	 * 상위 엘리먼트를 기반으로 해당 태그를 부모 태그에 연결합니다.
	 *
	 * @param map 상위 엘리먼트와 태그 매핑 정보
	 * @param tags 현재 태그
	 * @param element 현재 엘리먼트
	 */
	private static void mapSectionFromArticle(Map<Element, ? extends Tags>  map, Tags tags, Element element) {
		Element parentElement = (Element) element.getParentNode();
		Tags parentTag = map.get(parentElement);
		if (parentTag != null) {
			parentTag.addTag(tags);
		}
	}

	/**
	 * XML에서 SECTION 태그를 파싱하여 SectionTag 리스트에 추가합니다.
	 *
	 * @param root XML 루트 엘리먼트
	 * @param allSections 파싱된 SectionTag 리스트
	 * @param sectionMap SECTION 엘리먼트와 SectionTag 매핑 정보
	 */
	private static void parseSesctions(Element root, List<SectionTag> allSections, Map<Element, SectionTag> sectionMap) {
		NodeList secNodes = root.getElementsByTagName("SECTION");

		if(secNodes.getLength() > 0) {
			for (int i = 0; i < secNodes.getLength(); i++) {
				Element secEl     = (Element) secNodes.item(i);
				SectionTag secDto = new SectionTag();
				secDto.title      = secEl.getAttribute("title");
				secDto.articles   = new ArrayList<>();

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
	 */
	private static Document parseXmlString(String xml) {
		try {
			return documentBuilderFactory.newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			throw new ScraperException(ScraperErrorCode.PARSING_ERROR);
		}
	}

	/**
	 * XML 문자열이 null 이거나 비어있는지 확인합니다.
	 *
	 * @param xml 확인할 XML 문자열
	 * @return null 또는 비어있으면 true, 그렇지 않으면 false
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
	 */
	private static class DocTag implements Tags {
		public String title;
		public String type;
		public List<SectionTag> sections;

		DocTag(Element root, List sections) {
			this.title    = root.getAttribute("title");
			this.type     = root.getAttribute("type");
			this.sections = sections;
		}

		@Override
		public void addTag(Tags tags) {
			sections.add((SectionTag) tags);
		}

		@Override
		public boolean equalsClass(Tags tags) {
			return tags instanceof DocTag;
		}
	}

	/**
	 * SECTION 태그를 표현하는 클래스입니다.
	 */
	private static class SectionTag implements Tags {
		public String title;
		public List<ArticleTag> articles;

		@Override
		public void addTag(Tags tags) {
			articles.add((ArticleTag)tags);
		}

		@Override
		public boolean equalsClass(Tags tags) {
			return tags instanceof SectionTag;
		}
	}

	/**
	 * ARTICLE 태그를 표현하는 클래스입니다.
	 */
	private static class ArticleTag implements Tags {
		public String title;
		public List<ParagraphTag> paragraphs;

		@Override
		public void addTag(Tags tags) {
			paragraphs.add((ParagraphTag) tags);
		}

		@Override
		public boolean equalsClass(Tags tags) {
			return tags instanceof ArticleTag;
		}
	}

	/**
	 * PARAGRAPH 태그를 표현하는 클래스입니다.
	 */
	private static class ParagraphTag implements Tags {
		public String tagName;
		public String textIndent;
		public String marginLeft;
		public String text;

		@Override
		public void addTag(Tags tags) {
			//TODO: addTag Exception 하위 없음
		}

		@Override
		public boolean equalsClass(Tags tags) {
			return tags instanceof ParagraphTag;
		}
	}

	/**
	 * 태그 클래스 간 공통 인터페이스입니다.
	 */
	private interface Tags {
		void addTag(Tags tags);
		boolean equalsClass(Tags tags);
	}
}