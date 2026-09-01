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
import com.example.condohub.modelo.DiaColeta
import com.example.condohub.ui.componentes.LinhaInformacao
import com.example.condohub.ui.componentes.TituloSecao
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 11 - Coleta sustentavel
//
// Pilar AMBIENTAL do ESG. Concentra a orientacao de descarte do
// condominio: em que dia passa cada coleta e o que vai em cada
// lixeira. Separar corretamente reduz a contaminacao dos
// reciclaveis, que e a principal causa de rejeito na triagem.
// ============================================================

@Composable
fun TelaColeta() {
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
                        text = "COLETA SELETIVA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "6 coletas por semana",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Deixe o material na lixeira do andar ate as 7h do dia da coleta.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        item {
            TituloSecao("Calendario da semana", modifier = Modifier.padding(top = 14.dp))
        }

        items(Repositorio.calendarioColeta) { dia -> LinhaColeta(dia) }

        item {
            TituloSecao("O que vai em cada lixeira", modifier = Modifier.padding(top = 14.dp))
        }

        item {
            LinhaInformacao("📘", "Azul - papel", "Caixas, jornais e cadernos. Sem papel engordurado.")
        }
        item {
            LinhaInformacao("📕", "Vermelha - plastico", "Garrafas, potes e embalagens limpas e secas.")
        }
        item {
            LinhaInformacao("📒", "Amarela - metal", "Latas, tampas e papel-aluminio limpo.")
        }
        item {
            LinhaInformacao("📗", "Verde - vidro", "Garrafas e potes inteiros. Vidro quebrado vai embalado.")
        }
        item {
            LinhaInformacao("🔋", "Descarte especial", "Pilhas, baterias e lampadas ficam no coletor da portaria.")
        }
    }
}

@Composable
private fun LinhaColeta(dia: DiaColeta) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Branco)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 30.dp)
                    .background(VerdeClaro, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dia.dia.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeEscuro
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(dia.tipo, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Text(dia.observacao, fontSize = 12.5.sp, color = TextoSecundario)
            }
        }
    }
}
