package com.example.condohub.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.Repositorio
import com.example.condohub.ui.componentes.ListaVazia
import com.example.condohub.ui.componentes.RotuloCampo
import com.example.condohub.ui.componentes.Selo
import com.example.condohub.ui.componentes.TituloSecao
import com.example.condohub.ui.componentes.formatarDataCurta
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.Divisor
import com.example.condohub.ui.theme.Erro
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeEscuro
import java.util.Calendar
import java.util.TimeZone

// ============================================================
// TELA 6 - Reserva de espacos comuns
//
// Substitui o caderno da portaria por um registro com protocolo,
// visivel ao morador e a administracao. Evita reserva duplicada
// no mesmo espaco, data e horario.
// ============================================================

private fun paraIso(millis: Long): String {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
    return "%04d-%02d-%02d".format(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TelaReserva(aoAvisar: (String) -> Unit) {

    var espaco by rememberSaveable { mutableStateOf(Repositorio.espacos.first()) }
    var horario by rememberSaveable { mutableStateOf(Repositorio.horarios.first()) }

    val daquiUmaSemana = remember { System.currentTimeMillis() + 7 * 86_400_000L }
    var dataMillis by rememberSaveable { mutableStateOf(daquiUmaSemana) }
    var mostrarCalendario by remember { mutableStateOf(false) }

    val dataIso = paraIso(dataMillis)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

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
