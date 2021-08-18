package dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyH2Dialect extends H2Dialect {

  // * 사용자 함수 추가 시
  public MyH2Dialect() {
    registerFunction(
        "group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING)); // 함수 등록

  }
}
