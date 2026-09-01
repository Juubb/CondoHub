package com.example.condohub

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExemploInstrumentadoTest {
    @Test
    fun pacote_do_aplicativo_esta_correto() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.condohub", contexto.packageName)
    }
}
