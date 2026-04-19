### [CE 7.c] ¿Que librería/clases has utilizado para realizar la práctica.? Comenta los métodos mas interesantes

Para realizar esta práctica no he necesitado instalar ninguna librería externa (como Gson o similares). He utilizado las clases estándar de Java/Kotlin para el manejo de ficheros, concretamente **`java.io.File`** y para el control de errores **`java.io.IOException`**.

[Enlace a las importaciones utilizadas](https://github.com/IES-Rafael-Alberti/2526-u7-7-3-travelbookerdao-apergom/blob/main/src/main/kotlin/es/iesra/datos/dao/ReservaDaoFicheros.kt#L6-L7)

Los métodos que me han resultado más interesantes y útiles han sido:
*   `.exists()`: Fundamental para comprobar si la carpeta o el fichero ya estaban creados antes de intentar operar con ellos.
*   `.mkdirs()`: Muy útil porque me permite crear toda la ruta de directorios de golpe si no existe.
*   `.readLines()`: Me ha simplificado mucho la lectura, ya que devuelve automáticamente una lista de Strings (cada línea del archivo), evitándome tener que gestionar un `Scanner` o un `BufferedReader` a mano.
*   `.writeText()`: Escribe todo el contenido de una vez en el archivo (sobreescribiéndolo), cerrando el flujo automáticamente por debajo.

---

### [CE 7.d] Gestión de archivos y formato

**2.a ¿Que formato le has dado a la información del fichero para guardar y recuperar la información?**
He utilizado un formato de texto plano estructurado, similar a un **CSV**, separando los atributos de cada objeto por punto y coma (`;`). El primer campo de cada línea siempre es una palabra clave (`VUELO` o `HOTEL`) que me sirve como "chivato" para saber qué clase debo instanciar cuando recupero los datos. Por ejemplo: `VUELO;id;descripcion;origen;destino;hora`.

**2.b ¿Que estrategia has usado para trabajar con los ficheros?**
Mi estrategia ha sido encapsular toda la gestión en el DAO. He decidido guardar los ficheros en una carpeta llamada `data/` en la raíz del proyecto, en un archivo llamado `reservas.txt`. 
Para asegurar que el archivo existe cuando el programa lo necesite, he colocado la lógica de creación en el bloque `init` del DAO. Así, en el momento en que se instancia la clase `ReservaDaoFicheros`, el programa comprueba si la carpeta y el archivo existen y, si no, los crea.

[Enlace a la estrategia de creación en el init](https://github.com/IES-Rafael-Alberti/2526-u7-7-3-travelbookerdao-apergom/blob/main/src/main/kotlin/es/iesra/datos/dao/ReservaDaoFicheros.kt#L12-L20)

**2.c ¿Cómo se gestionan los errores?**
Para gestionar los errores he envuelto las operaciones de entrada y salida (lectura y escritura) en bloques `try-catch`, capturando la excepción específica `IOException`. Si ocurre un error al leer, informo por consola y devuelvo una lista vacía para no romper el programa. Si ocurre al escribir, devuelvo `false` para que la capa de servicios sepa que la operación no tuvo éxito.

[Enlace a la gestión de errores (try-catch en método de guardado)](https://github.com/IES-Rafael-Alberti/2526-u7-7-3-travelbookerdao-apergom/blob/main/src/main/kotlin/es/iesra/datos/dao/ReservaDaoFicheros.kt#L83-L94)

---

### [CE 7.e] Formas de acceso

**3.a Describe la forma de acceso para leer información**
Para leer la información, el método `obtenerTodas()` accede al archivo mediante `readLines()`. Recorro cada línea, utilizo el método `.split(";")` para separar los datos y compruebo el primer elemento. Si es "VUELO", utilizo esos trozos de texto para instanciar un objeto `ReservaVuelo`; si es "HOTEL", instancio un `ReservaHotel`. Finalmente, los añado a una lista y la devuelvo.

[Enlace al código de lectura y mapeo de objetos](https://github.com/IES-Rafael-Alberti/2526-u7-7-3-travelbookerdao-apergom/blob/main/src/main/kotlin/es/iesra/datos/dao/ReservaDaoFicheros.kt#L30-L51)

**3.b Describe la forma de acceso para escribir información**
Para escribir (crear una nueva reserva), mi estrategia es: primero obtengo toda la lista de reservas actuales cargándolas en memoria. Añado el nuevo objeto a esa lista. Luego, uso un método privado auxiliar llamado `guardarTodas()` que se encarga de recorrer la lista, transformar cada objeto en una cadena de texto separada por punto y coma, unir todo con saltos de línea (`\n`) y sobreescribir el archivo por completo usando `writeText()`.

[Enlace al método crear y guardado en archivo](https://github.com/IES-Rafael-Alberti/2526-u7-7-3-travelbookerdao-apergom/blob/main/src/main/kotlin/es/iesra/datos/dao/ReservaDaoFicheros.kt#L22-L28)

**3.c Describe la forma de acceso para actualizar información.**
La actualización funciona de manera muy parecida a la escritura. Primero cargo todas las reservas desde el fichero a una lista mutable. Utilizo la función `indexOfFirst` para buscar en qué posición está la reserva antigua que coincide con el `id` de la reserva que quiero actualizar. Si la encuentro, sustituyo el objeto viejo por el nuevo en esa posición de la lista. Por último, vuelvo a llamar a `guardarTodas()` para que vuelque toda la lista actualizada en el fichero sobreescribiéndolo.

[Enlace permanente al código de actualización](https://github.com/IES-Rafael-Alberti/2526-u7-7-3-travelbookerdao-apergom/blob/main/src/main/kotlin/es/iesra/datos/dao/ReservaDaoFicheros.kt#L57-L65)
