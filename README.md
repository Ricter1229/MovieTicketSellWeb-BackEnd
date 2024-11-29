//////////////////////////////////////////////////////////////////

SQL 資料庫指令：

```sql
DROP TABLE if exists Member;

CREATE TABLE Member (
    account     varchar(20)     NOT NULL  primary key,
    password    varchar(20)     NOT NULL,
    email       varchar(40)     NOT NULL,
    phone_no     char(10)        NOT NULL,
    birth_date   datetime        NOT NULL,
)
GO

INSERT INTO Member values ('andy', 'andy', 'andy@gmail.com', '0123456789', '1990-07-05');
INSERT INTO Member values ('bob', 'bob', 'bob@gmail.com', '3456789012', '1993-11-23');
INSERT INTO Member values ('cclemon', 'cclemon', 'cclemon@gmail.com', '8134569072', '2014-03-01');
```

改變部分： phoneNo => phone_no. birthDate => birth_date. 刪除id, account 成為 primary key

///////////////////////////////////////////////////////////////////

新增 java pom.xml dependencies：

```xml
<dependency>
  <groupId>org.json</groupId>
  <artifactId>json</artifactId>
  <version>20240303</version>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-impl</artifactId>
  <version>0.11.5</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.11.5</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-jackson</artifactId>
  <version>0.11.5</version>
</dependency>

<dependency>
  <groupId>com.nimbusds</groupId>
  <artifactId>nimbus-jose-jwt</artifactId>
  <version>9.47</version>
</dependency>
```
新增 application.properties：

```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=rolland.cj.wu@gmail.com
spring.mail.password=cbwn lyex gsyf xujk
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```
