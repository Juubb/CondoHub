package com.example.condohub.dados

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.example.condohub.modelo.Aviso
import com.example.condohub.modelo.DiaColeta
import com.example.condohub.modelo.Eleito
import com.example.condohub.modelo.Evento
import com.example.condohub.modelo.Ocorrencia
import com.example.condohub.modelo.Pauta
import com.example.condohub.modelo.Regra
import com.example.condohub.modelo.Reserva
import com.example.condohub.modelo.Vaga
import java.util.Calendar

// ============================================================
// Repositorio em memoria
//
// As listas sao "mutableStateList", entao qualquer alteracao feita
// pelo usuario (criar evento, reservar espaco, votar) redesenha
// automaticamente as telas que dependem delas.
// ============================================================

object Repositorio {

    // ---------- identificacao do morador ----------
    const val CONDOMINIO = "Residencial Jardim das Flores"
    const val UNIDADE = "Apartamento 204 - Bloco A"

    // Coordenadas usadas na consulta de previsao do tempo (Sao Paulo - SP)
    const val LATITUDE = -23.5505
    const val LONGITUDE = -46.6333
    const val CIDADE = "Sao Paulo, SP"

    // ---------- eventos ----------
    val eventos = mutableStateListOf(
        Evento("e1", "🎃", "Dia das Criancas", "2026-10-12", "15:00", "Area de lazer",
            "Tarde de brincadeiras para a criancada, com pula-pula, pintura facial e lanche coletivo. Cada familia contribui com um item para a mesa.", confirmadosBase = 23),
        Evento("e2", "🏢", "Assembleia ordinaria", "2026-10-18", "19:30", "Salao de festas",
            "Prestacao de contas do semestre, votacao do reajuste da taxa condominial e eleicao de dois conselheiros. A presenca de cada unidade conta para o quorum.", confirmadosBase = 41),
        Evento("e3", "🏊", "Reabertura da piscina", "2026-11-05", "10:00", "Piscina",
            "A piscina reabre depois da manutencao do sistema de filtragem. Uso liberado das 8h as 22h, mediante apresentacao da carteirinha na portaria.", confirmadosBase = 12),
        Evento("e4", "🎬", "Cinema ao ar livre", "2026-11-09", "19:00", "Area de lazer",
            "Sessao com projetor e telao montados no gramado. Traga sua cadeira ou canga. Em caso de chuva, a sessao passa para o salao de festas.", confirmadosBase = 31),
        Evento("e5", "🧘", "Alongamento coletivo", "2026-11-15", "08:00", "Quadra",
            "Aula aberta de 50 minutos conduzida por uma moradora educadora fisica. Indicada para todas as idades, sem necessidade de inscricao.", confirmadosBase = 8),
        Evento("e6", "🔁", "Feira de trocas", "2026-11-23", "09:00", "Hall do Bloco A",
            "Traga roupas, livros e utensilios em bom estado para trocar com os vizinhos. O que sobrar e doado para a instituicao parceira do bairro.", confirmadosBase = 17),
        Evento("e7", "🎅", "Chegada do Papai Noel", "2026-12-15", "18:00", "Portaria",
            "Entrega de lembrancinhas para as criancas do condominio. Cadastre o nome e a idade da crianca com a sindica ate 5 de dezembro.", confirmadosBase = 26),
        Evento("e8", "🎄", "Confraternizacao", "2026-12-20", "19:00", "Area externa",
            "Encontro de fim de ano com amigo secreto (ate R$ 50) e ceia compartilhada. Confirme a presenca ate 10 de dezembro.")
    )

    /** Eventos sempre ordenados por data, incluindo os criados pelo morador. */
    val eventosOrdenados: List<Evento>
        get() = eventos.sortedWith(compareBy({ it.data }, { it.horario }))

    /**
     * Resposta da unidade ao convite de cada evento:
     *   true  = vou
     *   false = nao vou
     *   ausente = ainda nao respondeu
     */
    val presencas = mutableStateMapOf<String, Boolean>()

    /** Registra a resposta. Tocar de novo na mesma opcao desfaz a escolha. */
    fun responderPresenca(id: String, vai: Boolean) {
        if (presencas[id] == vai) presencas.remove(id) else presencas[id] = vai
    }

