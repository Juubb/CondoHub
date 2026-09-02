package com.example.condohub.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.CondoHubTheme
import com.example.condohub.ui.theme.FundoApp
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 1 - Login
//
// Porta de entrada do morador. A faixa verde no topo carrega a
// identidade do aplicativo, e o formulario vem em um cartao que
// sobe sobre ela, dando hierarquia clara entre marca e acao.
//
// A validacao e local: o aplicativo nao possui back-end, como o
// enunciado da atividade permite.
// ============================================================

@Composable
fun TelaLogin(
    aoEntrar: () -> Unit,
    aoAvisar: (String) -> Unit
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var erroUsuario by rememberSaveable { mutableStateOf(false) }
    var erroSenha by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp)
            .verticalScroll(rememberScrollState())
    ) {

        // ---------- faixa de marca ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeCondo)
                .padding(start = 24.dp, end = 24.dp, top = 72.dp, bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏢", fontSize = 40.sp)
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "CondoHub",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                color = Color.White
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Governanca residencial na palma da sua mao",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.85f)
            )
        }

        // ---------- cartao do formulario ----------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-28).dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Branco),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {

                Text(
                    text = "Entrar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Use os dados cadastrados na administracao.",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it; erroUsuario = false },
                    label = { Text("E-mail ou CPF") },
                    singleLine = true,
                    isError = erroUsuario,
                    shape = RoundedCornerShape(12.dp),
                    supportingText = if (erroUsuario) {
                        { Text("Informe seu e-mail ou CPF") }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdeCondo,
                        focusedLabelColor = VerdeCondo
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it; erroSenha = false },
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    isError = erroSenha,
                    shape = RoundedCornerShape(12.dp),
                    supportingText = if (erroSenha) {
                        { Text("Informe sua senha") }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdeCondo,
                        focusedLabelColor = VerdeCondo
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = { aoAvisar("Um link de recuperacao seria enviado ao e-mail cadastrado.") },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Esqueci minha senha", color = VerdeEscuro, fontSize = 14.sp)
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        erroUsuario = usuario.isBlank()
                        erroSenha = senha.isBlank()
                        if (!erroUsuario && !erroSenha) {
                            aoEntrar()
                        } else {
                            aoAvisar("Preencha os dois campos para entrar.")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeEscuro)
                ) {
                    Text("Entrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // ---------- rodape ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = { aoAvisar("O cadastro e liberado pela administracao do condominio.") }) {
                Text("Ainda nao tenho acesso", color = VerdeEscuro, fontSize = 15.sp)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Versao de demonstracao: qualquer e-mail e senha sao aceitos.",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                textAlign = TextAlign.Center,
                color = VerdeEscuro,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VerdeClaro, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            )

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLoginPreview() {
    CondoHubTheme {
        TelaLogin(aoEntrar = {}, aoAvisar = {})
    }
}
