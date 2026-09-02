package com.example.condohub.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.Repositorio
import com.example.condohub.modelo.Evento
import com.example.condohub.navegacao.Rotas
import com.example.condohub.ui.componentes.LinhaInformacao
import com.example.condohub.ui.componentes.TituloSecao
import com.example.condohub.ui.componentes.formatarData
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.CondoHubTheme
import com.example.condohub.ui.theme.Erro
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoPrincipal
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 2 - Home
//
// Painel principal do morador. Reune, em um so lugar, o que hoje
// fica espalhado em grupos de mensagem, murais e comunicados
// impressos: identificacao da unidade, agenda de eventos, acesso
// aos servicos e avisos da administracao.
// ============================================================

/** Servico exibido na grade da home. */
private data class ItemServico(
    val emoji: String,
    val titulo: String,
    val rota: String,
    val contador: Int = 0
)

@Composable
fun TelaHome(
    aoNavegar: (String) -> Unit,
    aoSair: () -> Unit
) {
    val eventos = Repositorio.eventosOrdenados

    val servicos = listOf(
        ItemServico("🗳", "Votacoes", Rotas.VOTACOES, Repositorio.pautas.size),
        ItemServico("🏠", "Reservar espaco", Rotas.RESERVA, Repositorio.reservas.size),
        ItemServico("⚠️", "Ocorrencias", Rotas.OCORRENCIA, Repositorio.ocorrencias.size),
        ItemServico("📋", "Regras", Rotas.REGRAS),
        ItemServico("👥", "Corpo de eleitos", Rotas.ELEITOS),
        ItemServico("🚗", "Garagem", Rotas.GARAGEM),
        ItemServico("♻️", "Coleta sustentavel", Rotas.COLETA)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // -------- cabecalho --------
        item {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ola, Morador!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Bem-vindo ao CondoHub",
                        fontSize = 14.sp,
                        color = TextoSecundario
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Branco, RoundedCornerShape(20.dp))
                        .clickable { aoSair() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Sair da conta",
                        tint = VerdeEscuro,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // -------- identificacao do condominio --------
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VerdeCondo)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "SEU CONDOMINIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = Repositorio.CONDOMINIO,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = Repositorio.UNIDADE,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // -------- eventos --------
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TituloSecao("Proximos eventos", modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .background(VerdeClaro, RoundedCornerShape(8.dp))
                        .clickable { aoNavegar(Rotas.NOVO_EVENTO) }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = VerdeEscuro,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Novo", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VerdeEscuro)
                }
            }
        }

        item {
            // LazyRow ja e arrastavel na horizontal por padrao no Android
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                items(eventos, key = { it.id }) { evento ->
                    CartaoEvento(
                        evento = evento,
                        resposta = Repositorio.presencas[evento.id],
                        aoClicar = { aoNavegar("${Rotas.EVENTO}/${evento.id}") }
                    )
                }
            }
        }

        // -------- servicos --------
        item {
            TituloSecao("Servicos do condominio", modifier = Modifier.padding(top = 12.dp))
        }

        items(servicos.chunked(2)) { dupla ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                dupla.forEach { servico ->
                    CartaoServico(
                        servico = servico,
                        aoClicar = { aoNavegar(servico.rota) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (dupla.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        // -------- avisos --------
        item {
            TituloSecao("Informacoes importantes", modifier = Modifier.padding(top = 12.dp))
        }

        items(Repositorio.avisos) { aviso ->
            LinhaInformacao(
                emoji = aviso.emoji,
                titulo = aviso.titulo,
                descricao = aviso.descricao
            )
        }
    }
}

/** Card horizontal do carrossel de eventos. */
@Composable
private fun CartaoEvento(
    evento: Evento,
    resposta: Boolean?,
    aoClicar: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .height(if (resposta != null) 165.dp else 145.dp)
            .clickable { aoClicar() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = evento.emoji, fontSize = 28.sp)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = evento.titulo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            Spacer(Modifier.height(12.dp))

            Text("Data: ${formatarData(evento.data)}", fontSize = 13.sp, color = TextoSecundario)
            Text("Horario: ${evento.horario}", fontSize = 13.sp, color = TextoSecundario)
            Text("Local: ${evento.local}", fontSize = 13.sp, color = TextoSecundario, maxLines = 1)

            if (resposta != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (resposta) "VOCE VAI" else "VOCE NAO VAI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (resposta) VerdeCondo else Erro
                )
            }
        }
    }
}

/** Card quadrado da grade de servicos. */
@Composable
private fun CartaoServico(
    servico: ItemServico,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable { aoClicar() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = servico.emoji, fontSize = 25.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text = servico.titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeCondo
            )
            if (servico.contador > 0) {
                Text(
                    text = "${servico.contador} registro(s)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoSecundario
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaHomePreview() {
    CondoHubTheme {
        TelaHome(aoNavegar = {}, aoSair = {})
    }
}
