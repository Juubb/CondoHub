package com.example.condohub.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.Repositorio
import com.example.condohub.ui.componentes.TituloSecao
import com.example.condohub.ui.componentes.formatarData
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.Erro
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 3 - Detalhe do evento
//
// Mostra a descricao completa da atividade e permite ao morador
// confirmar presenca, o que ajuda a administracao a dimensionar
// o espaco (pilar Social do ESG).
// ============================================================

@Composable
fun TelaEvento(
    eventoId: String,
    aoVoltar: () -> Unit,
    aoAvisar: (String) -> Unit
) {
    val evento = Repositorio.buscarEvento(eventoId)

    if (evento == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FundoApp)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Evento nao encontrado.", color = TextoSecundario)
        }
        return
    }

    val confirmado = Repositorio.presencas[eventoId] == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp)
            .verticalScroll(rememberScrollState())
    ) {

        // faixa verde com o titulo do evento
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeCondo)
                .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp)
        ) {
            Text(text = evento.emoji, fontSize = 46.sp)
            Spacer(Modifier.height(10.dp))
            Text(
                text = evento.titulo,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = evento.local,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {

            LinhaFato("Data", formatarData(evento.data))
            Spacer(Modifier.height(10.dp))
            LinhaFato("Horario", evento.horario)
            Spacer(Modifier.height(10.dp))
            LinhaFato("Local", evento.local)

            Spacer(Modifier.height(24.dp))
            TituloSecao("Sobre o evento")
            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Branco)
            ) {
                Text(
                    text = evento.descricao,
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    color = Color(0xFF5C6663),
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    Repositorio.alternarPresenca(eventoId)
                    aoAvisar(
                        if (confirmado) "Presenca cancelada em ${evento.titulo}."
                        else "Presenca confirmada em ${evento.titulo}!"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeEscuro)
            ) {
                Text(
                    text = if (confirmado) "Cancelar presenca" else "Confirmar presenca",
                    fontSize = 16.sp
                )
            }

            if (evento.criadoPeloMorador) {
                TextButton(
                    onClick = {
                        Repositorio.removerEvento(eventoId)
                        aoAvisar("Evento excluido.")
                        aoVoltar()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Excluir evento", color = Erro)
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun LinhaFato(rotulo: String, valor: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Branco)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.width(90.dp)) {
                Text(rotulo, fontSize = 12.sp, color = TextoSecundario)
            }
            Text(valor, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }
}
