package com.example.condohub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// ============================================================
// Tema do CondoHub
//
// O app usa uma unica paleta clara em vez das cores dinamicas do
// Android 12+, para que a identidade visual (verde) seja a mesma em
// qualquer aparelho. Isso tambem garante que as telas do relatorio
// correspondam ao que o usuario ve.
// ============================================================

private val EsquemaCondoHub = lightColorScheme(
    primary = VerdeCondo,
    onPrimary = Branco,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeEscuro,
    secondary = VerdeEscuro,
    onSecondary = Branco,
    background = FundoApp,
    onBackground = TextoPrincipal,
    surface = Branco,
    onSurface = TextoPrincipal,
    surfaceVariant = VerdeClaro,
    onSurfaceVariant = VerdeEscuro,
    error = Erro,
    onError = Branco
)

@Composable
fun CondoHubTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaCondoHub,
        typography = Typography,
        content = content
    )
}
