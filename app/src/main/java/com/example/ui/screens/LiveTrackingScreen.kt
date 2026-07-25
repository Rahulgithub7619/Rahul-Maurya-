package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.components.InvoiceDialog
import com.example.ui.components.StatusBadge
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronPrimary

@Composable
fun LiveTrackingScreen(
    order: Order,
    isHindi: Boolean,
    onBack: () -> Unit
) {
    var showInvoiceDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 80.dp)
    ) {
        // Top Bar
        Surface(
            shadowElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (isHindi) "ऑर्डर नंबर #${order.id}" else "Live Order Tracking #${order.id}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (isHindi) "अनुमानित समय: ${order.estimatedDeliveryTime}" else "Estimated Delivery: ${order.estimatedDeliveryTime}",
                        fontSize = 12.sp,
                        color = SaffronPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Status Stepper Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (isHindi) "ऑर्डर स्थिति" else "Order Status", fontWeight = FontWeight.Bold)
                            StatusBadge(status = order.status, isHindi = isHindi)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stepper timeline
                        val steps = listOf(
                            OrderStatus.CONFIRMED to (if (isHindi) "ऑर्डर कन्फर्म" else "Order Confirmed"),
                            OrderStatus.PREPARING to (if (isHindi) "रसोई में खाना बन रहा है" else "Chef Preparing Meals"),
                            OrderStatus.PACKED to (if (isHindi) "गरम टिफिन में पैक" else "Packed Hot in Tiffin"),
                            OrderStatus.OUT_FOR_DELIVERY to (if (isHindi) "रास्ते में (डिलीवरी पार्टनर)" else "Out for Delivery"),
                            OrderStatus.DELIVERED to (if (isHindi) "डिलीवर हुआ" else "Delivered")
                        )

                        val currentIdx = steps.indexOfFirst { it.first == order.status }.coerceAtLeast(0)

                        steps.forEachIndexed { idx, (status, label) ->
                            val isCompleted = idx <= currentIdx
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isCompleted) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    } else {
                                        Text("${idx + 1}", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCompleted) MaterialTheme.colorScheme.onSurface else Color.Gray
                                )
                            }

                            if (idx < steps.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 11.dp)
                                        .width(2.dp)
                                        .height(20.dp)
                                        .background(if (idx < currentIdx) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                )
                            }
                        }
                    }
                }
            }

            // Driver Details Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🛵", fontSize = 28.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(order.driverName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("${order.driverRating} • ${if (isHindi) "डिलीवरी पार्टनर" else "Tiffin Delivery Partner"}", fontSize = 11.sp)
                                }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { /* Call Driver simulation */ },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(IndiaGreen)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call Driver", tint = Color.White)
                            }
                        }
                    }
                }
            }

            // Simulated Live Map Route Graphic Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📍 🛵 ➔ 🏠", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isHindi) "लाइव लोकेशन ट्रैक हो रही है (जयपुर सिटी)" else "Live GPS Route: Mansarovar to Destination",
                                fontWeight = FontWeight.Bold,
                                color = IndiaGreen,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "ETA: 15 Mins • Speed: 28 km/h",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Invoice PDF Trigger Card
            item {
                Button(
                    onClick = { showInvoiceDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = IndiaGreen),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.Receipt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isHindi) "डिजिटल बिल / इनवॉइस देखें 🧾" else "VIEW DIGITAL INVOICE 🧾", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showInvoiceDialog) {
            InvoiceDialog(
                order = order,
                isHindi = isHindi,
                onDismiss = { showInvoiceDialog = false }
            )
        }
    }
}
