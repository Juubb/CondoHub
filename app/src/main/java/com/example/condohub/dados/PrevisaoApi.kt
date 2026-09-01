package com.example.condohub.dados

import com.example.condohub.modelo.DiaPrevisao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// ============================================================
// Integracao com servico externo (requisito da atividade)
//
// Servico: Open-Meteo Forecast API
// Endereco: https://api.open-meteo.com/v1/forecast
//
// API publica e gratuita, sem cadastro e sem chave de acesso.
// Devolve a previsao do tempo dos proximos dias para uma
// coordenada.
//
// No CondoHub ela alimenta a tela de reserva de espacos: antes de
// reservar a churrasqueira, a quadra ou a area externa, o morador
// ve a previsao daquele dia e a chance de chuva, evitando remarcar
// depois de ja ter convidado os visitantes.
//
// A consulta usa HttpURLConnection e org.json, ambos nativos do
// Android, para nao depender de bibliotecas externas.
// ============================================================

object PrevisaoApi {

    const val ENDERECO_BASE = "https://api.open-meteo.com/v1/forecast"

    /** Monta a URL completa da consulta (citada tambem na documentacao do projeto). */
    fun montarUrl(latitude: Double, longitude: Double, dias: Int = 7): String =
        "$ENDERECO_BASE?latitude=$latitude&longitude=$longitude" +
            "&daily=weather_code,temperature_2m_max,temperature_2m_min," +
            "precipitation_probability_max" +
            "&timezone=America%2FSao_Paulo&forecast_days=$dias"

    /**
     * Busca a previsao dos proximos dias para a regiao do condominio.
     * Roda em Dispatchers.IO para nao travar a interface.
     *
     * @throws Exception quando nao ha internet ou o servico responde com erro
     */
    suspend fun buscar(latitude: Double, longitude: Double): List<DiaPrevisao> =
        withContext(Dispatchers.IO) {
            val conexao = URL(montarUrl(latitude, longitude)).openConnection() as HttpURLConnection
            conexao.requestMethod = "GET"
            conexao.connectTimeout = 10_000
            conexao.readTimeout = 10_000

            try {
                if (conexao.responseCode != HttpURLConnection.HTTP_OK) {
                    throw Exception("O servico respondeu com codigo ${conexao.responseCode}")
                }

                val corpo = conexao.inputStream.bufferedReader().use { it.readText() }
                val diario = JSONObject(corpo).getJSONObject("daily")

                val datas = diario.getJSONArray("time")
                val codigos = diario.getJSONArray("weather_code")
                val maximas = diario.getJSONArray("temperature_2m_max")
                val minimas = diario.getJSONArray("temperature_2m_min")
                val chuvas = diario.getJSONArray("precipitation_probability_max")

                (0 until datas.length()).map { i ->
                    DiaPrevisao(
                        data = datas.getString(i),
                        codigo = codigos.optInt(i, 0),
                        tempMax = maximas.optDouble(i, 0.0),
                        tempMin = minimas.optDouble(i, 0.0),
                        chanceChuva = chuvas.optInt(i, 0)
                    )
                }
            } finally {
                conexao.disconnect()
            }
        }
}
