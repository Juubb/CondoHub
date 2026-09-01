package com.example.condohub.ui.telas

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.Repositorio
import com.example.condohub.modelo.Regra
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.VerdeCondo

// ============================================================
// TELA 8 - Regimento interno
//
// Deixa as regras acessiveis a qualquer momento, no lugar do
// documento impresso que costuma ficar so na portaria.
// Transparencia de regras e um item classico de Governanca.
// ============================================================

@Composable
fun TelaRegras() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Toque em uma regra para ler o texto completo do regimento.",
                fontSize = 13.5.sp,
                lineHeight = 20.sp,
                color = Color(0xFF5C6663),
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        items(Repositorio.regras) { regra ->
            ItemRegra(regra)
        }
    }
}

@Composable
private fun ItemRegra(regra: Regra) {
    var aberta by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { aberta = !aberta },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Branco)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = regra.emoji, fontSize = 20.sp)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = regra.titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (aberta) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (aberta) "Recolher" else "Expandir",
                    tint = VerdeCondo,
                    modifier = Modifier.size(22.dp)
                )
            }

            if (aberta) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = regra.texto,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = Color(0xFF5C6663),
                    modifier = Modifier.padding(start = 32.dp)
                )
            }
        }
    }
}
