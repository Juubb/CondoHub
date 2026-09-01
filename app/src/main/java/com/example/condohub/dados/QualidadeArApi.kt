package com.example.condohub.dados

import com.example.condohub.modelo.QualidadeAr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// ============================================================
// Integracao com servico externo (requisito da atividade)
//
// Servico: Open-Meteo Air Quality API
// Endereco: https://air-quality-api.open-meteo.com/v1/air-quality
//
// API publica e gratuita, sem necessidade de cadastro ou chave.
// Fornece o indice europeu de qualidade do ar (EAQI) e as
// concentracoes de material particulado PM2.5 e PM10.
//
// A consulta e feita com HttpURLConnection e org.json, ambos
// nativos do Android, para nao depender de bibliotecas externas.
// ============================================================

object QualidadeArApi {

    const val ENDERECO_BASE = "https://air-quality-api.open-meteo.com/v1/air-quality"

    /** Monta a URL completa da consulta (usada tambem na documentacao do projeto). */
    fun montarUrl(latitude: Double, longitude: Double): String =
        "$ENDERECO_BASE?latitude=$latitude&longitude=$longitude" +
            "&current=european_aqi,pm10,pm2_5&timezone=America%2FSao_Paulo"

    /**
     * Consulta a qualidade do ar da regiao do condominio.
     * Roda em Dispatchers.IO para nao travar a interface.
     *
     * @return os dados lidos da API
     * @throws Exception quando nao ha internet ou o servico responde com erro
     */
    suspend fun buscar(latitude: Double, longitude: Double): QualidadeAr =
        withContext(Dispatchers.IO) {
            val conexao = (URL(montarUrl(latitude, longitude)).openConnection() as HttpURLConnection)
            conexao.requestMethod = "GET"
            conexao.connectTimeout = 10_000
            conexao.readTimeout = 10_000

            try {
                if (conexao.responseCode != HttpURLConnection.HTTP_OK) {
                    throw Exception("O servico respondeu com codigo ${conexao.responseCode}")
                }

                val corpo = conexao.inputStream.bufferedReader().use { it.readText() }
                val atual = JSONObject(corpo).getJSONObject("current")

                QualidadeAr(
                    indice = atual.optInt("european_aqi", 0),
                    pm25 = atual.optDouble("pm2_5", 0.0),
                    pm10 = atual.optDouble("pm10", 0.0)
                )
            } finally {
                conexao.disconnect()
            }
        }
}
