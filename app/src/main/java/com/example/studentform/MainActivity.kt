package com.example.studentform

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentform.ui.theme.StudentFormTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentFormTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFF0F2F5)
                ) { innerPadding ->
                    StudentForm(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentForm(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var nameState by remember { mutableStateOf("") }
    var lastNameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dateState by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAgreed by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Date(it)
                        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        dateState = format.format(date)
                    }
                }) {
                    Text("არჩევა", color = Color(0xFF3F51B5), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { }) {
                    Text("გაუქმება", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(
                text = "რეგისტრაცია",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A237E)
            )
            Text(
                text = "შეავსეთ სტუდენტის მონაცემები",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }

        CustomStyledTextField(
            value = nameState,
            onValueChange = { nameState = it },
            label = "სახელი",
            icon = Icons.Default.Person,
            isError = showErrors && nameState.isBlank()
        )
        
        CustomStyledTextField(
            value = lastNameState,
            onValueChange = { lastNameState = it },
            label = "გვარი",
            icon = Icons.Default.Person,
            isError = showErrors && lastNameState.isBlank()
        )

        CustomStyledTextField(
            value = emailState,
            onValueChange = { emailState = it },
            label = "იმეილი",
            icon = Icons.Default.Email,
            isError = showErrors && emailState.isBlank()
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = dateState,
                onValueChange = {},
                placeholder = { Text("დაბადების თარიღი") },
                readOnly = true,
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF3F51B5)) },
                isError = showErrors && dateState.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3F51B5),
                    unfocusedBorderColor = Color(0xFFC5CAE9),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    errorBorderColor = Color.Red
                )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { }
            )
        }

        Text(
            text = "აირჩიეთ მიმართულება",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF2C3E50)
        )

        val directions = listOf("Android", "iOS", "Web")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(directions) { direction ->
                val isSelected = selectedOption == direction
                Surface(
                    modifier = Modifier.clickable { selectedOption = direction },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFFE8EAF6) else Color.White,
                    shadowElevation = if (isSelected) 4.dp else 1.dp,
                    border = when {
                        showErrors && selectedOption == null -> BorderStroke(2.dp, Color.Red)
                        isSelected -> BorderStroke(2.dp, Color(0xFF3F51B5))
                        else -> BorderStroke(1.dp, Color(0xFFE0E0E0))
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedOption = direction },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF3F51B5))
                        )
                        Text(
                            text = direction,
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFF3F51B5) else Color.DarkGray
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                checked = isAgreed,
                onCheckedChange = { isAgreed = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF3F51B5)
                )
            )
            Text(
                text = "ვეთანხმები წესებს და პირობებს",
                modifier = Modifier.padding(start = 12.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (showErrors && !isAgreed) Color.Red else Color(0xFF7F8C8D)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (nameState.isBlank() || lastNameState.isBlank() || emailState.isBlank() || 
                    dateState.isBlank() || selectedOption == null || !isAgreed) {
                    showErrors = true
                    Toast.makeText(context, "შეავსეთ ყველა ველი!", Toast.LENGTH_SHORT).show()
                } else {
                    showErrors = false
                    Toast.makeText(context, "მონაცემები წარმატებით გაიგზავნა!", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Text(text = "მონაცემების გაგზავნა", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CustomStyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFF3F51B5)) },
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3F51B5),
            unfocusedBorderColor = Color(0xFFC5CAE9),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            errorBorderColor = Color.Red
        )
    )
}

@Preview(showBackground = true)
@Composable
fun StudentFormPreview() {
    StudentFormTheme {
        StudentForm()
    }
}
