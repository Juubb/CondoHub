package com.example.condohub

import com.example.condohub.dados.Repositorio
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Testes das regras de negocio do CondoHub.
 * Rodam na JVM, sem precisar de emulador.
 */
class RepositorioTest {

    @Test
    fun eventos_ficam_ordenados_por_data() {
        val datas = Repositorio.eventosOrdenados.map { it.data }
        assertEquals(datas.sorted(), datas)
    }

    @Test
    fun reserva_duplicada_e_detectada() {
        Repositorio.reservas.clear()
        Repositorio.adicionarReserva("Quadra", "2026-12-01", "Tarde 13h-17h")

        assertTrue(Repositorio.jaReservado("Quadra", "2026-12-01", "Tarde 13h-17h"))
        assertTrue(!Repositorio.jaReservado("Quadra", "2026-12-02", "Tarde 13h-17h"))
    }

    @Test
    fun ocorrencia_recebe_protocolo() {
        Repositorio.ocorrencias.clear()
        val protocolo = Repositorio.adicionarOcorrencia("Barulho", "Bloco A", "Obra fora do horario")

        assertTrue(protocolo.startsWith("OC-"))
        assertEquals(1, Repositorio.ocorrencias.size)
    }

    @Test
    fun trocar_o_voto_nao_soma_dois_votos() {
        val pauta = Repositorio.pautas.first()
        val totalAntes = pauta.total

        Repositorio.votar(pauta.id, true)
        Repositorio.votar(pauta.id, false)

        val depois = Repositorio.pautas.first { it.id == pauta.id }
        assertEquals(totalAntes + 1, depois.total)
    }

    @Test
    fun resposta_de_presenca_tem_tres_estados() {
        val evento = Repositorio.eventos.first()
        Repositorio.presencas.clear()

        // sem resposta
        assertEquals(null, Repositorio.presencas[evento.id])

        // marca que vai: entra na contagem de confirmados
        Repositorio.responderPresenca(evento.id, true)
        assertEquals(true, Repositorio.presencas[evento.id])
        assertEquals(evento.confirmadosBase + 1, Repositorio.confirmadosDe(evento))

        // troca para nao vou: sai da contagem
        Repositorio.responderPresenca(evento.id, false)
        assertEquals(false, Repositorio.presencas[evento.id])
        assertEquals(evento.confirmadosBase, Repositorio.confirmadosDe(evento))

        // tocar de novo na mesma opcao desfaz a resposta
        Repositorio.responderPresenca(evento.id, false)
        assertEquals(null, Repositorio.presencas[evento.id])
    }
}
