package com.example.condohub.ui.telas

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.condohub.modelo.Vaga
import com.example.condohub.ui.componentes.Selo
import com.example.condohub.ui.componentes.TituloSecao
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 10 - Garagem
//
// Mapa das vagas: quais pertencem a unidade do morador, quais
// estao livres para visitantes e quais sao de uso especial
// (carga de veiculo eletrico e acessibilidade).
// ============================================================

@Composable
fun TelaGaragem() {

    val minhas = Repositorio.vagas.filter { it.minha }
    val visitantes = Repositorio.vagas.filter { it.tipo == "Visitante" }
    val especiais = Repositorio.vagas.filter { !it.minha && it.tipo != "Visitante" }
    val livres = visitantes.count { it.ocupante == "Livre" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VerdeCondo)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "VAGAS DE VISITANTE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "$livres de ${visitantes.size} livres agora",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Permanencia maxima de 4 horas, conforme o regimento interno.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        item { TituloSecao("Minhas vagas", modifier = Modifier.padding(top = 12.dp)) }
        items(minhas) { vaga -> CartaoVaga(vaga) }

        item { TituloSecao("Visitantes", modifier = Modifier.padding(top = 12.dp)) }
        items(visitantes) { vaga -> CartaoVaga(vaga) }

        item { TituloSecao("Uso especial", modifier = Modifier.padding(top = 12.dp)) }
        items(especiais) { vaga -> CartaoVaga(vaga) }
    }
}

@Composable
private fun CartaoVaga(vaga: Vaga) {
    val livre = vaga.ocupante == "Livre"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Branco)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(VerdeClaro, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = vaga.numero,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeEscuro
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(vaga.tipo, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(vaga.ocupante, fontSize = 13.sp, color = TextoSecundario)
            }

            if (vaga.tipo == "Visitante") {
                Selo(if (livre) "Livre" else "Ocupada", confirmado = livre)
            }
        }
    }
}
