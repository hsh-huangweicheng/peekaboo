package analyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import bean.NamedEntity;
import bean.NamedEntityType;
import table.Table;
import utils.NlpUtils;
import wos.WosRecord;

public class NlpFXAnalyzer implements Analyzer {

	private Set<String> utSet = new HashSet<>();

	private File outputFile;

	public NlpFXAnalyzer(File outputFile) {
		this.outputFile = outputFile;

		if (null != outputFile) {
			try {
				if (outputFile.exists()) {
					List<String> readLines = FileUtils.readLines(outputFile, "UTF-8");
					readLines.forEach(line -> {
						if (StringUtils.isNotBlank(line)) {
							utSet.add(line.split("\t")[0]);
						}
					});
				} else {
					String header = StringUtils.join(new String[] { "UT", "TYPE", "NAME", "IDENDITY" }, "\t");
					FileUtils.writeLines(outputFile, Arrays.asList(new String[] { header }));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void scan(WosRecord wosRecord) {

		String utValue = wosRecord.getString("UT");
		String fxValue = wosRecord.getString("FX");

		if (utSet.contains(utValue)) {
			System.out.println("\t" + utValue + " aleady processed.");
			return;
		}

		if (StringUtils.isNotBlank(fxValue)) {

			long timestamp = System.currentTimeMillis();
			int size = -1;
			try {
				List<NamedEntity> namedEntityList = NlpUtils.getNamedEntityList(fxValue);
				List<String> identityOfNamedEntity = getIdentityOfNamedEntity(namedEntityList, fxValue);

				List<String> lines = new ArrayList<>();
				size = namedEntityList.size();
				for (int i = 0; i < size; i++) {
					NamedEntity namedEntity = namedEntityList.get(i);
					String identity = identityOfNamedEntity.get(i);
					lines.add(StringUtils.join(new String[] { utValue, namedEntity.type, namedEntity.name, identity }, "\t"));
				}

				if (null != outputFile) {
					try {
						FileUtils.writeLines(this.outputFile, lines, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} finally {
				System.out.printf("\t%s in [%s] found [%2d] entities, used [%3.3f] seconds\r\n", utValue, wosRecord.file.getAbsolutePath(), size,
						(System.currentTimeMillis() - timestamp) / 1000.0);
			}
		}
	}

	@Override
	public Table[] getTables() {
		return null;
	}

	@Override
	public String getName() {
		return "NLP";
	}

	private List<String> getIdentityOfNamedEntity(List<NamedEntity> namedEntityList, String text) {
		int size = namedEntityList.size();
		List<String> identityList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			NamedEntity namedEntity = namedEntityList.get(i);
			final int beginPosition = namedEntity.getBeginPosition();
			int endPosition = text.length();
			if (i < (size - 1)) {
				NamedEntity nextEntity = namedEntityList.get(i + 1);
				endPosition = nextEntity.getBeginPosition();
			}

			String identityOfText = "";
			if (namedEntity.type.equals(NamedEntityType.ORGANIZATION)) {
				identityOfText = getIdentityOfText(text.substring(beginPosition, endPosition));
			}
			identityList.add(identityOfText);
		}
		return identityList;
	}

	public String getIdentityOfText(String text) {
		Pattern pattern = Pattern.compile("\\b(?![.A-Z/-]+\\b)([.A-Z/\\d-]{4,})[ \\]\\)\\}]");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

}
