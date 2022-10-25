package com.example.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var isShowResult by remember { mutableStateOf(false) }
            var result by remember { mutableStateOf("0") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (isShowResult)
                    ResultScreen(expression = result) {
                        result = ""
                        isShowResult = false
                    }
                else
                    CalculatorScreen {
                        result = it
                        isShowResult = true
                    }
            }


        }
    }
}

@Composable
fun CalculatorScreen(onCalculateClick: (result: String) -> Unit) {
    val context = LocalContext.current
    var result by remember { mutableStateOf("") }
    var numberText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$result$numberText",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            fontSize = 24.sp,
            color = Color.Black
        )

        TextField(
            value = numberText,
            onValueChange = {
                numberText = it.trim()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Number") },
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.padding(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CalculatorButton(operator = Operator.Plus(), numberText) {
                result = "$result$it "
                numberText = ""
            }
            CalculatorButton(operator = Operator.Minus(), numberText) {
                result = "$result$it "
                numberText = ""
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CalculatorButton(operator = Operator.Multiply(), numberText) {
                result = "$result$it "
                numberText = ""
            }
            CalculatorButton(operator = Operator.Divide(), numberText) {
                result = "$result$it "
                numberText = ""
            }
        }

        Spacer(modifier = Modifier.padding(24.dp))

        Button(onClick = {
            result = ""
            numberText = ""
        }) {
            Text(text = "Clear")
        }

        Spacer(modifier = Modifier.padding(24.dp))

        Button(onClick = {
            if (numberText.isEmpty())
                Toast.makeText(context, "Type the last number!", Toast.LENGTH_SHORT).show()
            else
                onCalculateClick.invoke("$result$numberText")
        }) {
            Text(text = "Calculate")
        }

    }
}

@Composable
fun CalculatorButton(operator: Operator, value: String, onClick: (result: String) -> Unit) {
    val context = LocalContext.current
    Button(onClick = {
        try {
            val number = value.replace(",", ".").toDouble()
            onClick.invoke(operator.handle(number))
        } catch (e: Exception) {
            Toast.makeText(context, "Type a correct number!", Toast.LENGTH_SHORT).show()
        }
    }) { operator.Text() }
}

@Composable
fun ResultScreen(expression: String, onResultClose: () -> Unit) {

    val result: String = try {
        "Result is:\n${ExpressionBuilder(expression).build().evaluate()}"
    } catch (e: Exception) {
        "Unable to calculate!"
    }

    BackHandler {
        onResultClose.invoke()
    }

    Text(
        text = result, color = Color.Black,
        textAlign = TextAlign.Center,
        fontSize = 24.sp
    )

    Spacer(modifier = Modifier.padding(24.dp))

    Button(onClick = { onResultClose.invoke() }) {
        Text(text = "Back")
    }

}