#FROM registry.cn-beijing.aliyuncs.com/authing-public/openjdk:11-jre-240808
FROM registry.cn-beijing.aliyuncs.com/authing-public/openjdk:11-jre-250701
WORKDIR /app
ADD target/*.jar /app/starter.jar
ADD .native/*.so /usr/lib/
# copy arthas
#COPY --from=registry.cn-hangzhou.aliyuncs.com/shay/arthas /opt/arthas /opt/arthas
EXPOSE 8080
ENV AUTHING_MODE "prod"
ENV JAVA_OPTS ""
ENV TZ "Asia/Shanghai"
ENV DEF_JAVA_OPTS "-Djava.security.egd=file:/dev/./urandom -Dfile.encoding=utf-8"
ENTRYPOINT ["sh","-c","java -Duser.timezone=${TZ} $DEF_JAVA_OPTS $JAVA_OPTS -jar /app/starter.jar --spring.profiles.active=${AUTHING_MODE}"]
#ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app/worker-starter.jar"]
# docker run -it --rm --env SAP_EXECUTE_TEST=true --env SAP_CREDENTIALS_VANKE_HOST=eccqas.vanke.com registry.cn-hangzhou.aliyuncs.com/shay/sap-tool