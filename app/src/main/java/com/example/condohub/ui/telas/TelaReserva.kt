package com.example.condohub.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.PrevisaoApi
import com.example.condohub.dados.Repositorio
import com.example.condohub.modelo.DiaPrevisao
import com.example.condohub.ui.componentes.ListaVazia
import com.example.condohub.ui.componentes.RotuloCampo
import com.example.condohub.ui.componentes.Selo
import com.example.condohub.ui.componentes.TituloSecao
import com.example.condohub.ui.componentes.formatarDataCurta
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.CeuFundo
import com.example.condohub.ui.theme.Divisor
import com.example.condohub.ui.theme.Erro
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TempoBom
import com.example.condohub.ui.theme.TempoInstavel
import com.example.condohub.ui.theme.TempoRuim
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro
import java.util.Calendar
import java.util.TimeZone

// ============================================================
// TELA 6 - Reserva de espacos comuns
//
// Substitui o caderno da portaria por um registro com protocolo.
//
// E tambem a tela que consome o SERVICO EXTERNO do projeto:
// antes de confirmar, o morador ve a previsao do tempo dos
// proximos dias (Open-Meteo). Ao escolher uma data com alta
// chance de chuva, o app avisa antes que ele reserve a
// churrasqueira e precise remarcar depois.
// ============================================================

private fun paraIso(millis: Long): String {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
    return "%04d-%02d-%02d".format(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH)
    )
}

/** Estado da consulta ao servico de previsao. */
private sealed interface EstadoPrevisao {
    data object Carregando : EstadoPrevisao
    data class Sucesso(val dias: List<DiaPrevisao>) : EstadoPrevisao
    data class Falha(val mensagem: String) : EstadoPrevisao
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TelaReserva(aoAvisar: (String) -> Unit) {

    var espaco by rememberSaveable { mutableStateOf(Repositorio.espacos.first()) }
    var horario by rememberSaveable { mutableStateOf(Repositorio.horarios.first()) }

    val daquiUmaSemana = remember { System.currentTimeMillis() + 3 * 86_400_000L }
    var dataMillis by rememberSaveable { mutableStateOf(daquiUmaSemana) }
    var mostrarCalendario by remember { mutableStateOf(false) }

    var previsao by remember { mutableStateOf<EstadoPrevisao>(EstadoPrevisao.Carregando) }
    var tentativa by remember { mutableIntStateOf(0) }

    val dataIso = paraIso(dataMillis)

    // Consulta o servico externo ao abrir a tela
    LaunchedEffect(tentativa) {
        previsao = EstadoPrevisao.Carregando
        previsao = try {
            EstadoPrevisao.Sucesso(
                PrevisaoApi.buscar(Repositorio.LATITUDE, Repositorio.LONGITUDE)
            )
        } catch (e: Exception) {
            EstadoPrevisao.Falha("Nao foi possivel carregar a previsao. Verifique sua conexao.")
        }
    }

    val diaEscolhido = (previsao as? EstadoPrevisao.Sucesso)
        ?.dias?.firstOrNull { it.data == dataIso }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // ---------- previsao do tempo (servico externo) ----------
        when (val atual = previsao) {
            is EstadoPrevisao.Carregando -> CartaoPrevisaoCarregando()
            is EstadoPrevisao.Falha -> CartaoPrevisaoFalha(atual.mensagem) { tentativa++ }
            is EstadoPrevisao.Sucesso -> CartaoPrevisao(
                dias = atual.dias,
                dataSelecionada = dataIso,
                aoEscolherDia = { dia -> dataMillis = isoParaMillis(dia.data) }
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- formulario ----------
        RotuloCampo("Espaco")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Repositorio.espacos.forEach { opcao ->
                FilterChip(
                    selected = opcao == espaco,
                    onClick = { espaco = opcao },
                    label = { Text(opcao) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeClaro,
                        selectedLabelColor = VerdeEscuro
                    )
                )
            }
        }

        Spacer(Modifier.height(22.dp))
        RotuloCampo("Data")
        Button(
            onClick = { mostrarCalendario = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Branco,
                contentColor = VerdeEscuro
            )
        ) {
            Text(formatarDataCurta(dataIso), fontSize = 16.sp)
        }

        // aviso do tempo para a data escolhida
        if (diaEscolhido != null) {
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (diaEscolhido.bomParaAreaExterna) VerdeClaro else Color(0xFFFDF3DC),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(diaEscolhido.emoji, fontSize = 20.sp)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = diaEscolhido.aviso,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = if (diaEscolhido.bomParaAreaExterna) VerdeEscuro else Color(0xFF8A6100)
                )
            }
        }

