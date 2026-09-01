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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.dados.Repositorio
import com.example.condohub.ui.componentes.ListaVazia
import com.example.condohub.ui.componentes.RotuloCampo
import com.example.condohub.ui.componentes.Selo
import com.example.condohub.ui.componentes.TituloSecao
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.Divisor
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 7 - Registro de ocorrencias e manutencoes
//
// Cada registro recebe um numero de protocolo, o que da ao
// morador um comprovante e permite acompanhar a resposta da
// administracao. E a face fiscalizatoria do pilar de Governanca.
// ============================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaOcorrencia(aoAvisar: (String) -> Unit) {

    var tipo by rememberSaveable { mutableStateOf(Repositorio.tiposOcorrencia.first()) }
    var local by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var erroLocal by rememberSaveable { mutableStateOf(false) }
    var erroDescricao by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        RotuloCampo("Tipo")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Repositorio.tiposOcorrencia.forEach { opcao ->
                FilterChip(
                    selected = opcao == tipo,
                    onClick = { tipo = opcao },
                    label = { Text(opcao) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeClaro,
                        selectedLabelColor = VerdeEscuro
                    )
                )
            }
        }

        Spacer(Modifier.height(22.dp))
        RotuloCampo("Onde aconteceu")
        OutlinedTextField(
            value = local,
            onValueChange = { local = it; erroLocal = false },
            label = { Text("Local") },
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
            onValueChange = { descricao = it; erroDescricao = false },
            label = { Text("Conte o que aconteceu") },
            minLines = 3,
            isError = erroDescricao,
            supportingText = if (erroDescricao) ({ Text("Descreva a ocorrencia") }) else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdeCondo,
                focusedLabelColor = VerdeCondo
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = {
                erroLocal = local.isBlank()
                erroDescricao = descricao.isBlank()
                if (erroLocal || erroDescricao) {
                    aoAvisar("Preencha o local e a descricao.")
                    return@Button
                }
                val protocolo = Repositorio.adicionarOcorrencia(tipo, local.trim(), descricao.trim())
                local = ""
                descricao = ""
                aoAvisar("Ocorrencia registrada. Protocolo $protocolo.")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeEscuro)
        ) {
            Text("Enviar ocorrencia", fontSize = 16.sp)
        }

        Spacer(Modifier.height(28.dp))
        TituloSecao("Minhas ocorrencias")
        Spacer(Modifier.height(12.dp))

        if (Repositorio.ocorrencias.isEmpty()) {
            ListaVazia("Nenhuma ocorrencia registrada.")
        } else {
            Repositorio.ocorrencias.forEach { ocorrencia ->
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
                                text = ocorrencia.tipo,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Selo(ocorrencia.status)
                        }

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Local: ${ocorrencia.local}",
                            fontSize = 13.sp,
                            color = TextoSecundario
                        )
                        Text(
                            text = ocorrencia.descricao,
                            fontSize = 13.5.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF5C6663)
                        )

                        Spacer(Modifier.height(10.dp))
                        HorizontalDivider(color = Divisor)
                        Spacer(Modifier.height(10.dp))

                        Row {
                            Text(
                                text = ocorrencia.protocolo,
                                fontSize = 12.sp,
                                color = TextoSecundario,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Aberta em ${ocorrencia.abertaEm}",
                                fontSize = 12.sp,
                                color = TextoSecundario
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}
