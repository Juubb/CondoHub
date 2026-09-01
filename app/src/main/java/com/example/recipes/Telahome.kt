package com.example.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TelaHome() {

    val verde = Color(0xFF087F6E)
    val fundo = Color(0xFFF5F7F6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fundo)
            .padding(20.dp)
    ) {

        // ==========================================
        // CABEÇALHO
        // ==========================================

        Text(
            text = "Olá, Morador! 👋",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Text(
            text = "Bem-vindo ao CondoHub",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))


        // ==========================================
        // CONDOMÍNIO
        // ==========================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = verde
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "SEU CONDOMÍNIO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Residencial Jardim das Flores",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Apartamento 204 • Bloco A",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        // ==========================================
        // PRÓXIMOS EVENTOS
        // ==========================================

        Text(
            text = "Próximos eventos",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(12.dp))


        val scrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            EventoCard(
                emoji = "🎃",
                titulo = "Dia das Crianças",
                data = "12 de outubro",
                horario = "15:00",
                local = "Área de lazer"
            )

            EventoCard(
                emoji = "🏢",
                titulo = "Assembleia",
                data = "18 de outubro",
                horario = "19:30",
                local = "Salão de festas"
            )

            EventoCard(
                emoji = "🎄",
                titulo = "Confraternização",
                data = "20 de dezembro",
                horario = "19:00",
                local = "Área externa"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        // ==========================================
        // SERVIÇOS
        // ==========================================

        Text(
            text = "Serviços do condomínio",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ServicoCard(
                emoji = "🏠",
                titulo = "Reservar salão",
                modifier = Modifier.weight(1f)
            )

            ServicoCard(
                emoji = "📋",
                titulo = "Regras",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ServicoCard(
                emoji = "👥",
                titulo = "Corpo de eleitos",
                modifier = Modifier.weight(1f)
            )

            ServicoCard(
                emoji = "♻️",
                titulo = "Coleta sustentável",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ServicoCard(
                emoji = "⚠️",
                titulo = "Registrar ocorrência",
                modifier = Modifier.weight(1f)
            )

            ServicoCard(
                emoji = "💬",
                titulo = "Tirar dúvidas",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        // ==========================================
        // INFORMAÇÕES
        // ==========================================

        Text(
            text = "Informações importantes",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(12.dp))


        InformacaoCard(
            emoji = "🔧",
            titulo = "Manutenção",
            descricao = "Sexta-feira • 09:00 às 12:00"
        )

        Spacer(modifier = Modifier.height(10.dp))


        InformacaoCard(
            emoji = "🚨",
            titulo = "Teste do alarme de incêndio",
            descricao = "Terça-feira • 14:00"
        )

        Spacer(modifier = Modifier.height(10.dp))


        InformacaoCard(
            emoji = "🏛️",
            titulo = "Plenária do condomínio",
            descricao = "18 de outubro • 19:30"
        )

        Spacer(modifier = Modifier.height(10.dp))


        InformacaoCard(
            emoji = "💧",
            titulo = "Limpeza da caixa d'água",
            descricao = "25 de outubro • 08:00"
        )
    }
}


// ==================================================
// CARD DO CARROSSEL
// ==================================================

@Composable
fun EventoCard(
    emoji: String,
    titulo: String,
    data: String,
    horario: String,
    local: String
) {

    Card(
        modifier = Modifier
            .width(250.dp)
            .height(145.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = emoji,
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = titulo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "📅 $data",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Text(
                text = "🕐 $horario",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Text(
                text = "📍 $local",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}


// ==================================================
// CARD DE SERVIÇO
// ==================================================

@Composable
fun ServicoCard(
    emoji: String,
    titulo: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.height(115.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = emoji,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF087F6E)
            )
        }
    }
}


// ==================================================
// CARD DE INFORMAÇÃO
// ==================================================

@Composable
fun InformacaoCard(
    emoji: String,
    titulo: String,
    descricao: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
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
                    .background(
                        color = Color(0xFFE5F3F0),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = emoji,
                    fontSize = 21.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    text = titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = descricao,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaHomePreview() {
    TelaHome()
}