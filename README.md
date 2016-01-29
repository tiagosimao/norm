# NORM
<b>Not an O.R.M.</b>  
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.irenical.norm/norm/badge.svg?style=flat)](http://mvnrepository.com/artifact/org.irenical.norm/norm)


Easy to use, lightweight java framework, built on top of JDBC that allows you to build high performance DB access for your Java applications, without the typical productivity and reliability penalties of not using an ORM.

<b>Things you can do with NORM</b>  
- Preventing resource leaks caused by developer error, such as unclosed connections, result sets or statements  
- Easily building SQL queries, without sacrificing the ability to do weird stuff  
- ResultSet to Java objects mapping  
- Truly modular framework, allowing the developer to pick and choose what features of NORM to use, even in a multiple framework context  

<b>Things you cannot expect from NORM</b>  
- Hiding the relational model from your application  
- Non-JDBC data sources support  

<h2>NORM Modules</h2>
<h3>Norm Transaction</h3> (optional)
- Transaction and SQL operations handling
- Plugs to any connection pool
- JDBC driver agnostic
- Ensures resource release

```maven
<dependency>
  <groupId>org.irenical.norm</groupId>
  <artifactId>norm-transaction</artifactId>
  <version>0.4.0</version>
</dependency>
```
[More about norm-transaction](https://github.com/tiagosimao/norm/wiki/Norm-Transaction)  

<h3>Norm Query</h3> (optional)
- Simple generic query builder
- Sub-modules for different JDBC drivers to further help in query building

(incomplete)

<h3>Norm Graph</h3> (optional)
- Maps JDBC ResultSet to object graph

(todo)

<h3>NORM</h3> (very optional)
- Glues the other modules together to simplify the usage
- Less suited for a multiple framework context

(todo)
