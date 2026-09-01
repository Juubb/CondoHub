package com.example.condohub.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.Repositorio
import com.example.condohub.modelo.Evento
import com.example.condohub.ui.componentes.RotuloCampo
import com.example.condohub.ui.componentes.formatarDataCurta
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro
import java.util.Calendar
import java.util.TimeZone

// ============================================================
// TELA 4 - Novo evento
//
// Qualquer morador pode propor uma atividade para o mural do
// condominio, o que amplia a participacao da comunidade na vida
// coletiva (pilar Social do ESG).
// ============================================================

private val EMOJIS = listOf("🎉", "🏢", "🎄", "🏊", "🎬", "🧘", "🔁", "♻️", "🎅", "⚽", "📚", "🎨")
private val HORARIOS = listOf("08:00", "10:00", "15:00", "18:00", "19:00", "20:00")

/** Converte a data escolhida no calendario (millis em UTC) para "2026-10-12". */
private fun millisParaIso(millis: Long): String {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
    return "%04d-%02d-%02d".format(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TelaNovoEvento(
    aoVoltar: () -> Unit,
    aoAvisar: (String) -> Unit
) {
    var emoji by rememberSaveable { mutableStateOf(EMOJIS.first()) }
    var titulo by rememberSaveable { mutableStateOf("") }
    var local by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var horario by rememberSaveable { mutableStateOf("19:00") }

    var erroTitulo by rememberSaveable { mutableStateOf(false) }
    var erroLocal by rememberSaveable { mutableStateOf(false) }

    val amanha = remember { System.currentTimeMillis() + 86_400_000L }
    var dataMillis by rememberSaveable { mutableStateOf(amanha) }
    var mostrarCalendario by remember { mutableStateOf(false) }

    val dataIso = millisParaIso(dataMillis)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        RotuloCampo("Icone")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EMOJIS.forEach { opcao ->
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = if (opcao == emoji) VerdeClaro else Branco,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { emoji = opcao },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = opcao, fontSize = 22.sp, textAlign = TextAlign.Center)
                }
            }
        }

        Spacer(Modifier.height(22.dp))
        RotuloCampo("Titulo")
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it; erroTitulo = false },
            label = { Text("Nome do evento") },
            singleLine = true,
            isError = erroTitulo,
            supportingText = if (erroTitulo) ({ Text("De um nome ao evento") }) else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdeCondo,
                focusedLabelColor = VerdeCondo
            ),
            modifier = Modifier.fillMaxWidth()
        )

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
            HORARIOS.forEach { opcao ->
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

        Spacer(Modifier.height(22.dp))
        RotuloCampo("Local")
        OutlinedTextField(
            value = local,
            onValueChange = { local = it; erroLocal = false },
            label = { Text("Onde vai acontecer") },
            singleLine = true,
            isError = erroLocal,
            supportingText = if (erroLocal) ({ Text("Informe o local") }) else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdeCondo,
                focusedLabelColor = VerdeCondo
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(22.dp))
        RotuloCampo("Descricao")
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Detalhes para os moradores (opcional)") },
            minLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdeCondo,
                focusedLabelColor = VerdeCondo
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = {
                erroTitulo = titulo.isBlank()
                erroLocal = local.isBlank()
                if (erroTitulo || erroLocal) {
                    aoAvisar("Preencha o titulo e o local.")
                    return@Button
                }

                Repositorio.adicionarEvento(
                    Evento(
                        id = "u" + System.currentTimeMillis(),
                        emoji = emoji,
                        titulo = titulo.trim(),
                        data = dataIso,
                        horario = horario,
                        local = local.trim(),
                        descricao = descricao.trim().ifBlank {
                            "Evento proposto por um morador. Procure a administracao para mais detalhes."
                        },
                        criadoPeloMorador = true
                    )
                )
                aoAvisar("Evento \"${titulo.trim()}\" publicado no mural.")
                aoVoltar()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeEscuro)
        ) {
            Text("Publicar evento", fontSize = 16.sp)
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
