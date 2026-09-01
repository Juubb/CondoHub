package com.example.condohub.modelo

// ============================================================
// Modelos de dados do CondoHub
//
// Todos os dados sao mantidos em memoria (sem back-end), como
// permite o enunciado da atividade. A unica fonte externa e a
// API publica de qualidade do ar.
// ============================================================

/** Evento do calendario do condominio. */
data class Evento(
    val id: String,
    val emoji: String,
    val titulo: String,
    val data: String,       // formato ISO: "2026-10-12"
    val horario: String,    // "19:30"
    val local: String,
    val descricao: String,
    val criadoPeloMorador: Boolean = false
)

/** Reserva de um espaco comum do condominio. */
data class Reserva(
    val protocolo: String,
    val espaco: String,
    val data: String,
    val horario: String,
    val status: String = "Pendente"
)

/** Ocorrencia ou pedido de manutencao aberto por um morador. */
data class Ocorrencia(
    val protocolo: String,
    val tipo: String,
    val local: String,
    val descricao: String,
    val abertaEm: String,
    val status: String = "Em analise"
)

/** Pauta em votacao na assembleia digital (pilar de Governanca). */
data class Pauta(
    val id: String,
    val titulo: String,
    val descricao: String,
    val prazo: String,
    var votosSim: Int,
    var votosNao: Int
) {
    val total: Int get() = votosSim + votosNao
    val percentualSim: Int get() = if (total == 0) 0 else (votosSim * 100) / total
}

/** Membro eleito do condominio. */
data class Eleito(
    val cargo: String,
    val nome: String,
    val unidade: String
)

/** Regra do regimento interno. */
data class Regra(
    val emoji: String,
    val titulo: String,
    val texto: String
)

/** Aviso ou comunicado da administracao. */
data class Aviso(
    val emoji: String,
    val titulo: String,
    val descricao: String
)

/** Dia do calendario de coleta seletiva. */
data class DiaColeta(
    val dia: String,
    val tipo: String,
    val observacao: String
)

/** Vaga da garagem. */
data class Vaga(
    val numero: String,
    val tipo: String,
    val ocupante: String,
    val minha: Boolean = false
)

/** Resposta da API publica de qualidade do ar. */
data class QualidadeAr(
    val indice: Int,
    val pm25: Double,
    val pm10: Double
) {
    /** Classificacao conforme a escala europeia de qualidade do ar (EAQI). */
    val classificacao: String
        get() = when {
            indice <= 20 -> "Boa"
            indice <= 40 -> "Razoavel"
            indice <= 60 -> "Moderada"
            indice <= 80 -> "Ruim"
            indice <= 100 -> "Muito ruim"
            else -> "Extremamente ruim"
        }

    /** Recomendacao pratica para os moradores. */
    val recomendacao: String
        get() = when {
            indice <= 40 -> "Boas condicoes para atividades ao ar livre na area de lazer."
            indice <= 60 -> "Grupos sensiveis devem evitar exercicio intenso ao ar livre."
            indice <= 80 -> "Prefira atividades em ambientes fechados hoje."
            else -> "Mantenha janelas fechadas e evite as areas externas."
        }
}
