FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/emacs-bindings.jar /emacs-bindings/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/emacs-bindings/app.jar"]
