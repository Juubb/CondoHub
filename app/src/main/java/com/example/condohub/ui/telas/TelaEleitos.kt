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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.condohub.modelo.Eleito
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeCondo

// ============================================================
// TELA 9 - Corpo de eleitos
//
// Mostra quem responde pelo condominio e onde encontrar cada um.
// Saber quem esta no comando e a base da prestacao de contas
// (pilar de Governanca).
// ============================================================

@Composable
fun TelaEleitos() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Mandato vigente: janeiro de 2026 a dezembro de 2027.",
                fontSize = 13.5.sp,
                lineHeight = 20.sp,
                color = Color(0xFF5C6663),
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        items(Repositorio.eleitos) { eleito ->
            CartaoEleito(eleito)
        }
    }
}

/** Extrai as iniciais do nome para o avatar. */
private fun iniciais(nome: String): String {
    val partes = nome.trim().split(" ").filter { it.isNotBlank() }
    if (partes.isEmpty()) return "?"
    val primeira = partes.first().first()
    val ultima = if (partes.size > 1) partes.last().first() else ' '
    return "$primeira$ultima".trim().uppercase()
}

@Composable
private fun CartaoEleito(eleito: Eleito) {
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
                    .size(46.dp)
                    .background(VerdeCondo, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciais(eleito.nome),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(13.dp))

            Column {
                Text(
                    text = eleito.cargo.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = VerdeCondo
                )
                Text(
                    text = eleito.nome,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = eleito.unidade,
                    fontSize = 13.sp,
                    color = TextoSecundario
                )
            }
        }
    }
}
