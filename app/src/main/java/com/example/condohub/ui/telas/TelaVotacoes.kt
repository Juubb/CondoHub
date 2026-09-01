package com.example.condohub.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.Repositorio
import com.example.condohub.modelo.Pauta
import com.example.condohub.ui.componentes.Selo
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.Erro
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 5 - Votacoes (assembleia digital)
//
// Nucleo do pilar de GOVERNANCA. Cada morador vota nas pautas
// em aberto e ve o resultado parcial em tempo real, o que
// substitui a votacao presencial de bracos levantados por um
// processo rastreavel e transparente. O voto pode ser alterado
// enquanto a pauta estiver aberta.
// ============================================================

@Composable
fun TelaVotacoes(aoAvisar: (String) -> Unit) {

    val jaVotadas = Repositorio.meusVotos.size
    val total = Repositorio.pautas.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VerdeCondo)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "ASSEMBLEIA DIGITAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "$jaVotadas de $total pautas votadas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Seu voto e registrado por unidade e pode ser alterado ate o encerramento.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        items(Repositorio.pautas, key = { it.id }) { pauta ->
            CartaoPauta(
                pauta = pauta,
                meuVoto = Repositorio.meusVotos[pauta.id],
                aoVotar = { aFavor ->
                    Repositorio.votar(pauta.id, aFavor)
                    aoAvisar(
                        if (aFavor) "Voto a favor registrado." else "Voto contrario registrado."
                    )
                }
            )
        }
    }
}

@Composable
private fun CartaoPauta(
    pauta: Pauta,
    meuVoto: Boolean?,
    aoVotar: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = pauta.titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )
                if (meuVoto != null) {
                    Spacer(Modifier.height(8.dp))
                    Selo("Votado", confirmado = true)
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = pauta.descricao,
                fontSize = 13.5.sp,
                lineHeight = 20.sp,
                color = Color(0xFF5C6663)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = pauta.prazo,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextoSecundario
            )

            Spacer(Modifier.height(14.dp))

            // ---- barra de resultado parcial ----
            BarraResultado(pauta)

            Spacer(Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${pauta.percentualSim}% a favor",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeEscuro,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${pauta.total} votos",
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }

            Spacer(Modifier.height(14.dp))

            // ---- botoes de voto ----
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { aoVotar(true) },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (meuVoto == true) VerdeEscuro else VerdeClaro,
                        contentColor = if (meuVoto == true) Color.White else VerdeEscuro
                    )
                ) {
                    Text("A favor", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { aoVotar(false) },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (meuVoto == false) Erro else Color(0xFFF3E7E6),
                        contentColor = if (meuVoto == false) Color.White else Erro
                    )
                ) {
                    Text("Contra", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/** Barra horizontal proporcional aos votos a favor e contra. */
@Composable
private fun BarraResultado(pauta: Pauta) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .background(Color(0xFFEDF1F0), RoundedCornerShape(5.dp))
    ) {
        if (pauta.votosSim > 0) {
            Box(
                modifier = Modifier
                    .weight(pauta.votosSim.toFloat())
                    .fillMaxHeight()
                    .background(VerdeCondo, RoundedCornerShape(5.dp))
            )
        }
        if (pauta.votosNao > 0) {
            Box(
                modifier = Modifier
                    .weight(pauta.votosNao.toFloat())
                    .fillMaxHeight()
                    .background(Color(0xFFD9A7A2), RoundedCornerShape(5.dp))
            )
        }
    }
}
