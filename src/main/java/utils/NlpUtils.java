package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

import bean.NamedEntityType;
import bean.NamedEntity;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class NlpUtils {

	private static final String[] VALID_TYPES = new String[] { NamedEntityType.LOCATION, NamedEntityType.PERSON, NamedEntityType.ORGANIZATION };

	public static List<NamedEntity> getNamedEntityList(String text) {

		List<NamedEntity> retList = new ArrayList<>();

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(text);
		pipeline.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		String name = "";
		int beginPosition = 0;
		String lastNe = "";

		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String ne = token.get(NamedEntityTagAnnotation.class);

				if (ne.equals(lastNe)) {
					name += " " + word;
				} else {
					appendNamedEntity(retList, beginPosition, name, lastNe);
					beginPosition = token.beginPosition();
					name = word;
				}

				lastNe = ne;
			}
		}

		appendNamedEntity(retList, beginPosition, name, lastNe);

		return retList;
	}

	private static void appendNamedEntity(List<NamedEntity> list, int beginPosition, String name, String ne) {

		boolean validNe = ArrayUtils.contains(VALID_TYPES, ne);

		if (validNe && !"".equals(name)) {
			list.add(new NamedEntity(beginPosition, name, ne));
		}
	}
}
