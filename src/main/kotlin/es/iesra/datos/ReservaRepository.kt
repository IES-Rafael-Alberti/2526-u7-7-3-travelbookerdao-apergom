package es.iesra.datos

import es.iesra.datos.dao.IReservaDao
import es.iesra.datos.dao.ReservaDaoFicheros
import es.iesra.dominio.Reserva

class ReservaRepository(private val dao: IReservaDao = ReservaDaoFicheros()) : IReservaRepository {

    override fun agregar(reserva: Reserva): Boolean {
        return dao.crear(reserva)
    }

    override fun obtenerTodas(): List<Reserva> {
        return dao.obtenerTodas()
    }
}