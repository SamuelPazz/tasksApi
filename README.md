### Fluxo Resumido:
- O usuário faz uma requisição HTTP informando o objeto, criar um novo usuário e sua senha por exemplo.
- A requisição passa pelo SecurityConfig para verificar se a rota é pública ou precisa de autenticação.
- O controller processa a requisição, valida o objeto e delega ao service.
- O service valida as regras de negócio (criptografa a senha e defini o tipo do usuário) e delega ao repository.
- O repositório salva o objeto no banco de dados.
- O controlador retorna uma resposta com o status 201 Created e a URI do novo recurso.



