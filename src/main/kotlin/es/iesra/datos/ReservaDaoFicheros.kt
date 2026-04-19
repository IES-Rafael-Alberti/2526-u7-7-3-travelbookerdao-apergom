package es.iesra.datos.dao

import es.iesra.dominio.Reserva
import es.iesra.dominio.ReservaVuelo
import es.iesra.dominio.ReservaHotel
import java.io.File
import java.io.IOException

class ReservaDaoFicheros(private val rutaArchivo: String = "data/reservas.txt") : IReservaDao {
    private val archivo = File(rutaArchivo)

    init {
        val directorio = archivo.parentFile
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs()
        }
        if (!archivo.exists()) {
            archivo.createNewFile()
        }
    }

    override fun crear(reserva: Reserva): Boolean {
        val reservas = obtenerTodas().toMutableList()
        if (reservas.any { it.id == reserva.id }) return false
        
        reservas.add(reserva)
        return guardarTodas(reservas)
    }

    override fun obtenerTodas(): List<Reserva> {
        val listaReservas = mutableListOf<Reserva>()
        try {
            if (!archivo.exists() || archivo.length() == 0L) return emptyList()

            archivo.readLines().forEach { linea ->
                val partes = linea.split(";")
                if (partes.isNotEmpty()) {
                    val tipo = partes[0]
                    if (tipo == "VUELO" && partes.size >= 5) {
                        val vuelo = ReservaVuelo.creaInstancia(partes[2], partes[3], partes[4], partes[5])
                        listaReservas.add(vuelo)
                    } else if (tipo == "HOTEL" && partes.size >= 4) {
                        val hotel = ReservaHotel.creaInstancia(partes[2], partes[3], partes[4].toInt())
                        listaReservas.add(hotel)
                    }
                }
            }
        } catch (e: IOException) {
            println("Error al leer el archivo: ${e.message}")
        }
        return listaReservas
    }

    override fun obtenerPorId(id: String): Reserva? {
        return obtenerTodas().find { it.id == id }
    }

    override fun actualizar(reserva: Reserva): Boolean {
        val reservas = obtenerTodas().toMutableList()
        val index = reservas.indexOfFirst { it.id == reserva.id }
        
        if (index != -1) {
            reservas[index] = reserva
            return guardarTodas(reservas)
        }
        return false
    }

    override fun eliminar(id: String): Boolean {
        val reservas = obtenerTodas().toMutableList()
        val eliminada = reservas.removeIf { it.id == id }
        
        if (eliminada) {
            guardarTodas(reservas)
        }
        return eliminada
    }

    private fun guardarTodas(reservas: List<Reserva>): Boolean {
        return try {
            val contenido = reservas.joinToString("\n") { reserva ->
                when (reserva) {
                    is ReservaVuelo -> "VUELO;${reserva.id};${reserva.descripcion};${reserva.origen};${reserva.destino};${reserva.horaVuelo}"
                    is ReservaHotel -> "HOTEL;${reserva.id};${reserva.descripcion};${reserva.ubicacion};${reserva.numeroNoches}"
                    else -> ""
                }
            }
            archivo.writeText(contenido)
            true
        } catch (e: IOException) {
            println("Error al escribir en el archivo: ${e.message}")
            false
        }
    }
}
