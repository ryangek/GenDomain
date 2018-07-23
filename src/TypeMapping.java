public class TypeMapping {
	private String[] typeSQL;
	private String[] typeJava;
	public TypeMapping (String[] typeSQL, String[] typeJava){
		this.setTypeSQL(typeSQL);
		this.setTypeJava(typeJava);
	}
	public String TypeMap (String str) {
		for(int i=0; i<typeSQL.length; i++) {
			if (typeSQL[i].equals(str)) {
				return typeJava[i];
			}
		}
		return "Something";
	}
	public String[] getTypeSQL() {
		return typeSQL;
	}
	public void setTypeSQL(String[] typeSQL) {
		this.typeSQL = typeSQL;
	}
	public String[] getTypeJava() {
		return typeJava;
	}
	public void setTypeJava(String[] typeJava) {
		this.typeJava = typeJava;
	}
}
