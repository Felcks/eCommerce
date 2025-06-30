# E-commerce App

Aplicativo Android que simula uma loja online, exibindo uma lista de produtos, detalhes, favoritos e avaliações de loja. O projeto utiliza Arquitetura Hexagonal, MVVM, Jetpack Compose, Retrofit, multi-módulos e outras tecnologias modernas.

---

## Funcionalidades implementadas
- Listagem de produtos consumindo a API DummyJSON ([endpoint](https://dummyjson.com/products))
- Indicador de carregamento durante requisições
- Tratamento de erros (ex: ausência de conexão)
- Tela de detalhes do produto com informações completas: preço, desconto, estoque, avaliação, etc.
- Paginação na lista de produtos
- Busca em tempo real por nome ou descrição do produto
- Favoritar/desfavoritar produtos
- Listagem de produtos favoritos
- Avaliação da loja
- Ícones customizados por categoria de avaliação: <3, 3–4, >4
- Dois modos de execução baseados em flavors
  - Use a flavor "complete" para visualizar a opção de avaliar loja
  - Use a Flavor "simple" para esconder a opção de avaliar loja

## Conceitos-chave deste projeto
- Arquitetura Hexagonal
- Projeto Multi Módulo
- MVVM (Model-View-ViewModel)
- Jetpack Compose
- Navegação com Jetpack Compose Navigation
- Injeção de dependência com Dagger/Hilt
- Testes unitários e mocks

## Principais bibliotecas utilizadas
- Retrofit — Consumo de API, integração com Kotlin Coroutines
- OkHttp — Suporte ao Retrofit
- Gson — Conversão entre JSON e objetos Kotlin
- Jetpack ViewModel — Gerenciamento de estado
- Kotlin Coroutines — Chamadas assíncronas
- Dagger/Hilt — Injeção de dependência
- Compose Coil — Carregamento de imagens
- Mockk — Mocks para testes
- JUnit — Testes unitários
- Paging 3 — Paginação de listas

---

## Por que Arquitetura Hexagonal?
A arquitetura hexagonal foi escolhida para garantir que as regras de negócio (domínio) fiquem isoladas de detalhes de implementação, como frameworks, banco de dados ou interface gráfica. Isso facilita a manutenção, testes e evolução do projeto.

![Hexagonal Architecture](images/hexagonal_architecture.png)

## Demonstração
![Home Screen](images/image_0.png)
![Detalhe do Produto](images/image_1.png)
![Favoritos](images/image_2.png)
![Avaliação da Loja](images/image_3.png)

---

## Contato
E-mail: matheusfelipecorreaalves@gmail.com