        Spacer(Modifier.height(22.dp))
        RotuloCampo("Horario")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Repositorio.horarios.forEach { opcao ->
                FilterChip(
                    selected = opcao == horario,
                    onClick = { horario = opcao },
                    label = { Text(opcao) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeClaro,
                        selectedLabelColor = VerdeEscuro
                    )
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = {
                if (Repositorio.jaReservado(espaco, dataIso, horario)) {
                    aoAvisar("Voce ja tem uma reserva nesse espaco, data e horario.")
                } else {
                    Repositorio.adicionarReserva(espaco, dataIso, horario)
                    aoAvisar("Reserva solicitada. Aguarde a aprovacao da sindica.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeEscuro)
        ) {
            Text("Solicitar reserva", fontSize = 16.sp)
        }

        Spacer(Modifier.height(28.dp))
        TituloSecao("Minhas reservas")
        Spacer(Modifier.height(12.dp))

        if (Repositorio.reservas.isEmpty()) {
            ListaVazia("Voce ainda nao tem reservas.\nEscolha um espaco acima para comecar.")
        } else {
            Repositorio.reservas.forEach { reserva ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Branco)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = reserva.espaco,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Selo(reserva.status)
                        }

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "${formatarDataCurta(reserva.data)} - ${reserva.horario}",
                            fontSize = 13.5.sp,
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(10.dp))
                        HorizontalDivider(color = Divisor)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = reserva.protocolo,
                                fontSize = 12.sp,
                                color = TextoSecundario,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = {
                                Repositorio.cancelarReserva(reserva.protocolo)
                                aoAvisar("Reserva cancelada.")
                            }) {
                                Text("Cancelar", color = Erro, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }

    if (mostrarCalendario) {
        val estadoCalendario = rememberDatePickerState(initialSelectedDateMillis = dataMillis)

        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoCalendario.selectedDateMillis?.let { dataMillis = it }
                    mostrarCalendario = false
                }) {
                    Text("OK", color = VerdeEscuro)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) {
                    Text("Cancelar", color = VerdeEscuro)
                }
            }
        ) {
            DatePicker(state = estadoCalendario)
        }
    }
}

/** Converte "2026-09-06" de volta para milissegundos em UTC. */
private fun isoParaMillis(iso: String): Long {
    val p = iso.split("-")
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.clear()
    cal.set(p[0].toInt(), p[1].toInt() - 1, p[2].toInt())
    return cal.timeInMillis
}

/** Sigla do dia da semana a partir da data ISO. */
private fun diaDaSemana(iso: String): String {
    val p = iso.split("-")
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.clear()
    cal.set(p[0].toInt(), p[1].toInt() - 1, p[2].toInt())
    return when (cal.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "DOM"
        Calendar.MONDAY -> "SEG"
        Calendar.TUESDAY -> "TER"
        Calendar.WEDNESDAY -> "QUA"
        Calendar.THURSDAY -> "QUI"
        Calendar.FRIDAY -> "SEX"
        else -> "SAB"
    }
}

@Composable
private fun CartaoPrevisao(
    dias: List<DiaPrevisao>,
    dataSelecionada: String,
    aoEscolherDia: (DiaPrevisao) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {

            Text(
                text = "PREVISAO PARA AS AREAS EXTERNAS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextoSecundario,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(dias) { dia ->
                    val selecionado = dia.data == dataSelecionada
                    Column(
                        modifier = Modifier
                            .width(64.dp)
                            .background(
                                color = if (selecionado) VerdeClaro else CeuFundo,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .then(
                                if (selecionado) Modifier.border(
                                    1.5.dp, VerdeCondo, RoundedCornerShape(12.dp)
                                ) else Modifier
                            )
                            .clickable { aoEscolherDia(dia) }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = diaDaSemana(dia.data),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selecionado) VerdeEscuro else TextoSecundario
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(dia.emoji, fontSize = 22.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${dia.tempMax.toInt()}°",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${dia.tempMin.toInt()}°",
                            fontSize = 11.sp,
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${dia.chanceChuva}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                dia.chanceChuva >= 70 -> TempoRuim
                                dia.chanceChuva >= 40 -> TempoInstavel
                                else -> TempoBom
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Toque em um dia para reservar nele. Fonte: Open-Meteo.",
                fontSize = 11.sp,
                color = TextoSecundario,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun CartaoPrevisaoCarregando() {
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
                modifier = Modifier.size(26.dp),
                color = VerdeCondo,
                strokeWidth = 3.dp
            )
            Spacer(Modifier.width(16.dp))
            Text("Carregando a previsao do tempo...", fontSize = 14.sp, color = TextoSecundario)
        }
    }
}

@Composable
private fun CartaoPrevisaoFalha(mensagem: String, aoTentarDeNovo: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("Previsao indisponivel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(mensagem, fontSize = 13.5.sp, lineHeight = 20.sp, color = TextoSecundario)
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
