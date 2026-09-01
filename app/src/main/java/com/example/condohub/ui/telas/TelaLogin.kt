package com.example.condohub.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condohub.ui.theme.Branco
import com.example.condohub.ui.theme.CondoHubTheme
import com.example.condohub.ui.theme.TextoSecundario
import com.example.condohub.ui.theme.VerdeClaro
import com.example.condohub.ui.theme.VerdeCondo
import com.example.condohub.ui.theme.VerdeEscuro

// ============================================================
// TELA 1 - Login
//
// Porta de entrada do morador. Valida os campos localmente
// (o app nao possui back-end, conforme o enunciado permite).
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
            .background(Branco)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🏢", fontSize = 64.sp)

        Text(
            text = "CondoHub",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeEscuro
        )

        Text(
            text = "Governanca residencial na palma da sua mao",
            textAlign = TextAlign.Center,
            color = TextoSecundario,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it; erroUsuario = false },
            label = { Text("E-mail ou CPF") },
            singleLine = true,
            isError = erroUsuario,
            supportingText = if (erroUsuario) {
                { Text("Informe seu e-mail ou CPF") }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdeCondo,
                focusedLabelColor = VerdeCondo
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it; erroSenha = false },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = erroSenha,
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
            Text("Esqueci minha senha", color = VerdeEscuro)
        }

        Spacer(modifier = Modifier.height(18.dp))

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
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeEscuro)
        ) {
            Text("Entrar", fontSize = 16.sp)
        }

        TextButton(
            onClick = { aoAvisar("O cadastro e liberado pela administracao do condominio.") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Ainda nao tenho acesso", color = VerdeEscuro)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Versao de demonstracao: qualquer e-mail e senha sao aceitos.",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = VerdeEscuro,
            lineHeight = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeClaro, RoundedCornerShape(10.dp))
                .padding(12.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLoginPreview() {
    CondoHubTheme {
        TelaLogin(aoEntrar = {}, aoAvisar = {})
    }
}
