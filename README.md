### Criar do zero:
https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.3.4&packaging=jar&jvmVersion=21&groupId=com.samuelpaz&artifactId=tasksApi&name=tasksApi&description=Projeto%20api%20de%20tasks%20e%20users&packageName=com.samuelpaz.tasksApi&dependencies=
### Baixar maven:
https://maven.apache.org/download.cgi
### Baixar postman (testar api):
http://www.postman.com/downloads/

### JDK utilizada: 17

### Diretórios criados e/ou modificados
    Pastas: config, controllers, exceptions, models, services, view
    Arquivos: application.properties, pom.xml


### No pom.xml consta todas as dependências utilizadas no projeto

### Fluxo Resumido:
- O usuário faz uma requisição HTTP informando o objeto, criar um novo usuário e sua senha por exemplo.
- A requisição passa pelo SecurityConfig para verificar se a rota é pública ou precisa de autenticação.
- O controller processa a requisição, valida o objeto e delega ao service.
- O service valida as regras de negócio (criptografa a senha e defini o tipo do usuário) e delega ao repository.
- O repositório salva o objeto no banco de dados.
- O controlador retorna uma resposta com o status 201 Created e a URI do novo recurso.



