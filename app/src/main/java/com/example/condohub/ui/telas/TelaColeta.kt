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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.QualidadeArApi
import com.example.condohub.dados.Repositorio
import com.example.condohub.modelo.DiaColeta
import com.example.condohub.modelo.QualidadeAr
import com.example.condohub.ui.componentes.LinhaInformacao
import com.example.condohub.ui.componentes.TituloSecao
import com.example.condohub.ui.theme.ArBom
import com.example.condohub.ui.theme.ArModerado
import com.example.condohub.ui.theme.ArMuitoRuim
import com.example.condohub.ui.theme.ArRazoavel
import com.example.condohub.ui.theme.ArRuim
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 11 - Coleta sustentavel e qualidade do ar
//
// Pilar AMBIENTAL do ESG e unica tela que consome um servico
// externo (requisito da atividade).
//
// Servico: Open-Meteo Air Quality API
// https://air-quality-api.open-meteo.com/v1/air-quality
//
// Traz o indice europeu de qualidade do ar (EAQI) e o material
// particulado (PM2.5 e PM10) da regiao do condominio, junto com
// uma recomendacao pratica sobre o uso das areas externas.
// ============================================================

private sealed interface EstadoConsulta {
    data object Carregando : EstadoConsulta
    data class Sucesso(val dados: QualidadeAr) : EstadoConsulta
    data class Falha(val mensagem: String) : EstadoConsulta
}

@Composable
fun TelaColeta() {

    var estado by remember { mutableStateOf<EstadoConsulta>(EstadoConsulta.Carregando) }
    var tentativa by remember { mutableIntStateOf(0) }

    // Consulta a API quando a tela abre e a cada vez que o usuario
    // pede para tentar de novo.
    LaunchedEffect(tentativa) {
        estado = EstadoConsulta.Carregando
        estado = try {
            EstadoConsulta.Sucesso(
                QualidadeArApi.buscar(Repositorio.LATITUDE, Repositorio.LONGITUDE)
            )
        } catch (e: Exception) {
            EstadoConsulta.Falha(
                "Nao foi possivel consultar a qualidade do ar. Verifique sua conexao."
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // ---------- qualidade do ar (dado externo) ----------
        item {
            when (val atual = estado) {
                is EstadoConsulta.Carregando -> CartaoCarregando()
                is EstadoConsulta.Falha -> CartaoFalha(atual.mensagem) { tentativa++ }
                is EstadoConsulta.Sucesso -> CartaoQualidadeAr(atual.dados) { tentativa++ }
            }
        }

        // ---------- calendario de coleta ----------
        item {
            TituloSecao("Calendario da semana", modifier = Modifier.padding(top = 14.dp))
        }

        items(Repositorio.calendarioColeta) { dia -> LinhaColeta(dia) }

        // ---------- guia das lixeiras ----------
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
    }
}

/** Cor do indicador conforme a faixa do indice europeu. */
private fun corDoIndice(indice: Int): Color = when {
    indice <= 20 -> ArBom
    indice <= 40 -> ArRazoavel
    indice <= 60 -> ArModerado
    indice <= 80 -> ArRuim
    else -> ArMuitoRuim
}

@Composable
private fun CartaoQualidadeAr(dados: QualidadeAr, aoAtualizar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            Text(
                text = "QUALIDADE DO AR - ${Repositorio.CIDADE.uppercase()}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextoSecundario
            )

            Spacer(Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(corDoIndice(dados.indice), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${dados.indice}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = dados.classificacao,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = corDoIndice(dados.indice)
                    )
                    Text(
                        text = "Indice europeu (EAQI)",
                        fontSize = 12.sp,
                        color = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Medida("PM2.5", "%.1f".format(dados.pm25), Modifier.weight(1f))
                Medida("PM10", "%.1f".format(dados.pm10), Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = dados.recomendacao,
                fontSize = 13.5.sp,
                lineHeight = 20.sp,
                color = VerdeEscuro,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VerdeClaro, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Fonte: Open-Meteo Air Quality API",
                fontSize = 11.sp,
                color = TextoSecundario
            )

            Button(
                onClick = aoAtualizar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeClaro,
                    contentColor = VerdeEscuro
                )
            ) {
                Text("Atualizar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Medida(rotulo: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(FundoApp, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(rotulo, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextoSecundario)
        Spacer(Modifier.height(3.dp))
        Text("$valor µg/m³", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CartaoCarregando() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = VerdeCondo,
                strokeWidth = 3.dp
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Consultando a qualidade do ar...",
                fontSize = 14.sp,
                color = TextoSecundario
            )
        }
    }
}

@Composable
private fun CartaoFalha(mensagem: String, aoTentarDeNovo: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Qualidade do ar indisponivel",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = mensagem,
                fontSize = 13.5.sp,
                lineHeight = 20.sp,
                color = TextoSecundario
            )
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = aoTentarDeNovo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeEscuro)
            ) {
                Text("Tentar de novo", fontSize = 14.sp)
            }
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
