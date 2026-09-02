# CondoHub

Plataforma mobile de governança residencial voltada à transparência, organização e
participação dos moradores na gestão condominial.

Aplicativo Android nativo em **Kotlin + Jetpack Compose**, com 11 telas e integração
a uma API pública de previsão do tempo. Projeto ESG com foco no pilar de **Governança**.

---

## Só quero usar o app

Baixe e instale o APK direto no celular Android:

**[`app/release/app-release.apk`](app/release/app-release.apk)** (10,5 MB)

1. Abra o link acima no GitHub e clique em **Download**
2. No celular, toque no arquivo baixado
3. O Android vai avisar que é de "fonte desconhecida" — autorize a instalação
4. Login: **qualquer** e-mail e **qualquer** senha (só não pode deixar em branco)

Requer Android 7.0 ou superior.

---

## Quero abrir o projeto no Android Studio

### 1. Clonar

```bash
git clone https://github.com/Juubb/CondoHub.git
```

No Android Studio: **File → Open** e selecione a pasta clonada. Escolha a **pasta**,
não um arquivo dentro dela.

### 2. Ajustar o JDK do Gradle

> **Este passo é obrigatório.** Sem ele o projeto não sincroniza.

O Android Studio recente vem com **Java 25** embutido, mas o Gradle 8.11.1 deste
projeto só aceita até o **Java 23**. Sem trocar, aparece:

```
Incompatible Gradle JVM version
The project's Gradle version 8.11.1 is incompatible with
the Gradle JVM version 25 currently selected to run Gradle build.
```

Para corrigir:

1. `Ctrl + Alt + S` (abre as Settings)
2. **Build, Execution, Deployment → Build Tools → Gradle**
3. No campo **Gradle JDK**, abra o dropdown → **Download JDK…**
4. **Version:** `17` · **Vendor:** `Eclipse Temurin` → **Download**
5. **Apply → OK** e sincronize

### 3. Rodar

Precisa de um aparelho:

- **Celular:** Configurações → Sobre o telefone → toque 7× em *Número da versão* →
  volte → Opções do desenvolvedor → ligue **Depuração USB** → conecte o cabo
- **Emulador:** Device Manager → `+` → Pixel 8 → imagem API 35 → Finish

Depois é só apertar **Run** (▶).

### 4. Gerar o APK release

**Build → Generate Signed App Bundle / APK…** → **APK** → *Create new…* para o
keystore → variante **release**. O arquivo sai em `app/release/app-release.apk`.

---

## Estrutura

```
app/src/main/java/com/example/condohub/
├── MainActivity.kt        Ponto de entrada + NavHost com as 11 rotas
├── navegacao/             Constantes de rota e títulos da barra superior
├── modelo/                Classes de dados (Evento, Pauta, Reserva, ...)
├── dados/
│   ├── Repositorio.kt     Fonte de dados em memória, observável pelo Compose
│   └── PrevisaoApi.kt     Consumo da API externa de previsão do tempo
└── ui/
    ├── theme/             Cores, tipografia e tema
    ├── componentes/       Peças reutilizadas (barra, selos, listas)
    └── telas/             As 11 telas
```

Não há back-end. Os dados vivem em memória num `object Repositorio` com
`mutableStateListOf`, então qualquer alteração do usuário — votar, reservar,
criar evento — redesenha as telas automaticamente.

---

## As 11 telas

| # | Tela | Pilar ESG |
|---|------|-----------|
| 1 | Login | — |
| 2 | Home | — |
| 3 | Detalhe do evento | Social |
| 4 | Novo evento | Social |
| 5 | **Votações** (assembleia digital) | **Governança** |
| 6 | **Reservar espaço** (consome a API) | Governança |
| 7 | Registrar ocorrência | Governança |
| 8 | Regras do condomínio | Governança |
| 9 | Corpo de eleitos | Governança |
| 10 | Garagem | Ambiental |
| 11 | Coleta sustentável | Ambiental |

---

## Serviço externo consumido

**Open-Meteo Forecast API** — pública, gratuita, sem cadastro nem chave de acesso.

```
https://api.open-meteo.com/v1/forecast
```

Usada na tela **Reservar espaço**: antes de confirmar a reserva da churrasqueira ou
da área externa, o morador vê a previsão dos próximos 7 dias e a chance de chuva de
cada um. Ao escolher uma data com risco alto, o app avisa e sugere um espaço coberto.

Implementação em [`dados/PrevisaoApi.kt`](app/src/main/java/com/example/condohub/dados/PrevisaoApi.kt),
com `HttpURLConnection` e `org.json` — ambos nativos do Android, sem dependência externa.

---

## Testes

```bash
./gradlew test
```

Quatro testes unitários cobrindo as regras de negócio: ordenação dos eventos,
reserva duplicada, geração de protocolo e os três estados da confirmação de presença.

---

## Documentação

O relatório da entrega está em [`docs/CondoHub-Relatorio.pdf`](docs/CondoHub-Relatorio.pdf),
com objetivo, tecnologia, aplicação no contexto ESG, descrição das telas e o endereço
do serviço consumido.

---

## Integrantes

| Nome | RM |
|------|-----|
| Vitor Augusto Brenguere da Silva | RM568061 |
| Breno Laurentino da Silva | RM567249 |
| Julia Silva de Azevedo | RM567533 |
| Yuri Lima Lucena Travassos de Luna | RM567746 |
