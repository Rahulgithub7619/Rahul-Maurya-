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
fun SubscriptionScreen(
    plans: List<TiffinPlan>,
    subscriptions: List<Subscription>,
    userAddresses: List<UserAddress>,
    isHindi: Boolean,
    onCreateSubscription: (TiffinPlan, String, String, String) -> Unit,
    onTogglePause: (String) -> Unit,
    onSkipDate: (String, String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedPlanForSubscribe by remember { mutableStateOf<TiffinPlan?>(null) }
    var selectedSlot by remember { mutableStateOf("Lunch (1:00 PM - 2:00 PM)") }
    var startDate by remember { mutableStateOf("2026-08-01") }
    var selectedAddress by remember { mutableStateOf(userAddresses.firstOrNull()?.houseNo + ", " + userAddresses.firstOrNull()?.street ?: "Home Address") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Header: Plans vs Active
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SaffronPrimary
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text(if (isHindi) "टिफिन योजनाएं" else "Subscribe Plans", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text(if (isHindi) "सक्रिय सब्स्क्रिप्शन (${subscriptions.size})" else "Active Tiffins (${subscriptions.size})", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTabIndex == 0) {
            // Plans List
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                items(plans) { plan ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isHindi) plan.nameHi else plan.nameEn,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isHindi) plan.descriptionHi else plan.descriptionEn,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (plan.isPopular) {
                                    Surface(
                                        color = SaffronPrimary,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = if (isHindi) "सर्वाधिक लोकप्रिय" else "MOST POPULAR",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = "₹${plan.totalPrice.toInt()}",
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.Black,
                                            color = IndiaGreen
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "(₹${plan.pricePerMeal.toInt()}/" + (if (isHindi) "थाली" else "meal") + ")",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    color = IndiaGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${plan.discountPercent}% OFF",
                                        color = IndiaGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Features Checklist
                            val features = if (isHindi) plan.featuresHi else plan.featuresEn
                            features.forEach { feat ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = IndiaGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(feat, fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { selectedPlanForSubscribe = plan },
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (isHindi) "सब्स्क्राइब करें" else "SUBSCRIBE NOW", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // Active Subscriptions List
            if (subscriptions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isHindi) "कोई सक्रिय सब्स्क्रिप्शन नहीं है" else "No active subscriptions yet.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    items(subscriptions) { sub ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = if (isHindi) sub.planNameHi else sub.planNameEn,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text("ID: ${sub.id}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }

                                    Surface(
                                        color = if (sub.status == SubscriptionStatus.ACTIVE) IndiaGreen.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = sub.status.name,
                                            color = if (sub.status == SubscriptionStatus.ACTIVE) IndiaGreen else Color.Red,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(if (isHindi) "समय स्लॉट:" else "Delivery Slot:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(sub.deliveryTimeSlot, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(if (isHindi) "वैधता:" else "Validity:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("${sub.startDate} - ${sub.endDate}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Skipped Dates Chip List
                                if (sub.skippedDates.isNotEmpty()) {
                                    Text(
                                        text = if (isHindi) "स्किप की गई तिथियां:" else "Skipped Dates:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        sub.skippedDates.forEach { dt ->
                                            AssistChip(
                                                onClick = { onSkipDate(sub.id, dt) },
                                                label = { Text(dt, fontSize = 10.sp) },
                                                trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(12.dp)) }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onTogglePause(sub.id) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = if (sub.status == SubscriptionStatus.ACTIVE) (if (isHindi) "पॉज़ टिफिन" else "Pause Tiffin")
                                            else (if (isHindi) "रिज्यूम करें" else "Resume Tiffin"),
                                            fontSize = 12.sp
                                        )
                                    }

                                    Button(
                                        onClick = { onSkipDate(sub.id, "2026-07-28") },
                                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(if (isHindi) "दिन स्किप करें" else "Skip a Day", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Modal for Customizing & Subscribing
        selectedPlanForSubscribe?.let { plan ->
            ModalBottomSheet(
                onDismissRequest = { selectedPlanForSubscribe = null }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = if (isHindi) "सब्स्क्रिप्शन कॉन्फ़िगर करें" else "Configure Tiffin Subscription",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(if (isHindi) "डिलीवरी का समय चुनें:" else "Select Delivery Slot:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    val slots = listOf("Breakfast (8:00 AM - 9:00 AM)", "Lunch (1:00 PM - 2:00 PM)", "Dinner (8:00 PM - 9:00 PM)")
                    slots.forEach { slot ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedSlot == slot,
                                onClick = { selectedSlot = slot }
                            )
                            Text(slot, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(if (isHindi) "शुरू करने की तिथि:" else "Start Date:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            onCreateSubscription(plan, selectedSlot, startDate, selectedAddress)
                            selectedPlanForSubscribe = null
                            selectedTabIndex = 1
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = if (isHindi) "भुगतान करें और सब्स्क्राइब करें (₹${plan.totalPrice.toInt()})" else "CONFIRM & PAY (₹${plan.totalPrice.toInt()})",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
