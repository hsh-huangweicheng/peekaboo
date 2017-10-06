import utils.NlpUtils;

public class TestNLP {

	public void test() {
		// 鏋勯�犱竴涓猄tanfordCoreNLP瀵硅薄锛岄厤缃甆LP鐨勫姛鑳斤紝濡俵emma鏄瘝骞插寲锛宯er鏄懡鍚嶅疄浣撹瘑鍒瓑

		String text = "NSF's Office of International Science" + "   and Engineering (Americas Program), grant EAR-0634807, and is gratefully"
				+ "   acknowledged. Additional funding was provided by PIP CONICET"
				+ "   11220080102828, UBACYT X220, and an IRM visiting fellowship granted to"
				+ "   M. Domeier. E. Tohver acknowledges funding from the Australian Research"
				+ "   Council (LP0991834) and use of the UWA-Curtin joint facilities for"
				+ "   imaging (Centre for Microscopy, Characterization and Analysis) and U-Pb"
				+ "   SHRIMP II facility at the John de Laeter Centre for Geochronology.";
		System.out.println(System.currentTimeMillis());
		System.out.println(NlpUtils.getNamedEntityList(text));
		System.out.println(System.currentTimeMillis());
		System.out.println(NlpUtils.getNamedEntityList(text));
		System.out.println(System.currentTimeMillis());
		System.out.println(NlpUtils.getNamedEntityList(text));
		System.out.println(System.currentTimeMillis());
	}

	public static void main(String[] args) {
		TestNLP nlp = new TestNLP();
		nlp.test();
	}

}