    /** Total de unidades confirmadas, ja contando a resposta do morador. */
    fun confirmadosDe(evento: Evento): Int =
        evento.confirmadosBase + if (presencas[evento.id] == true) 1 else 0

    fun adicionarEvento(evento: Evento) = eventos.add(evento)

    fun removerEvento(id: String) {
        eventos.removeAll { it.id == id }
        presencas.remove(id)
    }

    fun buscarEvento(id: String): Evento? = eventos.firstOrNull { it.id == id }

    // ---------- reservas ----------
    val espacos = listOf("Salao de festas", "Churrasqueira", "Quadra", "Espaco gourmet")
    val horarios = listOf("Manha 08h-12h", "Tarde 13h-17h", "Noite 18h-23h")

    val reservas = mutableStateListOf<Reserva>()

    fun jaReservado(espaco: String, data: String, horario: String): Boolean =
        reservas.any { it.espaco == espaco && it.data == data && it.horario == horario }

    fun adicionarReserva(espaco: String, data: String, horario: String) {
        reservas.add(
            0,
            Reserva(
                protocolo = "RES-" + System.currentTimeMillis().toString().takeLast(6),
                espaco = espaco,
                data = data,
                horario = horario
            )
        )
    }

    fun cancelarReserva(protocolo: String) = reservas.removeAll { it.protocolo == protocolo }

    // ---------- ocorrencias ----------
    val tiposOcorrencia = listOf("Barulho", "Manutencao", "Limpeza", "Seguranca", "Outro")

    val ocorrencias = mutableStateListOf<Ocorrencia>()
    private var sequenciaOcorrencia = 0

    fun adicionarOcorrencia(tipo: String, local: String, descricao: String): String {
        sequenciaOcorrencia++
        val agora = Calendar.getInstance()
        val protocolo = "OC-%d-%04d".format(agora.get(Calendar.YEAR), sequenciaOcorrencia)
        val dataTexto = "%02d/%02d/%d".format(
            agora.get(Calendar.DAY_OF_MONTH),
            agora.get(Calendar.MONTH) + 1,
            agora.get(Calendar.YEAR)
        )
        ocorrencias.add(0, Ocorrencia(protocolo, tipo, local, descricao, dataTexto))
        return protocolo
    }

    // ---------- votacoes (governanca) ----------
    val pautas = mutableStateListOf(
        Pauta(
            "p1",
            "Instalacao de placas solares no telhado",
            "Investimento de R$ 84.000 financiado em 24 parcelas, com estimativa de reducao de 40% na conta de energia das areas comuns.",
            "Encerra em 18 de outubro",
            votosSim = 41, votosNao = 12
        ),
        Pauta(
            "p2",
            "Contratacao de coleta seletiva porta a porta",
            "Servico semanal de recolhimento de reciclaveis em cada andar, com custo de R$ 6,50 por unidade ao mes.",
            "Encerra em 18 de outubro",
            votosSim = 33, votosNao = 21
        ),
        Pauta(
            "p3",
            "Reajuste da taxa condominial em 6,2%",
            "Correcao anual pelo IPCA acumulado, necessaria para manter o equilibrio do fundo de reserva.",
            "Encerra em 25 de outubro",
            votosSim = 18, votosNao = 29
        ),
        Pauta(
            "p4",
            "Uso do salao de festas ate as 24h",
            "Ampliacao do horario limite nos fins de semana, mediante deposito de caucao de R$ 300.",
            "Encerra em 30 de outubro",
            votosSim = 27, votosNao = 25
        )
    )

    /** Guarda o voto do morador em cada pauta: true = sim, false = nao. */
    val meusVotos = mutableStateMapOf<String, Boolean>()

    fun votar(pautaId: String, aFavor: Boolean) {
        val indice = pautas.indexOfFirst { it.id == pautaId }
        if (indice == -1) return
        val pauta = pautas[indice]

        // desfaz o voto anterior, se o morador estiver mudando de opiniao
        var sim = pauta.votosSim
        var nao = pauta.votosNao
        when (meusVotos[pautaId]) {
            true -> sim--
            false -> nao--
            else -> {}
        }
        if (aFavor) sim++ else nao++

        pautas[indice] = pauta.copy(votosSim = sim, votosNao = nao)
        meusVotos[pautaId] = aFavor
    }

