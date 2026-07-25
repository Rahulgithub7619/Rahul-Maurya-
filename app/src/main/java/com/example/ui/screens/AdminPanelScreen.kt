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
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    meals: List<Meal>,
    orders: List<Order>,
    subscriptions: List<Subscription>,
    isHindi: Boolean,
    onAddMeal: (Meal) -> Unit,
    onToggleMeal: (String) -> Unit,
    onDeleteMeal: (String) -> Unit,
    onSendNotification: (String, String, String, String) -> Unit,
    onBackToCustomer: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddMealModal by remember { mutableStateOf(false) }

    var newTitleEn by remember { mutableStateOf("") }
    var newTitleHi by remember { mutableStateOf("") }
    var newPrice by remember { mutableStateOf("") }
    var newDescEn by remember { mutableStateOf("") }
    var newDescHi by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf(MealCategory.SPECIAL_THALI) }

    var notifTitleEn by remember { mutableStateOf("") }
    var notifTitleHi by remember { mutableStateOf("") }
    var notifBodyEn by remember { mutableStateOf("") }
    var notifBodyHi by remember { mutableStateOf("") }
    var showNotifSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Top Bar
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
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isHindi) "बालाजी एडमिन डैशबोर्ड" else "Balaji Admin Portal",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (isHindi) "प्रबंधन एवं रिपोर्ट" else "Restaurant Operations Manager",
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
                    Text(if (isHindi) "ग्राहक मोड" else "Exit Admin", fontSize = 12.sp)
                }
            }
        }

        // Navigation Tabs
        TabRow(selectedTabIndex = selectedTab, containerColor = MaterialTheme.colorScheme.surface, contentColor = SaffronPrimary) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text(if (isHindi) "डैशबोर्ड" else "KPI Stats") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text(if (isHindi) "मेनू प्रबंधक" else "Menu Mgr") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text(if (isHindi) "सूचनाएं" else "Notifications") })
        }

        if (selectedTab == 0) {
            // KPI Stats Dashboard
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatBox("128", if (isHindi) "कुल ऑर्डर" else "Total Orders", SaffronPrimary, Modifier.weight(1f))
                        StatBox("₹38,450", if (isHindi) "कुल राजस्व" else "Total Revenue", IndiaGreen, Modifier.weight(1f))
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatBox("42", if (isHindi) "सक्रिय ग्राहक" else "Active Subscribers", Color(0xFF1976D2), Modifier.weight(1f))
                        StatBox("14", if (isHindi) "लंबित डिलीवरी" else "Pending Deliveries", Color(0xFFE65100), Modifier.weight(1f))
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(if (isHindi) "हाल के ऑर्डर्स" else "Live Recent Orders", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(12.dp))

                            orders.take(5).forEach { ord ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("#${ord.id} • ${ord.createdAt}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("${ord.items.size} Items • ${ord.deliveryAddress}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text("₹${ord.totalAmount.toInt()}", fontWeight = FontWeight.ExtraBold, color = IndiaGreen)
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        } else if (selectedTab == 1) {
            // Menu Management
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = { showAddMealModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isHindi) "नया व्यंजन जोड़ें" else "ADD NEW MEAL TO MENU", fontWeight = FontWeight.Bold)
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(meals) { meal ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(if (isHindi) meal.titleHi else meal.titleEn, fontWeight = FontWeight.Bold)
                                    Text("₹${meal.price.toInt()} • ${meal.category.name}", fontSize = 12.sp, color = IndiaGreen)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Switch(
                                        checked = meal.isAvailable,
                                        onCheckedChange = { onToggleMeal(meal.id) }
                                    )
                                    IconButton(onClick = { onDeleteMeal(meal.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Broadcast Push Notifications
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(if (isHindi) "पुश नोटिफिकेशन भेजें 📢" else "Send Broadcast Push Notification 📢", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(value = notifTitleEn, onValueChange = { notifTitleEn = it }, label = { Text("Title (English)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = notifTitleHi, onValueChange = { notifTitleHi = it }, label = { Text("शीर्षक (हिंदी)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = notifBodyEn, onValueChange = { notifBodyEn = it }, label = { Text("Message Body (English)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = notifBodyHi, onValueChange = { notifBodyHi = it }, label = { Text("संदेश विवरण (हिंदी)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                Button(
                    onClick = {
                        if (notifTitleEn.isNotBlank()) {
                            onSendNotification(notifTitleEn, notifTitleHi, notifBodyEn, notifBodyHi)
                            showNotifSuccess = true
                            notifTitleEn = ""
                            notifTitleHi = ""
                            notifBodyEn = ""
                            notifBodyHi = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isHindi) "सभी ग्राहकों को भेजें" else "BROADCAST TO ALL USERS", fontWeight = FontWeight.Bold)
                }

                if (showNotifSuccess) {
                    Text(if (isHindi) "नोटिफिकेशन सफलतापूर्वक भेजा गया! ✅" else "Notification sent successfully! ✅", color = IndiaGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showAddMealModal) {
        AlertDialog(
            onDismissRequest = { showAddMealModal = false },
            title = { Text(if (isHindi) "नया व्यंजन जोड़ें" else "Add New Meal") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = newTitleEn, onValueChange = { newTitleEn = it }, label = { Text("Title (English)") })
                    OutlinedTextField(value = newTitleHi, onValueChange = { newTitleHi = it }, label = { Text("Title (Hindi)") })
                    OutlinedTextField(value = newPrice, onValueChange = { newPrice = it }, label = { Text("Price (₹)") })
                    OutlinedTextField(value = newDescEn, onValueChange = { newDescEn = it }, label = { Text("Description") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitleEn.isNotBlank() && newPrice.isNotBlank()) {
                            val meal = Meal(
                                titleEn = newTitleEn,
                                titleHi = if (newTitleHi.isBlank()) newTitleEn else newTitleHi,
                                descriptionEn = newDescEn,
                                descriptionHi = newDescHi,
                                price = newPrice.toDoubleOrNull() ?: 120.0,
                                calories = 550,
                                imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500&q=80",
                                category = newCategory,
                                mealType = MealType.LUNCH
                            )
                            onAddMeal(meal)
                            showAddMealModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text(if (isHindi) "सहेजें" else "SAVE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMealModal = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun StatBox(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Black, fontSize = 22.sp, color = color)
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
