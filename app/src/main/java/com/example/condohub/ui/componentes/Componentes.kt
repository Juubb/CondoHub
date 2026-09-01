package com.example.condohub.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.ConfirmadoFundo
import com.example.condohub.ui.theme.PendenteFundo
import com.example.condohub.ui.theme.TextoPrincipal
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// Componentes reutilizados por varias telas
// ============================================================

/** Barra superior verde, com botao de voltar, usada em todas as telas internas. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraTopo(titulo: String, aoVoltar: () -> Unit) {
    TopAppBar(
        title = { Text(titulo, fontSize = 20.sp, fontWeight = FontWeight.Medium) },
        navigationIcon = {
            IconButton(onClick = aoVoltar) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = VerdeCondo,
            titleContentColor = Branco,
            navigationIconContentColor = Branco
        )
    )
}

/** Titulo de secao dentro de uma tela. */
@Composable
fun TituloSecao(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        color = TextoPrincipal,
        modifier = modifier
    )
}

/** Rotulo pequeno em caixa alta, usado acima dos campos dos formularios. */
@Composable
fun RotuloCampo(texto: String) {
    Text(
        text = texto.uppercase(),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.6.sp,
        color = TextoPrincipal,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

/** Linha com icone quadrado a esquerda, titulo e descricao. */
@Composable
fun LinhaInformacao(
    emoji: String,
    titulo: String,
    descricao: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                    .size(45.dp)
                    .background(VerdeClaro, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 21.sp)
            }

            Spacer(Modifier.size(12.dp))

            Column {
                Text(titulo, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(3.dp))
                Text(descricao, fontSize = 13.sp, color = TextoSecundario, lineHeight = 18.sp)
            }
        }
    }
}

/** Selo colorido de status (Pendente, Confirmada, Em analise). */
@Composable
fun Selo(texto: String, confirmado: Boolean = false) {
    val fundo = if (confirmado) ConfirmadoFundo else PendenteFundo
    val cor = if (confirmado) VerdeEscuro else Color(0xFF8A6100)

    Box(
        modifier = Modifier
            .background(fundo, RoundedCornerShape(6.dp))
            .padding(horizontal = 9.dp, vertical = 3.dp)
    ) {
        Text(
            text = texto.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp,
            color = cor
        )
    }
}

/** Mensagem exibida quando uma lista ainda nao tem nenhum item. */
@Composable
fun ListaVazia(texto: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Branco)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 26.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = texto,
                fontSize = 14.sp,
                color = TextoSecundario,
                lineHeight = 20.sp
            )
        }
    }
}

/** Converte "2026-10-12" em "12 de outubro". */
fun formatarData(iso: String): String {
    val meses = listOf(
        "janeiro", "fevereiro", "marco", "abril", "maio", "junho",
        "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"
    )
    return try {
        val partes = iso.split("-")
        "${partes[2].toInt()} de ${meses[partes[1].toInt() - 1]}"
    } catch (e: Exception) {
        iso
    }
}

/** Converte "2026-10-12" em "12/10/2026". */
fun formatarDataCurta(iso: String): String = try {
    val p = iso.split("-")
    "${p[2]}/${p[1]}/${p[0]}"
} catch (e: Exception) {
    iso
}
