package com.example.condohub.modelo

// ============================================================
// Modelos de dados do CondoHub
//
// Todos os dados sao mantidos em memoria (sem back-end), como
// permite o enunciado da atividade. A unica fonte externa e a
// API publica de previsao do tempo.
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

/**
 * Previsao do tempo de um dia, vinda da API publica Open-Meteo.
 *
 * Usada na tela de reserva: antes de reservar a churrasqueira ou a
 * area externa, o morador ve como o tempo deve estar naquele dia.
 */
data class DiaPrevisao(
    val data: String,        // formato ISO: "2026-09-06"
    val codigo: Int,         // codigo WMO devolvido pela API
    val tempMax: Double,
    val tempMin: Double,
    val chanceChuva: Int     // probabilidade de precipitacao, em %
) {
    /** Icone correspondente ao codigo meteorologico. */
    val emoji: String
        get() = when (codigo) {
            0 -> "☀️"                          // ceu limpo
            1 -> "🌤️"                    // poucas nuvens
            2 -> "⛅"                                // parcialmente nublado
            3 -> "☁️"                          // nublado
            45, 48 -> "🌫️"               // nevoa
            in 51..57 -> "🌦️"            // garoa
            in 61..67 -> "🌧️"            // chuva
            in 71..77, 85, 86 -> "🌨️"    // neve
            in 80..82 -> "🌦️"            // pancadas de chuva
            in 95..99 -> "⛈️"                  // trovoada
            else -> "🌥️"
        }

    /** Descricao em portugues do codigo meteorologico. */
    val descricao: String
        get() = when (codigo) {
            0 -> "Ceu limpo"
            1 -> "Poucas nuvens"
            2 -> "Parcialmente nublado"
            3 -> "Nublado"
            45, 48 -> "Nevoa"
            in 51..57 -> "Garoa"
            in 61..67 -> "Chuva"
            in 71..77, 85, 86 -> "Neve"
            in 80..82 -> "Pancadas de chuva"
            in 95..99 -> "Trovoada"
            else -> "Instavel"
        }

    /** Indica se o dia favorece o uso das areas externas do condominio. */
    val bomParaAreaExterna: Boolean
        get() = chanceChuva < 40 && codigo < 51

    /** Aviso mostrado ao morador ao escolher esta data para a reserva. */
    val aviso: String
        get() = when {
            chanceChuva >= 70 -> "Alta chance de chuva. Considere um espaco coberto."
            chanceChuva >= 40 -> "Pode chover. Vale ter um plano B para area externa."
            else -> "Tempo favoravel para uso das areas externas."
        }
}
