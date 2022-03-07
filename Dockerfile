FROM java:8

EXPOSE 8081

ADD target/demo-account-payment.jar demo-account-payment.jar

ENTRYPOINT ["java","-jar","demo-account-payment.jar"]
