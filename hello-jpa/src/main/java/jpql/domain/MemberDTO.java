package jpql.domain;

public class MemberDTO {

    private String username;
    private int age;

    public MemberDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public MemberDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public int getAge() {
        return age;
    }

    public MemberDTO setAge(int age) {
        this.age = age;
        return this;
    }
}
