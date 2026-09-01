package com.example.condohub.navegacao

// ============================================================
// Rotas de navegacao do aplicativo
//
// Cada constante corresponde a uma tela registrada no NavHost
// da MainActivity.
// ============================================================

object Rotas {
    const val LOGIN = "login"
    const val HOME = "home"
    const val EVENTO = "evento"          // recebe o id: "evento/{eventoId}"
    const val NOVO_EVENTO = "novoEvento"
    const val VOTACOES = "votacoes"
    const val RESERVA = "reserva"
    const val OCORRENCIA = "ocorrencia"
    const val REGRAS = "regras"
    const val ELEITOS = "eleitos"
    const val GARAGEM = "garagem"
    const val COLETA = "coleta"

    /** Titulo exibido na barra superior de cada tela interna. */
    fun tituloDe(rota: String?): String = when {
        rota == null -> ""
        rota.startsWith(EVENTO) -> "Evento"
        rota == NOVO_EVENTO -> "Novo evento"
        rota == VOTACOES -> "Votacoes"
        rota == RESERVA -> "Reservar espaco"
        rota == OCORRENCIA -> "Registrar ocorrencia"
        rota == REGRAS -> "Regras do condominio"
        rota == ELEITOS -> "Corpo de eleitos"
        rota == GARAGEM -> "Garagem"
        rota == COLETA -> "Coleta e qualidade do ar"
        else -> ""
    }

    /** Telas que exibem a barra superior verde com o botao de voltar. */
    fun temBarraSuperior(rota: String?): Boolean =
        rota != null && rota != LOGIN && rota != HOME
}
