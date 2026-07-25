package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.components.StatusBadge
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronPrimary

@Composable
fun DeliveryBoyScreen(
    orders: List<Order>,
    isHindi: Boolean,
    onUpdateStatus: (String, OrderStatus) -> Unit,
    onBackToCustomer: () -> Unit
) {
    var showOtpDialogForOrderId by remember { mutableStateOf<String?>(null) }
    var otpInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isHindi) "डिलीवरी पार्टनर पोर्टल" else "Delivery Partner App",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (isHindi) "सुरेश शर्मा • जयपुर मंसासरोवर जोन" else "Suresh Sharma • Jaipur Mansarovar Zone",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = onBackToCustomer,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isHindi) "बाहर आएं" else "Exit Partner")
                }
            }
        }

        // Daily Earnings Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = IndiaGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(if (isHindi) "आज की कमाई" else "Today's Earnings", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                    Text("₹680", color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(if (isHindi) "पूर्ण डिलीवरी" else "Completed Deliveries", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                    Text("12 Tiffins", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }

        Text(
            text = if (isHindi) "आवंटित टिफिन डिलीवरी (${orders.size})" else "Assigned Tiffin Deliveries (${orders.size})",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(orders) { order ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("#${order.id}", fontWeight = FontWeight.ExtraBold)
                            StatusBadge(status = order.status, isHindi = isHindi)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Rahul Sharma (+91 98123 45678)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(order.deliveryAddress, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { /* Call simulation */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isHindi) "कॉल करें" else "Call Customer", fontSize = 11.sp)
                            }

                            Button(
                                onClick = {
                                    if (order.status == OrderStatus.OUT_FOR_DELIVERY) {
                                        showOtpDialogForOrderId = order.id
                                    } else {
                                        onUpdateStatus(order.id, OrderStatus.OUT_FOR_DELIVERY)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = when (order.status) {
                                        OrderStatus.CONFIRMED -> if (isHindi) "पिक-अप करें" else "Pick Up Tiffin"
                                        OrderStatus.OUT_FOR_DELIVERY -> if (isHindi) "डिलीवर करें (OTP)" else "Mark Delivered"
                                        else -> if (isHindi) "पूर्ण हुआ" else "Delivered"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showOtpDialogForOrderId != null) {
        AlertDialog(
            onDismissRequest = { showOtpDialogForOrderId = null },
            title = { Text(if (isHindi) "ग्राहका का OTP दर्ज करें" else "Enter Customer Delivery OTP") },
            text = {
                Column {
                    Text(if (isHindi) "सत्यापन के लिए ग्राहक से 4-अंकों का OTP पूछें:" else "Ask customer for 4-digit verification OTP:", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { if (it.length <= 4) otpInput = it },
                        placeholder = { Text("4819") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val id = showOtpDialogForOrderId
                        if (id != null) {
                            onUpdateStatus(id, OrderStatus.DELIVERED)
                            showOtpDialogForOrderId = null
                            otpInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndiaGreen)
                ) {
                    Text(if (isHindi) "सत्यापित करें" else "VERIFY & DELIVER")
                }
            }
        )
    }
}
