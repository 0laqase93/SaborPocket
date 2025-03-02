package com.pmdm.saborpocket.Data

import android.app.Application
import androidx.room.Room
import com.pmdm.saborpocket.Entities.RecetaEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Aplicacion : Application() {
    companion object {
        lateinit var baseDeDatos: BaseDeDatos
    }

    override fun onCreate() {
        super.onCreate()

        baseDeDatos = Room.databaseBuilder(
            this,
            BaseDeDatos::class.java,
            "BaseDeDatos"
        )
            .fallbackToDestructiveMigration()
            .build()

        agregarRecetas()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun agregarRecetas() {
        GlobalScope.launch(Dispatchers.IO) {
            val recetasObtenidas = baseDeDatos
                .recetaDao()
                .obtenerTodasLasRecetas()

            if (recetasObtenidas.isEmpty()) {
                listOf(
                    RecetaEntity(
                        label = "Paella Valenciana",
                        image = "https://lacocinademasito.com/wp-content/uploads/2019/12/Paella-valenciana.jpg",
                        source = "La Cocina de Masito",
                        yield = 4,
                        calories = 650.0,
                        ingredients = mapOf(
                            "Arroz bomba" to "800g",
                            "Pollo" to "1Kg",
                            "Conejo" to "500g",
                            "garrofón" to "150g",
                            "bachoqueta" to "150g",
                            "Tomate" to "2",
                            "Aceite de oliva virgen extra" to "75ml",
                            "Agua" to "1L",
                            "Azafrán" to "4g",
                            "Pimentón dulce" to "1 cucharita",
                            "Romero" to "2 ramas",
                            "Sal" to "Al gusto",
                            "Pimienta" to "Al gusto"
                        ),
                        instructions = listOf(
                            "Añadimos aceite y sal a la paella y ponemos temperatura fuerte.",
                            "Incorporamos el pollo y el conejo previamente salpimentado y freímos a conciencia, es importante que se fría bien, freímos también el hígado del pollo.",
                            "Abrimos un hueco en el medio y ponemos ahora la judía verde plana y el garrofón y bajamos la temperatura y cocinamos un par de minutos.",
                            "Incorporamos el tomate rallado y sofreímos otro par de minutos, temperatura media.",
                            "Cuando ya casi no tengamos agua de tomate echamos el azafrán y el pimentón y removemos rápido para que no se queme.",
                            "Cubrimos con agua hasta que sobrepase un cm los remaches de las asas de la paella.",
                            "Encendemos el fuego exterior y cocemos a temperatura alta 10 minutos y otros 15 a fuego medio.",
                            "Durante la cocción añade el romero, el colorante y prueba el punto de sal.",
                            "Deja que cocine hasta que el agua llegue a los remaches y en ese punto retira el romero y añade el arroz.",
                            "Cocina a fuego alto y el resto a fuego medio o bajo, tiene que evaporar el agua a la vez que se hace el arroz.",
                            "Una vez listo deja reposar unos minutos y disfruta."
                        ),
                        strYoutube = "https://www.youtube.com/watch?v=vWySIREExjo&ab_channel=LacocinadeMasito",
                        totalTime = 65,
                        esFavorita = false
                    ),
                    RecetaEntity(
                        label = "Tortilla de Patatas",
                        image = "https://lacocinademasito.com/wp-content/uploads/2014/05/Tortilla-de-patatas-sin-cebolla.jpg",
                        source = "La Cocina de Masito",
                        yield = 4,
                        calories = 200.0,
                        ingredients = mapOf(
                            "Patatas medianas" to "10",
                            "Huevos" to "10",
                            "Aceite de oliva" to "Para freir",
                            "Sal" to "Al gusto",
                            "Pimienta" to "Al gusto"
                        ),
                        instructions = listOf(
                            "Lavar, pelar y cortar las patatas en rodajas finas.",
                            "Cocer las patatas en aceite a fuego medio hasta que estén blandas, sin freírlas.",
                            "Escurrir las patatas y mezclarlas con huevos sin batir hasta obtener una masa homogénea.",
                            "Ajustar la cantidad de huevo y sal al gusto.",
                            "Calentar una sartén antiadherente con un poco de aceite.",
                            "Verter la mezcla en la sartén y mover ligeramente para que cuaje bien.",
                            "Dar la vuelta a la tortilla con la ayuda de un plato o tapa.",
                            "Cocinar por el otro lado hasta que esté dorada.",
                            "Servir caliente, ideal con ensalada."
                        ),
                        strYoutube = "https://www.youtube.com/watch?v=zSBHOPKrvv0&ab_channel=LacocinadeMasito",
                        totalTime = 20,
                        esFavorita = false
                    ),
                    RecetaEntity(
                        label = "Gazpacho",
                        image = "https://lacocinademasito.com/wp-content/uploads/2025/01/Receta-de-Gazpacho.jpg",
                        source = "La Cocina de Masito",
                        yield = 10          ,
                        calories = 120.0,
                        ingredients = mapOf(
                            "Tomates maduros" to "2kg",
                            "Cebolla pequeña" to "1",
                            "Pimiento rojo grande" to "1/2",
                            "Pimiento verde italiano" to "1",
                            "Pepino" to "1",
                            "Sal" to "Al gusto",
                            "Aceite de oliva" to "50ml",
                            "Vinagre" to "Al gusto"
                        ),
                        instructions = listOf(
                            "Para este rico gazpacho, lo primero que vamos a hacer es quitar el rabo de los tomates.",
                            "Los lavamos muy bien, los cortamos en trozos (da igual el tamaño por que los vamos a triturar).",
                            "Los introducimos en una olla o recipiente alto junto con 1 diente de ajo pelado.",
                            "Lavamos los pimientos, el rojo y el verde. Les quitamos las semillas y la parte blanca.",
                            "Luego los cortamos en trozos y añadimos al tomate. Lavamos y le quitamos la piel del pepino.",
                            "Lo agregamos también en trocitos a la olla. Pelamos la cebolla, la cortamos en trozos.",
                            "Igualmente la incorporamos a nuestro recipiente para hacer el gazpacho.",
                            "Este plato es un primer plato estupendo e incluso como aperitivo va de maravilla.",
                            "Ahora, con la ayuda de una batidora, procedemos a triturarlo todo; con paciencia, que no hay prisa.",
                            "Debe quedarnos todo lo más triturado posible, homogéneo y sin ningún trozo de verdura entero.",
                            "Cuando esté en ese puto, vertemos el vinagre y el aceite de oliva virgen extra, salamos.",
                            "Es importante añadir el aceite de oliva al gazpacho en este momento y no antes para que emulsione bien."
                        ),
                        strYoutube = "https://www.youtube.com/watch?v=zzNpW51VfpQ&embeds_referring_euri=https%3A%2F%2Flacocinademasito.com%2F&source_ve_path=Mjg2NjY",
                        totalTime = 10,
                        esFavorita = false
                    ),
                    RecetaEntity(
                        label = "Espaguetis con pesto",
                        image = "https://lacocinademasito.com/wp-content/uploads/2024/12/Espaguetis-en-salsa-pesto.jpg",
                        source = "La Cocina de Masito",
                        yield = 4,
                        calories = 300.0,
                        ingredients = mapOf(
                            "Espagueti" to "300g",
                            "Ajo" to "1",
                            "Albahaca fresca" to "40g",
                            "Piñones" to "75g",
                            "Sal" to "Al gusto",
                            "Agua" to "50ml",
                            "Aceite de oliva" to "50ml",
                            "Queso manchego" to "125g"
                        ),
                        instructions = listOf(
                            "En el vaso de la batidora echamos la albahaca, los piñones, el queso, el diente de ajo, el agua y el aceite.",
                            "Trituramos todo bien hasta que quede bien fino y reservamos en un cuenco.",
                            "En agua hirviendo, incorporamos una cucharada de sal y cocemos los espaguetis según las instrucciones del fabricante.",
                            "En el mismo cuenco donde tenemos el pesto, incorporamos los espaguetis y removemos bien para integrarlos con la salsa pesto.",
                            "Servimos en un plato con más queso rallado y unas hojitas de albahaca."
                        ),
                        strYoutube = "https://www.youtube.com/watch?v=DV1ASrqYV9Q&ab_channel=LacocinadeMasito",
                        totalTime = 20,
                        esFavorita = false
                    ),
                    RecetaEntity(
                        label = "Pollo a la toscana",
                        image = "https://lacocinademasito.com/wp-content/uploads/2025/02/Pollo-a-la-toscana.jpg",
                        source = "La Cocina de Masito",
                        yield = 4,
                        calories = 450.0,
                        ingredients = mapOf(
                            "Pechugas de pollo" to "4",
                            "Sal" to "Al gusto",
                            "Pimienta" to "Al gusto",
                            "Aceite de oliva" to "Al gusto",
                            "Nata líquida" to "500ml",
                            "Espinacas baby" to "200g",
                            "Tomate seco" to "90g",
                            "Queso parmesano" to "60ml",
                            "Orégano" to "1 cucharadita",
                            "Albahaca" to "1 cucharada",
                            "Caldo de carne" to "230ml",
                            "Cebolla" to "1/2",
                            "Ajo" to "1 diente"
                        ),
                        instructions = listOf(
                            "Cogemos las pechugas de pollo entera y le hacemos unos cortes de unos 3 cm de profundidad y salpimentamos.",
                            "Salpimentamos el pollo y marcamos las pechugas a un fuego alto de 8/9 hasta que queden bien doradas por fuera, una vez doradas las pechugas, las reservamos.",
                            "Coortamos el tomate seco en trozos pequeños, al igual que la cebolla que partiremos en brunoise y el ajo en trozos pequeños.",
                            "pochamos en aceite de oliva virgen extra 10 minutos la cebolla y el ajo con un poco de sal a un fuego de 5/9.",
                            "Agregamos las espinacas baby y los tomates secos y cocinamos 3 minutos a un fuego de 5/9.",
                            "Añadimos la nata liquida al 20 % de materia grasa, el orégano, la albahaca, la pimienta negra y la sal.",
                            "Cocinamos durante un minutos escaso para integrar los sabores.",
                            "ponemos el caldo en este momento, yo he usado caldo de huesos e incorporamos también el queso rallado.",
                            "Con la salsa toscana bien integrada, metemos el pollo y cocinamos durante 8 minutos a un fuego de 5/9 y con la tapa puesta.",
                            "Servimos las pechugas, napando con la salsa por encima y a disfrutar."
                        ),
                        strYoutube = "https://www.youtube.com/watch?v=7epkLrUN1AQ&ab_channel=LacocinadeMasito                       ",
                        totalTime = 50,
                        esFavorita = false
                    )
                ).forEach { receta ->
                    baseDeDatos
                        .recetaDao()
                        .agregarReceta(receta)
                }
            }
        }
    }
}