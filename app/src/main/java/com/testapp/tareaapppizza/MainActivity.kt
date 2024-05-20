package com.testapp.tareaapppizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanPequenoCesar()
        }
    }
}

@Composable
fun PlanPequenoCesar() {
    var selectedSize by remember { mutableStateOf("") }
    var selectedToppings by remember { mutableStateOf(emptySet<String>()) }
    var totalPrice by remember { mutableStateOf(0.0) }
    var showAlert by remember { mutableStateOf(false) }

    val sizePrices = mapOf("8-10 plgs" to 180.0, "11-13 plgs" to 250.0, "18 plgs" to 360.0)
    val toppingPrices = mapOf("Piña" to 30.0, "Jamón" to 45.0, "Pepperoni" to 60.0, "Hongos" to 45.0)

    fun calculateTotalPrice(size: String, toppings: Set<String>): Double {
        val sizePrice = sizePrices[size] ?: 0.0
        val toppingsPrice = toppings.sumOf { topping -> toppingPrices[topping] ?: 0.0 }
        return sizePrice + toppingsPrice
    }

    LaunchedEffect(selectedSize, selectedToppings) {
        totalPrice = calculateTotalPrice(selectedSize, selectedToppings)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.pizza),
            contentDescription = "Pizza",
            modifier = Modifier
                .size(200.dp)
                .padding(10.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Tamaño", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                sizePrices.keys.forEach { size ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedSize == size,
                            onClick = { selectedSize = size }
                        )
                        Text(size)
                    }
                }
            }

            Column {
                Text("Ingredientes", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                toppingPrices.keys.forEach { topping ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedToppings.contains(topping),
                            onCheckedChange = {
                                if (it) {
                                    selectedToppings = selectedToppings + topping
                                } else {
                                    selectedToppings = selectedToppings - topping
                                }
                            }
                        )
                        Text(topping)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Total: C$${"%.2f".format(totalPrice)}", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            showAlert = true
        }) {
            Text("Pagar")
        }

        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("¡Pedido realizado!") },
                text = { Text("Ha pedido una pizza de tamaño $selectedSize con ${selectedToppings.joinToString(", ")}. Total a pagar: C$${"%.2f".format(totalPrice)}") },
                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlanPequenoCesar()
}
