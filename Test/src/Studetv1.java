import java.io.Serializable;

public class Studetv1 implements Serializable {
	private static final long serialVersionUID = 3319006382455197956l;
	private int age;
	private int sex;
	private String a;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	private String namea;

	public String getNamea() {
		return namea;
	}

	public void setNamea(String namea) {
		this.namea = namea;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String toString() {
		return a + "\t" + age + "\t" + name + "\t" + sex + "\t";
	}
}