    // ---------- corpo de eleitos ----------
    val eleitos = listOf(
        Eleito("Sindica", "Marta Albuquerque", "Bloco B - Apto 801", confirmadosBase = 38),
        Eleito("Subsindico", "Ricardo Nunes", "Bloco A - Apto 302"),
        Eleito("Conselho fiscal", "Helena Prado", "Bloco A - Apto 105"),
        Eleito("Conselho fiscal", "Jorge Tavares", "Bloco C - Apto 604"),
        Eleito("Conselho fiscal", "Sueli Ramos", "Bloco B - Apto 207")
    )

    // ---------- regimento interno ----------
    val regras = listOf(
        Regra("🔇", "Silencio",
            "Das 22h as 8h em dias uteis e das 22h as 9h aos domingos e feriados. Obras e reformas somente de segunda a sexta, das 8h as 17h."),
        Regra("🐶", "Animais",
            "Permitidos, desde que conduzidos na guia e no colo dentro do elevador. O tutor e responsavel por recolher os dejetos nas areas comuns."),
        Regra("🎉", "Areas de lazer",
            "Reserva antecipada pelo aplicativo. O morador responde pelos convidados e pela devolucao do espaco limpo ate as 10h do dia seguinte."),
        Regra("🚚", "Mudancas",
            "De segunda a sabado, das 8h as 17h, agendadas na portaria com 48h de antecedencia. Uso obrigatorio do elevador de servico."),
        Regra("🚗", "Estacionamento",
            "Uma vaga por unidade, sem transferencia para terceiros. Visitantes usam as vagas rotativas por no maximo 4 horas."),
        Regra("🗑", "Lixo",
            "Sacos fechados nas lixeiras do andar. Reciclaveis vao para os conteineres do subsolo, conforme o calendario de coleta."),
        Regra("👤", "Visitantes",
            "Cadastro previo no interfone ou pelo aplicativo. A portaria nao libera entrada sem autorizacao do morador.")
    )

    // ---------- avisos ----------
    val avisos = listOf(
        Aviso("🔧", "Manutencao dos elevadores", "Sexta-feira - 09:00 as 12:00"),
        Aviso("🚨", "Teste do alarme de incendio", "Terca-feira - 14:00"),
        Aviso("🏛", "Plenaria do condominio", "18 de outubro - 19:30"),
        Aviso("💧", "Limpeza da caixa d'agua", "25 de outubro - 08:00")
    )

    // ---------- coleta seletiva ----------
    val calendarioColeta = listOf(
        DiaColeta("Seg", "Organico", "Restos de comida e lixo do banheiro"),
        DiaColeta("Ter", "Reciclavel", "Papel, plastico e metal"),
        DiaColeta("Qua", "Organico", "Restos de comida e lixo do banheiro"),
        DiaColeta("Qui", "Reciclavel", "Papel, plastico e metal"),
        DiaColeta("Sex", "Organico", "Restos de comida e lixo do banheiro"),
        DiaColeta("Sab", "Vidro e oleo", "Conteiner especial do subsolo")
    )

    // ---------- garagem ----------
    val vagas = listOf(
        Vaga("A-12", "Coberta", "Apto 204 - Bloco A", minha = true),
        Vaga("A-13", "Coberta", "Apto 204 - Bloco A (segunda vaga)", minha = true),
        Vaga("V-01", "Visitante", "Livre"),
        Vaga("V-02", "Visitante", "Ocupada ate 16:30"),
        Vaga("V-03", "Visitante", "Livre"),
        Vaga("E-01", "Carga eletrica", "Livre"),
        Vaga("E-02", "Carga eletrica", "Em uso - Apto 501"),
        Vaga("PCD-1", "Acessibilidade", "Reservada")
    )

    /** Zera os dados criados na sessao (usado ao sair da conta). */
    fun sair() {
        reservas.clear()
        ocorrencias.clear()
        presencas.clear()
        meusVotos.clear()
        eventos.removeAll { it.criadoPeloMorador }
    }
}
