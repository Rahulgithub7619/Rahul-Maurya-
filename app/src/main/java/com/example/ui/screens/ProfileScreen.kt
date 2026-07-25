package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    walletTransactions: List<WalletTransaction>,
    isHindi: Boolean,
    isDarkMode: Boolean,
    onLanguageToggle: () -> Unit,
    onThemeToggle: () -> Unit,
    onAddMoneyToWallet: (Double) -> Unit,
    onAddAddress: (UserAddress) -> Unit
) {
    var showAddMoneyModal by remember { mutableStateOf(false) }
    var addAmountInput by remember { mutableStateOf("500") }
    var showAddAddressModal by remember { mutableStateOf(false) }

    var newHouseNo by remember { mutableStateOf("") }
    var newStreet by remember { mutableStateOf("") }
    var newLandmark by remember { mutableStateOf("") }
    var newLabel by remember { mutableStateOf("Home") }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 80.dp)
    ) {
        // User Profile Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(SaffronPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(userProfile.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text(userProfile.phone, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(userProfile.email, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Wallet Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = IndiaGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(if (isHindi) "बालाजी वॉलेट बैलेंस" else "Balaji Wallet Balance", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                            Text("₹${userProfile.walletBalance.toInt()}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 26.sp)
                        }

                        Button(
                            onClick = { showAddMoneyModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = IndiaGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isHindi) "पैसे जोड़ें" else "Add Money", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Refer & Earn Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SaffronPrimary.copy(alpha = 0.12f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(if (isHindi) "रिफर करें एवं ₹100 कमाएं 🎁" else "Refer & Earn ₹100 🎁", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                if (isHindi) "अपने कोड ${userProfile.referralCode} का उपयोग करें" else "Share code ${userProfile.referralCode} with friends",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { /* Share simulation */ },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isHindi) "शेयर" else "Share")
                        }
                    }
                }
            }
        }

        // Saved Addresses Section
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isHindi) "सहेजे गए पते" else "Saved Delivery Addresses", fontWeight = FontWeight.Bold)
                        TextButton(onClick = { showAddAddressModal = true }) {
                            Text("+ " + (if (isHindi) "नया पता" else "Add New"), color = SaffronPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    userProfile.addresses.forEach { addr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("${addr.label}: ${addr.houseNo}, ${addr.street}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Text("${addr.landmark}, ${addr.city} - ${addr.pincode}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // App Preferences (Language & Theme)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(if (isHindi) "ऐप सेटिंग्स" else "App Preferences", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isHindi) "भाषा (Language)" else "App Language", fontSize = 14.sp)
                        Button(
                            onClick = onLanguageToggle,
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary.copy(alpha = 0.15f), contentColor = SaffronPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isHindi) "हिंदी ➔ EN" else "English ➔ हिंदी", fontWeight = FontWeight.Bold)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isHindi) "डार्क मोड" else "Dark Mode Theme", fontSize = 14.sp)
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onThemeToggle() },
                            colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary)
                        )
                    }
                }
            }
        }

        // Support & Help Links
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(if (isHindi) "सहायता एवं नीतियां" else "Support & Legal Policies", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileOptionRow(Icons.Default.Help, if (isHindi) "अक्सर पूछे जाने वाले सवाल (FAQs)" else "Frequently Asked Questions") {}
                    ProfileOptionRow(Icons.Default.PrivacyTip, if (isHindi) "गोपनीयता नीति (Privacy Policy)" else "Privacy Policy") {}
                    ProfileOptionRow(Icons.Default.Gavel, if (isHindi) "नियम एवं शर्तें" else "Terms & Conditions") {}
                    ProfileOptionRow(Icons.Default.Info, if (isHindi) "हमारे बारे में (About Us)" else "About Balaji Tiffin") {}
                }
            }
        }
    }

    // Modal Add Money to Wallet
    if (showAddMoneyModal) {
        AlertDialog(
            onDismissRequest = { showAddMoneyModal = false },
            title = { Text(if (isHindi) "वॉलेट में पैसे जोड़ें" else "Add Money to Wallet") },
            text = {
                Column {
                    OutlinedTextField(
                        value = addAmountInput,
                        onValueChange = { addAmountInput = it },
                        prefix = { Text("₹ ") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = addAmountInput.toDoubleOrNull() ?: 500.0
                        onAddMoneyToWallet(amt)
                        showAddMoneyModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text(if (isHindi) "UPI से जोड़ें" else "PAY VIA UPI")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMoneyModal = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }

    // Modal Add Address
    if (showAddAddressModal) {
        AlertDialog(
            onDismissRequest = { showAddAddressModal = false },
            title = { Text(if (isHindi) "नया डिलीवरी पता जोड़ें" else "Add New Delivery Address") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = newLabel, onValueChange = { newLabel = it }, label = { Text("Label (Home/Office)") }, shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = newHouseNo, onValueChange = { newHouseNo = it }, label = { Text("Flat / House No.") }, shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = newStreet, onValueChange = { newStreet = it }, label = { Text("Street / Area") }, shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = newLandmark, onValueChange = { newLandmark = it }, label = { Text("Landmark") }, shape = RoundedCornerShape(12.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newHouseNo.isNotBlank()) {
                            onAddAddress(UserAddress(label = newLabel, houseNo = newHouseNo, street = newStreet, landmark = newLandmark))
                            showAddAddressModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text(if (isHindi) "सहेजें" else "SAVE ADDRESS")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAddressModal = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontSize = 13.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
    }
}
