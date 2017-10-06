package bean;

public class NamedEntity {

	public final String name;
	public final String type;
	private int beginPosition;

	public NamedEntity(int beginPosition, String name, String type) {
		this.setBeginPosition(beginPosition);
		this.name = name;
		this.type = type;
	}

	public String toString() {
		return this.name;
	}

	public int getBeginPosition() {
		return beginPosition;
	}

	public void setBeginPosition(int beginPosition) {
		this.beginPosition = beginPosition;
	}
	
}
