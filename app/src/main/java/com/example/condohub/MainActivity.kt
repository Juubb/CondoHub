package com.example.condohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.condohub.dados.Repositorio
import com.example.condohub.navegacao.Rotas
import com.example.condohub.ui.componentes.BarraTopo
import com.example.condohub.ui.telas.TelaColeta
import com.example.condohub.ui.telas.TelaEleitos
import com.example.condohub.ui.telas.TelaEvento
import com.example.condohub.ui.telas.TelaGaragem
import com.example.condohub.ui.telas.TelaHome
import com.example.condohub.ui.telas.TelaLogin
import com.example.condohub.ui.telas.TelaNovoEvento
import com.example.condohub.ui.telas.TelaOcorrencia
import com.example.condohub.ui.telas.TelaRegras
import com.example.condohub.ui.telas.TelaReserva
import com.example.condohub.ui.telas.TelaVotacoes
import com.example.condohub.ui.theme.CondoHubTheme
import kotlinx.coroutines.launch

// ============================================================
// CondoHub
//
// Plataforma mobile de governanca residencial voltada a
// transparencia, organizacao e participacao dos moradores na
// gestao condominial.
//
// Projeto ESG - pilar principal: GOVERNANCA (G)
//   G - votacoes, regras, corpo de eleitos, ocorrencias com
//       protocolo e reservas registradas
//   S - eventos e canais de participacao da comunidade
//   E - coleta seletiva, vagas de carga eletrica e pauta de
//       energia solar em votacao
// ============================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CondoHubTheme {
                AppCondoHub()
            }
        }
    }
}

@Composable
fun AppCondoHub() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val escopo = rememberCoroutineScope()

    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    /** Exibe uma mensagem curta na parte de baixo da tela. */
    val avisar: (String) -> Unit = { mensagem ->
        escopo.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(mensagem)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (Rotas.temBarraSuperior(rotaAtual)) {
                BarraTopo(
                    titulo = Rotas.tituloDe(rotaAtual),
                    aoVoltar = { navController.popBackStack() }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { espacamento ->

        NavHost(
            navController = navController,
            startDestination = Rotas.LOGIN,
            modifier = Modifier.padding(espacamento)
        ) {

            // ---------- 1. Login ----------
            composable(Rotas.LOGIN) {
                TelaLogin(
                    aoEntrar = {
                        navController.navigate(Rotas.HOME) {
                            popUpTo(Rotas.LOGIN) { inclusive = true }
                        }
                    },
                    aoAvisar = avisar
                )
            }

            // ---------- 2. Home ----------
            composable(Rotas.HOME) {
                TelaHome(
                    aoNavegar = { rota -> navController.navigate(rota) },
                    aoSair = {
                        Repositorio.sair()
                        navController.navigate(Rotas.LOGIN) {
                            popUpTo(Rotas.HOME) { inclusive = true }
                        }
                    }
                )
            }

            // ---------- 3. Detalhe do evento ----------
            composable(
                route = "${Rotas.EVENTO}/{eventoId}",
                arguments = listOf(navArgument("eventoId") { type = NavType.StringType })
            ) { entrada ->
                TelaEvento(
                    eventoId = entrada.arguments?.getString("eventoId").orEmpty(),
                    aoVoltar = { navController.popBackStack() },
                    aoAvisar = avisar
                )
            }

            // ---------- 4. Novo evento ----------
            composable(Rotas.NOVO_EVENTO) {
                TelaNovoEvento(
                    aoVoltar = { navController.popBackStack() },
                    aoAvisar = avisar
                )
            }

            // ---------- 5. Votacoes ----------
            composable(Rotas.VOTACOES) {
                TelaVotacoes(aoAvisar = avisar)
            }

            // ---------- 6. Reservar espaco (consome a API de previsao) ----------
            composable(Rotas.RESERVA) {
                TelaReserva(aoAvisar = avisar)
            }

            // ---------- 7. Registrar ocorrencia ----------
            composable(Rotas.OCORRENCIA) {
                TelaOcorrencia(aoAvisar = avisar)
            }

            // ---------- 8. Regras ----------
            composable(Rotas.REGRAS) {
                TelaRegras()
            }

            // ---------- 9. Corpo de eleitos ----------
            composable(Rotas.ELEITOS) {
                TelaEleitos()
            }

            // ---------- 10. Garagem ----------
            composable(Rotas.GARAGEM) {
                TelaGaragem()
            }

            // ---------- 11. Coleta sustentavel ----------
            composable(Rotas.COLETA) {
                TelaColeta()
            }
        }
    }
}
